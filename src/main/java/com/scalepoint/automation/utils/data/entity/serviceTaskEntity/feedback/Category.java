package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.feedback;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Category
{
    @XmlAttribute
    private String parentCategory;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String uniqueId;

    public Category(){
        com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Category category
                = new com.scalepoint.automation.utils.data.entity.serviceTaskEntity.taskData.Category();

        this.parentCategory = category.getParentCategory();
        this.name = category.getName();
        this.uniqueId = category.getUniqueId();
    }

    public String getParentCategory ()
    {
        return parentCategory;
    }

    public void setParentCategory (String parentCategory)
    {
        this.parentCategory = parentCategory;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getUniqueId ()
    {
        return uniqueId;
    }

    public void setUniqueId (String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [parentCategory = "+parentCategory+", name = "+name+", uniqueId = "+uniqueId+"]";
    }
}
