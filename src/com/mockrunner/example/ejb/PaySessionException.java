package com.mockrunner.example.ejb;

/**
 * Exception class for 
 * {@link PaySessionBean}.
 */
public class PaySessionException extends Exception
{
    public final static int UNKNOWN = 0;
    public final static int UNKNOWN_CUSTOMER = 1;
    public final static int UNKNOWN_BILL = 2;
    public final static int WRONG_BILL_FOR_CUSTOMER = 3;
    public final static int WRONG_AMOUNT_FOR_BILL = 4;
    
    private int code = UNKNOWN;
    
    public PaySessionException(int code)
    {
        this.code = code;
    }
    
    public int getCode()
    {
        return code;
    }
}
