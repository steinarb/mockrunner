package com.mockrunner.example.struts;

import org.apache.struts.validator.ValidatorActionForm;

public class GreetingsValidatorForm extends ValidatorActionForm
{
    private String name;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
