package com.letianpai.robot.installer.callback;


import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo;

import java.util.ArrayList;

/**
 * 应用状态更新回调
 */

public class AppStatusUpdateCallback {

    private ArrayList<AppStatusUpdateListener> mAppStatusUpdateListener = new ArrayList<>();
    private DownloadFileInfo currentDownloadFileInfo;


    private static class AppStatusUpdateCallbackHolder {
        private static AppStatusUpdateCallback instance = new AppStatusUpdateCallback();
    }

    private AppStatusUpdateCallback() {

    }

    public static AppStatusUpdateCallback getInstance() {
        return AppStatusUpdateCallbackHolder.instance;
    }

    public interface AppStatusUpdateListener {
        void appUpdateStatusFinished(DownloadFileInfo downloadFileInfo, int status);
        void appDownloadProgress(DownloadFileInfo downloadFileInfo, int progress);
        void appInstallSuccess(String packageName, int status);
        void appUninstall(String packageName, int status);
    }

    public void setAppStatusUpdateListener(AppStatusUpdateListener listener) {
        this.mAppStatusUpdateListener.add(listener);
    }

    public void updateApkStatus(DownloadFileInfo downloadFileInfo, int status) {
        this.currentDownloadFileInfo = downloadFileInfo;
        for (int i = 0;i< mAppStatusUpdateListener.size();i++){
            if (mAppStatusUpdateListener.get(i) != null) {
                mAppStatusUpdateListener.get(i).appUpdateStatusFinished(downloadFileInfo, status);
            }
        }
    }

    public void installApkSuccess(String packageName, int status) {
        for (int i = 0;i< mAppStatusUpdateListener.size();i++){
            if (mAppStatusUpdateListener.get(i) != null) {
                mAppStatusUpdateListener.get(i).appInstallSuccess(packageName, status);
            }
        }
    }
    public void uninstallApk(String packageName, int status) {
        for (int i = 0;i< mAppStatusUpdateListener.size();i++){
            if (mAppStatusUpdateListener.get(i) != null) {
                mAppStatusUpdateListener.get(i).appUninstall(packageName, status);
            }
        }
    }

    public void updateApkDownloadProgress(DownloadFileInfo downloadFileInf, int progress) {

        for (int i = 0;i< mAppStatusUpdateListener.size();i++){
            if (mAppStatusUpdateListener.get(i) != null) {
                mAppStatusUpdateListener.get(i).appDownloadProgress(downloadFileInf, progress);
            }
        }
    }


    public DownloadFileInfo getCurrentAppStoreData() {
        return currentDownloadFileInfo;
    }


}
