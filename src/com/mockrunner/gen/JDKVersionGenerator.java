package com.mockrunner.gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mockrunner.gen.util.GeneratorUtil;
import com.mockrunner.gen.util.JavaLineProcessor;
import com.mockrunner.util.StreamUtil;

public class JDKVersionGenerator
{
    private final static String src14Dir = "src";
    private final static String src13Dir = "src1.3";
    
    public static void main(String[] args) throws Exception
    {
        JDKVersionGenerator synchVersionUtil = new JDKVersionGenerator();
        synchVersionUtil.doSynchronize();
    }
    
    public void doSynchronize() throws Exception
    {
        doSynchronizeJDBCJDK13();
    }
    
    private Map prepareJDBCJDK13()
    {
        Map jdbcFiles = new HashMap();
        
        JavaLineProcessor mockConnectionProc = new JavaLineProcessor();
        mockConnectionProc.addLine("import java.sql.Savepoint");
        mockConnectionProc.addLine("private int holdability;");
        mockConnectionProc.addLine("holdability = ResultSet.HOLD_CURSORS_OVER_COMMIT;");
        mockConnectionProc.addBlock("public int getHoldability()");
        mockConnectionProc.addBlock("public void setHoldability(int holdability)");
        mockConnectionProc.addBlock("public Savepoint setSavepoint()");
        mockConnectionProc.addBlock("public Savepoint setSavepoint(String name)");
        mockConnectionProc.addBlock("public void releaseSavepoint(Savepoint savepoint)");
        mockConnectionProc.addBlock("public void rollback(Savepoint savepoint)");
        jdbcFiles.put("com.mockrunner.mock.jdbc.MockConnection", mockConnectionProc);
        
        JavaLineProcessor mockDatabaseMetadataProc = new JavaLineProcessor();
        mockDatabaseMetadataProc.addLine("private int sqlStateType = sqlStateSQL99;");
        mockDatabaseMetadataProc.addBlock("public int getSQLStateType()");
        mockDatabaseMetadataProc.addBlock("public void setSQLStateType(int sqlStateType)");
        jdbcFiles.put("com.mockrunner.mock.jdbc.MockDatabaseMetaData", mockDatabaseMetadataProc);
        
        JavaLineProcessor mockPreparedStatementProc = new JavaLineProcessor();
        mockPreparedStatementProc.addLine("import java.sql.ParameterMetaData;");
        mockPreparedStatementProc.addLine("private MockParameterMetaData parameterMetaData;");
        mockPreparedStatementProc.addLine("prepareParameterMetaData();");
        mockPreparedStatementProc.addLine("prepareParameterMetaData();");
        mockPreparedStatementProc.addLine("prepareParameterMetaData();");
        mockPreparedStatementProc.addBlock("private void prepareParameterMetaData()");
        mockPreparedStatementProc.addBlock("public ParameterMetaData getParameterMetaData()");
        jdbcFiles.put("com.mockrunner.mock.jdbc.MockPreparedStatement", mockPreparedStatementProc);
        
        JavaLineProcessor mockStatementProc = new JavaLineProcessor();
        mockStatementProc.addLine("private int resultSetHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;");
        mockStatementProc.addBlock("try");
        mockStatementProc.addBlock("catch(SQLException exc)");
        mockStatementProc.addBlock("try");
        mockStatementProc.addBlock("catch(SQLException exc)");
        mockStatementProc.addLine("this.resultSetHoldability = resultSetHoldability;");
        mockStatementProc.addBlock("public int getResultSetHoldability()");
        jdbcFiles.put("com.mockrunner.mock.jdbc.MockStatement", mockStatementProc);
        
        JavaLineProcessor jdbcTestCaseAdapterProc = new JavaLineProcessor();
        jdbcTestCaseAdapterProc.addLine("import com.mockrunner.mock.jdbc.MockSavepoint;");
        jdbcTestCaseAdapterProc.addBlock("protected List getSavepoints()");
        jdbcTestCaseAdapterProc.addBlock("protected MockSavepoint getSavepoint(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected MockSavepoint getSavepoint(String name)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointPresent(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointPresent(String name)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointReleased(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointReleased(String name)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointNotReleased(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointNotReleased(String name)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointRollbacked(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointRollbacked(String name)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointNotRollbacked(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointNotRollbacked(String name)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointRolledBack(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointRolledBack(String name)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointNotRolledBack(int index)");
        jdbcTestCaseAdapterProc.addBlock("protected void verifySavepointNotRolledBack(String name)");
        jdbcFiles.put("com.mockrunner.jdbc.JDBCTestCaseAdapter", jdbcTestCaseAdapterProc);
        jdbcFiles.put("com.mockrunner.jdbc.BasicJDBCTestCaseAdapter", jdbcTestCaseAdapterProc);
        
        JavaLineProcessor jdbcTestModuleProc = new JavaLineProcessor();
        jdbcTestModuleProc.addLine("import com.mockrunner.mock.jdbc.MockSavepoint;");
        jdbcTestModuleProc.addBlock("public List getSavepoints()");
        jdbcTestModuleProc.addBlock("public MockSavepoint getSavepoint(int index)");
        jdbcTestModuleProc.addBlock("public MockSavepoint getSavepoint(String name)");
        jdbcTestModuleProc.addBlock("public void verifySavepointPresent(int index)");
        jdbcTestModuleProc.addBlock("public void verifySavepointPresent(String name)");
        jdbcTestModuleProc.addBlock("public void verifySavepointReleased(int index)");
        jdbcTestModuleProc.addBlock("public void verifySavepointReleased(String name)");
        jdbcTestModuleProc.addBlock("public void verifySavepointNotReleased(int index)");
        jdbcTestModuleProc.addBlock("public void verifySavepointNotReleased(String name)");
        jdbcTestModuleProc.addBlock("public void verifySavepointRollbacked(int index)");
        jdbcTestModuleProc.addBlock("public void verifySavepointRollbacked(String name)");
        jdbcTestModuleProc.addBlock("public void verifySavepointNotRollbacked(int index)");
        jdbcTestModuleProc.addBlock("public void verifySavepointNotRollbacked(String name)");
        jdbcTestModuleProc.addBlock("public void verifySavepointRolledBack(int index)");
        jdbcTestModuleProc.addBlock("public void verifySavepointRolledBack(String name)");
        jdbcTestModuleProc.addBlock("public void verifySavepointNotRolledBack(int index)");
        jdbcTestModuleProc.addBlock("public void verifySavepointNotRolledBack(String name)");
        jdbcFiles.put("com.mockrunner.jdbc.JDBCTestModule", jdbcTestModuleProc);
        
        jdbcFiles.put("com.mockrunner.mock.jdbc.MockSavepoint", new Boolean(false));
        jdbcFiles.put("com.mockrunner.mock.jdbc.MockParameterMetaData", new Boolean(false));
        
        return jdbcFiles;
    }

