package com.mockrunner.gen.jar;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/* Very simple ant task for the JarAnalyzer project
 * http://www.kirkk.com/wiki/wiki.php/Main/JarAnalyzer
 * by Kirk Knoernschild
 */
public class JarAnalyzerTask extends Task
{
    private File srcdir;
    private File destfile;
    
    public void setSrcdir(File srcdir)
    {
        this.srcdir = srcdir;
    }
    
    public void setDestfile(File destfile)
    {
        this.destfile = destfile;
    }
    
    public void execute() throws BuildException
    {
        validateAttributes();
        try
        {
            new TextSummary().createSummary(srcdir, destfile);
            log(destfile + " successfully created");
        } 
        catch(Exception exc)
        {
            throw new BuildException(exc);
        }
    }
    
    private void validateAttributes()
    {
        if(null == srcdir)
        {
            throw new BuildException("Missing srcdir");
        }
        if(null == destfile)
        {
            throw new BuildException("Missing destfile");
        }
        if(!(srcdir.exists() && srcdir.isDirectory()))
        {
            throw new BuildException("srcdir must be a valid directory");
        }
        if(destfile.isDirectory())
        {
            throw new BuildException("srcdir must be a file");
        }
    }
}
