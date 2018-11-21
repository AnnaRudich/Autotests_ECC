package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;


public class ServiceLines
{
    private ServiceLine serviceLine;

    public ServiceLine getServiceLine ()
    {
        return serviceLine;
    }

    public void setServiceLine (ServiceLine serviceLine)
    {
        this.serviceLine = serviceLine;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [serviceLine = "+serviceLine+"]";
    }
}
