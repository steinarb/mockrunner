package com.mockrunner.example.struts;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of {@link OrderManager}.
 * Used in the test {@link OrderActionTest}.
 */
public class MockOrderManager extends OrderManager
{
    private Map products = new HashMap();
    
    public MockOrderManager()
    {
    
    }

    public void setStock(String id, int amount)
    {
        products.put(id, new Integer(amount));
    }
    
    public int getStock(String id)
    {
        Integer amount = (Integer)products.get(id);
        if(null == amount) return 0;
        return amount.intValue();
    }

    public void order(String id, int amount)
    {
        int available = getStock(id);
        if(available < amount) throw new RuntimeException("not enough in stock");
        Integer newStock = new Integer(available - amount);
        products.put(id, newStock);
    }

}
