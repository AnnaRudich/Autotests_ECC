package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.services.externalapi.exception.ServerApiException;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.Category;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.ReductionRule;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Executor;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.scalepoint.automation.utils.Http.*;

public class ReductionRulesApi extends ServerApi {

    public static final String URL_CREATE_UPDATE_RR = Configuration.getEccUrl() + "webshop/jsp/toAdminPage/AddEditReductionRule";
    public static final String URL_ASSIGN_RR_TO_IC_PAGE = Configuration.getEccUrl() + "webshop/jsp/toAdminPage/reduction_rule_assignment_edit.jsp";
    public static final String URL_ASSIGN_RR_TO_IC = Configuration.getEccUrl() + "webshop/jsp/toAdminPage/ProcessReductionRuleAssignment?AssgnRefNum=&CompanyRefNum=";

    public ReductionRulesApi(User user) {
        super(user);
    }

    public ReductionRulesApi(Executor executor) {
        super(executor);
    }

    public String createReductionRuleWithOneLine(ReductionRule reductionRule, boolean isPolicy, String claimReduc,
                                                 String ageFrom, String ageTo,
                                                 String priceRangeFrom, String priceRangeTo,
                                                 String documentation, String claimantRating) {
        List<NameValuePair> rrParams = getReductionRuleParams(reductionRule, isPolicy, claimReduc, ageFrom, ageTo, priceRangeFrom, priceRangeTo, documentation, claimantRating);
        return createReductionRule(reductionRule, rrParams);
    }

    public String createReductionRuleRuleWithTwoLines(ReductionRule reductionRule, boolean isPolicy,
                                                      String claimReduc, String claimReduc2,
                                                      String ageFrom, String ageFrom2, String ageTo, String ageTo2,
                                                      String priceRangeFrom, String priceRangeFrom2, String priceRangeTo, String priceRangeTo2,
                                                      String documentation, String documentation2, String claimantRating) {
        List<NameValuePair> rrParams = getReductionRuleParamsWithTwoLines(reductionRule, isPolicy, claimReduc, claimReduc2, ageFrom, ageFrom2, ageTo, ageTo2, priceRangeFrom, priceRangeFrom2,
                priceRangeTo, priceRangeTo2, documentation, documentation2, claimantRating);
        return createReductionRule(reductionRule, rrParams);
    }

    private String createReductionRule(ReductionRule reductionRule, List<NameValuePair> rrParams) {
        String ruleId;
        try {
            ruleId = post(URL_CREATE_UPDATE_RR, rrParams, executor).returnResponse().getHeaders("Location")[0].toString().split("showRule=")[1];
            if (!ruleId.matches("\\d*\\d"))
                throw new IncorrectRRFormatException();
        } catch (IOException | IncorrectRRFormatException e) {
            throw new RuntimeException(e);
        }
        log.info("RR created: " + reductionRule.getRrName());
        return ruleId;
    }

