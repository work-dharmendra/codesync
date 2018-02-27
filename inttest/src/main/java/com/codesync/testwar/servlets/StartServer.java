package com.codesync.testwar.servlets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.codesync.common.Status;
import com.codesync.common.dto.*;

/**
 * @author Dharmendra.Singh.
 */
public class StartServer {

    public static void main(String[] args) {
        try {
            ServerSocket welcomeSocket = new ServerSocket(9988);

            while (true) {
                try {
                    // Create the Client Socket
                    Socket clientSocket = welcomeSocket.accept();
                    System.out.println("Socket Extablished...");
                    // Create input and output streams to client
                    ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());

            /* Create Message object and retrive information */
                    SyncRequestDTO syncRequestDTO = (SyncRequestDTO) inFromClient.readObject();
                    System.out.println("syncRequestDTO = " + syncRequestDTO);
            /* Modify the message object */
                    SyncResponseDTO syncResponseDTO = new SyncResponseDTO();
                    syncResponseDTO.status = Status.SUCCESS;


            /* Send the modified Message object back */
                    outToClient.writeObject(syncResponseDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
