/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mockrunner.mock.jdbc;

/**
 *
 * @author Administrator
 */
public class ParameterIndex implements ParameterReference{
    private final int index;

    public ParameterIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.index;
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
        final ParameterIndex other = (ParameterIndex) obj;
        return this.index == other.index;
    }

    @Override
    public String toString() {
        return "ParameterIndex{" + "index=" + index + '}';
    }
    
}
