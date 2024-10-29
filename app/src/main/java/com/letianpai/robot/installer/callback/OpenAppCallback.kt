package com.letianpai.robot.installer.callback


/**
 * Created by liujunbin
 */
class OpenAppCallback private constructor() {
    private val mOpenAppCommandListener = ArrayList<OpenAppCommandListener?>()

    private object OpenAppCallbackHolder {
        val instance: OpenAppCallback = OpenAppCallback()
    }

    interface OpenAppCommandListener {
        fun onResponseOpenApp(command: String?)
    }

    fun setOpenAppListener(listener: OpenAppCommandListener?) {
        mOpenAppCommandListener.add(listener)
    }

    fun openApp(packageName: String?) {
        for (i in mOpenAppCommandListener.indices) {
            if (mOpenAppCommandListener[i] != null) {
                mOpenAppCommandListener[i]!!.onResponseOpenApp(packageName)
            }
        }
    }

    companion object {
        val instance: OpenAppCallback
            get() = OpenAppCallbackHolder.instance
    }
}












