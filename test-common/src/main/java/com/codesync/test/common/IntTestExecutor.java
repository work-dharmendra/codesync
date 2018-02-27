package com.codesync.test.common;

import java.util.logging.Level;
import java.util.logging.Logger;

public class IntTestExecutor {

    private static final Logger LOGGER = Logger.getLogger(IntTestExecutor.class.getName());

    public TestDto execute(TestDto testDto) {
        LOGGER.log(Level.INFO, "executing IntTestExecutor for {0}", testDto);

        try {
            Class.forName(testDto.className);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "class not found for {0}", testDto.className);
        }
        TestDto result = null;

        ClassExecutor classExecutor = new ClassExecutor();
        result = classExecutor.execute(testDto);
        switch (testDto.nameOfTest){
            case IntTestList.BASIC_METHOD_BODY_SYNC:
                result = classExecutor.execute(testDto);
                break;
            case IntTestList.NEWCLASS_NEWMETHOD:
                result = classExecutor.execute(testDto);
                break;
        }

        return result;
    }

    public void a() {

    }
}
