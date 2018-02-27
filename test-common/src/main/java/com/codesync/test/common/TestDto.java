package com.codesync.test.common;

import java.util.Arrays;

public class TestDto {

    public String nameOfTest;
    public String className;
    public String methodName;
    public Class[] methodParameters;
    public Object[] methodParameterValue;
    public String result;
    public TestException testException;

    public String getNameOfTest() {
        return nameOfTest;
    }

    public void setNameOfTest(String nameOfTest) {
        this.nameOfTest = nameOfTest;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getMethodParameters() {
        return methodParameters;
    }

    public void setMethodParameters(Class[] methodParameters) {
        this.methodParameters = methodParameters;
    }

    public Object[] getMethodParameterValue() {
        return methodParameterValue;
    }

    public void setMethodParameterValue(Object[] methodParameterValue) {
        this.methodParameterValue = methodParameterValue;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TestException getTestException() {
        return testException;
    }

    public void setTestException(TestException testException) {
        this.testException = testException;
    }

    public TestDto() {
    }

    public TestDto(String nameOfTest, String className, String methodName, Class[] methodParameters) {
        this.nameOfTest = nameOfTest;
        this.className = className;
        this.methodName = methodName;
        this.methodParameters = methodParameters;
    }

    @Override
    public String toString() {
        return "TestDto{" +
                "nameOfTest='" + nameOfTest + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodParameters=" + Arrays.toString(methodParameters) +
                ", methodParameterValue=" + Arrays.toString(methodParameterValue) +
                ", result='" + result + '\'' +
                ", testException=" + testException +
                '}';
    }
}
