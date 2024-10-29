package com.letianpai.robot.installer.callback


/**
 * Created by liujunbin
 */
class ModeChangeCmdCallback private constructor() {
    private var mModeChangeCommandListener: ModeChangeCommandListener? = null

    private object ModeChangeCmdCallbackHolder {
        val instance: ModeChangeCmdCallback = ModeChangeCmdCallback()
    }

    fun interface ModeChangeCommandListener {
        fun onRobotModeChange(command: String, data: String)
    }

    fun setModeChangeCommandListener(listener: ModeChangeCommandListener?) {
        this.mModeChangeCommandListener = listener
    }

    fun changeRobotMode(command: String, data: String) {
        if (mModeChangeCommandListener != null) {
            mModeChangeCommandListener!!.onRobotModeChange(command, data)
        }
    }

    companion object {
        val instance: ModeChangeCmdCallback
            get() = ModeChangeCmdCallbackHolder.instance
    }
}












