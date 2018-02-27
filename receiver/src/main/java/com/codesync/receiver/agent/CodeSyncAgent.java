package com.codesync.receiver.agent;

import com.codesync.common.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.codesync.receiver.AppInjector;
import com.codesync.receiver.server.IServer;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.lang.instrument.Instrumentation;

/**
 * @author Dharmendra.Singh
 */
public class CodeSyncAgent {
    private static Logger LOG = Logger.getLogger(Constants.LOG_NAME);

    public static void premain(String args, Instrumentation instrumentation) {

        try {
            Injector injector= Guice.createInjector( new AppInjector());
            instrumentation.addTransformer(injector.getInstance(CodeSyncClassTransformer.class));
            //add transformer from spring loaded
            //instrumentation.addTransformer(injector.getInstance(ClassFileTransformer.class));
            injector.getInstance(IServer.class).startServer();

        } catch (Exception e) {
            LOG.log(Level.FINE, "exception in initializing code sync agent", e);

        }
    }

}
