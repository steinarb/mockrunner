package com.mockrunner.example.connector;

import java.io.UnsupportedEncodingException;

/**
 * Helper class to convert Java types to commarea bytes and back.
 */
public class Marshaller
{
    public static byte[] marshalString(String data, int size)
    {
        if(null == data) data = "";
        if(data.length() > size) data = data.substring(0, size);
        StringBuffer buffer = new StringBuffer(data);
        for(int ii = buffer.length(); ii < size; ii++)
        {
            buffer.append(" ");
        }
        try
        {
            return buffer.toString().getBytes("Cp273");
        } 
        catch (UnsupportedEncodingException exc)
        {
            exc.printStackTrace();
        }
        return new byte[size];
    }

    public static String unmarshalString(byte[] data)
    {
        try
        {
            return new String(data, "Cp273");
        }
        catch (UnsupportedEncodingException exc)
        {
            exc.printStackTrace();
        }
        return "";
    }
    
    public static byte[] marshalNumber(int number)
    {
        return new byte[] {(byte)((number >> 24) & 0xFF), (byte)((number >> 16) & 0xFF), 
                           (byte)((number >> 8) & 0xFF), (byte)(number & 0xFF)};
    }

    public static int unmarshalNumber(byte[] data)
    {
        int shiftBits = (data.length - 1) * 8;
        int result = 0;
        for(int ii = 0; ii < data.length; ii++)
        {
              result |= ((0xFF & (int)data[ii]) << (shiftBits - (ii * 8)));
        }
        return result;
    }
}
