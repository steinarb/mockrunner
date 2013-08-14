package com.mockrunner.connector;

import java.lang.reflect.Method;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;

/**
 * Implementor for IBM environments using the
 * Web Services Invocation Framework
 */
public class WSIFInteraction implements InteractionImplementor 
{
    private boolean enabled;
    private String isClassName;
    private String requestPartName;
    private Object requestPart;
    private String responsePartName;
    private Object responsePart;

	/**
	 * @param isClassName would be com.ibm.connector2.ims.ico.IMSInteractionSpec, com.ibm.connector2.cics.ECIInteractionSpec
	 */
	public WSIFInteraction(String isClassName,
		                   String requestPartName,
		                   Object requestPart,
		                   String responsePartName,
		                   Object responsePart) 
    {
		super();
		this.isClassName = isClassName;
		this.requestPartName = requestPartName;
		this.requestPart = requestPart;
		this.responsePartName = responsePartName;
		this.responsePart = responsePart;
        this.enabled = true;
	}
    
    /**
     * Enables this implementor.
     */
    public void enable()
    {
        this.enabled = true;
    }
    
    /**
     * Disables this implementor. {@link #canHandle(InteractionSpec, Record, Record)}
     * always returns <code>false</code>, if this implementor is disabled.
     */
    public void disable()
    {
        this.enabled = false;
    }

	public boolean canHandle(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse)
    {
        if(!enabled) return false;
        Class specClass = getClassNamed(interactionSpec.getClass(), isClassName);
        if (specClass != null)
        {
            try
            {
                Class wsifMessageClass = getClassNamed(actualRequest.getClass(), "org.apache.wsif.base.WSIFDefaultMessage");
                Class wsifFormatPartClass = getClassNamed(requestPart.getClass(), "com.ibm.wsif.format.jca.WSIFFormatPartImpl");
                if (wsifMessageClass != null && wsifFormatPartClass != null)
                {
                    Class[] gopParams = new Class[1];
                    gopParams[0] = String.class;
                    Method gop = wsifMessageClass.getMethod("getObjectPart", gopParams);
                    if (gop != null)
                    {
                        Object[] gopArgs = new Object[1];
                        gopArgs[0] = requestPartName;
                        Object o1 = gop.invoke(actualRequest, gopArgs);
                        if (o1 != null)
                        {
                            Class[] elementsParams = new Class[0];
                            Method elements = wsifFormatPartClass.getMethod("elements", elementsParams);
                            if (elements != null)
                            {
                                Object[] elementsArgs = new Object[0];
                                Object map1 = elements.invoke(requestPart, elementsArgs);
                                Object map2 = elements.invoke(o1, elementsArgs);
                                if (map1.equals(map2))
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } 
            catch(Exception exc)
            {
                return false;
            }
        }
        return false;
    }

    /**
     * not implemented yet.
     */
    public Record execute(InteractionSpec interactionSpec, Record actualRequest) throws ResourceException
    {
        throw new RuntimeException(this.getClass().getName() + " does not implement public Record execute(InteractionSpec, Record)");
    }

    public boolean execute(InteractionSpec interactionSpec, Record actualRequest, Record actualResponse) throws ResourceException
    {
        if(!canHandle(interactionSpec, actualRequest, actualResponse)) return false;
        try
        {
            Class wsifMessageClass = getClassNamed(actualRequest.getClass(), "org.apache.wsif.base.WSIFDefaultMessage");
            Class[] sopParams = new Class[2];
            sopParams[0] = String.class;
            sopParams[1] = Object.class;
            Method sop = wsifMessageClass.getMethod("setObjectPart", sopParams);
            if (sop != null)
            {
                Object[] sopArgs = new Object[2];
                sopArgs[0] = responsePartName;
                sopArgs[1] = responsePart;
                sop.invoke(actualResponse, sopArgs);
                return true;
            }
        } 
        catch(Exception exc)
        {
            ResourceException resExc = new ResourceException("execute() failed");
            resExc.setLinkedException(exc);
            throw resExc;
        }
        return false;
    }

    /**
     * 
     * @param cl
     * @param className
     * @return null if not found
     */
    private Class getClassNamed(Class cl, String className)
    {
        if (cl == null)
        {
            return null;
        }
        if (cl.getName().equals(className))
        {
            return cl;
        }
        Class[] classes = cl.getDeclaredClasses();
        for (int current = 0; current < classes.length; current++)
        {
            Class c = classes[current];
            if (className.equals(c.getName()))
            {
                return c;
            }
        }
        return getClassNamed(cl.getSuperclass(), className);
    }
}