    public String assignReductionRuleToIC(String rrId, InsuranceCompany insComp, Category category, String policyName) {
        try {
            String content = post(URL_ASSIGN_RR_TO_IC_PAGE, getReductionRuleAssigment(rrId), executor).returnContent().toString();

            String[] splitContentByIC = content.split(insComp.getIcName())[0].split("<option value=");
            String lastOneIc = splitContentByIC[splitContentByIC.length - 1].replaceAll("[^\\d]", "");
            String contentByCat = content.split("categoryoption.options\\[count\\].text = \"" + category.getCategoryName() + "\";")[0];
            String[] psNumSplit = contentByCat.split("categoryoption.options\\[count\\].value = ");
            String psCatNum = psNumSplit[psNumSplit.length - 1].replaceAll("[^\\d]", "");
            String psGroupNum = psNumSplit[psNumSplit.length - 2].split("if\\(currentGroupObj == ")[1].replaceAll("[^\\d]", "");
            String assignRrGetRequest = URL_ASSIGN_RR_TO_IC + lastOneIc +
                    "&PseudoCatRefNum=" + psCatNum +
                    "&PseudoCatGroupRefNum=" + psGroupNum +
                    "&Policy=" + policyName +
                    "&ActSelected=A" +
                    "&RuleRefNum=" + rrId +
                    "&companyCode=" + psCatNum +
                    "&categorygroup=" + psGroupNum +
                    "&category=" + psCatNum +
                    "&policy=0" +
                    "&newPolicy=" + policyName;

            ensure200Code(get(assignRrGetRequest, executor).returnResponse().getStatusLine().getStatusCode());
            log.info("Reduction rule was assigned to " + insComp.getIcName());

            return psCatNum;

        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public String assignReductionRuleToIcAndAnyPolicy(String rrId, String insComp, String category) {
        try {
            String content = post(URL_ASSIGN_RR_TO_IC_PAGE, getReductionRuleAssigment(rrId), executor).returnContent().toString();

            String[] splitContentByIC = content.split(insComp)[0].split("<option value=");
            String lastOneIc = splitContentByIC[splitContentByIC.length - 1].replaceAll("[^\\d]", "");
            String contentByCat = content.split("categoryoption.options\\[count\\].text = \"" + category + "\";")[0];
            String[] psNumSplit = contentByCat.split("categoryoption.options\\[count\\].value = ");
            String psCatNum = psNumSplit[psNumSplit.length - 1].replaceAll("[^\\d]", "");
            String psGroupNum = psNumSplit[psNumSplit.length - 2].split("if\\(currentGroupObj == ")[1].replaceAll("[^\\d]", "");
            String assignRrGetRequest = URL_ASSIGN_RR_TO_IC + lastOneIc +
                    "&PseudoCatRefNum=" + psCatNum +
                    "&PseudoCatGroupRefNum=" + psGroupNum +
                    "&Policy=null" +
                    "&ActSelected=A" +
                    "&RuleRefNum=" + rrId +
                    "&companyCode=7" +
                    "&categorygroup=" + psGroupNum +
                    "&category=" + psCatNum +
                    "&policy=null";

            ensure200Code(get(assignRrGetRequest, executor).returnResponse().getStatusLine().getStatusCode());
            log.info("Reduction rule was assigned to " + insComp);
            return psCatNum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class IncorrectRRFormatException extends RuntimeException {

    }

    private static List<NameValuePair> getReductionRuleParams(ReductionRule reductionRule, boolean isPolicy, String claimReduc,
                                                              String ageFrom, String ageTo,
                                                              String priceRangeFrom, String priceRangeTo,
                                                              String documentation, String claimantRating) {
        List<NameValuePair> rrParams = new ArrayList<>();
        rrParams.add(new BasicNameValuePair("ruleNumber", "0"));
        rrParams.add(new BasicNameValuePair("actionType", "save"));
        rrParams.add(new BasicNameValuePair("isAssigned", "false"));
        rrParams.add(new BasicNameValuePair("xml", getRuleXmlString(reductionRule.getRrName(), isPolicy, reductionRule.getDescription1(), claimReduc, ageFrom, ageTo, priceRangeFrom, priceRangeTo, documentation, claimantRating)));
        rrParams.add(new BasicNameValuePair("name", reductionRule.getRrName()));
        if (isPolicy) {
            rrParams.add(new BasicNameValuePair("depreciation_type", "1"));
        } else {
            rrParams.add(new BasicNameValuePair("depreciation_type", "2"));
        }
        rrParams.add(new BasicNameValuePair("roundbase", ""));
        rrParams.add(new BasicNameValuePair("roundtype", "R"));
        rrParams.add(new BasicNameValuePair("roundrule", "A"));

        rrParams.add(new BasicNameValuePair("ruleLineNo", ""));
        rrParams.add(new BasicNameValuePair("desc", reductionRule.getDescription1()));
        rrParams.add(new BasicNameValuePair("from", ageFrom));
        rrParams.add(new BasicNameValuePair("to", ageTo));
        rrParams.add(new BasicNameValuePair("priceFrom", priceRangeFrom));
        rrParams.add(new BasicNameValuePair("priceTo", priceRangeTo));
        if (documentation.isEmpty()) {
            rrParams.add(new BasicNameValuePair("documentation", "null"));
        } else {
            rrParams.add(new BasicNameValuePair("documentation", documentation));
        }
        if (claimantRating.isEmpty()) {
            rrParams.add(new BasicNameValuePair("claimantRating", "0"));
        } else {
            rrParams.add(new BasicNameValuePair("claimantRating", claimantRating));
        }
        rrParams.add(new BasicNameValuePair("claimreduction", claimReduc));

        rrParams.add(new BasicNameValuePair("ruleLineNo", ""));
        rrParams.add(new BasicNameValuePair("desc", ""));
        rrParams.add(new BasicNameValuePair("from", ""));
        rrParams.add(new BasicNameValuePair("to", ""));
        rrParams.add(new BasicNameValuePair("priceFrom", ""));
        rrParams.add(new BasicNameValuePair("priceTo", ""));
        rrParams.add(new BasicNameValuePair("documentation", "null"));
        rrParams.add(new BasicNameValuePair("claimantRating", "0"));
        rrParams.add(new BasicNameValuePair("claimreduction", ""));

        return rrParams;
    }


    private static List<NameValuePair> getReductionRuleParamsWithTwoLines(ReductionRule reductionRule, boolean isPolicy,
                                                                          String claimReduc, String claimReduc2,
                                                                          String ageFrom, String ageFrom2, String ageTo, String ageTo2,
                                                                          String priceRangeFrom, String priceRangeFrom2, String priceRangeTo, String priceRangeTo2,
                                                                          String documentation, String documentation2, String claimantRating) {
        List<NameValuePair> rrParams = new ArrayList<>();
        rrParams.add(new BasicNameValuePair("ruleNumber", "0"));
        rrParams.add(new BasicNameValuePair("actionType", "save"));
        rrParams.add(new BasicNameValuePair("isAssigned", "false"));
        rrParams.add(new BasicNameValuePair("xml", getRuleXmlStringWithTwoLines(reductionRule.getRrName(), isPolicy, reductionRule.getDescription1(), claimReduc, claimReduc2,
                ageFrom, ageFrom2, ageTo, ageTo2, priceRangeFrom, priceRangeFrom2, priceRangeTo, priceRangeTo2, documentation, documentation2, claimantRating)));
        rrParams.add(new BasicNameValuePair("name", reductionRule.getRrName()));
        if (isPolicy) {
            rrParams.add(new BasicNameValuePair("depreciation_type", "1"));
        } else {
            rrParams.add(new BasicNameValuePair("depreciation_type", "2"));
        }
        rrParams.add(new BasicNameValuePair("roundbase", ""));
        rrParams.add(new BasicNameValuePair("roundtype", "R"));
        rrParams.add(new BasicNameValuePair("roundrule", "A"));

        rrParams.add(new BasicNameValuePair("ruleLineNo", ""));
        rrParams.add(new BasicNameValuePair("desc", reductionRule.getDescription1()));
        rrParams.add(new BasicNameValuePair("from", ageFrom));
        rrParams.add(new BasicNameValuePair("to", ageTo));
        rrParams.add(new BasicNameValuePair("priceFrom", priceRangeFrom));
        rrParams.add(new BasicNameValuePair("priceTo", priceRangeTo));
        if (documentation.isEmpty() || documentation.equals("Undefined")) {
            rrParams.add(new BasicNameValuePair("documentation", "null"));
        } else if (documentation.equals("Sufficient documentation")) {
            rrParams.add(new BasicNameValuePair("documentation", "1"));
        } else if (documentation.equals("Insufficient documentation")) {
            rrParams.add(new BasicNameValuePair("documentation", "2"));
        }
        if (claimantRating.isEmpty()) {
            rrParams.add(new BasicNameValuePair("claimantRating", "0"));
        } else {
            rrParams.add(new BasicNameValuePair("claimantRating", claimantRating));
        }
        rrParams.add(new BasicNameValuePair("claimreduction", claimReduc));

        rrParams.add(new BasicNameValuePair("ruleLineNo", ""));

        rrParams.add(new BasicNameValuePair("desc", reductionRule.getDescription1()));
        rrParams.add(new BasicNameValuePair("from", ageFrom2));
        rrParams.add(new BasicNameValuePair("to", ageTo2));
        rrParams.add(new BasicNameValuePair("priceFrom", priceRangeFrom2));
        rrParams.add(new BasicNameValuePair("priceTo", priceRangeTo2));
        if (documentation.isEmpty() || documentation.equals("Undefined")) {
            rrParams.add(new BasicNameValuePair("documentation", "null"));
        } else if (documentation.equals("Sufficient documentation")) {
            rrParams.add(new BasicNameValuePair("documentation", "1"));
        } else if (documentation.equals("Insufficient documentation")) {
            rrParams.add(new BasicNameValuePair("documentation", "2"));
        }
        if (claimantRating.isEmpty()) {
            rrParams.add(new BasicNameValuePair("claimantRating", "0"));
        } else {
            rrParams.add(new BasicNameValuePair("claimantRating", claimantRating));
        }
        rrParams.add(new BasicNameValuePair("claimreduction", claimReduc2));

        rrParams.add(new BasicNameValuePair("ruleLineNo", ""));

        rrParams.add(new BasicNameValuePair("desc", ""));
        rrParams.add(new BasicNameValuePair("from", ""));
        rrParams.add(new BasicNameValuePair("to", ""));
        rrParams.add(new BasicNameValuePair("priceFrom", ""));
        rrParams.add(new BasicNameValuePair("priceTo", ""));
        rrParams.add(new BasicNameValuePair("documentation", "null"));
        rrParams.add(new BasicNameValuePair("claimantRating", "0"));
        rrParams.add(new BasicNameValuePair("claimreduction", ""));

        return rrParams;
    }


    private static List<NameValuePair> getReductionRuleAssigment(String ruleId) {
        List<NameValuePair> rrParams = new ArrayList<>();
        rrParams.add(new BasicNameValuePair("actionType", ""));
        rrParams.add(new BasicNameValuePair("ruleNumber", ""));
        rrParams.add(new BasicNameValuePair("ruleREFNUM", ruleId));
        rrParams.add(new BasicNameValuePair("sqlSTATUS", ""));
        rrParams.add(new BasicNameValuePair("companyCode", "-1"));
        rrParams.add(new BasicNameValuePair("policy", "Any"));
        rrParams.add(new BasicNameValuePair("categorygroup", "-1"));
        rrParams.add(new BasicNameValuePair("category", "-1"));
        rrParams.add(new BasicNameValuePair("published", "-1"));
        rrParams.add(new BasicNameValuePair("searchText", ""));
        rrParams.add(new BasicNameValuePair("ruleItemsList", ruleId));

        return rrParams;
    }

    private static String getRuleXmlString(String rrName, boolean isPolicy, String description, String claimReduc,
                                           String ageFrom, String ageTo,
                                           String priceRangeFrom, String priceRangeTo,
                                           String documentation, String claimantRating) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<Rule id=\"0\" ")
                .append("name=\"" + rrName + "\"  ")
                .append("published=\"true\" ");
        if (isPolicy) {
            stringBuilder.append("depreciation_type=\"" + "1\" ");
        } else {
            stringBuilder.append("depreciation_type=\"" + "2\" ");
        }
        stringBuilder.append("max_depreciation=\"\" ")
                .append("life_span=\"\" ")
                .append("years_before_reduction=\"0\">")

                .append("<Ruledef>")
                .append("<interval>" + description + "</interval>")
                .append("<seqno>1</seqno>")
                .append("<claimreduc>0." + claimReduc + "</claimreduc>")
                .append("cashreduc/><type>false</type>")
                .append("<from>" + ageFrom + "</from>")
                .append("<to>" + ageTo + "</to>")
                .append("<priceRangeFrom>" + priceRangeFrom + "</priceRangeFrom>")
                .append("<priceRangeTo>" + priceRangeTo + "</priceRangeTo>");
        if (documentation.isEmpty()) {
            stringBuilder.append("<documentation>null</documentation>");
        } else {
            stringBuilder.append("<documentation>" + documentation + "</documentation>");
        }
        if (claimantRating.isEmpty()) {
            stringBuilder.append("<claimantRating>0</claimantRating>");
        } else {
            stringBuilder.append("<claimantRating>" + claimantRating + "</claimantRating>");
        }
        stringBuilder.append("</Ruledef></Rule>");

        return stringBuilder.toString();
    }

    private static String getRuleXmlStringWithTwoLines(String rrName, boolean isPolicy, String description, String claimReduc, String claimReduc2,
                                                       String ageFrom, String ageFrom2, String ageTo, String ageTo2,
                                                       String priceRangeFrom, String priceRangeFrom2, String priceRangeTo, String priceRangeTo2,
                                                       String documentation, String documentation2, String claimantRating) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<Rule id=\"0\" ")
                .append("name=\"" + rrName + "\"  ")
                .append("published=\"true\" ");
        if (isPolicy) {
            stringBuilder.append("depreciation_type=\"" + "1\" ");
        } else {
            stringBuilder.append("depreciation_type=\"" + "2\" ");
        }
        stringBuilder.append("max_depreciation=\"\" ")
                .append("life_span=\"\" ")
                .append("years_before_reduction=\"0\">")

                .append("<Ruledef>")
                .append("<interval>" + description + "</interval>")
                .append("<seqno>1</seqno>")
                .append("<claimreduc>0." + claimReduc + "</claimreduc>")
                .append("cashreduc/><type>false</type>")
                .append("<from>" + ageFrom + "</from>")
                .append("<to>" + ageTo + "</to>")
                .append("<priceRangeFrom>" + priceRangeFrom + "</priceRangeFrom>")
                .append("<priceRangeTo>" + priceRangeTo + "</priceRangeTo>");
        if (documentation.isEmpty() || documentation.equals("Undefined")) {
            stringBuilder.append("<documentation>null</documentation>");
        } else if (documentation.equals("Sufficient documentation")) {
            stringBuilder.append("<documentation>1</documentation>");
        } else if (documentation.equals("Insufficient documentation")) {
            stringBuilder.append("<documentation>2</documentation>");
        }

        if (claimantRating.isEmpty()) {
            stringBuilder.append("<claimantRating>0</claimantRating>");
        } else {
            stringBuilder.append("<claimantRating>" + claimantRating + "</claimantRating>");
        }
        stringBuilder.append("</Ruledef>")
                .append("<Ruledef>")
                .append("<interval>" + description + "</interval>")
                .append("<seqno>1</seqno>")
                .append("<claimreduc>0." + claimReduc2 + "</claimreduc>")
                .append("cashreduc/><type>false</type>")
                .append("<from>" + ageFrom2 + "</from>")
                .append("<to>" + ageTo2 + "</to>")
                .append("<priceRangeFrom>" + priceRangeFrom2 + "</priceRangeFrom>")
                .append("<priceRangeTo>" + priceRangeTo2 + "</priceRangeTo>");
        if (documentation2.isEmpty() || documentation2.equals("Undefined")) {
            stringBuilder.append("<documentation>null</documentation>");
        } else if (documentation2.equals("Sufficient documentation")) {
            stringBuilder.append("<documentation>1</documentation>");
        } else if (documentation2.equals("Insufficient documentation")) {
            stringBuilder.append("<documentation>2</documentation>");
        }
        if (claimantRating.isEmpty()) {
            stringBuilder.append("<claimantRating>0</claimantRating>");
        } else {
            stringBuilder.append("<claimantRating>" + claimantRating + "</claimantRating>");
        }

        stringBuilder.append("</Ruledef></Rule>");

        return stringBuilder.toString();
    }
}
