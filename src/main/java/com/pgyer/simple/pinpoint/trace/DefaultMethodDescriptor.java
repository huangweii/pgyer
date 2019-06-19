package com.pgyer.simple.pinpoint.trace;

import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;

import java.util.Arrays;

public class DefaultMethodDescriptor implements MethodDescriptor {
    private String className;

    private String methodName;

    private String[] parameterTypes;

    private String[] parameterVariableName;


    private String parameterDescriptor;

    private String apiDescriptor;

    private int lineNumber;

    private int apiId = 0;

    private String fullName;

    public DefaultMethodDescriptor() {
    }

    public DefaultMethodDescriptor(String className, String methodName, String[] parameterTypes, String[] parameterVariableName) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameterVariableName = parameterVariableName;
    }

    public String getParameterDescriptor() {
        return parameterDescriptor;
    }

    public void setParameterDescriptor(String parameterDescriptor) {
        this.parameterDescriptor = parameterDescriptor;
    }


    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParameterVariableName(String[] parameterVariableName) {
        this.parameterVariableName = parameterVariableName;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }


    public String getMethodName() {
        return methodName;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public String[] getParameterTypes() {
        return parameterTypes;
    }


    public String[] getParameterVariableName() {
        return parameterVariableName;
    }


    public int getLineNumber() {
        return lineNumber;
    }


    public String getFullName() {
        if (fullName != null) {
            return fullName;
        }
        StringBuilder buffer = new StringBuilder(256);
        buffer.append(className);
        buffer.append(".");
        buffer.append(methodName);
//        buffer.append(parameterDescriptor);
//        if (lineNumber != -1) {
//            buffer.append(":");
//            buffer.append(lineNumber);
//        }
        fullName = buffer.toString();
        return fullName;
    }

    public void setApiDescriptor(String apiDescriptor) {
        this.apiDescriptor = apiDescriptor;
    }


    public String getApiDescriptor() {
        return apiDescriptor;
    }


    public void setApiId(int apiId) {
        this.apiId = apiId;
    }


    public int getApiId() {
        return apiId;
    }



    public String toString() {
        return "DefaultMethodDescriptor{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + (parameterTypes == null ? null : Arrays.asList(parameterTypes)) +
                ", parameterVariableName=" + (parameterVariableName == null ? null : Arrays.asList(parameterVariableName)) +
                ", parameterDescriptor='" + parameterDescriptor + '\'' +
                ", apiDescriptor='" + apiDescriptor + '\'' +
                ", lineNumber=" + lineNumber +
                ", apiId=" + apiId +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
