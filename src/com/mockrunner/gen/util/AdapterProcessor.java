package com.mockrunner.gen.util;

import java.util.List;

public interface AdapterProcessor
{
    public void process(Class module, List excludedMethods);
    public String getName();
    public String getOutput();
}
