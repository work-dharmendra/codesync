package com.codesync.sender.listener;

import com.codesync.common.Constants;
import com.codesync.sender.injector.AppInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author Dharmendra.Singh.
 */
public class StartCodeSync {

    private static final Logger USER_LOGGER = Logger.getLogger(Constants.USER_LOG_NAME);

    public static void main(String []args){
        args = new String[]{"/Users/dharmendra/code/codesync/inttest/src/main/resources/codesync.json"};
        Injector injector = Guice.createInjector( new AppInjector(Paths.get(args[0])));
        USER_LOGGER.info("starting codesync from " + args[0]);

        FileWatcher fileWatcher = injector.getInstance(FileWatcher.class);
        fileWatcher.watchDir();

    }
}
