package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.entity.CwaTaskLog;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.*;
import java.util.*;

@SuppressWarnings("SqlDialectInspection")
public class DatabaseApi {

    public static final String RV_TEMPLATE_FILENAME = "latest_master_template_29-06-2016.xlsm";
    public static final String RV_SERVICE_AGREEMENT_NAME = "AutotestTemplate";

    private static Logger logger = LoggerFactory.getLogger(DatabaseApi.class);
    private JdbcTemplate jdbcTemplate;
    private List<CwaTaskLog> cwaTaskLogs;

    public DatabaseApi(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createDefaultServiceAgreementIfNotExists(Integer icId) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from ServiceAgreementTemplate where name = ?", new Object[]{RV_SERVICE_AGREEMENT_NAME}, Integer.class);
        if (count > 0) {
            logger.info("RV agreement found");
            return;
        }

        Integer rnvTemplateFileId = insertRnvTemplateFile(icId);
        logger.info("RV template file inserted with id: {}", rnvTemplateFileId);
        Integer serviceAgreementTemplateId = insertServiceAgreementTemplate(rnvTemplateFileId, icId);
        logger.info("Service agreement inserted with id: {}", serviceAgreementTemplateId );
        assignTemplateToServiceAgreements(serviceAgreementTemplateId);
    }

    private Integer insertRnvTemplateFile(Integer insCompanyId) {
        try {
            byte[] bytes = IOUtils.toByteArray(DatabaseApi.class.getClassLoader().getResourceAsStream("templates/" + RV_TEMPLATE_FILENAME));
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement("INSERT INTO SupplierFileData (FileName, FileType, [Data], " +
                        " [Size], InsuranceCompanyId, CreatedOn, Source) " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, RV_TEMPLATE_FILENAME);
                ps.setString(2, "application/vnd.ms-excel.sheet.macroEnabled.12");
                ps.setBytes(3, bytes);
                ps.setInt(4, bytes.length);
                ps.setInt(5, insCompanyId);
                ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                ps.setString(7, "RepairValuationFile");
                return ps;
            }, generatedKeyHolder);

           return generatedKeyHolder.getKey().intValue();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Integer insertServiceAgreementTemplate(Integer rvTemplateFileId, Integer insuranceCompanyId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("ServiceAgreementTemplate").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", RV_SERVICE_AGREEMENT_NAME);
        parameters.put("status", 1);
        parameters.put("supplierFileName", "Autotest");
        parameters.put("supplierFileId", rvTemplateFileId);
        parameters.put("insuranceCompanyId", insuranceCompanyId);
        Number key = insert.executeAndReturnKey(parameters);
        return key.intValue();
    }

    public Integer getUserIdByClaimToken(String claimToken){
        return jdbcTemplate.queryForObject("select UserId from [dbo].[User] where UserToken = ?", Integer.class, claimToken.replace("c.", ""));
    }

    public List<CwaTaskLog> getCwaTaskLogsForClaimId(Integer claimId){
        return cwaTaskLogs = this.jdbcTemplate.query(
                "select claimId, taskType, taskId, taskStatus, taskPayload from CwaTaskLog where claimId = ?",
                new CwaTaskLogMapper(),
                claimId
        );
    }

    private static final class CwaTaskLogMapper implements RowMapper<CwaTaskLog> {

        public CwaTaskLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            CwaTaskLog cwaTaskLog = new CwaTaskLog();
            cwaTaskLog.setClaimId(rs.getInt("claimId"));
            cwaTaskLog.setTaskType(rs.getString("taskType"));
            cwaTaskLog.setTaskStatus(rs.getString("taskStatus"));
            cwaTaskLog.setTaskId(rs.getString("taskId"));
            cwaTaskLog.setTaskPayload(rs.getString("taskPayload"));
            return cwaTaskLog;
        }
    }

    private void assignTemplateToServiceAgreements(Integer rvTemplateId) {
        jdbcTemplate.update("update ServiceAgreement set defaultTemplateId = ? where name like 'AutoTest%'", rvTemplateId);
    }
}
