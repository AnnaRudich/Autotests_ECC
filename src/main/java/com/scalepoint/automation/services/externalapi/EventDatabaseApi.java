package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaim;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.Changes;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.EventClaimUpdated;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.scalepoint.automation.services.externalapi.EventDatabaseApi.EventType.CLAIM_SETTLED;
import static com.scalepoint.automation.services.externalapi.EventDatabaseApi.EventType.CLAIM_UPDATED;
import static com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.Changes.Property.CASE_CLOSED;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.*;


@SuppressWarnings("unchecked")
public class EventDatabaseApi {

    private static Logger logger = LogManager.getLogger(EventDatabaseApi.class);

    private JdbcTemplate jdbcTemplate;

    public EventDatabaseApi(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public EventClaimSettled getEventClaimSettled(ClaimRequest claimRequest) {
        return getEventClaimSettled(claimRequest, 0);
    }

    public EventClaimSettled getEventClaimSettled(ClaimRequest claimRequest, int eventIndex) {
        return (EventClaimSettled) getEventClaim(CLAIM_SETTLED, claimRequest, eventIndex);

    }

    public EventClaimUpdated getEventClaimUpdated(ClaimRequest claimRequest) {
        return getEventClaimUpdated(claimRequest, 0);
    }

    public EventClaimUpdated getEventClaimUpdated(ClaimRequest claimRequest, int eventIndex) {
        return (EventClaimUpdated)getEventClaim(CLAIM_UPDATED, claimRequest, eventIndex);
    }


    public EventClaim getEventClaim(EventType eventType, ClaimRequest claimRequest, int eventIndex) {
        boolean notFound = true;
        int i = 0;
        EventClaim eventClaim = null;
        while(i<20 && notFound){
            logger.info("Looking for {}th event with type: {}, and case_number: {}", eventIndex+1, eventType.getType(), claimRequest.getCaseNumber());
            try {
                i++;
                final List<EventClaim> events = getEventsForType(eventType, claimRequest.getCompany(), claimRequest.getCaseNumber());
                if (events.size() < eventIndex + 1) {
                    Thread.sleep(1000);
                    throw new NoSuchElementException();
                }
                eventClaim = events.get(eventIndex);
                notFound = false;
            } catch (NoSuchElementException ex){
                logger.info("No found element in events database");
            } catch (InterruptedException ex){
                logger.error(ex);
            }
        }

        if (eventClaim == null){
            logger.error("Please look into Events API DB, event might not have been managed to be created in time. Look into DB for {}th event({}) for case number {} of {}",
                    eventIndex+1, eventType.getType(), claimRequest.getCaseNumber(), claimRequest.getCompany());
        }

        return eventClaim;
    }


    private List<String> getEventPayloadsForType(EventType type, String company, String caseNumber){
        String query = String.format("select Payload from dk_outbound_queue_%s where Type = ? and JSON_VALUE(Payload, '$.case.number')= ? order by id asc", company);
        logger.info("Query: {}", query);
        final List<String> list = jdbcTemplate.queryForList(query, String.class, type.getType(), caseNumber);
        logger.info("Result is {} size", list.size());
        return list;
    }

    private List<EventClaim> getEventsForType(EventType type, List<String> payloadList) {
        return payloadList.stream().map(json ->  {
            try {
                logger.info("PayLoad: {}", json);
                final EventClaim event = (EventClaim) new ObjectMapper().readValue(json, type.getClazz());
                event.setJsonString(json);
                return event;
            } catch (IOException e) {
                throw new InvalidParameterException("Not valid object");
            }
        }).collect(toList());
    }

    private List<EventClaim> getEventsForType(EventType type, String company, String caseNumber) {
        List<String> payloadList = getEventPayloadsForType(type, company, caseNumber);

        return getEventsForType(type, payloadList);
    }

    public enum EventType {

        CLAIM_UPDATED("claim_updated", EventClaimUpdated.class),
        CLAIM_SETTLED("case_settled", EventClaimSettled.class);

        private String type;
        private Class clazz;

        EventType(String type, Class clazz){
            this.type = type;
            this.clazz = clazz;
        }

        public String getType() {
            return type;
        }

        public Class getClazz() {
            return clazz;
        }
    }

    public void assertThatCloseCaseEventWasCreated(ClaimRequest claimRequest) {
        assertThatCloseCaseEventWasCreated(claimRequest, 0);
    }

    public void assertThatCloseCaseEventWasCreated(ClaimRequest claimRequest, int eventIndex) {
        assertThat(getEventClaimUpdated(claimRequest, eventIndex).getCase().getNumber())
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was created in event-api")
                .isEqualTo(claimRequest.getCaseNumber());
    }


    public void assertThatCaseSettledEventWasCreated(ClaimRequest claimRequest){
        assertThat(getEventClaimSettled(claimRequest).getCase().getNumber())
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was created in event-api")
                .isEqualTo(claimRequest.getCaseNumber());
    }

    public void assertThatCloseCaseEventWasNotCreated(ClaimRequest claimRequest) {
        final List<EventClaim> eventsForClaimUpdate = getEventsForType(CLAIM_UPDATED, claimRequest.getCompany(), claimRequest.getCaseNumber());
        assertThat(eventsForClaimUpdate.isEmpty())
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was not created in event-api")
                .isTrue();
    }

    public void assertNumberOfCloseCaseEventsThatWasCreatedForClaim(ClaimRequest claimRequest, int eventsNumber) {
        with().pollInterval(1, SECONDS).await().atMost(10, SECONDS).untilAsserted(() ->
                assertThat(getSizeOfEventUpdatedList(claimRequest))
                        .as("There are expected to be " + eventsNumber + " CloseCase events created in event-api for case with number: " + claimRequest.getCaseNumber() + " but actual was " + getSizeOfEventUpdatedList(claimRequest))
                        .isEqualTo(eventsNumber));
    }

    private int getSizeOfEventUpdatedList(ClaimRequest claimRequest) {
        return getEventsUpdatedList(claimRequest).size();
    }

    private List getEventsUpdatedList(ClaimRequest claimRequest) {
        return getEventsForType(CLAIM_UPDATED, claimRequest.getCompany(), claimRequest.getCaseNumber())
                .stream()
                .filter(event -> hasProperty((EventClaimUpdated)event, CASE_CLOSED))
                .collect(toList());
    }

    private boolean hasProperty(EventClaimUpdated event, Changes.Property property) {
        return event.getChanges().stream().anyMatch(c -> c.getProperty().equals(property));
    }

    private boolean hasCaseNumber(EventClaimUpdated event, String caseNumber) {
        return event.getCase().getNumber().equals(caseNumber);
    }
}
