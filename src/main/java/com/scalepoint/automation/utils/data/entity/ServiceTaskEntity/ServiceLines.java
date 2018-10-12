package com.scalepoint.automation.utils.data.entity.ServiceTaskEntity;


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
