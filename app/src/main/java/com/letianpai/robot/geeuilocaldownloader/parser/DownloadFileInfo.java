package com.letianpai.robot.geeuilocaldownloader.parser;

import java.io.Serializable;

/**
 * @author liujunbin
 */
public class DownloadFileInfo implements Serializable {

    private String url;
    private String file_name;
    private int downloadStatus;
    private String fileType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "DownloadFileInfo{" +
                "url='" + url + '\'' +
                ", file_name='" + file_name + '\'' +
                ", downloadStatus=" + downloadStatus +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
