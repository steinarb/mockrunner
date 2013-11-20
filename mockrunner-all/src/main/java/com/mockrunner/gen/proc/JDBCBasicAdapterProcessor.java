package com.mockrunner.gen.proc;

public class JDBCBasicAdapterProcessor extends BasicAdapterProcessor
{
    protected String[] getTearDownMethodCodeLines(MemberInfo memberInfo)
    {
        String[] codeLines = new String[7];
        codeLines[0] = "super.tearDown();";
        codeLines[1] = "if(null != " + memberInfo.getFactoryMember() + ")";
        codeLines[2] = "{";
        codeLines[3] = "    " + memberInfo.getFactoryMember() + ".restoreDrivers();";
        codeLines[4] = "}";
        codeLines[5] = memberInfo.getModuleMember() + " = null;";
        codeLines[6] = memberInfo.getFactoryMember() + " = null;";
        return codeLines;
    }
}
