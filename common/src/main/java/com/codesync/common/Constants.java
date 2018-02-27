package com.codesync.common;

/**
 * Created by Dharmendra.Singh on 6/11/14.
 */
public class Constants {
    public static final String LOG_NAME = "codesync";
    public static final String LOCAL_DIR = ".codesync";
    public static final String CONFIGURATIONS_FILE = "codesync";
    public static final int DEFAULT_PORT = 9988;

    //This field represent allowable file extension which needs to be synced. All other file will be ignored
    public static final String [] ALLOWED_EXTENSION = new String [ ]{ "class", "html", "htm" };

    public static final String USER_LOG_NAME = "codesync_user";

    public static final String CODESYNC_FILE = "/codesync.json";
}
