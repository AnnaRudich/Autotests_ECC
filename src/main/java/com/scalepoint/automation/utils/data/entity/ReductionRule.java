package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReductionRule implements Cloneable {

    private String rrName = RandomUtils.randomName("RR");
    private String policyName1 = "Pola" + RandomUtils.randomInt();
    private String policyName2 = "Polb" + RandomUtils.randomInt();
    private String description1 = "Desc1" + RandomUtils.randomInt();
    //    @XmlElement
//    private String description1;
    @XmlElement
    private String ageFrom1;
    @XmlElement
    private String ageTo1;

    @XmlElement
    private String ageFrom2;
    @XmlElement
    private String ageTo2;

    @XmlElement
    private String claimReduction1;

    @XmlElement
    private String claimReduction2;

    @XmlElement
    private String priceRangeFrom0;
    @XmlElement
    private String priceRangeTo0;

    @XmlElement
    private String priceRangeFrom1;
    @XmlElement
    private String priceRangeTo1;

    @XmlElement
    private String priceRangeFrom2;
    @XmlElement
    private String priceRangeTo2;

    @XmlElement
    private String policyAssignmentAny;
    @XmlElement
    private String policyAssignmentAddNew;

    public String getRrName() {
        return rrName;
    }

    public void setRrName(String rrName) {
        this.rrName = rrName;
    }

    public String getDescription1() {
        return description1;
    }

    public String getAgeFrom1() {
        return ageFrom1;
    }

    public void setAgeFrom1(String ageFrom1) {
        this.ageFrom1 = ageFrom1;
    }

    public String getAgeTo1() {
        return ageTo1;
    }

    public void setAgeTo1(String ageTo1) {
        this.ageTo1 = ageTo1;
    }

    public String getAgeFrom2() {
        return ageFrom2;
    }

    public void setAgeFrom2(String ageFrom2) {
        this.ageFrom2 = ageFrom2;
    }

    public String getAgeTo2() {
        return ageTo2;
    }

    public void setAgeTo2(String ageTo2) {
        this.ageTo2 = ageTo2;
    }

    public String getClaimReduction1() {
        return claimReduction1;
    }

    public void setClaimReduction1(String claimReduction1) {
        this.claimReduction1 = claimReduction1;
    }

    public String getClaimReduction2() {
        return claimReduction2;
    }

    public void setClaimReduction2(String claimReduction2) {
        this.claimReduction1 = claimReduction2;
    }

    public String getPriceRangeFrom0() {
        return priceRangeFrom0;
    }

    public void setPriceRangeFrom0(String priceRangeFrom0) {
        this.priceRangeFrom0 = priceRangeFrom0;
    }

    public String getPriceRangeTo0() {
        return priceRangeTo0;
    }

    public void setPriceRangeTo0(String priceRangeTo0) {
        this.priceRangeTo0 = priceRangeTo0;
    }

    public String getPriceRangeFrom1() {
        return priceRangeFrom1;
    }

    public void setPriceRangeFrom1(String priceRangeFrom1) {
        this.priceRangeFrom1 = priceRangeFrom1;
    }

    public String getPriceRangeTo1() {
        return priceRangeTo1;
    }

    public void setPriceRangeTo1(String priceRangeTo1) {
        this.priceRangeTo1 = priceRangeTo1;
    }

    public String getPolicyAssignmentAny() {
        return policyAssignmentAny;
    }

    public String getPolicyAssignmentAddNew() {
        return policyAssignmentAddNew;
    }

    public ReductionRule clone() throws CloneNotSupportedException {
        return (ReductionRule) super.clone();
    }

    public String getPriceRangeFrom2() {
        return priceRangeFrom2;
    }

    public String getPriceRangeTo2() {
        return priceRangeTo2;
    }

    public String getPolicyName1() {
        return policyName1;
    }

    public void setPolicyName1(String policyName1) {
        this.policyName1 = policyName1;
    }

    public String getPolicyName2() {
        return policyName2;
    }

    public void setPolicyName2(String policyName2) {
        this.policyName2 = policyName2;
    }
}
