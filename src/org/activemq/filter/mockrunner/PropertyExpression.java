/** 
 * 
 * Copyright 2004 Protique Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 * 
 **/

package org.activemq.filter.mockrunner;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Alwin Ibba: Changed package
 * 
 * Represents a property  expression
 * 
 * @version $Revision: 1.2 $
 */
public class PropertyExpression implements Expression {

    private String name;

    public PropertyExpression(String name) {
        this.name = name;
    }

    public Object evaluate(Message message) throws JMSException {
        Object result = null;
        if (name != null) {
            result = message.getObjectProperty(name);
        }
        if (result == null) {
            //see if a defined header property
            if (name.equals("JMSType")) {
                result = message.getJMSType();
            }
            else if (name.equals("JMSMessageID")) {
                result = message.getJMSMessageID();
            }
            else if (name.equals("JMSCorrelationID")) {
                result = message.getJMSCorrelationID();
            }
            else if (name.equals("JMSPriority")) {
                result = new Integer(message.getJMSPriority());
            }
            else if (name.equals("JMSTimestamp")) {
                result = new Long(message.getJMSTimestamp());
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {

        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }
        return name.equals(((PropertyExpression) o).name);

    }

}
