package com.codesync.common.dto;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

/**
 * @author Dharmendra.Singh
 */
public class SyncRequestDTO implements Externalizable{
    private static final long serialVersionUID = -5107199748916833795L;

    public SyncRequestDTO(){
        eventId = UUID.randomUUID().toString();
    }
    public String eventId;
    public String id;
    public String fullPath;
    public String relativePath;
    public byte[] data;
    //public FileType fileType;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(eventId);
        out.writeObject(id);
        out.writeObject(fullPath);
        out.writeObject(relativePath);
        out.writeObject(data);
        //out.writeObject(fileType);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        eventId = (String) in.readObject();
        id = (String) in.readObject();
        fullPath = (String) in.readObject();
        relativePath = (String) in.readObject();
        data = (byte[]) in.readObject();
        //fileType = (FileType) in.readObject();
    }

    @Override
    public String toString() {
        return "SyncRequestDTO{" +
                "eventId='" + eventId + '\'' +
                ", id='" + id + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", fullPath='" + fullPath + '\'' +
                //", data=" + Arrays.toString(data) +
                '}';
    }
}
