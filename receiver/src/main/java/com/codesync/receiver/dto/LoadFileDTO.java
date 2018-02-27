package com.codesync.receiver.dto;

/**
 * @author Dharmendra.Singh.
 */
public class LoadFileDTO {
    public String appId;
    public String fullPath;
    public String relativePath;

    public byte[] data;

    @Override
    public String toString() {
        return "LoadFileDTO{" +
                "appId='" + appId + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", relativePath='" + relativePath + '\'' +
                '}';
    }
}
