package com.codesync.senderui;

import com.codesync.common.Constants;
import com.codesync.common.Util;
import com.codesync.common.entity.Configurations;
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

    private static java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Constants.LOG_NAME);
    private Path path;
    public AppInjector(){

    }

    public AppInjector(Path path){
        this.path = path;
    }

    @Override
    protected void configure() {
        //LogManager.getLogManager().reset();
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
        java.util.logging.Level defaultLogLevel = java.util.logging.Level.INFO;
        handler.setLevel(java.util.logging.Level.ALL);
        if(System.getProperty("log.level") != null){
            LOG.setLevel(java.util.logging.Level.parse(System.getProperty("log.level").toUpperCase()));
        }else{
            LOG.setLevel(defaultLogLevel);
        }

        LOG.addHandler(handler);
        requestStaticInjection(Util.class);
        Configurations configurations = Util.initConfigurations(path);

        bind(Configurations.class).toInstance(configurations);
        bind(ISender.class).to(SocketSender.class);

    }
}
