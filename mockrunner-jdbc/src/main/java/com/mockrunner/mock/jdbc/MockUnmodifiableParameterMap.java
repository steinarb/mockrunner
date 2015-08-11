/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockrunner.mock.jdbc;


public class MockUnmodifiableParameterMap extends MockParameterMap {

    public MockUnmodifiableParameterMap(MockParameterMap other) {
        super(other);
    }
    
    @Override
    public void put(int index, Object object){
        throw new UnsupportedOperationException("Put is not supported on a MockUnmodifiableParameterMap");
    }

    @Override
    public void put(String name, Object object){
        throw new UnsupportedOperationException("Put is not supported on a MockUnmodifiableParameterMap");
    }
}
