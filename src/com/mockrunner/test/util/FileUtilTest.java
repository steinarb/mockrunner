package com.mockrunner.test.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.mockrunner.util.common.FileUtil;
import com.mockrunner.util.common.StreamUtil;

import junit.framework.TestCase;

public class FileUtilTest extends TestCase
{
    public void testGetLinesFromFile()
    {
        File file = new File("src/com/mockrunner/test/util/testlines.txt");
        List lineList = FileUtil.getLinesFromFile(file);
        doTestLines(lineList);
    }
    
    public void testFindFile() throws IOException
    {
        Reader reader = FileUtil.findFile("src/com/mockrunner/test/util/testlines.txt");
        List lineList = StreamUtil.getLinesFromReader(reader);
        reader.close();
        doTestLines(lineList);
        reader = FileUtil.findFile("/com/mockrunner/test/util/testlines.txt");
        lineList = StreamUtil.getLinesFromReader(reader);
        doTestLines(lineList);
        reader.close();
    }
    
    private void doTestLines(List lineList)
    {
        assertTrue(lineList.size() == 6);
        assertEquals("line1", lineList.get(0));
        assertEquals("line2", lineList.get(1));
        assertEquals("line3", lineList.get(2));
        assertEquals("", lineList.get(3));
        assertEquals("line4", lineList.get(4));
        assertEquals("line5", lineList.get(5));
    }
}
