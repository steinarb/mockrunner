package com.mockrunner.test.gen;

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
                int lineIndex = checkLine(currentLine, tempLinesToParse);
                if(lineIndex >= 0)
                {
                    resultList.add(new Line(currentLine, input.getLineNumber()));
                    tempLinesToParse.remove(lineIndex);
                }
                else
                {
                    int blockIndex = checkBlock(currentLine, tempBlocksToParse);
                    if(blockIndex >= 0)
                    {
	                    int startLineNumber = input.getLineNumber();
	                    int endLineNumber = determineEndLineNumber(input);
	                    if(endLineNumber > startLineNumber)
	                    {
	                        resultList.add(new Block(currentLine, startLineNumber, endLineNumber));
	                        tempBlocksToParse.remove(blockIndex);
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
    
    private int checkLine(String currentLine, List tempLinesToParse)
    {
        for(int ii = 0; ii < tempLinesToParse.size(); ii++)
        {
            String nextLine = (String)tempLinesToParse.get(ii);
            if(currentLine.trim().indexOf(nextLine) != -1)
            {
                return ii;
            }
        }
        return -1;
    }
    
    private int checkBlock(String currentLine, List tempBlocksToParse)
    {
        for(int ii = 0; ii < tempBlocksToParse.size(); ii++)
        {
            String nextLine = (String)tempBlocksToParse.get(ii);
            if(currentLine.trim().indexOf(nextLine) != -1)
            {
                return ii;
            }
        }
        return -1;
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
