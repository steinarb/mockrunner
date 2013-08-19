package com.mockrunner.example.struts;

import org.apache.struts.action.ActionForm;

/**
 * The <code>ActionForm</code> for the {@link OrderAction}.
 */
public class OrderForm extends ActionForm
{
    private String id;
    private int amount;
    
    public int getAmount()
    {
        return amount;
    }

    public String getId()
    {
        return id;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
