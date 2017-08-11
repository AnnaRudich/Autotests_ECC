package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
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

import static com.scalepoint.automation.services.externalapi.EventDatabaseApi.EventType.CLAIM_SETTLED;
import static com.scalepoint.automation.services.externalapi.EventDatabaseApi.EventType.CLAIM_UPDATED;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class EventDatabaseApi {

    private static Logger logger = LoggerFactory.getLogger(DatabaseApi.class);

    private JdbcTemplate jdbcTemplate;

    public EventDatabaseApi(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public EventClaimSettled getEventClaimSettled(ClaimRequest claimRequest) {
        return getEventsForClaimSettled(claimRequest.getCompany()).stream()
                .filter(eventClaimUpdated -> eventClaimUpdated.getCase().getNumber().equals(claimRequest.getCaseNumber()))
                .findFirst().get();
    }

    public List<EventClaimUpdated> getEventsForClaimUpdate(String company){
        EventType eventType = CLAIM_UPDATED;
        return getEventsForType(eventType.getClazz(), eventType, company);
    }

    public List<EventClaimSettled> getEventsForClaimSettled(String company){
        EventType eventType = CLAIM_SETTLED;
        return getEventsForType(eventType.getClazz(), eventType, company);
    }


    private <T> List getEventsForType(Class<T> t, EventType type, String company){
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
        assertThat(getEventsForClaimUpdate(claimRequest.getCompany())
                .stream().anyMatch(event -> event.getCase().getNumber().equals(claimRequest.getCaseNumber())))
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was created in event-api")
                .isTrue();
    }

    public void assertThatCloseCaseEventWasNotCreated(ClaimRequest claimRequest) {
        assertThat(getEventsForClaimUpdate(claimRequest.getCompany())
                .stream().anyMatch(event -> event.getCase().getNumber().equals(claimRequest.getCaseNumber())))
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was not created in event-api")
                .isFalse();
    }

    public void assertNumberOfCloseCaseEventsThatWasCreatedForClaim(ClaimRequest claimRequest, int numberOfRequests) {
        assertThat(getEventsForClaimUpdate(claimRequest.getCompany())
                .stream().filter(event -> event.getCase().getNumber().equals(claimRequest.getCaseNumber())).count())
                .as("Check if number of events (" + numberOfRequests + ") with case number: " + claimRequest.getCaseNumber() + " was created in event-api")
                .isEqualTo(numberOfRequests);
    }
}
