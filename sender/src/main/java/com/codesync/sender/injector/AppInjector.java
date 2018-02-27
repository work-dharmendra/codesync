package com.codesync.sender.injector;

import com.codesync.common.Constants;
import com.codesync.common.Util;
import com.codesync.common.entity.Configurations;
import java.util.logging.Logger;
import com.codesync.sender.ISender;
import com.codesync.sender.SocketSender;
import com.google.inject.AbstractModule;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

/**
 * @author Dharmendra.Singh.
 */
public class AppInjector extends AbstractModule {

    private static Logger LOG = Logger.getLogger(Constants.LOG_NAME);
    private static Logger USER_LOGGER = Logger.getLogger(Constants.USER_LOG_NAME);

    private Path path;
    public AppInjector(){

    }

    public AppInjector(Path path){
        this.path = path;
    }

    @Override
    protected void configure() {
        java.util.logging.LogManager.getLogManager().reset();
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(new java.util.logging.Formatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                StringBuilder builder = new StringBuilder(1000);
                builder.append("[").append(record.getLevel()).append("] - ");
                builder.append("[").append(record.getSourceClassName()).append(".");
                builder.append(record.getSourceMethodName()).append("] - ");;
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
        java.util.logging.ConsoleHandler userLogHandler = new java.util.logging.ConsoleHandler();
        userLogHandler.setFormatter(new java.util.logging.Formatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {

                return record.getMessage() + "\n";
            }
        });
        java.util.logging.Level defaultLogLevel = java.util.logging.Level.INFO;
        //handler.setLevel(Level.ALL);
        if(System.getProperty("codesync.log.level") != null){
            LOG.setLevel(java.util.logging.Level.parse(System.getProperty("codesync.log.level").toUpperCase()));
            LOG.addHandler(handler);
        }else{
            LOG.setLevel(defaultLogLevel);
        }
        LOG.setLevel(java.util.logging.Level.OFF);
        LOG.addHandler(userLogHandler);
        USER_LOGGER.setLevel(java.util.logging.Level.INFO);
        for(java.util.logging.Handler handler1 : USER_LOGGER.getHandlers()){
            USER_LOGGER.removeHandler(handler1);
        }
        USER_LOGGER.addHandler(userLogHandler);

        requestStaticInjection(Util.class);
        Configurations configurations = Util.initConfigurations(path);

        bind(Configurations.class).toInstance(configurations);
        bind(ISender.class).to(SocketSender.class);

    }
}
