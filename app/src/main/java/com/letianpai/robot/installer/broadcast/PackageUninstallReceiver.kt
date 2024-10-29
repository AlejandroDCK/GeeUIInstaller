package com.letianpai.robot.installer.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PackageUninstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 获取卸载的包名
        val packageName = intent.data!!.schemeSpecificPart
        Log.e("letianpai_appstore", "2_PackageUninstallReceiver_packageName: $packageName")
        //        AppStatusUpdateCallback.getInstance().uninstallApk(packageName, AppStoreConsts.APP_STORE_STATUS_UNINSTALL);
//        Log.e("letianpai_appstore","2_PackageUninstallReceiver_packageName: "+ packageName);
//            if (PACKAGE_NAME.equals(packageName)) {
//                // 处理APK卸载事件
//                // 在这里可以执行相应的操作
//            }
    }
}
