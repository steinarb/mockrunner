package com.mockrunner.jms;

/**
 * The <code>ConfigurationManager</code> is used
 * for global settings of the JMS test framework.
 */
public class ConfigurationManager
{
    private boolean doCloneOnSend;
    
    public ConfigurationManager()
    {
        doCloneOnSend = false;
    }
    
    /**
     * Get the clone on send flag, see {@link #setDoCloneOnSend}
     * for a description of this option.
     * @return the clone on send flag
     */
    public boolean getDoCloneOnSend()
    {
        return doCloneOnSend;
    }
    
    /**
     * Set if a message should be cloned before sending it.
     * Default is <code>false</code>, i.e. the message is not
     * cloned. This has the advantage that the sent message can
     * be examined afterwards (e.g. if it is acknowledged).
     * If you set this to <code>true</code>, the message will
     * be cloned, i.e. the sent message will not be altered
     * and you have to obtain the received message in order
     * to examine it. However, the <code>true</code> option
     * is closer to a real JMS server, where you can send
     * the same message multiple times and the messages do
     * not influence each other.
     * @param doCloneOnSend the clone on send flag,
     *        default is <code>false</code>
     */
    public void setDoCloneOnSend(boolean doCloneOnSend)
    {
        this.doCloneOnSend = doCloneOnSend;
    }
}
