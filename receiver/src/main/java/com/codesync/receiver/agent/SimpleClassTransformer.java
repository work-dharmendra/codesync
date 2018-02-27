package com.codesync.receiver.agent;

import com.codesync.common.Constants;
import java.util.logging.Logger;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Dharmendra.Singh
 */
public class SimpleClassTransformer implements ClassFileTransformer {

    private static Logger LOG = Logger.getLogger(Constants.LOG_NAME);

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (className.contains("codesync")) {
            System.out.println("loading class = " + className);
        }
        return classfileBuffer;
    }

}
