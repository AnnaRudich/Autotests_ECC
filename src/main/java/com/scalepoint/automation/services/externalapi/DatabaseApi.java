package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.utils.data.entity.Assignment;
import com.scalepoint.automation.utils.data.entity.CwaTaskLog;
import com.scalepoint.ecc.thirdparty.integrations.model.cwa.TaskType;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.EventType;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("SqlDialectInspection")
public class DatabaseApi {

    public static final String RV_TEMPLATE_FILENAME = "latest_master_template_29-06-2016.xlsm";
    public static final String RV_SERVICE_AGREEMENT_NAME = "AutotestTemplate";

    private static Logger logger = LogManager.getLogger(DatabaseApi.class);
    private JdbcTemplate jdbcTemplate;

    public DatabaseApi(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public XpriceInfo findProduct(PriceConditions... priceConditions){
       return jdbcTemplate.queryForObject(
                "SELECT top(1) pr.ProductKey, xp.productId,pr.marketPrice,pr.invoicePrice,xp.invoicePrice,xp.supplierShopPrice,xp.supplierName " +
                        "FROM XPrice as xp join Product as pr on xp.productId = pr.ProductID where " +
                         Stream.of(priceConditions).map(PriceConditions::getCondition).collect(Collectors.joining(" and ")),new XpriceInfoMapper());
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
        return jdbcTemplate.queryForObject("select UserId from [dbo].[User] where UserToken = ?", Integer.class, claimToken);
    }

    public Integer getUserIdByClaimNumber(String claimNumber){
        try {
            return jdbcTemplate.queryForObject("select UserId from [dbo].[User] where ClaimNumber = ?", Integer.class, claimNumber);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return null;
        }
    }

    public List<CwaTaskLog> getCwaTaskLogsForClaimId(Integer claimId){
        return this.jdbcTemplate.query(
                "select claimId, taskType, taskId, taskStatus, taskPayload from CwaTaskLog where claimId = ?",
                new CwaTaskLogMapper(),
                claimId
        );
    }

    String delete = " DELETE FROM ReductionRuleConfiguration " +
    " WHERE PseudoCategory = (SELECT PG.DefaultPseudoCatId FROM PSEUDOCAT_Group as PG " +
            "WHERE PG.groupTextId = (SELECT TPG.TextId FROM Text_PSEUDOCAT_Group as TPG WHERE TPG.GroupName = ?))" +
    " AND InsuranceCompany = (SELECT IC.ICRFNBR FROM INSCOMP as IC WHERE IC.CompanyCode = ?)" +
    " AND Policy = ?";

    public void removeAssignment(Assignment assignment){
        this.jdbcTemplate.update(delete, assignment.getPseudoCategory(), assignment.getCompany(), assignment.getPolicy());
    }

    public String getSettlementRevisionTokenByClaimNumber(String claimNumber){
        return jdbcTemplate.queryForObject("SELECT SettlementRevisionToken FROM SettlementRevision WHERE ClaimNumber = ?", String.class, claimNumber);
    }

    public String getSettlementRevisionTokenByClaimNumberAndClaimStatusSettled(String claimNumber){
        return jdbcTemplate.queryForObject("SELECT SettlementRevisionToken FROM SettlementRevision WHERE ClaimNumber = ? AND ClaimStatus = 'S'", String.class, claimNumber);
    }

    public String getSettlementRevisionTokenByClaimNumberAndClaimStatusCancelled(String claimNumber){
        return jdbcTemplate.queryForObject("SELECT SettlementRevisionToken FROM SettlementRevision WHERE ClaimNumber = ? AND ClaimStatus = 'X'", String.class, claimNumber);
    }

    private static final class CwaTaskLogMapper implements RowMapper<CwaTaskLog> {

        public CwaTaskLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            CwaTaskLog cwaTaskLog = new CwaTaskLog();
            cwaTaskLog.setClaimId(rs.getInt("claimId"));
            cwaTaskLog.setTaskType( TaskType.valueOf(rs.getString("taskType")));
            cwaTaskLog.setTaskStatus( EventType.valueOf(rs.getString("taskStatus")));
            cwaTaskLog.setTaskId(rs.getString("taskId"));
            cwaTaskLog.setTaskPayload(rs.getString("taskPayload"));
            return cwaTaskLog;
        }
    }

    private static final class XpriceInfoMapper implements RowMapper<XpriceInfo> {

        public XpriceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            XpriceInfo xpriceInfo = new XpriceInfo();
            xpriceInfo.setProductId(rs.getInt("ProductId"));
            xpriceInfo.setProductKey(rs.getString("ProductKey"));
            xpriceInfo.setInvoicePrice(rs.getDouble("invoicePrice"));
            xpriceInfo.setMarketPrice(rs.getDouble("marketPrice"));
            xpriceInfo.setSupplierName(rs.getString("supplierName"));
            xpriceInfo.setSupplierShopPrice(rs.getDouble("supplierShopPrice"));
            return xpriceInfo;
        }
    }

    private void assignTemplateToServiceAgreements(Integer rvTemplateId) {
        jdbcTemplate.update("update ServiceAgreement set defaultTemplateId = ? where name like 'AutoTest%'", rvTemplateId);
    }

    public enum PriceConditions {
        /**
         * xp is xprice table
         * pr is product table
         */
        ORDERALBLE("xp.orderable=1"),
        INVOICE_PRICE_LOWER_THAN_10("xp.invoicePrice < 10"),
        INVOICE_PRICE_LOWER_THAN_MARKET_PRICE("pr.marketPrice>xp.invoicePrice"),
        MARKET_PRICE_EQUAL_INVOICE_PRICE("pr.marketPrice=xp.invoicePrice"),
        INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE("pr.marketPrice<xp.invoicePrice");

        private String condition;
        PriceConditions(String condition){
            this.condition = condition;
        }

        public String getCondition(){
            return condition;
        }
    }
}
