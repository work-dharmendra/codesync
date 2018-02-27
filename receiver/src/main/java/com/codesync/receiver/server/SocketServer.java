package com.codesync.receiver.server;

import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.common.dto.SyncResponseDTO;
import java.util.logging.Level;
import com.codesync.receiver.Syncer;
import com.codesync.receiver.common.Util;
import com.codesync.receiver.dto.LoadFileDTO;
import com.google.inject.Inject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * It creates server using ServerSocket to receive changed file request.
 * @author Dharmendra.Singh.
 */
public class SocketServer implements IServer {

    private static Logger LOG = Logger.getLogger(SocketServer.class.getName());

    private int portNumber;

    public SocketServer(int portNumber) {
        this.portNumber = portNumber;
    }

    @Inject
    Syncer syncer;

    @Override
    public void startServer() {

        //create socketserver in new thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket welcomeSocket = new ServerSocket(portNumber)) {

                    while (true) {
                        try (Socket clientSocket = welcomeSocket.accept()) {

                            LOG.log(Level.FINE, "SocketServer.startServer : New Sync request received. ");
                            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
                            SyncRequestDTO syncRequestDTO = (SyncRequestDTO) inFromClient.readObject();

                            LOG.log(Level.FINE, "SocketServer.startServer : syncRequestDTO = " + syncRequestDTO);
                            LoadFileDTO dto = syncer.sync(Util.convertToLoadFile(syncRequestDTO));

                            LOG.log(Level.FINE, "SocketServer.startServer : loadFileDTO = " + dto);
                            SyncResponseDTO syncResponseDTO = Util.convertToSyncResponse(dto);

                            LOG.log(Level.FINE, "SocketServer.startServer : syncResponseDTO = " + syncResponseDTO);

                            outToClient.writeObject(syncResponseDTO);
                            outToClient.flush();

                            inFromClient.close();
                            outToClient.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            LOG.log(Level.SEVERE, "Exception while syncing", e);

                        }

                    }

                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Exception in startServer", e);
                }
            }
        }).start();


    }
}
