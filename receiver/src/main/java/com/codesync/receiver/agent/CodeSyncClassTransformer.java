package com.codesync.receiver.agent;

import com.codesync.common.Constants;
import com.codesync.common.Util;
import com.codesync.common.entity.Configurations;
import java.util.logging.Level;
import com.codesync.receiver.ServerConfigurations;

import com.google.inject.Inject;


import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Logger;
import org.springsource.loaded.agent.ClassPreProcessorAgentAdapter;

/**
 * @author Dharmendra.Singh
 */
public class CodeSyncClassTransformer implements ClassFileTransformer {

    private static Logger LOG = Logger.getLogger(Constants.LOG_NAME);

    private static List<String> packageList = new ArrayList<>();

    public static Map<Integer, ClassLoader> classLoaderList = new HashMap<>();
    //contains framework specific class like javaassist, cglib
    public static List<String> skipClass = new ArrayList<>();

    static {
        String packageName = System.getProperty("codesync.package");
        //String packageName = "com.dhar";
        if (packageName != null) {
            for (String pac : packageName.split(",")) {
                pac = pac.replaceAll("\\.", "/");
                packageList.add(pac);
                LOG.log(Level.INFO, "watching package " + pac);
            }
        }

        skipClass.add("$$_javassist");
        skipClass.add("$$EnhancerBySpringCGLIB");
        skipClass.add("$$EnhancerByCGLIB");
    }

    @Inject
    ClassPreProcessorAgentAdapter classPreProcessorAgentAdapter;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        InputStream inputStream = loader.getResourceAsStream(Constants.CODESYNC_FILE);
        byte[] newClassByte = classfileBuffer;

        //only transform class which are in user package.
        //LOG.log(Level.FINE, "transforming class2 " + className);
        //LOG.log(Level.FINE, "transforming class111 " + className);
        //LOG.log(Level.FINE, "transforming class111 " + className);
        for (String packageName : packageList) {
            if (className.contains(packageName)) {
                classLoaderList.put(loader.hashCode(), loader);
                if (!Util.containsInList(className, skipClass)) {
                    LOG.log(Level.FINE, "transforming class " + className);
                    newClassByte = classPreProcessorAgentAdapter.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                }
            }
        }

        if (inputStream != null) {
            Configurations configurations = Util.initConfigurations(inputStream);
            if (ServerConfigurations.getAppClassLoader(configurations.getId()) == null) {
                LOG.info("CodeSyncClassTransformer.transform : codesync.json is found.");
                LOG.info("CodeSyncClassTransformer.transform : setting classloader for = " + configurations.getId());
                ServerConfigurations.addAppClassLoader(configurations.getId(), loader);
                ServerConfigurations.createLocalPath(configurations.getId());
            }
        }

        return newClassByte;
    }

}
