package com.codesync.sender;

import com.codesync.common.Constants;
import com.codesync.common.Status;
import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.common.dto.SyncResponseDTO;
import com.codesync.common.entity.Configurations;
import java.util.logging.Level;
import com.google.inject.Inject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Dharmendra.Singh.
 */
public class SocketSender implements ISender {

    private static Logger DEBUG_LOG = Logger.getLogger(Constants.LOG_NAME);
    private static Logger USER_LOGGER = Logger.getLogger(Constants.USER_LOG_NAME);

    @Inject
    Configurations configurations;

    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }

    @Override
    public SyncResponseDTO send(SyncRequestDTO syncRequestDTO) {
        SyncResponseDTO syncResponseDTO = new SyncResponseDTO();
        for (String remoteApp : configurations.getRemoteApp()) {

            DEBUG_LOG.log(Level.FINE, "SocketSender.send : sync for remoteApp = " + remoteApp);
            String host = remoteApp.split(":")[0];
            int port = Integer.parseInt(remoteApp.split(":")[1]);
            try (Socket clientSocket = new Socket(host, port)) {
                ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

                DEBUG_LOG.log(Level.FINE, "SocketSender.send : sending SyncRequestDTO = " + syncRequestDTO);
                outToServer.writeObject(syncRequestDTO);
                outToServer.flush();

                DEBUG_LOG.log(Level.FINE, "SocketSender.send : getting response from Server ");

                syncResponseDTO = (SyncResponseDTO) inFromServer.readObject();

                DEBUG_LOG.log(Level.FINE, "SocketSender.send : received SyncResponseDTO = " + syncResponseDTO);
            } catch (Exception e) {
                syncResponseDTO.status = Status.FAIL;
                DEBUG_LOG.log(Level.SEVERE, "SocketSender.send : exception occurred while " +
                        "syncing path = " + syncRequestDTO.relativePath
                        + ", remote = " + remoteApp, e);
            }
        }

        return syncResponseDTO;
    }
}
