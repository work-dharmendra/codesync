package com.codesync.receiver;

import com.codesync.common.Constants;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.codesync.receiver.common.Util;
import com.codesync.receiver.loader.ClassLoad;
import com.codesync.receiver.loader.SpringClassLoader;
import com.codesync.receiver.server.IServer;
import com.codesync.receiver.server.SocketServer;
import com.google.inject.AbstractModule;
import org.springsource.loaded.agent.ClassPreProcessorAgentAdapter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.instrument.ClassFileTransformer;

/**
 * @author Dharmendra.Singh.
 */
public class AppInjector extends AbstractModule {

    private static Logger LOG = Logger.getLogger(Constants.LOG_NAME);
    private static Logger USER_LOGGER = Logger.getLogger(Constants.USER_LOG_NAME);

    private static ClassFileTransformer classFileTransformer = new ClassPreProcessorAgentAdapter();

    @Override
    protected void configure() {
        java.util.logging.Handler handler;
        if(System.getProperty("codesync.log.file") != null){
            try {
                handler = new java.util.logging.FileHandler(System.getProperty("codesync.log.file"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            handler = new java.util.logging.ConsoleHandler();
        }

        handler.setFormatter(new java.util.logging.Formatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                StringBuilder builder = new StringBuilder();
                builder.append("[").append(record.getLevel()).append("] - ");
                builder.append("[").append(record.getSourceClassName()).append(".");
                builder.append(record.getSourceMethodName()).append("] - ");
                builder.append(formatMessage(record)).append("  ");
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    builder.append(sw.toString());
                }
                builder.append("\n");
                return builder.toString();
            }
        });
        handler.setLevel(java.util.logging.Level.FINEST);
        ConsoleHandler userLogHandler = new ConsoleHandler();
        userLogHandler.setFormatter(new java.util.logging.Formatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {

                return record.getMessage() + "\n";
            }
        });

        for(java.util.logging.Handler handler1 : USER_LOGGER.getHandlers()){
            USER_LOGGER.removeHandler(handler1);
        }

        for(java.util.logging.Handler handler1 : LOG.getHandlers()){
            LOG.removeHandler(handler1);
        }
        LOG.addHandler(handler);
        LOG.setLevel(Level.FINE);

        USER_LOGGER.setLevel(Level.FINEST);
        USER_LOGGER.addHandler(userLogHandler);
        requestStaticInjection(Util.class);
        requestInjection(ServerConfigurations.class);

        SocketServer socketServer = new SocketServer(com.codesync.receiver.common.Constants.DEFAULT_PORT);
        bind(IServer.class).toInstance(socketServer);

        bind(ClassFileTransformer.class).toInstance(classFileTransformer);
        bind(ClassPreProcessorAgentAdapter.class).toInstance((ClassPreProcessorAgentAdapter) classFileTransformer);

        SpringClassLoader springClassLoader = new SpringClassLoader();
        bind(ClassLoad.class).toInstance(springClassLoader);

    }
}
