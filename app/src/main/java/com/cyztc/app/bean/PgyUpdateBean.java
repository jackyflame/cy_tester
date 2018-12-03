package com.cyztc.app.bean;

/**
 * Created by ywl5320 on 2017/8/10.
 */

public class PgyUpdateBean extends BaseBean{

    private String lastBuild;
    private String downloadURL;
    private String releaseNote;

    public String getLastBuild() {
        return lastBuild;
    }

    public void setLastBuild(String lastBuild) {
        this.lastBuild = lastBuild;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }
}
