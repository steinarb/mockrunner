/** 
 * 
 * Copyright 2004 Protique Ltd
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
package org.activemq.filter.mockrunner;

import java.util.HashSet;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * Alwin Ibba: Changed package
 * Alwin Ibba: Modification to be Java 1.3 compatible
 * 
 * A filter performing a comparison of two objects
 * 
 * @version $Revision: 1.2 $
 */
public abstract class ComparisonExpression extends BinaryExpression implements BooleanExpression {

    public static BooleanExpression createBetween(Expression value, Expression left, Expression right) {
        return LogicExpression.createAND(createGreaterThanEqual(value, left), createLessThanEqual(value, right));
    }

    public static BooleanExpression createNotBetween(Expression value, Expression left, Expression right) {
        return LogicExpression.createOR(createLessThan(value, left), createGreaterThan(value, right));
    }

    static final private HashSet REGEXP_CONTROL_CHARS = new HashSet();

    static {
        REGEXP_CONTROL_CHARS.add(new Character('.'));
        REGEXP_CONTROL_CHARS.add(new Character('\\'));
        REGEXP_CONTROL_CHARS.add(new Character('['));
        REGEXP_CONTROL_CHARS.add(new Character(']'));
        REGEXP_CONTROL_CHARS.add(new Character('^'));
        REGEXP_CONTROL_CHARS.add(new Character('$'));
        REGEXP_CONTROL_CHARS.add(new Character('?'));
        REGEXP_CONTROL_CHARS.add(new Character('*'));
        REGEXP_CONTROL_CHARS.add(new Character('+'));
        REGEXP_CONTROL_CHARS.add(new Character('{'));
        REGEXP_CONTROL_CHARS.add(new Character('}'));
        REGEXP_CONTROL_CHARS.add(new Character('|'));
        REGEXP_CONTROL_CHARS.add(new Character('('));
        REGEXP_CONTROL_CHARS.add(new Character(')'));
        REGEXP_CONTROL_CHARS.add(new Character(':'));
        REGEXP_CONTROL_CHARS.add(new Character('&'));
        REGEXP_CONTROL_CHARS.add(new Character('<'));
        REGEXP_CONTROL_CHARS.add(new Character('>'));
        REGEXP_CONTROL_CHARS.add(new Character('='));
        REGEXP_CONTROL_CHARS.add(new Character('!'));
    }

    static class LikeExpression extends UnaryExpression implements BooleanExpression {

        Pattern likePattern;

        /**
         * @param left
         */
        public LikeExpression(Expression right, String like, int escape) {
            super(right);

            StringBuffer regexp = new StringBuffer(like.length() * 2);
            regexp.append("\\A"); // The beginning of the input
            for (int i = 0; i < like.length(); i++) {
                char c = like.charAt(i);
                if (escape == (0xFFFF & c)) {
                    i++;
                    if (i >= like.length()) {
                        // nothing left to escape...
                        break;
                    }

                    char t = like.charAt(i);
                    regexp.append("\\x");
                    regexp.append(Integer.toHexString(0xFFFF & t));
                }
                else if (c == '%') {
                    regexp.append(".*?"); // Do a non-greedy match 
                }
                else if (c == '_') {
                    regexp.append("."); // match one 
                }
                else if (REGEXP_CONTROL_CHARS.contains(new Character(c))) {
                    regexp.append("\\x");
                    regexp.append(Integer.toHexString(0xFFFF & c));
                }
                else {
                    regexp.append(c);
                }
            }
            regexp.append("\\Z"); 

            try
            {
                likePattern = new Perl5Compiler().compile(regexp.toString(), Perl5Compiler.EXTENDED_MASK | Perl5Compiler.SINGLELINE_MASK);
            }
            catch(MalformedPatternException exc)
            {
                throw new RuntimeException(exc.getMessage());
            }

        }

        /**
         * @see org.activemq.filter.UnaryExpression#getExpressionSymbol()
         */
        public String getExpressionSymbol() {
            return "LIKE";
        }

