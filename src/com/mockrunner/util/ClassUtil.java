package com.mockrunner.util;

public class ClassUtil
{
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
}
