package com.codesync.test.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassExecutor {
    private static final Logger LOGGER = Logger.getLogger(ClassExecutor.class.getName());

    public TestDto execute(TestDto testDto) {
        LOGGER.log(Level.INFO, "executing testDto = {0}", testDto);
        TestDto result = new TestDto();

        boolean error = false;
        Class clazz = null;
        try {
            result.className = testDto.className;
            LOGGER.log(Level.INFO, "checking class existence for {0}", testDto.className);
            clazz = Class.forName(testDto.className);
        } catch (ClassNotFoundException e) {
            testDto.testException = TestException.CLASS_NOT_FOUND;
            error = true;
            LOGGER.log(Level.INFO, "class not found for {0}", testDto.className);
        }

        if (error) {
            return result;
        }

        Method method = null;
        try {
            LOGGER.log(Level.INFO, "checking method existence for class = {0}, method = {1}"
                    , new Object[]{testDto.className, testDto.methodName});
            method = clazz.getDeclaredMethod(testDto.methodName, testDto.methodParameters);
        } catch (NoSuchMethodException e) {
            error = true;
            testDto.testException = TestException.METHOD_NOT_FOUND;
            LOGGER.log(Level.INFO, "method not found for class = {0}, method = {1}"
                    , new Object[]{testDto.className, testDto.methodName});
        }

        if (error) {
            return testDto;
        }

        try {
            Object obj = clazz.newInstance();
            LOGGER.log(Level.INFO, "invoking method for class = {0}, method = {1}"
                    , new Object[]{testDto.className, testDto.methodName});
            Object methodResult = method.invoke(obj, testDto.methodParameterValue);
            if (methodResult != null) {
                testDto.result = String.valueOf(methodResult);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            testDto.testException = TestException.METHOD_INVOKE_EXCEPTION;
            LOGGER.log(Level.INFO, "method invoke erro for class = {0}, method = {1}"
                    , new Object[]{testDto.className, testDto.methodName});
        }

        return testDto;
    }
}
