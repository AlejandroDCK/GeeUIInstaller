package com.letianpai.robot.installer.callback;

import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo;

import java.util.ArrayList;

/**
 * 应用状态更新回调
 */

public class DownloadCallback {

    private ArrayList<DownloadUpdateListener> mDownloadUpdateListener = new ArrayList<>();

    private static class DownloadUpdateCallbackHolder {
        private static DownloadCallback instance = new DownloadCallback();
    }

    private DownloadCallback() {

    }

    public static DownloadCallback getInstance() {
        return DownloadUpdateCallbackHolder.instance;
    }

    public interface DownloadUpdateListener {
        void onDownloadAppStart(DownloadFileInfo downloadFileInfo);

        void onInstallApp(DownloadFileInfo downloadFileInfo);

    }

    public void addDownloadUpdateListener(DownloadUpdateListener listener) {
        this.mDownloadUpdateListener.add(listener);
    }

    public void removeDownloadUpdateListener(DownloadUpdateListener listener) {
        this.mDownloadUpdateListener.remove(listener);
    }

    public void setDownloadApp(DownloadFileInfo downloadFileInfo) {
        for (int i = 0; i < mDownloadUpdateListener.size(); i++) {
            if (mDownloadUpdateListener.get(i) != null) {
                mDownloadUpdateListener.get(i).onDownloadAppStart(downloadFileInfo);
            }
        }

    }

    public void installDownloadApp(DownloadFileInfo downloadFileInfo) {
        for (int i = 0; i < mDownloadUpdateListener.size(); i++) {
            if (mDownloadUpdateListener.get(i) != null) {
                mDownloadUpdateListener.get(i).onInstallApp(downloadFileInfo);
            }
        }
    }

}
