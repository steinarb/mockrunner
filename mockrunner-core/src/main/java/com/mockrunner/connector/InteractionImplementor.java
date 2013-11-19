package com.mockrunner.connector;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

/**
 * Implementations of this interface can be addded to {@link InteractionHandler}.
 * When one of the two <code>execute</code> methods of {@link com.mockrunner.mock.connector.cci.MockInteraction}
 * are called, the {@link InteractionHandler} iterates through the implementors
 * and dispatches the <code>execute</code> call to the first one which returns
 * <code>true</code> for {@link #canHandle}.
 * There are several implementations of this interface, e.g.
 * {@link StreamableRecordByteArrayInteraction},
 * {@link WSIFInteraction},
 * {@link IndexedRecordInteraction},
 * {@link MappedRecordInteraction}.
 * Of course, you can also implement your own version and it may be necessary
 * in many situations.
 */
public interface InteractionImplementor 
{
    /**
     * Implementors should return <code>true</code> if this implementor can handle the request.
     * Please note that for calls to {@link #execute(InteractionSpec, Record)},
     * the second <code>Record</code> parameter is <code>null</code>.
     * @param interactionSpec the <code>InteractionSpec</code> for the actual call
     * @param actualRequest the request for the actual call
     * @param actualResponse the response for the actual call, may be <code>null</code>
     * @return <code>true</code> if this implementor will handle the request and
     *         will return the specified response, <code>false</code> otherwise
     */
    public boolean canHandle(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse);
    
    /**
     * First version of the <code>Interaction.execute</code> methods.
     * @param interactionSpec the interaction spec
     * @param actualRequest the actual request
     * @return the response according to the current request
     */
    public Record execute(InteractionSpec interactionSpec, Record actualRequest) throws ResourceException;
    
    /**
     * Second version of the <code>Interaction.execute</code> methods.
     * @param interactionSpec the interaction spec
     * @param actualRequest the actual request
     * @param actualResponse the actual response
     * @return <code>true</code> under normal conditions
     */
    public boolean execute(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse) throws ResourceException;
}
