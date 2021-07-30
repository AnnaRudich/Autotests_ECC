package com.scalepoint.automation.utils.data.entity.communicationDesignerEmailTemplates;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommunicationDesignerEmailTemplates{

    @XmlElements({
            @XmlElement(name = "selfServiceEmailTemplate", type = SelfServiceEmailTemplate.class),
            @XmlElement(name = "customerWelcomeRejectionMail", type = CustomerWelcomeRejectionMailEmailTemplate.class),
            @XmlElement(name = "customerWelcomeEmailTemplate", type = CustomerWelcomeEmailTemplate.class),
            @XmlElement(name = "emailTemplate", type = EmailTemplate.class),
            @XmlElement(name = "customerWelcomeWithOutstandingEmailTemplate", type = CustomerWelcomeWithOutstandingEmailTemplate.class),
            @XmlElement(name = "orderConfirmationEmailTemplate", type = OrderConfirmationEmailTemplate.class),
            @XmlElement(name = "replacementMailEmailTemplate", type = ReplacementMailEmailTemplate.class),
            @XmlElement(name = "automaticCustomerWelcomeEmailTemplate", type = AutomaticCustomerWelcomeEmailTemplate.class),
            @XmlElement(name = "itemizationSubmitLossItemsEmailTemplate", type = ItemizationSubmitLossItemsEmailTemplate.class),
            @XmlElement(name = "itemizationSaveLossItemsEmailTemplate", type = ItemizationSaveLossItemsEmailTemplate.class),
            @XmlElement(name = "attachmentEmailTemplate", type = AttachmentEmailTemplate.class),
    })
    List<EmailTemplate> emailTemplates;

    public <T extends EmailTemplate> T getEmailTemplateByClass(Class<T> clazz){

        return (T) emailTemplates.stream()
                .filter(emailTemplate -> emailTemplate.getClass().equals(clazz))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
