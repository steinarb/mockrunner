package com.mockrunner.jms;

/**
 * The <code>ConfigurationManager</code> is used
 * for global settings of the JMS test framework.
 */
public class ConfigurationManager
{
    private boolean doCloneOnSend;
    private boolean useMessageSelectors;
    
    public ConfigurationManager()
    {
        doCloneOnSend = false;
        useMessageSelectors = true;
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
    
    /**
     * Get if message selectors should be used.
     * @return <code>true</code> use message selectors,
     *         <code>false</code> ignore message selectors
     */
    public boolean getUseMessageSelectors()
    {
        return useMessageSelectors;
    }

    /**
     * Set if message selectors should be used or simply
     * ignored while testing. Default is <code>true</code>,
     * i.e. message selectors are used. Message selector support
     * of Mockrunner is based on a modified version of the
     * selector parser of the open source JMS implementation
     * ActiveMQ. It is a bit experimental at the moment. If there
     * are problems with the parsing or if you don't need message
     * selectors at all, turn them off. Disabling selector parsing also
     * results in a better test performance.
     * @param useMessageSelectors <code>true</code> use message selectors,
     *                            <code>false</code> ignore message selectors
     */
    public void setUseMessageSelectors(boolean useMessageSelectors)
    {
        this.useMessageSelectors = useMessageSelectors;
    }
}