    private void doSynchronizeJDBCJDK13() throws Exception
    {
        System.out.println("Start processing for JDBC JDK1.3");
        Map jdbcProcMap = prepareJDBCJDK13();
        GeneratorUtil util = new GeneratorUtil();
        Map jdbcMap = new HashMap();
        File jdbc = new File(src14Dir + "/com/mockrunner/jdbc");
        File jdbcMock = new File(src14Dir + "/com/mockrunner/mock/jdbc");
        util.addJavaSrcFiles(src14Dir, jdbc, jdbcMap);
        util.addJavaSrcFiles(src14Dir, jdbcMock, jdbcMap);
        processFiles(jdbcProcMap, jdbcMap, src13Dir);
        System.out.println("Sucessfully finished processing for JDBC JDK1.3");
    }
    
    private void processFiles(Map procMap, Map map, String targetDir) throws FileNotFoundException, IOException
    {
        Iterator sourceIterator = map.keySet().iterator();
        while(sourceIterator.hasNext())
        {
            String currentFileName = (String)sourceIterator.next();
            File currentSourceFile = (File)map.get(currentFileName);
            File currentDestFile = new File(targetDir + currentFileName);
            String sourceFileContent = StreamUtil.getReaderAsString(new FileReader(currentSourceFile));
            System.out.println("Processing file " + currentSourceFile);
            String processedFileContent = processFile(currentFileName, sourceFileContent, procMap);
            if(null != processedFileContent)
            {
                writeFileContent(processedFileContent, currentDestFile);
            }
        }
    }

    private String processFile(String currentFileName, String fileContent, Map jdbcProcMap)
    {
        currentFileName = currentFileName.replace('\\', '.');
        currentFileName = currentFileName.replace('/', '.');
        currentFileName = currentFileName.substring(1);
        currentFileName = currentFileName.substring(0 , currentFileName.length() - 5);
        Object currentObject = (Object)jdbcProcMap.get(currentFileName);
        if(null == currentObject)
        {
            return fileContent;
        }
        else if(currentObject instanceof JavaLineProcessor)
        {
            return ((JavaLineProcessor)currentObject).process(fileContent);
        }
        else if(currentObject instanceof Boolean)
        {
            if(!((Boolean)currentObject).booleanValue())
            {
                return null;
            }
            else
            {
                return fileContent;
            }
        }
        return null;
    }

    private void writeFileContent(String fileContent, File currentDestFile) throws FileNotFoundException, IOException
    {
        if(!currentDestFile.getParentFile().exists())
        {
            currentDestFile.getParentFile().mkdirs();
        }
        System.out.println("Writing file " + currentDestFile);
        FileWriter currentDestFileWriter = new FileWriter(currentDestFile); 
        currentDestFileWriter.write(fileContent);
        currentDestFileWriter.flush();
        currentDestFileWriter.close();
    }
}
