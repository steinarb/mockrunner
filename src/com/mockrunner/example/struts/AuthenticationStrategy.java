package com.mockrunner.example.struts;

/**
 * Interface for different authentication strategies.
 * Used by {@link AuthenticationAction}.
 */
public interface AuthenticationStrategy
{
    boolean authenticate(String username, String password);
    boolean wasLastUserKnown();
    boolean wasLastPasswordOk();
}
