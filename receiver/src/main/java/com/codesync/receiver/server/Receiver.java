package com.codesync.receiver.server;

import com.codesync.common.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Created by Dharmendra.Singh on 6/11/14.
 */
public class Receiver {

    public static void startServer(){
        System.out.println("Receiver Start");

        try {
            SocketChannel sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);
            if (sChannel.connect(new InetSocketAddress("localhost", Constants.DEFAULT_PORT))) {

                ObjectInputStream ois =
                        new ObjectInputStream(sChannel.socket().getInputStream());

                String s = (String)ois.readObject();
                System.out.println("String is: '" + s + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("End Receiver");
    }
}
