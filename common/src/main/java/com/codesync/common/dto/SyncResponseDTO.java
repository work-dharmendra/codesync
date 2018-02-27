package com.codesync.common.dto;
import com.codesync.common.Status;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author Dharmendra.Singh
 */
public class SyncResponseDTO implements Externalizable{

    private static final long serialVersionUID = -2094130199629059034L;

    public String eventId;
    public Status status;

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(eventId);
        out.writeObject(status);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        eventId = (String) in.readObject();
        status = (Status) in.readObject();
    }

    @Override
    public String toString() {
        return "SyncResponseDTO{" +
                "eventId='" + eventId + '\'' +
                ", status=" + status +
                '}';
    }
}
