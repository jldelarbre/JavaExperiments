package com.github.jldelarbre.javaExperiments.reified_generics;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ReifiedGenericType<RG extends ReifiedGeneric> {

 // implements hashcode and equals ?
    
    private final ParameterizedType parameterizedType;// separate rawtype et type argument
    
    public ReifiedGenericType() {
        checkUseAnonymousClass(getClass());
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        parameterizedType = (ParameterizedType) genericSuperclass.getActualTypeArguments()[0];
    }
    
    public ReifiedGenericType(ParameterizedType parameterizedType) {
        this.parameterizedType = parameterizedType;
    }
    
    public ReifiedGenericType(Type[] typeArguments) {
        this.parameterizedType = null;
    }
    
    public ReifiedGenericType(ReifiedGenericType<? super RG> complementType) {
        this.parameterizedType = null;
    }
    
    static void checkUseAnonymousClass(Class<?> clazz) {
        if (!clazz.isAnonymousClass()) {
            throw new RuntimeException("Default implementation of reifiedGenericType() only works with anonymous classes, " +
                                       "otherwise reimplement the method.");
        }
    }
    
    public ParameterizedType parameterizedType() {
        return parameterizedType;
    }
    
    public String getSimpleTypeName() {
        StringBuilder sb = new StringBuilder();
        extractSubTypeSimpleName(parameterizedType(), sb);
        return sb.toString();
    }

    private void extractSubTypeSimpleName(Type typeToProcess, StringBuilder sb) {
        if (typeToProcess instanceof ParameterizedType) {
            ParameterizedType castTypeToProcess = (ParameterizedType) typeToProcess;
            sb.append(Class.class.cast(castTypeToProcess.getRawType()).getSimpleName() + "<");
            boolean isFirst = true;
            for (Type type : castTypeToProcess.getActualTypeArguments()) {
                if (!isFirst) {
                    sb.append(",");
                }
                extractSubTypeSimpleName(type, sb);
                isFirst = false;
            }
            sb.append(">");
        } else {
            sb.append(Class.class.cast(typeToProcess).getSimpleName());
        }
    }
    
    // Enhancement: Only check if it is exact type not super type
    public boolean isInstance(ReifiedGeneric rg) {
        return rg.reifiedGenericType().parameterizedType().equals(parameterizedType());
    }
    
    public RG cast(ReifiedGeneric rg) {
        if (!isInstance(rg)) {
            String errMsg = rg.reifiedGenericType().getSimpleTypeName() + " is not castable to " + getSimpleTypeName();
            throw new ClassCastException(errMsg);
        }
        @SuppressWarnings("unchecked")
        RG castRg = (RG) rg;
        return castRg;
    }
}
