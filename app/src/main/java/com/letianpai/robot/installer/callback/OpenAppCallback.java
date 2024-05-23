package com.letianpai.robot.installer.callback;


import java.util.ArrayList;

/**
 * Created by liujunbin
 */

public class OpenAppCallback {

    private ArrayList<OpenAppCommandListener> mOpenAppCommandListener = new ArrayList<>();

    private static class OpenAppCallbackHolder {
        private static OpenAppCallback instance = new OpenAppCallback();
    }

    private OpenAppCallback() {
    }

    public static OpenAppCallback getInstance() {
        return OpenAppCallbackHolder.instance;
    }

    public interface OpenAppCommandListener {
        void onResponseOpenApp(String command);
    }

    public void setOpenAppListener(OpenAppCommandListener listener) {
        this.mOpenAppCommandListener.add(listener);
    }

    public void openApp(String packageName) {
        for (int i = 0;i< mOpenAppCommandListener.size();i++){
            if (mOpenAppCommandListener.get(i) != null) {
                mOpenAppCommandListener.get(i).onResponseOpenApp(packageName);
            }
        }

    }

}












