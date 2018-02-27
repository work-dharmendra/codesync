package com.codesync.receiver.loader;

import com.codesync.receiver.dto.LoadFileDTO;

/**
 * @author Dharmendra.Singh.
 */
public interface ClassLoad {
    boolean loadClass(ClassLoader classLoader, LoadFileDTO dto);
}
