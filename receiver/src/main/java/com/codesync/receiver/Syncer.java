package com.codesync.receiver;

import com.codesync.common.Constants;
import java.util.logging.Level;
import com.codesync.receiver.dto.LoadFileDTO;
import com.codesync.receiver.loader.ClassLoad;
import com.google.inject.Inject;

import java.util.logging.Logger;

/**
 * @author Dharmendra.Singh.
 */
public class Syncer {
    private static Logger LOG = Logger.getLogger(Constants.LOG_NAME);
    private static Logger USER_LOGGER = Logger.getLogger(Constants.USER_LOG_NAME);

    @Inject
    ClassLoad classLoad;

    @Inject
    ServerConfigurations serverConfigurations;

    public LoadFileDTO sync(LoadFileDTO dto){
        LOG.log(Level.FINE, "Syncer.sync : syncing file for " + dto);

        classLoad.loadClass(ServerConfigurations.classLoaderMap.get(dto.appId), dto);

        LOG.log(Level.FINE, "Syncer.sync : complete syncing file for " + dto);

        USER_LOGGER.info("Updated " + dto.relativePath);
        return dto;
    }

}
