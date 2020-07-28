package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String taskId;
    private String serviceAgreementInformation;
    private String serviceAgreement;
    private String address;
    private String email;
    private String linesCount;
    private List<Line> lines;
    private List<String> attachments;
    private String noteToServicePartner;
    private String claimantEmailIsDefined;
    private String sendNoteToClaimant;
    private String noteToClaimant;
    private boolean attachmentsAsLinks;
    private String damageTypeList;
    private String damageType;
    private boolean showDamageTypes;
}
