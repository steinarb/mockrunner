package com.mockrunner.test.consistency;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JavaLineParser
{
    private String source;
    private List linesToParse;
    private List blocksToParse;
    
    public JavaLineParser(String source)
    {
        this.source = source;
        linesToParse = new ArrayList();
        blocksToParse = new ArrayList();
    }
    
    public void addLine(String line)
    {
        linesToParse.add(line);
    }
    
    public void addLines(List lines)
    {
        linesToParse.addAll(lines);
    }
    
    public void addBlock(String blockLine)
    {
        blocksToParse.add(blockLine);
    }
    
    public void addBlocks(List blocks)
    {
        blocksToParse.addAll(blocks);
    }
    
    public List parse()
    {
        List resultList = new ArrayList();
        LineNumberReader input = new LineNumberReader(new StringReader(source));
        String currentLine = null;
        try
        {
            while(null != (currentLine = input.readLine()))
            {
                if(checkLine(currentLine))
                {
                    resultList.add(new Line(currentLine, input.getLineNumber()));
                }
                else if(checkBlock(currentLine))
                {
                    int startLineNumber = input.getLineNumber();
                    int endLineNumber = determineEndLineNumber(input);
                    if(endLineNumber > startLineNumber)
                    {
                        resultList.add(new Block(currentLine, startLineNumber, endLineNumber));
                    }
                }
            }
        } 
        catch(IOException exc)
        {
            throw new RuntimeException(exc);
        }
        return resultList;
    }
    
    private boolean checkLine(String currentLine)
    {
        for(int ii = 0; ii < linesToParse.size(); ii++)
        {
            String nextLine = (String)linesToParse.get(ii);
            if(currentLine.trim().indexOf(nextLine) != -1)
            {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkBlock(String currentLine)
    {
        for(int ii = 0; ii < blocksToParse.size(); ii++)
        {
            String nextLine = (String)blocksToParse.get(ii);
            if(currentLine.trim().indexOf(nextLine) != -1)
            {
                return true;
            }
        }
        return false;
    }
    
    private int determineEndLineNumber(LineNumberReader input)
    {
        String currentLine = null;
        try
        {
            while((null != (currentLine = input.readLine())) && (currentLine.trim().indexOf("{") == -1));
            int level = 1;
            while((level > 0) && (null != (currentLine = input.readLine())))
            {
                if(currentLine.trim().indexOf("{") != -1)
                {
                    level++;
                }
                else if(currentLine.trim().indexOf("}") != -1)
                {
                    level--;
                }
            }
            if(level == 0)
            {
                return input.getLineNumber();
            }
            else
            {
                return -1;
            }
        } 
        catch(IOException exc)
        {
            throw new RuntimeException(exc);
        }
    }
    
    public static class Line
    {
        private String line;
        private int lineNumber;
        
        public Line(String line, int lineNumber)
        {
            this.line = line;
            this.lineNumber = lineNumber;
        }
        
        public String getLine()
        {
            return line;
        }
        
        public int getLineNumber()
        {
            return lineNumber;
        }
    }
    
    public static class Block extends Line
    {
        private int endLineNumber;
        
        public Block(String line, int lineNumber, int endLineNumber)
        {
            super(line, lineNumber);
            this.endLineNumber = endLineNumber;
        }
        
        public int getEndLineNumber()
        {
            return endLineNumber;
        }
    }
}
