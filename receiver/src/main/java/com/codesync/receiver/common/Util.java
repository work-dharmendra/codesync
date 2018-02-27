package com.codesync.receiver.common;

import com.codesync.common.Status;
import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.common.dto.SyncResponseDTO;
import com.codesync.receiver.dto.LoadFileDTO;

/**
 * @author Dharmendra.Singh.
 */
public class Util {

    public static LoadFileDTO convertToLoadFile(SyncRequestDTO requestDTO){
        LoadFileDTO dto = new LoadFileDTO();
        dto.appId = requestDTO.id;
        dto.fullPath = requestDTO.fullPath;
        dto.relativePath = requestDTO.relativePath;
        dto.data = requestDTO.data;

        return dto;
    }

    public static SyncResponseDTO convertToSyncResponse(LoadFileDTO loadFileDTO){
        SyncResponseDTO dto = new SyncResponseDTO();
        dto.status = Status.SUCCESS;
        return dto;
    }
}
