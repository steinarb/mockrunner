package com.mockrunner.example.jdbc;

import org.apache.struts.action.ActionForm;

/**
 * The <code>ActionForm</code> for the {@link PayAction}.
 */
public class PayForm extends ActionForm
{
    private String customerId;
    private String billId;
    private double amount;
      
    public double getAmount()
    {
        return amount;
    }

    public String getBillId()
    {
        return billId;
    }

    public String getCustomerId()
    {
        return customerId;
    }
   
    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public void setBillId(String billId)
    {
        this.billId = billId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    } 
}
