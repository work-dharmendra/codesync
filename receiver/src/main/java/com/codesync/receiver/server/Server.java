package com.codesync.receiver.server;

import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.common.entity.Configurations;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dharmendra.Singh on 6/11/14.
 */
public class Server {
    public static Map<Configurations, ClassLoader> appClassLoaderMap = new HashMap<Configurations, ClassLoader>();

    public void startServer(int portNumber){
        try {
            System.out.println("Receiver Start");

            SocketChannel sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);
            sChannel.bind( new InetSocketAddress("localhost", portNumber));
            while(true){
                if (sChannel.connect(new InetSocketAddress("localhost", portNumber))) {

                    ObjectInputStream ois =
                            new ObjectInputStream(sChannel.socket().getInputStream());

                    SyncRequestDTO syncRequestDTO = (SyncRequestDTO)ois.readObject();
                    System.out.println("syncDTO = " + syncRequestDTO);
                    System.out.println();
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("End Receiver");
    }
}
