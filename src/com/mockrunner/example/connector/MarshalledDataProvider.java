package com.mockrunner.example.connector;

/**
 * Implementors can convert their data to commarea bytes and back.
 */
public interface MarshalledDataProvider
{
    public byte[] marshal();
    
    public void unmarshal(byte[] data);
}
