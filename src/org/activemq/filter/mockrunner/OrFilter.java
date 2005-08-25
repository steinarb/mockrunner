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
 * Represents a logical OR operation on two filters
 *
 * @version $Revision: 1.2 $
 */
public class OrFilter implements Filter {

    private Filter left;
    private Filter right;

    public OrFilter(Filter left, Filter right) {
        this.left = left;
        this.right = right;
    }

    public boolean matches(Message message) throws JMSException {
        if (left.matches(message)) {
            return true;
        }
        else {
            return right.matches(message);
        }
    }

    public boolean isWildcard() {
        return left.isWildcard() || right.isWildcard();
    }

    public Filter getLeft() {
        return left;
    }

    public Filter getRight() {
        return right;
    }
}
