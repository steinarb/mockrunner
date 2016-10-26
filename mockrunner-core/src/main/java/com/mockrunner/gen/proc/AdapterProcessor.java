package com.mockrunner.gen.proc;

import java.util.List;

public interface AdapterProcessor
{
    void process(Class module, List excludedMethods);
    String getName();
    String getOutput();
}
