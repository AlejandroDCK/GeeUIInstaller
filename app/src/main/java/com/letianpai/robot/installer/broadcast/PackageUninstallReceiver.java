package com.letianpai.robot.installer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.letianpai.robot.installer.callback.AppStatusUpdateCallback;
import com.letianpai.robot.installer.consts.AppStoreConsts;


public class PackageUninstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 获取卸载的包名
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.e("letianpai_appstore","2_PackageUninstallReceiver_packageName: "+ packageName);
//        AppStatusUpdateCallback.getInstance().uninstallApk(packageName, AppStoreConsts.APP_STORE_STATUS_UNINSTALL);
//        Log.e("letianpai_appstore","2_PackageUninstallReceiver_packageName: "+ packageName);
//            if (PACKAGE_NAME.equals(packageName)) {
//                // 处理APK卸载事件
//                // 在这里可以执行相应的操作
//            }
    }
}
