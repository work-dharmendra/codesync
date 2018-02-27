package com.codesync.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dharmendra.Singh
 */

public class Configurations {
    private String id;
    private List<String> dirs = new ArrayList<>();
    private List<String> remoteApp = new ArrayList<>();

    private static Configurations configurations = new Configurations();

    //private Configurations(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRemoteApp() {
        return remoteApp;
    }

    public void setRemoteApp(List<String> remoteApp) {
        this.remoteApp = remoteApp;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public List<String> getDirs() {

        return dirs;
    }

    @Override
    public String toString() {
        return "Configurations{" +
                "id='" + id + '\'' +
                ", dirs=" + dirs +
                ", remoteApp=" + remoteApp +
                '}';
    }
}
