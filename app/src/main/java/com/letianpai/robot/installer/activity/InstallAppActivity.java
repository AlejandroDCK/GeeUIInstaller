package com.letianpai.robot.installer.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.letianpai.robot.installer.R;
import com.letianpai.robot.installer.broadcast.PackageInstallReceiver;
import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo;
import com.letianpai.robot.installer.service.InstallerService;

/**
 * @author liujunbin
 */
public class InstallAppActivity extends Activity {
    private DownloadFileInfo downloadFileInfo;
    private PackageInstallReceiver installReceiver;
//    private String downloadFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadFileInfo = (DownloadFileInfo) getIntent().getSerializableExtra("downloadapk");
        setContentView(R.layout.activity_install);
        registerAppReceiver();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        startService();
    }

    private void startService() {
        Intent intent = new Intent(InstallAppActivity.this, InstallerService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public DownloadFileInfo getDownloadFileInfo() {
        return downloadFileInfo;
    }

    public void registerAppReceiver() {
        installReceiver = new PackageInstallReceiver();
        IntentFilter installIntentFilter = new IntentFilter();
        installIntentFilter.addAction(PackageInstaller.ACTION_SESSION_COMMITTED);
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        installIntentFilter.addDataScheme("package");
        registerReceiver(installReceiver, installIntentFilter);

    }

    public void unregisterAppReceiver() {
        if (installReceiver != null) {
            unregisterReceiver(installReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadFileInfo = null;
        unregisterAppReceiver();


    }
}