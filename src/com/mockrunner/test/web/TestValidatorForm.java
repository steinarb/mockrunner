package com.mockrunner.test.web;

import org.apache.struts.validator.ValidatorForm;

public class TestValidatorForm extends ValidatorForm
{
    private String firstName;
    private String lastName;
    
    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
