package com.letianpai.robot.installer.callback;


/**
 * Created by liujunbin
 */

public class ModeChangeCmdCallback {

    private ModeChangeCommandListener mModeChangeCommandListener;

    private static class ModeChangeCmdCallbackHolder {
        private static ModeChangeCmdCallback instance = new ModeChangeCmdCallback();
    }

    private ModeChangeCmdCallback() {
    }

    public static ModeChangeCmdCallback getInstance() {
        return ModeChangeCmdCallbackHolder.instance;
    }

    public interface ModeChangeCommandListener {
        void onRobotModeChange(String command, String data);
    }

    public void setModeChangeCommandListener(ModeChangeCommandListener listener) {
        this.mModeChangeCommandListener = listener;
    }

    public void changeRobotMode(String command, String data) {
        if (mModeChangeCommandListener != null) {
            mModeChangeCommandListener.onRobotModeChange(command, data);
        }
    }

}












