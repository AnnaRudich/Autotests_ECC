package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TextSearch {
    @XmlElement
    private String simpleQuery;
    @XmlElement
    private String complexQuery;
    @XmlElement
    private String shortQuery;
    @XmlElement
    private String maxQuery;
    @XmlElement
    private String maxQueryLength;
    @XmlElement
    private String catLevel2;
    @XmlElement
    private String catLevel3;
    @XmlElement
    private String catLevel4;
    @XmlElement
    private String attribute;
    @XmlElement
    private String popProduct1;
    @XmlElement
    private String catProduct1;
    @XmlElement
    private String catProduct1Category;
    @XmlElement
    private String nonorderableProduct1;
    @XmlElement
    private String dndProduct1;
    @XmlElement
    private String dndProductVoucher1;
    @XmlElement
    private String dndProductVoucher2;
    @XmlElement
    private String dndProductVoucher3;
    @XmlElement
    private String benchmarkQueries;
    @XmlElement
    private String queriesWithMistake;
    @XmlElement
    private String fixedQueries;
    @XmlElement
    private String synonyms;
    @XmlElement
    private String queryWithSymbols;
    @XmlElement
    private String fixedQueryWithSymbols;

    @XmlElement
    private String queryWithOriginalLetters;

    @XmlElement
    private String fixedQueryWithOriginalLetters;

    @XmlElement
    private String manualProduct;

    @XmlElement
    private String orderableProduct1;
    @XmlElement
    private String orderableProduct2;
    @XmlElement
    private String orderableProduct3;
    @XmlElement
    private String nonorderableProduct2;
    @XmlElement
    private String bnOProduct;
    @XmlElement
    private String brand1;
    @XmlElement
    private String model1;
    @XmlElement
    private String group1;
    @XmlElement
    private String subgroup1;
    @XmlElement
    private String subgroup2;
    @XmlElement
    private String brokenQuery1;
    @XmlElement
    private String brokenQueryWithSpecialSymbols1;

    @XmlElement
    private String bnOProduct2;

    public String getSimpleQuery() {
        return simpleQuery;
    }

    public String getComplexQuery() {
        return complexQuery;
    }

    public String getShortQuery() {
        return shortQuery;
    }

    public String getMaxQuery() {
        return maxQuery;
    }

    public String getMaxQueryLength() {
        return maxQueryLength;
    }

    public String getCatLevel2() {
        return catLevel2;
    }

    public String getCatLevel3() {
        return catLevel3;
    }

    public String getCatLevel4() {
        return catLevel4;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getPopProduct1() {
        return popProduct1;
    }

    public String getCatProduct1() {
        return catProduct1;
    }

    public String getCatProduct1Category() {
        return catProduct1Category;
    }

    public String getNonorderableProduct1() {
        return nonorderableProduct1;
    }

    public String getDndProduct1() {
        return dndProduct1;
    }

    public String getDndProductVoucher1() {
        return dndProductVoucher1;
    }

    public String getDndProductVoucher2() {
        return dndProductVoucher2;
    }

    public String getDndProductVoucher3() {
        return dndProductVoucher3;
    }

    public String[] getBenchmarkQueryList() {
        String[] benchmarkQueryList = benchmarkQueries.split(",");
        for (int i = 0; i < benchmarkQueryList.length; i++) {
            benchmarkQueryList[i] = benchmarkQueryList[i].trim();
        }
        return benchmarkQueryList;
    }

    public String getDYMQueryWithSpecialSymbols() {
        return queryWithSymbols;
    }

    public String getFixedDYMQueryWithSpecialSymbols() {
        return fixedQueryWithSymbols;
    }

    public String[] getDYMQueries() {
        return queriesWithMistake.split(",");
    }

    public String[] getFixedDYMQueries() {
        fixedQueries = fixedQueries.toLowerCase();
        return fixedQueries.split(",");
    }

    public String getFixedQueries() {
        return fixedQueries;
    }

    public String[] getSynonymList() {
        return synonyms.split(",");
    }

    public String[] getDYMQueriesWithOriginalLetters() {
        return queryWithOriginalLetters.split(",");
    }

    public String[] getFixedDYMQueriesWithOriginalLetters() {
        return fixedQueryWithOriginalLetters.split(",");
    }

    public String getManualProductQuery() {
        return manualProduct;
    }

    public String getOrderableProductWithIpBiggerMp() {
        return orderableProduct1;
    }

    public String getOrderableProductWithIpEqualMp() {
        return orderableProduct2;
    }

    public String getOrderableProductWithIpLowerMp() {
        return orderableProduct3;
    }

    public String getNonrderableProductWithIpLowerMp() {
        return nonorderableProduct2;
    }

    public String getProductWithOnlyVoucherHandling() {
        return bnOProduct;
    }

    public String getProductWithOnlyVoucherHandling2() {
        return bnOProduct2;
    }

    public String getBrand1() {
        return brand1;
    }

    public String getModel1() {
        return model1;
    }

    public String getGroup1() {
        return group1;
    }

    public String getSubgroup1() {
        return subgroup1;
    }

    public String getSubgroup2() {
        return subgroup2;
    }

    public String getBrokenQuery1() {
        return brokenQuery1;
    }

    public String getBrokenQueryWithSpecialSymbols1() {
        return brokenQueryWithSpecialSymbols1;
    }
}