        /**
         * @see org.activemq.filter.Expression#evaluate(javax.jms.Message)
         */
        public Object evaluate(Message message) throws JMSException {

            Object rv = this.getRight().evaluate(message);

            if (rv == null) {
                return null;
            }

            if (!(rv instanceof String)) {
            	return Boolean.FALSE;
                //throw new RuntimeException("LIKE can only operate on String identifiers.  LIKE attemped on: '" + rv.getClass());
            }

            return (new Perl5Matcher().matches((String)rv, likePattern)) ? Boolean.TRUE : Boolean.FALSE;

        }

    }

    public static BooleanExpression createLike(Expression left, String right, String escape) {
        if (escape != null && escape.length() != 1) {
            throw new RuntimeException("The ESCAPE string litteral is invalid.  It can only be one character.  Litteral used: " + escape);
        }
        int c = -1;
        if (escape != null) {
            c = 0xFFFF & escape.charAt(0);
        }

        return new LikeExpression(left, right, c);
    }

    public static BooleanExpression createNotLike(Expression left, String right, String escape) {
        return UnaryExpression.createNOT(createLike(left, right, escape));
    }    

    public static BooleanExpression createInFilter(Expression left, List elements) {
    	
    	if( !(left instanceof PropertyExpression) )
    		throw new RuntimeException("Expected a property for In expression, got: "+left);    	
    	return UnaryExpression.createInExpression((PropertyExpression)left, elements, false);
    	
    }

    public static BooleanExpression createNotInFilter(Expression left, List elements) {
    	
    	if( !(left instanceof PropertyExpression) )
    		throw new RuntimeException("Expected a property for In expression, got: "+left);    	
    	return UnaryExpression.createInExpression((PropertyExpression)left, elements, true);

    }

    public static BooleanExpression createIsNull(Expression left) {
        return doCreateEqual(left, ConstantExpression.NULL);
    }

    public static BooleanExpression createIsNotNull(Expression left) {
        return UnaryExpression.createNOT(doCreateEqual(left, ConstantExpression.NULL));
    }

    public static BooleanExpression createNotEqual(Expression left, Expression right) {
        return UnaryExpression.createNOT(createEqual(left, right));
    }

    public static BooleanExpression createEqual(Expression left, Expression right) {
    	checkEqualOperand(left);
    	checkEqualOperand(right);
    	checkEqualOperandCompatability(left, right);
    	return doCreateEqual(left, right);
    }
    
	private static BooleanExpression doCreateEqual(Expression left, Expression right) {
        return new ComparisonExpression(left, right) {

            public Object evaluate(Message message) throws JMSException {
                Object obj1 = this.left.evaluate(message);
                Object obj2 = this.right.evaluate(message);
                
                // Iff one of the values is null
                if (obj1 == null ^ obj2 == null) {
                    return Boolean.FALSE;
                }
                if (obj1 == obj2 || obj1.equals(obj2)) {
                    return Boolean.TRUE;
                }
                Comparable lv = obj1 instanceof Comparable ? (Comparable) obj1 : null;
                Comparable rv = obj2 instanceof Comparable ? (Comparable) obj2 : null;
                if( lv==null || rv==null )
                    return Boolean.FALSE;
                return compare(lv, rv);
            }

            protected boolean asBoolean(int answer) {
                return answer == 0;
            }

            public String getExpressionSymbol() {
                return "=";
            }
        };
    }

    public static BooleanExpression createGreaterThan(final Expression left, final Expression right) {
    	checkLessThanOperand(left);
    	checkLessThanOperand(right);
        return new ComparisonExpression(left, right) {
            protected boolean asBoolean(int answer) {
                return answer > 0;
            }

            public String getExpressionSymbol() {
                return ">";
            }
        };
    }

    public static BooleanExpression createGreaterThanEqual(final Expression left, final Expression right) {
    	checkLessThanOperand(left);
    	checkLessThanOperand(right);
        return new ComparisonExpression(left, right) {
            protected boolean asBoolean(int answer) {
                return answer >= 0;
            }

            public String getExpressionSymbol() {
                return ">=";
            }
        };
    }

    public static BooleanExpression createLessThan(final Expression left, final Expression right) {
    	checkLessThanOperand(left);
    	checkLessThanOperand(right);
        return new ComparisonExpression(left, right) {

            protected boolean asBoolean(int answer) {
                return answer < 0;
            }

            public String getExpressionSymbol() {
                return "<";
            }

        };
    }

