package com.codesync.sender;

import com.codesync.common.dto.SyncRequestDTO;
import com.codesync.common.dto.SyncResponseDTO;

/**
 * @author Dharmendra.Singh.
 */
public interface ISender {

    SyncResponseDTO send(SyncRequestDTO syncRequestDTO);
}
