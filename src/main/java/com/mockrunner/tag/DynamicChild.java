package com.mockrunner.tag;

/**
 * Implementations of this interface can be used to simulate
 * scriptlets and EL expressions as tag childs. When the childs
 * of a tag are evaluated, the <code>evaluate</code> method is
 * called and the result will become part of the text body of a
 * tag (in fact the <code>toString</code> method is called on the
 * returned object).
 */
public interface DynamicChild
{
    public Object evaluate();
}
