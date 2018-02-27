package com.codesync.sender.listener;

import com.codesync.common.entity.Configurations;
import com.codesync.sender.delegate.StreamDelegate;
import com.codesync.sender.injector.AppInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import org.junit.runner.RunWith;
import org.junit.*;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Dharmendra.Singh.
 */
@RunWith(JMockit.class)
public class SenderTest {

    private Injector injector;

    @Mocked
    Configurations configurations;

    @Mocked
    Socket socket;

    @Mocked
    ObjectInputStream inputStream;

    @Mocked
    ObjectOutputStream outputStream;

    StreamDelegate streamDelegate = new StreamDelegate();

    //@Before
    public void setUp() throws Exception {
        injector = Guice.createInjector( new AppInjector());

        final Object obj = null;
        new NonStrictExpectations(){
            {
                configurations.getId();
                result = "test";

                configurations.getDirs();
                result = new ArrayList<>(Arrays.asList(new String[]{"testdir"}));

                configurations.getRemoteApp();
                result = new ArrayList<>(Arrays.asList(new String[]{"localhost:9988"}));

                outputStream.writeObject(withAny(obj));
                result = streamDelegate.outputStreamDelegate;

                inputStream.readObject();
                result = streamDelegate.inputStreamDelegate;
            }
        };

    }

    //@After
    public void tearDown() throws Exception {
        injector = null;

    }

    private File getFileToBeSynced(){
        return new File(String.valueOf(getClass().getResource("/HelloWorld.java")));
    }

    @Test
    public void testSenderSyncFileBasic(){
        //System.out.println("DD");
        //Sender sender = injector.getInstance(Sender.class);
        //sender.sync(getFileToBeSynced());
    }


}