	public static BooleanExpression createLessThanEqual(final Expression left, final Expression right) {
    	checkLessThanOperand(left);
    	checkLessThanOperand(right);
        return new ComparisonExpression(left, right) {

            protected boolean asBoolean(int answer) {
                return answer <= 0;
            }

            public String getExpressionSymbol() {
                return "<=";
            }
        };
    }

	/**
     * Only Numeric expressions can be used in >, >=, < or <= expressions.s 
     * 
	 * @param expr
	 */
	public static void checkLessThanOperand(Expression expr ) {
		if( expr instanceof ConstantExpression ) {
			Object value = ((ConstantExpression)expr).getValue();
			if( value instanceof Number )
				return;
			
			// Else it's boolean or a String..  
			throw new RuntimeException("Value '"+expr+"' cannot be compared.");
		}
		if( expr instanceof BooleanExpression ) {
			throw new RuntimeException("Value '"+expr+"' cannot be compared.");
		}		
	}

	/**
     * Validates that the expression can be used in == or <> expression.  
     * Cannot not be NULL TRUE or FALSE litterals.
     * 
	 * @param expr
	 */
	public static void checkEqualOperand(Expression expr ) {
		if( expr instanceof ConstantExpression ) {
			Object value = ((ConstantExpression)expr).getValue();
			if( value == null )
				throw new RuntimeException("'"+expr+"' cannot be compared.");
		}
	}

	/**
	 * 
	 * @param left
	 * @param right
	 */
	private static void checkEqualOperandCompatability(Expression left, Expression right) {
		if( left instanceof ConstantExpression && right instanceof ConstantExpression ) {
			if( left instanceof BooleanExpression && !(right instanceof BooleanExpression) )
				throw new RuntimeException("'"+left+"' cannot be compared with '"+right+"'");
		}
	}

	
	
    /**
     * @param left
     * @param right
     */
    public ComparisonExpression(Expression left, Expression right) {
        super(left, right);
    }

    public Object evaluate(Message message) throws JMSException {
        Comparable lv = (Comparable) left.evaluate(message);
        if (lv == null) {
            return null;
        }
        Comparable rv = (Comparable) right.evaluate(message);
        if (rv == null) {
            return null;
        }
        return compare(lv, rv);
    }

    protected Boolean compare(Comparable lv, Comparable rv) {
        Class lc = lv.getClass();
        Class rc = rv.getClass();
        // If the the objects are not of the same type,
        // try to convert up to allow the comparison.
        if (lc != rc) {
            if (lc == Integer.class) {
                if (rc == Long.class) {
                    lv = new Long(((Number) lv).longValue());
                }
                else if (rc == Float.class) {
                    lv = new Float(((Number) lv).floatValue());
                }
                else if (rc == Double.class) {
                    lv = new Double(((Number) lv).doubleValue());
                }
                else {
                    return Boolean.FALSE;
                }
            }
            else if (lc == Long.class) {
                if (rc == Integer.class) {
                    rv = new Long(((Number) rv).longValue());
                }
                else if (rc == Float.class) {
                    lv = new Float(((Number) lv).floatValue());
                }
                else if (rc == Double.class) {
                    lv = new Double(((Number) lv).doubleValue());
                }
                else {
                    return Boolean.FALSE;
                }
            }
            else if (lc == Float.class) {
                if (rc == Integer.class) {
                    rv = new Float(((Number) rv).floatValue());
                }
                else if (rc == Long.class) {
                    rv = new Float(((Number) rv).floatValue());
                }
                else if (rc == Double.class) {
                    lv = new Double(((Number) lv).doubleValue());
                }
                else {
                    return Boolean.FALSE;
                }
            } 
            else if (lc == Double.class) {
                if (rc == Integer.class) {
                    rv = new Double(((Number) rv).doubleValue());
                }
                else if (rc == Long.class) {
                    rv = new Double(((Number) rv).doubleValue());
                }
                else if (rc == Float.class) {
                	rv = new Float(((Number) rv).doubleValue());
                }
                else {
                    return Boolean.FALSE;
                }
            } 
            else 
                return Boolean.FALSE;
        }
        return asBoolean(lv.compareTo(rv)) ? Boolean.TRUE : Boolean.FALSE;
    }

    protected abstract boolean asBoolean(int answer);
}
