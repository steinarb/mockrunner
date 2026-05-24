package com.mockrunner.mock.web;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

public class MockFilterRegistrationDynamic implements Dynamic {

    private String name;
    private String className;
    private Map<String, String> initParameters = new HashMap<>();

    @Override
    public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames) {
        // No-op
    }

    @Override
    public Collection<String> getServletNameMappings() {
        return Collections.emptyList();
    }

    @Override
    public void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... urlPatterns) {
        // No-op
    }

    @Override
    public Collection<String> getUrlPatternMappings() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return name;
    }

    public MockFilterRegistrationDynamic setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public MockFilterRegistrationDynamic setClassName(String className) {
        this.className = className;
        return this;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        initParameters.put(name, value);
        return true;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> initParameters) {
        this.initParameters.putAll(initParameters);
        return Collections.emptySet();
    }

    @Override
    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    @Override
    public void setAsyncSupported(boolean isAsyncSupported) {
        // No-op
    }

}
