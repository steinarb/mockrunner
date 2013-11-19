package com.mockrunner.example.connector;

import java.io.ByteArrayOutputStream;

/**
 * Represents a person.
 */
public class Person extends DomainObjectRecord
{
    private String id;
    private String firstName;
    private String lastName;
    private int age;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }

    public byte[] marshal()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] encodedId = Marshaller.marshalString(id, 8);
        byte[] encodedFirstName = Marshaller.marshalString(firstName, 10);
        byte[] encodedLastName = Marshaller.marshalString(lastName, 10);
        byte[] encodedAge = Marshaller.marshalNumber(age);
        stream.write(encodedId, 0, encodedId.length);
        stream.write(encodedFirstName, 0, encodedFirstName.length);
        stream.write(encodedLastName, 0, encodedLastName.length);
        stream.write(encodedAge, 0, encodedAge.length);
        return stream.toByteArray();
    }

    public void unmarshal(byte[] data)
    {
        byte[] encodedId = new byte[8];
        byte[] encodedFirstName = new byte[10];
        byte[] encodedLastName = new byte[10];
        byte[] encodedAge = new byte[4];
        System.arraycopy(data, 0, encodedId, 0, 8);
        System.arraycopy(data, 8, encodedFirstName, 0, 10);
        System.arraycopy(data, 18, encodedLastName, 0, 10);
        System.arraycopy(data, 28, encodedAge, 0, 4);
        id = Marshaller.unmarshalString(encodedId).trim();
        firstName = Marshaller.unmarshalString(encodedFirstName).trim();
        lastName = Marshaller.unmarshalString(encodedLastName).trim();
        age = Marshaller.unmarshalNumber(encodedAge);
    }
}
