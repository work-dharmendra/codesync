package com.codesync.sender.listener;

import com.codesync.common.Constants;
import com.codesync.common.Status;
import com.codesync.common.Util;
import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.common.dto.SyncResponseDTO;
import com.codesync.common.entity.Configurations;
import java.util.logging.Level;
import com.codesync.sender.ISender;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * @author Dharmendra.Singh
 */
public class Sender {

    private final static Logger LOG = Logger.getLogger(Constants.LOG_NAME);
    private static Logger USER_LOGGER = Logger.getLogger(Constants.USER_LOG_NAME);

    @Inject
    Configurations configurations;

    @Inject
    ISender sender;

    @Inject
    Util util;

    public void sync(Path path) {
        LOG.log(Level.FINER, "Sender.sync : starts for path = " + path);

        SyncRequestDTO syncRequestDTO = null;
        try {
            syncRequestDTO = new SyncRequestDTO();
            syncRequestDTO.fullPath = path.toFile().getAbsolutePath();
            syncRequestDTO.id = configurations.getId();
            syncRequestDTO.relativePath = Util.getRelativePath(path).toString();
            if (!Files.isDirectory(path)) {
                syncRequestDTO.data = Files.readAllBytes(path.toAbsolutePath());
            }

            SyncResponseDTO response = sender.send(syncRequestDTO);

            if (response.status == Status.SUCCESS) {
                LOG.log(Level.FINE, "Sender.sync : synced complete for = " + syncRequestDTO.relativePath);
                USER_LOGGER.log(Level.INFO, "Updated : " + syncRequestDTO.relativePath);
            }else{
                LOG.log(Level.FINE, "Sender.sync : synced failed for = " + syncRequestDTO.relativePath);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception while syncing path = " + path, e);
        }
        LOG.log(Level.FINER, "Sender.sync : ends for path = " + path);
    }

}
