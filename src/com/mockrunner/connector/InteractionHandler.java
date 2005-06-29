package com.mockrunner.connector;

import java.util.Iterator;
import java.util.Vector;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

public class InteractionHandler 
{
	private Vector implementors = null;

	public InteractionHandler() 
    {
		implementors = new Vector();
	}
	
	public void addImplementor(InteractionImplementor ii)
    {
		implementors.add(ii);
	}
    
    public void clearImplementors()
    {
        implementors.clear();
    }
	
	public boolean execute(InteractionSpec is, Record request, Record response) throws ResourceException 
    {
		Iterator iter= implementors.iterator();
		while (iter.hasNext())
        {
			InteractionImplementor ii= (InteractionImplementor)iter.next();
			if (ii.execute(is, request, response))
            {
				// was processed here
				return true;
			}
		}
		// do I need to throw here?
		return false;
	}
}
