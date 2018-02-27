package com.codesync.sender.delegate;

import mockit.Delegate;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Dharmendra.Singh.
 */
public class StreamDelegate {
    private Object object;

    public Delegate<ObjectOutputStream> outputStreamDelegate = new Delegate<ObjectOutputStream>() {

        public void writeObject(Object obj){
            object = obj;
        }
    };

    public Delegate<ObjectInputStream> inputStreamDelegate = new Delegate<ObjectInputStream>() {

        public Object readObject(){
            /*SyncResponseDTO syncResponse = new SyncResponseDTO();
            return syncResponse;*/
            return null;
        }
    };

}
