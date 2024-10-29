package com.letianpai.robot.installer.activity

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInstaller
import android.os.Bundle
import android.view.View
import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo
import com.letianpai.robot.installer.R
import com.letianpai.robot.installer.broadcast.PackageInstallReceiver
import com.letianpai.robot.installer.service.InstallerService


/**
 * @author liujunbin
 */
class InstallAppActivity : Activity() {
    var downloadFileInfo: DownloadFileInfo? = null
        private set
    private var installReceiver: PackageInstallReceiver? = null

    //    private String downloadFileInfo;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downloadFileInfo = intent.getSerializableExtra("downloadapk") as DownloadFileInfo?
        setContentView(R.layout.activity_install)
        registerAppReceiver()
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        startService()
    }

    private fun startService() {
        val intent = Intent(this@InstallAppActivity, InstallerService::class.java)
        startService(intent)
    }

    override fun onResume() {
        super.onResume()
    }

    fun registerAppReceiver() {
        installReceiver = PackageInstallReceiver()
        val installIntentFilter = IntentFilter()
        installIntentFilter.addAction(PackageInstaller.ACTION_SESSION_COMMITTED)
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        installIntentFilter.addDataScheme("package")
        registerReceiver(installReceiver, installIntentFilter)
    }

    fun unregisterAppReceiver() {
        if (installReceiver != null) {
            unregisterReceiver(installReceiver)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadFileInfo = null
        unregisterAppReceiver()
    }
}