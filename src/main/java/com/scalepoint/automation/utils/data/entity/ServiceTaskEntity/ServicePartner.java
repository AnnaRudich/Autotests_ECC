package com.scalepoint.automation.utils.data.entity.ServiceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServicePartner
{
    @XmlElement
    private String phone;
    @XmlElement
    private String postalCode;
    @XmlElement
    private String cvrNumber;
    @XmlElement
    private String email;
    @XmlElement
    private String name;
    @XmlElement
    private String address1;

    private Bank bank;
    @XmlElement
    private String city;

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getPostalCode ()
    {
        return postalCode;
    }

    public void setPostalCode (String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getCvrNumber ()
    {
        return cvrNumber;
    }

    public void setCvrNumber (String cvrNumber)
    {
        this.cvrNumber = cvrNumber;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getAddress1 ()
    {
        return address1;
    }

    public void setAddress1 (String address1)
    {
        this.address1 = address1;
    }

    public Bank getBank ()
    {
        return bank;
    }

    public void setBank (Bank bank)
    {
        this.bank = bank;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [phone = "+phone+", postalCode = "+postalCode+", cvrNumber = "+cvrNumber+", email = "+email+", name = "+name+", address1 = "+address1+", bank = "+bank+", city = "+city+"]";
    }
}
