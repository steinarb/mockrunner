package com.mockrunner.gen.proc;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JavaLineParser
{
    private List linesToParse;
    private List blocksToParse;
    
    public JavaLineParser()
    {
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
    
    public List parse(String source)
    {
        List resultList = new ArrayList();
        List tempLinesToParse = new ArrayList(linesToParse);
        List tempBlocksToParse = new ArrayList(blocksToParse);
        LineNumberReader input = new LineNumberReader(new StringReader(source));
        String currentLine = null;
        try
        {
            while(null != (currentLine = input.readLine()))
            {
                String line = checkLine(currentLine, linesToParse);
                if(line != null)
                {
                    resultList.add(new Line(currentLine, input.getLineNumber()));
                    tempLinesToParse.remove(line);
                }
                else
                {
                    line = checkBlock(currentLine, blocksToParse);
                    if(line != null)
                    {
	                    int startLineNumber = input.getLineNumber();
	                    int endLineNumber = determineEndLineNumber(input);
	                    if(endLineNumber > startLineNumber)
	                    {
	                        resultList.add(new Block(currentLine, startLineNumber, endLineNumber));
	                        tempBlocksToParse.remove(line);
	                    }
                    }
                }
            }
        }
        catch(IOException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
        checkLinesOrBlocksLeft(tempLinesToParse, tempBlocksToParse);
        return resultList;
    }
    
    private void checkLinesOrBlocksLeft(List tempLinesToParse, List tempBlocksToParse)
    {
        StringBuffer message = new StringBuffer("");
        if(tempLinesToParse.size() > 0)
        {
            message.append("Lines not found:\n");
            for(int ii = 0; ii < tempLinesToParse.size(); ii++)
            {
                message.append(tempLinesToParse.get(ii).toString() + "\n");
            }
        }
        if(tempBlocksToParse.size() > 0)
        {
            message.append("Blocks not found:\n");
            for(int ii = 0; ii < tempBlocksToParse.size(); ii++)
            {
                message.append(tempBlocksToParse.get(ii).toString() + "\n");
            }
        }
        if(message.length() > 0)
        {
            throw new RuntimeException(message.toString());
        }
    }
    
    private String checkLine(String currentLine, List linesToParse)
    {
        for(int ii = 0; ii < linesToParse.size(); ii++)
        {
            String nextLine = (String)linesToParse.get(ii);
            if(currentLine.trim().indexOf(nextLine) != -1)
            {
                return nextLine;
            }
        }
        return null;
    }
    
    private String checkBlock(String currentLine, List blocksToParse)
    {
        for(int ii = 0; ii < blocksToParse.size(); ii++)
        {
            String nextLine = (String)blocksToParse.get(ii);
            if(currentLine.trim().indexOf(nextLine) != -1)
            {
                return nextLine;
            }
        }
        return null;
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
            throw new RuntimeException(exc.getMessage());
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
