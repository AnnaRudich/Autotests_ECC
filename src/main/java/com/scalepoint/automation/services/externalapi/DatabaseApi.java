package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.shared.ClaimStatus;
import com.scalepoint.automation.shared.CwaTaskLog;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.utils.data.entity.input.Assignment;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.EventType;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.cwa.TaskType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("SqlDialectInspection")
public class DatabaseApi {

    private static final int POLL_MS = 10;
    private static final int STATUS_CHANGE_TIMEOUT = 120;

    private static Logger logger = LogManager.getLogger(DatabaseApi.class);
    private JdbcTemplate jdbcTemplate;

    public DatabaseApi(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public XpriceInfo findProduct(PriceConditions... priceConditions) {
        return jdbcTemplate.queryForObject(
                "SELECT top(1) pr.ProductKey, xp.productId, xp.invoicePrice,xp.supplierShopPrice,xp.supplierName, " +
                        "xp.agreementId, xp.priceModelId, xp.priceModelTypeId," +
                        "xp.discountCategoryID,xp.discountFromDate, xp.discountToDate, xp.discountValue, " +
                        "xp.priceSourceType,xp.priceSourceSupplierID, xp.productOriginalId, xp.supplierId, xp.agreementId " +
                        "FROM XPrice as xp join Product as pr on xp.productId = pr.ProductID where " +
                        Stream.of(priceConditions).map(PriceConditions::getCondition).collect(Collectors.joining(" and ")), new XpriceInfoMapper());
    }

    public XpriceInfo findOrderableProduct(){
        return jdbcTemplate.queryForObject(
                "SELECT top(1) pr.ProductKey, xp.productId, xp.invoicePrice,xp.supplierShopPrice, xp.supplierName," +
                        "xp.agreementId, xp.priceModelId, xp.priceModelTypeId," +
                        "xp.discountCategoryID,xp.discountFromDate, xp.discountToDate, xp.discountValue, " +
                        "xp.priceSourceType,xp.priceSourceSupplierID, xp.productOriginalId, xp.supplierId, xp.agreementId " +
                        "FROM XPrice as xp join Product as pr on xp.productId = pr.ProductID WHERE xp.productId!=xp.productOriginalId AND pr.Published=1", new XpriceInfoMapper());
    }

    public Integer getUserIdByClaimToken(String claimToken) {
        return jdbcTemplate.queryForObject("select UserId from [dbo].[User] where UserToken = ?", Integer.class, claimToken);
    }

    public Integer getExternalIntegrationTransactionStatusCodeBy(Integer claimNumber) {
        return jdbcTemplate.queryForObject("Select StatusCode from [dbo].[ExternalIntegrationQueue_Transaction] where QueueItem =(" +
                "  Select ItemID from [dbo].[ExternalIntegrationQueue_Item] where Shopper = ?)", Integer.class, claimNumber);
    }

    public Integer getUserIdByClaimNumber(String claimNumber) {
        try {
            return jdbcTemplate.queryForObject("select UserId from [dbo].[User] where ClaimNumber = ?", Integer.class, claimNumber);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }

    public List<CwaTaskLog> getCwaTaskLogsForClaimId(Integer claimId) {
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

    public void removeAssignment(Assignment assignment) {
        this.jdbcTemplate.update(delete, assignment.getPseudoCategory(), assignment.getCompany(), assignment.getPolicy());
    }

    public String getSettlementRevisionTokenByClaimNumber(String claimNumber) {
        return jdbcTemplate.queryForObject("SELECT SettlementRevisionToken FROM SettlementRevision WHERE ClaimNumber = ?", String.class, claimNumber);
    }

    public String getSettlementRevisionTokenByClaimNumberAndClaimStatusSettled(String claimNumber) {
        return jdbcTemplate.queryForObject("SELECT SettlementRevisionToken FROM SettlementRevision WHERE ClaimNumber = ? AND ClaimStatus = 'S'", String.class, claimNumber);
    }

    public String getSettlementRevisionTokenByClaimNumberAndClaimStatusCancelled(String claimNumber) {
        return jdbcTemplate.queryForObject("SELECT SettlementRevisionToken FROM SettlementRevision WHERE ClaimNumber = ? AND ClaimStatus = 'X'", String.class, claimNumber);
    }

    public String getClaimStatus(String claimNumber){

        return jdbcTemplate.queryForObject("Select TOP 1 [USL].[ClaimStatus] from [dbo].[User] as [U] " +
                "join [dbo].[UserStatusLog] as [USL] on [USL].[Shopper] = [U].[UserID] " +
                "where [U].[ClaimNumber] = ? " +
                "order by [USL].[StartStamp] desc", String.class, claimNumber);
    }

    public VoucherInfo getVoucherInfo(Boolean isEvoucher){
        int isEvoucherNumber = isEvoucher ? 1 : 0;
        return jdbcTemplate.queryForObject(
                "select top (1) VoucherAgreementId, RebatePercentage, SupplierId "+
                        "FROM VoucherAgreement "+
                        "where EVoucher="+isEvoucherNumber+" and Status=1",
                new VoucherInfoMapper());
    }

    public int waitForFraudStatusChange(int status, String claimNumber){
        return  await()
                .pollInterval(POLL_MS, TimeUnit.MILLISECONDS)
                .timeout(STATUS_CHANGE_TIMEOUT, TimeUnit.SECONDS)
                .until(() -> jdbcTemplate.queryForObject("SELECT [fraudStatus]\n" +
                        "  FROM [User] WHERE [ClaimNumber] = ?", int.class, claimNumber), equalTo(status));
    }

    public void waitForClaimStatusChangedTo(Claim claim, ClaimStatus claimStatus){

        await()
                .pollInterval(POLL_MS, TimeUnit.MILLISECONDS)
                .timeout(STATUS_CHANGE_TIMEOUT, TimeUnit.SECONDS)
                .until(() -> {
                    String status = getClaimStatus(claim.getClaimNumber());
                    if (status != null) {
                        logger.info("Claims status: {}", status);
                        boolean equal = status.equalsIgnoreCase(claimStatus.getStatus());
                        return equal;
                    }
                    return null;
                }, is(true));
    }

    private static final class CwaTaskLogMapper implements RowMapper<CwaTaskLog> {

        public CwaTaskLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            CwaTaskLog cwaTaskLog = new CwaTaskLog();
            cwaTaskLog.setClaimId(rs.getInt("claimId"));
            cwaTaskLog.setTaskType(TaskType.valueOf(rs.getString("taskType")));
            cwaTaskLog.setTaskStatus(EventType.valueOf(rs.getString("taskStatus")));
            cwaTaskLog.setTaskId(rs.getString("taskId"));
            cwaTaskLog.setTaskPayload(rs.getString("taskPayload"));
            return cwaTaskLog;
        }
    }

    private static final class XpriceInfoMapper implements RowMapper<XpriceInfo> {

        public XpriceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            XpriceInfo xpriceInfo = new XpriceInfo();
            xpriceInfo.setProductId(rs.getString("ProductId"));
            xpriceInfo.setProductKey(rs.getString("ProductKey"));
            xpriceInfo.setInvoicePrice(rs.getDouble("invoicePrice"));
            xpriceInfo.setSupplierName(rs.getString("supplierName"));
            xpriceInfo.setSupplierShopPrice(rs.getDouble("supplierShopPrice"));
            xpriceInfo.setPriceModelID(rs.getString("priceModelId"));
            xpriceInfo.setPriceModelType(rs.getString("priceModelTypeId"));
            xpriceInfo.setDiscountFromDate(rs.getString("discountFromDate"));
            xpriceInfo.setDiscountToDate(rs.getString("discountToDate"));
            xpriceInfo.setDiscountValue(rs.getDouble("discountValue"));
            xpriceInfo.setPriceSourceType(rs.getString("priceSourceType"));
            xpriceInfo.setPriceSourceSupplierID(rs.getString("priceSourceSupplierID"));
            xpriceInfo.setOriginalProductID(rs.getString("productOriginalId"));
            xpriceInfo.setSupplierId(rs.getString("supplierId"));
            xpriceInfo.setAgreementId(rs.getString("agreementId"));
            return xpriceInfo;
        }
    }

    private static final class VoucherInfoMapper implements RowMapper<VoucherInfo>{
        public VoucherInfo mapRow(ResultSet rs, int i) throws SQLException {
            VoucherInfo voucherInfo = new VoucherInfo();
            voucherInfo.setVoucherId(rs.getString("VoucherAgreementId"));
            voucherInfo.setVoucherSupplierId(rs.getString("SupplierId"));
            voucherInfo.setPurchaseDiscount(rs.getDouble("RebatePercentage"));
            return voucherInfo;
        }
    }

    public enum PriceConditions {
        /**
         * xp is xprice table
         * pr is product table
         */
        ORDERABLE("xp.orderable=1"),

        INVOICE_PRICE_LOWER_THAN_10("xp.invoicePrice < 10"),
        INVOICE_PRICE_LOWER_THAN_MARKET_PRICE("pr.marketPrice>xp.invoicePrice"),
        INVOICE_PRICE_HIGHER_THAN_MARKET_PRICE("pr.marketPrice<xp.invoicePrice"),
        INVOICE_PRICE_EQUALS_MARKET_PRICE("pr.marketPrice=xp.invoicePrice"),

        PRODUCT_AS_VOUCHER_ONLY("xp.voucherOnlyInShop=1"),
        PRODUCT_AS_VOUCHER_ONLY_FALSE("xp.voucherOnlyInShop=0");


        private String condition;

        PriceConditions(String condition) {
            this.condition = condition;
        }

        public String getCondition() {
            return condition;
        }
    }
}
