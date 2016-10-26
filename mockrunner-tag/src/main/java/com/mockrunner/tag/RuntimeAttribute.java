package com.mockrunner.tag;

/**
 * Implementations of this interface can be used to simulate
 * scriptlets and EL expressions as tag attributes.
 * You can add it to the paramater map of any
 * {@link com.mockrunner.tag.NestedTag} instance.
 */
public interface RuntimeAttribute
{
    Object evaluate();
}
