package com.letianpai.robot.installer.callback

import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo


/**
 * 应用状态更新回调
 */
class AppStatusUpdateCallback private constructor() {
    private val mAppStatusUpdateListener = ArrayList<AppStatusUpdateListener?>()
    var currentAppStoreData: DownloadFileInfo? = null
        private set


    private object AppStatusUpdateCallbackHolder {
        val instance: AppStatusUpdateCallback = AppStatusUpdateCallback()
    }

    interface AppStatusUpdateListener {
        fun appUpdateStatusFinished(downloadFileInfo: DownloadFileInfo?, status: Int)
        fun appDownloadProgress(downloadFileInfo: DownloadFileInfo?, progress: Int)
        fun appInstallSuccess(packageName: String, status: Int)
        fun appUninstall(packageName: String?, status: Int)
    }

    fun setAppStatusUpdateListener(listener: AppStatusUpdateListener?) {
        mAppStatusUpdateListener.add(listener)
    }

    fun updateApkStatus(downloadFileInfo: DownloadFileInfo?, status: Int) {
        this.currentAppStoreData = downloadFileInfo
        for (i in mAppStatusUpdateListener.indices) {
            if (mAppStatusUpdateListener[i] != null) {
                mAppStatusUpdateListener[i]!!.appUpdateStatusFinished(downloadFileInfo, status)
            }
        }
    }

    fun installApkSuccess(packageName: String, status: Int) {
        for (i in mAppStatusUpdateListener.indices) {
            if (mAppStatusUpdateListener[i] != null) {
                mAppStatusUpdateListener[i]!!.appInstallSuccess(packageName, status)
            }
        }
    }

    fun uninstallApk(packageName: String?, status: Int) {
        for (i in mAppStatusUpdateListener.indices) {
            if (mAppStatusUpdateListener[i] != null) {
                mAppStatusUpdateListener[i]!!.appUninstall(packageName, status)
            }
        }
    }

    fun updateApkDownloadProgress(downloadFileInf: DownloadFileInfo?, progress: Int) {
        for (i in mAppStatusUpdateListener.indices) {
            if (mAppStatusUpdateListener[i] != null) {
                mAppStatusUpdateListener[i]!!.appDownloadProgress(downloadFileInf, progress)
            }
        }
    }


    companion object {
        val instance: AppStatusUpdateCallback
            get() = AppStatusUpdateCallbackHolder.instance
    }
}
