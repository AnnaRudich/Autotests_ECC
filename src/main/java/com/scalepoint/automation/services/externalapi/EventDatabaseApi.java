package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.Changes;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.EventClaimUpdated;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.scalepoint.automation.services.externalapi.EventDatabaseApi.EventType.CLAIM_SETTLED;
import static com.scalepoint.automation.services.externalapi.EventDatabaseApi.EventType.CLAIM_UPDATED;
import static com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.Changes.Property.CASE_CLOSED;
import static java.util.concurrent.TimeUnit.SECONDS;
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
        boolean notFound = true;
        int i = 0;
        EventClaimSettled eventClaimSettled = new EventClaimSettled();
        while(i<5 && notFound){
            try {
                i++;
                eventClaimSettled = getEventsForClaimSettled(claimRequest.getCompany(), claimRequest.getCaseNumber()).stream()
                        .findFirst().orElseThrow(NoSuchElementException::new);
                notFound = false;
            } catch (NoSuchElementException ex){
                logger.info("No found element in events database");
            }
        }
        return eventClaimSettled;
    }

    private EventClaimUpdated getEventClaimUpdated(ClaimRequest claimRequest) {
        boolean notFound = true;
        int i = 0;
        EventClaimUpdated eventClaimUpdated = new EventClaimUpdated();
        while(i<5 && notFound){
            try {
                i++;
                eventClaimUpdated = getEventsForClaimUpdate(claimRequest.getCompany(), claimRequest.getCaseNumber()).stream()
                        .findFirst().orElseThrow(NoSuchElementException::new);
                notFound = false;
            } catch (NoSuchElementException ex){
                logger.info("No found element in events database");
            }
        }
        return eventClaimUpdated;
    }

    private List<EventClaimUpdated> getEventsForClaimUpdate(String company, String caseNumber){
        return getEventsForType(CLAIM_UPDATED, company, caseNumber);
    }

    private List<EventClaimSettled> getEventsForClaimSettled(String company, String caseNumber){
        return getEventsForType(CLAIM_SETTLED, company, caseNumber);
    }

    private List getEventsForType(EventType type, String company, String caseNumber){
        logger.info("Looking for events with type: " + type.getType());
        String query = "select Payload from dk_outbound_queue_%s where Type = ? and JSON_VALUE(Payload, '$.case.number')= ? order by id desc";
        return this.jdbcTemplate.query(
                String.format(query, company),
                new EventsMapper(type),
                type.getType(),
                caseNumber
        );
    }

    private static final class EventsMapper<T> implements RowMapper<T> {

        private EventType eventType;

        EventsMapper(EventType eventType){
            this.eventType = eventType;
        }

        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                final String payload = rs.getString("Payload");
                logger.info("PayLoad: {}", payload);
                return new ObjectMapper().readValue(payload, (Class<T>) eventType.getClazz());
            } catch (IOException e) {
                throw new InvalidParameterException("Not valid object");
            }
        }
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
        assertThat(getEventClaimUpdated(claimRequest).getCase().getNumber())
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was created in event-api")
                .isEqualTo(claimRequest.getCaseNumber());
    }


    public void assertThatCaseSettledEventWasCreated(ClaimRequest claimRequest){
        assertThat(getEventClaimSettled(claimRequest).getCase().getNumber())
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was created in event-api")
                .isEqualTo(claimRequest.getCaseNumber());
    }

    public void assertThatCloseCaseEventWasNotCreated(ClaimRequest claimRequest) {
        final List<EventClaimUpdated> eventsForClaimUpdate = getEventsForClaimUpdate(claimRequest.getCompany(), claimRequest.getCaseNumber());
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

    private List<EventClaimUpdated> getEventsUpdatedList(ClaimRequest claimRequest) {
        return getEventsForClaimUpdate(claimRequest.getCompany(), claimRequest.getCaseNumber())
                .stream()
                .filter(event -> hasProperty(event, CASE_CLOSED))
                .collect(Collectors.toList());
    }

    private boolean hasProperty(EventClaimUpdated event, Changes.Property property) {
        return event.getChanges().stream().anyMatch(c -> c.getProperty().equals(property));
    }

    private boolean hasCaseNumber(EventClaimUpdated event, String caseNumber) {
        return event.getCase().getNumber().equals(caseNumber);
    }
}
