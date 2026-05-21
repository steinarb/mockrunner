package com.mockrunner.mock.web;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletSecurityElement;

public class MockDynamic implements Dynamic {
    HashSet<String> mappings = new HashSet<>();
    private String runAsRole;
    private String name;
    private Map<String, String> initParameters = new HashMap<>();

    @Override
    public Set<String> addMapping(String... urlPatterns) {
        for (String pattern : urlPatterns) {
            mappings.add(pattern);
        }

        return Collections.emptySet();
    }

    @Override
    public Collection<String> getMappings() {
        return mappings;
    }

    @Override
    public String getRunAsRole() {
        return runAsRole;
    }

    @Override
    public String getName() {
        return name;
    }

    public MockDynamic setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getClassName() {
        return null;
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

    @Override
    public void setLoadOnStartup(int loadOnStartup) {
        // No-op
    }

    @Override
    public Set<String> setServletSecurity(ServletSecurityElement constraint) {
        return Collections.emptySet();
    }

    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        // No-op
    }

    @Override
    public void setRunAsRole(String roleName) {
        runAsRole = roleName;
    }

}
