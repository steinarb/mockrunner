package com.mockrunner.util;

public class ClassUtil
{
    private final static String[] KEYWORDS = new String[]
    {
        "abstract", "assert", "boolean", "break", "byte",
        "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else",
        "extends", "final", "finally", "float", "for",
        "goto", "if", "implements", "import", "instanceof",
        "int", "interface", "long", "native", "new",
        "package", "private", "protected", "public", "return",
        "short", "static", "strictFP", "super", "switch",
        "synchronized", "this", "throw", "throws", "transient",
        "try", "void", "volatile", "while"
    };
    
    /**
     * Returns the name of the package of the specified class.
     * If the class has no package, an empty String will be
     * returned.
     * @param clazz the Class
     * @return the package name
     */
    public static String getPackageName(Class clazz)
    {
        Package classPackage = clazz.getPackage();
        if(null == classPackage) return "";
        return classPackage.getName();
    }
    
    /**
     * Returns the name of the specified class. This method
     * only returns the class name without package information.
     * @param clazz the Class
     * @return the class name
     */
    public static String getClassName(Class clazz)
    {
        String classPackage = getPackageName(clazz);
        if(classPackage.length() == 0)
        {
            return clazz.getName();
        }
        else
        {
            return clazz.getName().substring(classPackage.length() + 1);
        }
    }
    
    /**
     * Returns if the specified string is a Java language
     * keyword.
     * @param name the string
     * @return <code>true</code> if it is a keyword,
     *         <code>false</code> otherwise
     */
    public static boolean isKeyword(String name)
    {
        for(int ii = 0; ii < KEYWORDS.length; ii++)
        {
            if(KEYWORDS[ii].equals(name)) return true;
        }
        return false;
    }
    
    /**
     * Returns a suitable argument name for arguments
     * of type <code>argumentType</code> by converting 
     * the first character of the specified class name to 
     * lower case. If the resulting string is a Java 
     * keyword, <code>"Value"</code> is appended to the 
     * string.
     * @param argumentType the argument type
     * @return a suitable argument name
     */
    public static String getArgumentName(Class argumentType)
    {
       String name = getClassName(argumentType);
       name = StringUtil.lowerCase(name, 0);
        if(isKeyword(name))
        {
            name += "Value";
        }
        return name;
    }
}
