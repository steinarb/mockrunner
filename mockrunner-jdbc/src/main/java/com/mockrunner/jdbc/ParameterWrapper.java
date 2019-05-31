/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockParameterMap;

/**
 *
 * @author Administrator
 * @param <T> the wrapped type
 */
public class ParameterWrapper<T> {
    private final MockParameterMap parameters;
    private final T wrappedObject;

    public ParameterWrapper(T wrappedObject, MockParameterMap parameters)
    {
        this.wrappedObject = wrappedObject;
        this.parameters = parameters;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.parameters != null ? this.parameters.hashCode() : 0);
        hash = 19 * hash + (this.wrappedObject != null ? this.wrappedObject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParameterWrapper<?> other = (ParameterWrapper<?>) obj;
        if (this.parameters != other.parameters && (this.parameters == null || !this.parameters.equals(other.parameters))) {
            return false;
        }
        return !(this.wrappedObject != other.wrappedObject && (this.wrappedObject == null || !this.wrappedObject
                .equals(other.wrappedObject)));
    }

    public MockParameterMap getParameters()
    {
        return parameters;
    }

    public T getWrappedObject(){
        return wrappedObject;
    }

    
    
}
