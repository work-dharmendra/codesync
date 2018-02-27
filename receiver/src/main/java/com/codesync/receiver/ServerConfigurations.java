package com.codesync.receiver;

import com.codesync.common.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dharmendra.Singh.
 */
public class ServerConfigurations {

    private static Logger LOG = Logger.getLogger(com.codesync.common.Constants.LOG_NAME);

    public static Map<String, ClassLoader> classLoaderMap = new HashMap<>();
    public static Map<String, String> localPathMap = new HashMap<>();

    public static void addAppClassLoader(String appId, ClassLoader classLoader){
        classLoaderMap.put(appId, classLoader);
    }

    public static ClassLoader getAppClassLoader(String appId){
        return classLoaderMap.get(appId);
    }

    /**
     * It creates local directory structure to store modified html, jsp files.
     * @param appId
     */
    public static void createLocalPath(String appId){
        LOG.log(Level.FINE, "createLocalPath : starts for appId = " + appId);
        String userHomeDir = System.getProperty("user.home");
        LOG.log(Level.FINE, "createLocalPath : userHomeDir = " + userHomeDir);

        File localCodeSyncPath = new File(userHomeDir, Constants.LOCAL_DIR);
        localCodeSyncPath.mkdir();
        File localAppPath = new File(localCodeSyncPath, appId);
        localAppPath.mkdir();
        File fileWebInf = new File(localAppPath, "WEB-INF");
        fileWebInf.mkdir();

        localPathMap.put(appId, localAppPath.getAbsolutePath());
        LOG.log(Level.FINE, "createLocalPath : appId = " + appId + ", localPath = " + localAppPath.getAbsolutePath());
        LOG.log(Level.FINE, "createLocalPath : ends for appId = " + appId);
    }

    public static String getAppLocalPath(String appId){
        return localPathMap.get(appId);
    }
}
