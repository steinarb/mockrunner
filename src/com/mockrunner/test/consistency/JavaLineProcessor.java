package com.mockrunner.test.consistency;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import com.mockrunner.test.consistency.JavaLineParser.Block;
import com.mockrunner.test.consistency.JavaLineParser.Line;

public class JavaLineProcessor
{
    private JavaLineParser parser;
    private String source;
    
    public JavaLineProcessor(String source)
    {
        this.source = source;
        parser = new JavaLineParser(source);
    }
    
    public void addLines(List lines)
    {
        parser.addLines(lines);
    }
    
    public void addBlocks(List blocks)
    {
        parser.addBlocks(blocks);
    }
    
    public String process()
    {
        List result = parser.parse();
        LineNumberReader input = new LineNumberReader(new StringReader(source));
        StringWriter resultStringWriter = new StringWriter(source.length() + 100);
        PrintWriter output = new PrintWriter(resultStringWriter);
        for(int ii = 0; ii < result.size(); ii++)
        {
            Line nextLine = (Line)result.get(ii);
            int processUntil = nextLine.getLineNumber();
            while(input.getLineNumber() < processUntil)
            {
                try
                {
                    output.println(input.readLine());
                } 
                catch(IOException exc)
                {
                    throw new RuntimeException(exc);
                }
            }
            
        }
        output.flush();
        return resultStringWriter.toString();
    }
    
    private void handleLine(Line line, LineNumberReader input, PrintWriter output) throws IOException
    {
        StringBuffer nextLine = new StringBuffer(input.readLine());
        int firstNonWhitespace = 0;
        while(firstNonWhitespace < nextLine.length() && Character.isWhitespace(nextLine.charAt(firstNonWhitespace)))
        {
            firstNonWhitespace++;
        }
        if(firstNonWhitespace < nextLine.length())
        {
	        if(line instanceof Block)
	        {
	            
	        }
	        else
	        {
	            nextLine.insert(firstNonWhitespace, "//");
	            output.println(nextLine);
	        }
        }
    }
}
