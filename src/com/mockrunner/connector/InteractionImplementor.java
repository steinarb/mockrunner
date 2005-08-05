package com.mockrunner.connector;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

public interface InteractionImplementor 
{
    public boolean canHandle(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse);
    
    public Record execute(InteractionSpec interactionSpec, Record actualRequest) throws ResourceException;
    
    public boolean execute(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse) throws ResourceException;
}
