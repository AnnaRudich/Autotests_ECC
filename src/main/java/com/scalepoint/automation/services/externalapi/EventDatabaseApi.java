package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.Changes;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.updated.EventClaimUpdated;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.*;


@SuppressWarnings("unchecked")
public class EventDatabaseApi {

    private static Logger logger = LoggerFactory.getLogger(DatabaseApi.class);

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
                eventClaimSettled = getEventsForClaimSettled(claimRequest.getCompany()).stream()
                        .filter(eventClaim -> eventClaim.getCase().getNumber().equals(claimRequest.getCaseNumber()))
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
                eventClaimUpdated = getEventsForClaimUpdate(claimRequest.getCompany()).stream()
                        .filter(eventClaim -> eventClaim.getCase().getNumber().equals(claimRequest.getCaseNumber()))
                        .findFirst().orElseThrow(NoSuchElementException::new);
                notFound = false;
            } catch (NoSuchElementException ex){
                logger.info("No found element in events database");
            }
        }
        return eventClaimUpdated;
    }

    private List<EventClaimUpdated> getEventsForClaimUpdate(String company){
        return getEventsForType(CLAIM_UPDATED, company);
    }

    private List<EventClaimSettled> getEventsForClaimSettled(String company){
        return getEventsForType(CLAIM_SETTLED, company);
    }

    private <T> List getEventsForType(EventType type, String company){
        logger.info("Looking for events with type: " + type.getType());
        String query = "select Payload from dk_outbound_queue_%s where Type = ? order by id desc";
        return this.jdbcTemplate.query(
                String.format(query, company),
                new EventsMapper(type),
                (T) type.getType()
        );
    }

    private static final class EventsMapper<T> implements RowMapper<T> {

        private EventType eventType;

        EventsMapper(EventType eventType){
            this.eventType = eventType;
        }

        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return new ObjectMapper().readValue(rs.getString("Payload"), (Class<T>) eventType.getClazz());
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
        assertThat(getEventsForClaimUpdate(claimRequest.getCompany())
                .stream().anyMatch(event -> event.getCase().getNumber().equals(claimRequest.getCaseNumber())))
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was not created in event-api")
                .isFalse();
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
        return getEventsForClaimUpdate(claimRequest.getCompany())
                .stream()
                .filter(event -> event.getCase().getNumber().equals(claimRequest.getCaseNumber())
                        && event.getChanges().stream().anyMatch(c -> c.getProperty().equals(Changes.Property.CASE_CLOSED)))
                .collect(Collectors.toList());
    }
}
