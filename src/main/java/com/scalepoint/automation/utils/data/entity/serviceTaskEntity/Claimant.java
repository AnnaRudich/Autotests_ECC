package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

/**
 * Created by aru on 2018-11-20.
 */
public class Claimant {

    private String name;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+"]";
    }
}
