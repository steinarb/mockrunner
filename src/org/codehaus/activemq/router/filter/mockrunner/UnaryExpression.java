/** 
 * 
 * Copyright 2004 Hiram Chirino
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
package org.codehaus.activemq.router.filter.mockrunner;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * An expression which performs an operation on two expression values
 * 
 * Alwin Ibba: Changed package
 * 
 * @version $Revision: 1.1 $
 */
public abstract class UnaryExpression implements Expression {

    protected Expression right;

    public static Expression createNegate(Expression left) {
        return new UnaryExpression(left) {
            public Object evaluate(Message message) throws JMSException {
                Object lvalue = right.evaluate(message);
                if( lvalue == null )
                    return null;
                if (lvalue instanceof Number) { return negate((Number) lvalue); }
                throw new RuntimeException("Cannot call negate operation on: " + lvalue);
            }

            public String getExpressionSymbol() {
                return "-";
            }
        };
    }

    abstract static class BooleanUnaryExpression extends UnaryExpression implements BooleanExpression {
        public BooleanUnaryExpression(Expression left) {
            super(left);
        }
    };
    
    public static BooleanExpression createNOT(BooleanExpression left) {
        return new BooleanUnaryExpression(left) {
            public Object evaluate(Message message) throws JMSException {
                Boolean lvalue = (Boolean)right.evaluate(message);
                if( lvalue == null )
                    return null;
                return lvalue.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
            }
            public String getExpressionSymbol() {
                return "NOT";
            }
        };
    }

    private static Number negate(Number left) {
        if (left instanceof Integer) {
            return new Integer(-left.intValue());
        } else if (left instanceof Long) {
            return new Long(-left.longValue());
        } else {
            return new Double(-left.doubleValue());
        }
    }

    public UnaryExpression(Expression left) {
        this.right = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression expression) {
        right=expression;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "("+getExpressionSymbol()+" "+right.toString()+")";
    }
    
    /**
     * TODO: more efficient hashCode()
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return toString().hashCode();
	}
	
	/**
     * TODO: more efficient hashCode()
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		
		if( o==null || !this.getClass().equals(o.getClass()) )
			return false;
		return toString().equals( o.toString() );
		
	}

    /**
     * Returns the symbol that represents this binary expression.  For example, addition is 
     * represented by "+"
     * 
     * @return
     */
    abstract public String getExpressionSymbol();
    
}
