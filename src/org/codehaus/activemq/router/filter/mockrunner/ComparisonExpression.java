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
package org.codehaus.activemq.router.filter.mockrunner;

import java.util.HashSet;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * A filter performing a comparison of two objects
 * 
 * Alwin Ibba: Changed package
 * Alwin Ibba: Modified to use Jakarta ORO instead of
 * Java 1.4 regular expressions. This makes the class usable with
 * Java 1.3
 * 
 * @version $Revision: 1.1 $
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
            
            StringBuffer regexp = new StringBuffer(like.length()*2);
            regexp.append("\\A"); // The beginning of the input
            for( int i=0; i < like.length(); i++ ) {
                char c = like.charAt(i);
                if( escape == ( 0xFFFF & c ) ) {
                    i++;
                    if( i >= like.length() ) {
                        // nothing left to escape...
                        break;
                    }                    
                    
                    char t = like.charAt(i);
                    regexp.append("\\x");
                    regexp.append(Integer.toHexString(0xFFFF&t));
                } else if( c == '%' ) {
                    regexp.append(".*?"); // Do a non-greedy match 
                } else if( c == '_' ) {
                    regexp.append("."); // match one 
                } else if ( REGEXP_CONTROL_CHARS.contains(new Character(c)) ) {
                    regexp.append("\\x");
                    regexp.append(Integer.toHexString(0xFFFF&c));                    
                } else {
                    regexp.append(c);
                }
            }
            regexp.append("\\Z"); // The end of the input
                    
            try
            {
                likePattern = new Perl5Compiler().compile(regexp.toString(), Perl5Compiler.EXTENDED_MASK);
            }
            catch(MalformedPatternException exc)
            {
                throw new RuntimeException(exc.getMessage());
            }
        }

        /**
         * @see org.codehaus.activemq.router.filter.UnaryExpression#getExpressionSymbol()
         */
        public String getExpressionSymbol() {
            return "LIKE";
        }

        /**
         * @see org.codehaus.activemq.router.filter.Expression#evaluate(javax.jms.Message)
         */
        public Object evaluate(Message message) throws JMSException {
            
            Object rv = this.getRight().evaluate(message);
            
            if( rv == null )
                return null;
            
            if( !(rv instanceof String ))
                throw new RuntimeException("LIKE can only operate on String identifiers.  LIKE attemped on: '" + rv.getClass());
            
            return (new Perl5Matcher().matches((String)rv, likePattern)) ? Boolean.TRUE : Boolean.FALSE;
        }
        
    }

    public static BooleanExpression createLike(Expression left, String right, String escape) {        
        if( escape != null && escape.length()!=1 )
            throw new RuntimeException("The ESCAPE string litteral is invalid.  It can only be one character.  Litteral used: "+escape);
        int c=-1;
        if( escape!=null )
            c = 0xFFFF & escape.charAt(0);
        
        return new LikeExpression(left, right, c);        
    }

    public static BooleanExpression createNotLike(Expression left, String right, String escape) {
        return UnaryExpression.createNOT(createLike(left, right, escape));
    }

    public static BooleanExpression createInFilter(Expression left, List elements) {        
        if (elements.isEmpty()) 
            return ConstantExpression.FALSE;
        
        BooleanExpression answer = createEqual(left, (Expression) elements.get(0));
        for (int i = 1; i < elements.size(); i++) {
            answer = LogicExpression.createOR(answer, createEqual(left, (Expression) elements.get(i)));
        }
        return answer;
    }
    
    public static BooleanExpression createNotInFilter(Expression left, List elements) {        
        if (elements.isEmpty()) 
            return ConstantExpression.TRUE;
        
        BooleanExpression answer = createEqual(left, (Expression) elements.get(0));
        for (int i = 1; i < elements.size(); i++) {
            answer = LogicExpression.createOR(answer, createEqual(left, (Expression) elements.get(i)));
        }
        return UnaryExpression.createNOT(answer);
    }

    public static BooleanExpression createIsNull(Expression left) {
        return createEqual(left, ConstantExpression.NULL);
    }

    public static BooleanExpression createIsNotNull(Expression left) {
        return createNotEqual(left, ConstantExpression.NULL);
    }

    public static BooleanExpression createNotEqual(Expression left, Expression right) {
        return UnaryExpression.createNOT(createEqual(left, right));
    }

    public static BooleanExpression createEqual(Expression left, Expression right) {
        return new ComparisonExpression(left, right) {

            public Object evaluate(Message message) throws JMSException {
                Object obj1 = this.left.evaluate(message);
                Object obj2 = this.right.evaluate(message);
                
                Comparable lv = obj1 instanceof Comparable ? (Comparable) obj1 : null;
                Comparable rv = obj2 instanceof Comparable ? (Comparable) obj2 : null;
                // Iff one of the values is null
                if (lv == null ^ rv == null) return Boolean.FALSE; 
                if (lv == rv) return Boolean.TRUE;
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
     * @param left
     * @param right
     */
    public ComparisonExpression(Expression left, Expression right) {
        super(left, right);
    }

    public Object evaluate(Message message) throws JMSException {
        Comparable lv = (Comparable) left.evaluate(message);
        if (lv == null) return null;
        Comparable rv = (Comparable) right.evaluate(message);
        if (rv == null) return null;
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
                    lv = new Long(((Integer) lv).longValue());
                } else if (rc == Double.class) {
                    lv = new Double(((Long) lv).doubleValue());
                } else {
                    throw new RuntimeException("You cannot compare a '" + lc.getName() + "' and a '" + rc.getName()
                            + "'");
                }
            }
            if (lc == Long.class) {
                if (rc == Integer.class) {
                    rv = new Long(((Integer) rv).longValue());
                } else if (rc == Double.class) {
                    lv = new Double(((Long) lv).doubleValue());
                } else {
                    throw new RuntimeException("You cannot compare a '" + lc.getName() + "' and a '" + rc.getName()
                            + "'");
                }
            }
            if (lc == Double.class) {
                if (rc == Integer.class) {
                    rv = new Double(((Integer) rv).doubleValue());
                } else if (rc == Long.class) {
                    rv = new Double(((Long) rv).doubleValue());
                } else {
                    throw new RuntimeException("You cannot compare a '" + lc.getName() + "' and a '" + rc.getName()
                            + "'");
                }
            }
        }
        return asBoolean(lv.compareTo(rv)) ? Boolean.TRUE : Boolean.FALSE;
    }

    protected abstract boolean asBoolean(int answer);
}
