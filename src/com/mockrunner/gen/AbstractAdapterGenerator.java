package com.mockrunner.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mockrunner.gen.proc.AdapterProcessor;

public abstract class AbstractAdapterGenerator
{
    protected void generate() throws Exception
    {
        List units = initialize();
        Iterator iterator = units.iterator();
        while(iterator.hasNext())
        {
            ProcessingUnit nextUnit = (ProcessingUnit)iterator.next();
            AdapterProcessor processor = nextUnit.getProcessor();
            processor.process(nextUnit.getModule(), nextUnit.getExcludedMethods());
            writeOutputFile(processor);
        }
        System.out.println("Adapters successfully created");
    }
    
    private void writeOutputFile(AdapterProcessor processor) throws FileNotFoundException, IOException
    {
        System.out.println("Writing output file " + processor.getName());
        File currentFile = new File(getSrcDir() + "/" + processor.getName());
        FileOutputStream currentStream = new FileOutputStream(currentFile);
        Writer currentWriter = new OutputStreamWriter(currentStream, Charset.forName("ISO-8859-1"));
        currentWriter.write(processor.getOutput());
        currentWriter.flush();
        currentWriter.close();
    }
    
    protected abstract List initialize();
    
    protected abstract String getSrcDir();

    protected class ProcessingUnit
    {
        private Class module;
        private AdapterProcessor processor;
        private List excludedMethods;
        
        public ProcessingUnit(Class module, AdapterProcessor processor)
        {
            this(module, processor, new ArrayList());
        }
        
        public ProcessingUnit(Class module, AdapterProcessor processor, List excludedMethods)
        {
            this.module = module;
            this.processor = processor;
            this.excludedMethods = excludedMethods;
        }
        
        public Class getModule()
        {
            return module;
        }
        
        public List getExcludedMethods()
        {
            return excludedMethods;
        }
        
        public AdapterProcessor getProcessor()
        {
            return processor;
        }
    }
}
