package com.mockrunner.connector;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

public interface InteractionImplementor 
{
	public boolean execute(InteractionSpec is, Record request, Record response) throws ResourceException;
}
