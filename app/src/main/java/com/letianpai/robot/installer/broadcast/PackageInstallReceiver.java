package com.letianpai.robot.installer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.letianpai.robot.components.appinfo.AppListManager;
import com.letianpai.robot.installer.callback.AppStatusUpdateCallback;
import com.letianpai.robot.installer.consts.AppStoreConsts;


public class PackageInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 获取安装的包名
//            String packageName = intent.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME);
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.e("letianpai_auto_install", "packageName1: " + packageName);
        if (!TextUtils.isEmpty(packageName) && !AppListManager.isInThePackageList(packageName)){
            Log.e("letianpai_auto_install", "packageName2: " + packageName);
            AppStatusUpdateCallback.getInstance().installApkSuccess(packageName, AppStoreConsts.APP_STORE_STATUS_SUCCESS);
        }

    }

}