package com.mockrunner.gen.proc;

import java.util.List;

public interface AdapterProcessor
{
    public void process(Class module, List excludedMethods);
    public String getName();
    public String getOutput();
}
