package de.mockrunner.example;

public interface AuthenticationStrategy
{
    boolean authenticate(String username, String password);
    boolean wasLastUserKnown();
    boolean wasLastPasswordOk();
}
