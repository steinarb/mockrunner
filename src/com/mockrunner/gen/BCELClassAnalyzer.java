package com.mockrunner.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

public class BCELClassAnalyzer
{
    private Map methodMap;
    
    public BCELClassAnalyzer(Class clazz)
    {
        methodMap = new HashMap();
        prepareMethodMap(clazz);
    }

    public boolean isMethodDeprecated(java.lang.reflect.Method reflectMethod)
    {
        org.apache.bcel.classfile.Method bcelMethod = getBCELMethod(reflectMethod);
        if(null == bcelMethod) return false;
        Attribute[] attributes = bcelMethod.getAttributes();
        DeprecatedVisitor visitor = new DeprecatedVisitor();
        for(int ii = 0; ii < attributes.length; ii++)
        {
            attributes[ii].accept(visitor);
            if(visitor.isDeprecated())
            {
                return true;
            }
        }
        return false;
    }
    
    private org.apache.bcel.classfile.Method getBCELMethod(java.lang.reflect.Method reflectMethod)
    {
        return (org.apache.bcel.classfile.Method)methodMap.get(reflectMethod);
    }
    
    private void prepareMethodMap(Class clazz)
    {
        java.lang.reflect.Method[] reflectMethods = clazz.getDeclaredMethods();
        JavaClass javaClass = Repository.lookupClass(clazz);
        org.apache.bcel.classfile.Method[] bcelMethods = javaClass.getMethods();
        List reflectMethodList = new ArrayList(Arrays.asList(reflectMethods));
        List bcelMethodList = new ArrayList(Arrays.asList(bcelMethods));
        while(!reflectMethodList.isEmpty())
        {
            findAndAddBCELMethod(reflectMethodList, bcelMethodList);
        }
    }
    
    private void findAndAddBCELMethod(List reflectMethodList, List bcelMethodList)
    {
        java.lang.reflect.Method reflectMethod = (java.lang.reflect.Method)reflectMethodList.remove(0);
        for(int ii = 0; ii < bcelMethodList.size(); ii++)
        {
            org.apache.bcel.classfile.Method bcelMethod = (org.apache.bcel.classfile.Method)bcelMethodList.get(ii);
            if(areMethodsEqual(reflectMethod, bcelMethod))
            {
                methodMap.put(reflectMethod, bcelMethod);
            }
        }
    }
    
    private boolean areMethodsEqual(java.lang.reflect.Method reflectMethod, org.apache.bcel.classfile.Method bcelMethod)
    {
        if(null == reflectMethod || null == bcelMethod) return false;
        if(!(reflectMethod.getName().equals(bcelMethod.getName()))) return false;
        Class reflectReturnType = reflectMethod.getReturnType();
        Class bcelReturnType = typeToClass(bcelMethod.getReturnType());
        if(!(reflectReturnType.equals(bcelReturnType))) return false;
        Class[] reflectArgumentTypes = reflectMethod.getParameterTypes();
        Class[] bcelArgumentTypes = getBCELArgumentList(bcelMethod);
        return Arrays.equals(reflectArgumentTypes, bcelArgumentTypes);
    }
    
    private Class[] getBCELArgumentList(org.apache.bcel.classfile.Method bcelMethod)
    {
        Type[] bcelArguments = bcelMethod.getArgumentTypes();
        List argumentList = new ArrayList();
        for(int ii = 0; ii < bcelArguments.length; ii++)
        {
            Class currentClass = typeToClass(bcelArguments[ii]);
            if(null != currentClass)
            {
                argumentList.add(currentClass);
            }
        }
        return (Class[])argumentList.toArray(new Class[argumentList.size()]);
    }

    private Class typeToClass(Type type)
    {
        if(null == type) return null;
        if(type instanceof BasicType)
        {
            BasicType basicType = (BasicType)type;
            if(basicType.equals(Type.BOOLEAN))
            {
                return Boolean.TYPE;
            }
            if(basicType.equals(Type.BYTE))
            {
                return Byte.TYPE;
            }
            if(basicType.equals(Type.CHAR))
            {
                return Character.TYPE;
            }
            if(basicType.equals(Type.DOUBLE))
            {
                return Double.TYPE;
            }
            if(basicType.equals(Type.FLOAT))
            {
                return Float.TYPE;
            }
            if(basicType.equals(Type.INT))
            {
                return Integer.TYPE;
            }
            if(basicType.equals(Type.LONG))
            {
                return Long.TYPE;
            }
            if(basicType.equals(Type.SHORT))
            {
                return Short.TYPE;
            }
            if(basicType.equals(Type.VOID))
            {
                return Void.TYPE;
            }
        }
        else if(type instanceof ObjectType)
        {
            ObjectType objectType = (ObjectType)type;
            try
            {
                return Class.forName(objectType.getClassName());
            } 
            catch(ClassNotFoundException exc)
            {
                throw new RuntimeException(exc);
            }
        }
        return null;
    }
    
    private class DeprecatedVisitor extends EmptyVisitor
    {
        private boolean deprecated = false;
        
        public void visitDeprecated(Deprecated arg0)
        {
            deprecated = true;
        }
        
        public boolean isDeprecated()
        {
            return deprecated;
        }
    }
}
