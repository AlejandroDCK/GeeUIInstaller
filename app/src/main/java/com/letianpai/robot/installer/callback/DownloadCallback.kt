package com.letianpai.robot.installer.callback

import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo

/**
 * 应用状态更新回调
 */
class DownloadCallback private constructor() {
    private val mDownloadUpdateListener = ArrayList<DownloadUpdateListener?>()

    private object DownloadUpdateCallbackHolder {
        val instance: DownloadCallback = DownloadCallback()
    }

    interface DownloadUpdateListener {
        fun onDownloadAppStart(downloadFileInfo: DownloadFileInfo?)

        fun onInstallApp(downloadFileInfo: DownloadFileInfo?)
    }

    fun addDownloadUpdateListener(listener: DownloadUpdateListener?) {
        mDownloadUpdateListener.add(listener)
    }

    fun removeDownloadUpdateListener(listener: DownloadUpdateListener?) {
        mDownloadUpdateListener.remove(listener)
    }

    fun setDownloadApp(downloadFileInfo: DownloadFileInfo?) {
        for (i in mDownloadUpdateListener.indices) {
            if (mDownloadUpdateListener[i] != null) {
                mDownloadUpdateListener[i]!!.onDownloadAppStart(downloadFileInfo)
            }
        }
    }

    fun installDownloadApp(downloadFileInfo: DownloadFileInfo?) {
        for (i in mDownloadUpdateListener.indices) {
            if (mDownloadUpdateListener[i] != null) {
                mDownloadUpdateListener[i]!!.onInstallApp(downloadFileInfo)
            }
        }
    }

    companion object {
        val instance: DownloadCallback
            get() = DownloadUpdateCallbackHolder.instance
    }
}
