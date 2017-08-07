package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.EventClaimUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EventDatabaseApi {

    private static Logger logger = LoggerFactory.getLogger(DatabaseApi.class);

    private JdbcTemplate jdbcTemplate;

    public EventDatabaseApi(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EventClaimUpdated> getEventsForClaimUpdate(String company){
        return getEventsForType(EventType.CLAIM_UPDATED, company).stream()
                .filter(event -> event instanceof EventClaimUpdated)
                .map(event -> ((EventClaimUpdated) event))
                .collect(toList());
    }

    private List<Object> getEventsForType(EventType type, String company){
        logger.info("Looking for events with type: " + type.getType());
        String query = "select Payload from dk_outbound_queue_%s where Type = ? ";
        return this.jdbcTemplate.query(
                String.format(query, company),
                new EventsMapper(type),
                type.getType()
        );
    }

    private static final class EventsMapper implements RowMapper<Object> {

        private EventType eventType;

        EventsMapper(EventType eventType){
            this.eventType = eventType;
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return new ObjectMapper().readValue(rs.getString("Payload"), eventType.getObject().getClass());
            } catch (IOException e) {
                throw new InvalidParameterException("Not valid object");
            }
        }
    }

    public enum EventType {

        CLAIM_UPDATED("claim_updated", new EventClaimUpdated());

        private String type;
        private Object object;

        EventType(String type, Object object){
            this.type = type;
            this.object = object;
        }

        public String getType() {
            return type;
        }

        public Object getObject() {
            return object;
        }
    }
}
