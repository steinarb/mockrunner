package com.mockrunner.gen.proc;

import java.io.File;
import java.util.Map;

public class GeneratorUtil
{
    public void addJavaSrcFiles(String rootDir, File dir, Map resultMap)
    {
        File[] fileList = dir.listFiles();
        for(int ii = 0; ii < fileList.length; ii++)
        {
            File currentFile = fileList[ii];
            if(currentFile.isDirectory())
            {
                addJavaSrcFiles(rootDir, currentFile, resultMap);
            }
            else if(currentFile.isFile() && currentFile.getName().endsWith(".java"))
            {
                String key = currentFile.getPath().substring(rootDir.length());
                resultMap.put(key, currentFile);
            }
        }
    }
}
