package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sro on 10/11/13.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PasswordsVerification {
    @XmlElement
    private String numericalPass;
    @XmlElement
    private String specialCharPass;
    @XmlElement
    private String numAfterSpPass;
    @XmlElement
    private String numBeforeSpPass;
    @XmlElement
    private String numMiddleSpPass;
    @XmlElement
    private String difCasesPass;
    @XmlElement
    private String sameCharPass;
    @XmlElement
    private String customUpperPass;
    @XmlElement
    private String customLowPass;
    @XmlElement
    private String customUpLowPass;
    @XmlElement
    private String customUpLowSpecialPass;
    @XmlElement
    private String alertNotPartString;
    @XmlElement
    private String alertNotPartString2;
    @XmlElement
    private String altPass;
    @XmlElement
    private String charNumericalPass;
    @XmlElement
    private String nonLatynPass;
    @XmlElement
    private String passwordMustFollowRulesInfo;
    @XmlElement
    private String noPasswordInfo;

    public String getNonLatynPass() {
        return nonLatynPass;
    }

    public String getAltPass() {
        return altPass;
    }

    public String getCharNumericalPass() {
        return charNumericalPass;
    }

    public String getNumericalPass() {
        return numericalPass;
    }

    public String getSpecialCharPass() {
        return specialCharPass;
    }

    public String getNumAfterSpPass() {
        return numAfterSpPass;
    }

    public String getNumBeforeSpPass() {
        return numBeforeSpPass;
    }

    public String getNumMiddleSpPass() {
        return numMiddleSpPass;
    }

    public String getDifCasesPass() {
        return difCasesPass;
    }

    public String getSameCharPass() {
        return sameCharPass;
    }

    public String getCustomUpperPass() {
        return customUpperPass;
    }

    public String getCustomLowPass() {
        return customLowPass;
    }

    public String getCustomUpLowPass() {
        return customUpLowPass;
    }

    public String getCustomUpLowSpecialPass() {
        return customUpLowSpecialPass;
    }

    public String getAlertNotPartString() {
        return alertNotPartString;
    }

    public String getAlertNotPartString2() {
        return alertNotPartString2;
    }

    public String getPasswordMustFollowRulesMsg() {
        return passwordMustFollowRulesInfo;
    }

    public String getNoPasswordMsg() {
        return noPasswordInfo;
    }
}


