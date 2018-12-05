package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.feedback;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="serviceTask")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceTask extends com.scalepoint.automation.utils.data.entity.serviceTaskEntity.ServiceTask{

    @XmlAttribute
    private String takenSelfRisk;

    @XmlAttribute
    private String createdDate;

    @XmlAttribute
    private String uniqueId;

    @XmlElement
    private Invoice invoice;

    @XmlElement
    private ServiceLines serviceLines;

    @XmlElement
    private ServicePartner servicePartner;

    public ServiceTask(com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.ServiceTasks serviceTasks){
        com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.ServiceTask serviceTask
                = serviceTasks.getServiceTaskList().get(0);
        this.createdDate = serviceTask.getCreatedDate();
        this.uniqueId = serviceTask.getUniqueId();
        this.invoice = new InvoiceBuilder().build();
        this.servicePartner = new ServicePartner();
    }

    public String getCreatedDate ()
    {
        return createdDate;
    }

    public void setCreatedDate (String createdDate)
    {
        this.createdDate = createdDate;
    }

    public ServiceLines getServiceLines ()
    {
        return serviceLines;
    }

    public void setServiceLines (ServiceLines serviceLines)
    {
        this.serviceLines = serviceLines;
    }



    public ServicePartner getServicePartner ()
    {
        return servicePartner;
    }

    public void setServicePartner (ServicePartner servicePartner)
    {
        this.servicePartner = servicePartner;
    }

    public String getUniqueId ()
    {
        return uniqueId;
    }

    public void setUniqueId (String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getTakenSelfRisk() {
        return takenSelfRisk;
    }

    public void setTakenSelfRisk(String takenSelfRisk) {
        this.takenSelfRisk = takenSelfRisk;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [invoice = "+invoice+", takenSelfRisk = "+takenSelfRisk+", createdDate = "+createdDate+", serviceLines = "+serviceLines+", servicePartner = "+servicePartner+", uniqueId = "+uniqueId+"]";
    }
}
