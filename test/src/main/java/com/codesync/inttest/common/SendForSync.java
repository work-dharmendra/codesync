package com.codesync.inttest.common;

import com.codesync.common.Util;
import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.common.dto.SyncResponseDTO;
import com.codesync.common.entity.Configurations;
import com.codesync.sender.SocketSender;

import java.nio.file.Path;

public class SendForSync {

    private SocketSender socketSender;

    public SendForSync() {

    }

    public void init(Path path){
        Configurations configurations = Util.initConfigurations(path);
        socketSender = new SocketSender();
        socketSender.setConfigurations(configurations);
    }

    public SyncResponseDTO sync(SyncRequestDTO syncRequestDTO){
        return socketSender.send(syncRequestDTO);
    }
}
