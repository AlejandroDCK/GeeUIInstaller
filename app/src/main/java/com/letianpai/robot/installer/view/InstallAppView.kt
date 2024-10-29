package com.letianpai.robot.installer.view

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.content.pm.PackageInstaller.SessionParams
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo
import com.letianpai.robot.installer.R
import com.letianpai.robot.installer.activity.InstallAppActivity
import com.letianpai.robot.installer.callback.AppStatusUpdateCallback
import com.letianpai.robot.installer.callback.AppStatusUpdateCallback.AppStatusUpdateListener
import com.letianpai.robot.installer.callback.ModeChangeCmdCallback
import com.letianpai.robot.installer.callback.OpenAppCallback
import com.letianpai.robot.installer.consts.AppStoreConsts
import com.letianpai.robot.widget.back.BackButton
import com.letianpai.robot.widget.button.SettingsButton
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.ref.WeakReference

//import com.letianpai.robot.downloader.consts.AppStoreConsts;
//import com.letianpai.robot.downloader.parser.DownloadFileInfo;
/**
 * @author liujunbin
 */
class InstallAppView : LinearLayout {
    private var mContext: Context? = null
    private var install: SettingsButton? = null
    private var openApk: SettingsButton? = null
    private lateinit var display: SettingsButton
    private lateinit var apkName: TextView
    private var back: BackButton? = null
    private val downloadCode: TextView? = null
    private var updateViewHandler: UpdateViewHandler? = null
    private var currentPackageName: String? = null
    private var currentDownloadFileInfo: DownloadFileInfo? = null

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initViews(context)
    }

    private fun initViews(context: Context) {
        this.mContext = context
        inflate(context, R.layout.install_app, this)
        initView()
        fillData()
        addListeners()
    }

    private fun fillData() {
        currentDownloadFileInfo = (mContext as InstallAppActivity?)?.downloadFileInfo
    }

    private fun addListeners() {
        install!!.setOnClickListener {
            val downloadFileInfo = (mContext as InstallAppActivity?)?.downloadFileInfo
            if (downloadFileInfo != null) {
                showInstalling()
                // DownloadCallback.getInstance().installDownloadApp(downloadFileInfo);
                installApk(downloadFileInfo)
                AppStatusUpdateCallback.instance.updateApkStatus(
                    downloadFileInfo,
                    AppStoreConsts.APP_STORE_STATUS_INSTALLING
                )
            }
        }
        openApk!!.setOnClickListener { // launchApp(mContext,currentPackageName);
            openUserAppView(currentPackageName)
        }
        back!!.setOnClickListener {
            Log.e("letianpai", "back =========")
            (mContext as InstallAppActivity?)!!.finish()
        }
        AppStatusUpdateCallback.instance
            .setAppStatusUpdateListener(object : AppStatusUpdateListener {
                override fun appUpdateStatusFinished(
                    downloadFileInfo: DownloadFileInfo?,
                    status: Int
                ) {
                }

                override fun appDownloadProgress(
                    downloadFileInfo: DownloadFileInfo?,
                    progress: Int
                ) {
                }

                override fun appInstallSuccess(packageName: String, status: Int) {
                    installSuccess(packageName)

                    // TODO 此处增加删除内存的apk下载列表的逻辑
                }

                override fun appUninstall(packageName: String?, status: Int) {
                }
            })
    }

    private fun initView() {
        updateViewHandler = UpdateViewHandler(mContext!!)
        apkName = findViewById(R.id.apk_name)
        val downloadFileInfo = (mContext as InstallAppActivity?)?.downloadFileInfo
        if ((downloadFileInfo != null) && !TextUtils.isEmpty(downloadFileInfo.file_name)) {
            apkName.setText(downloadFileInfo.file_name)
        }

        install = findViewById(R.id.install)
        openApk = findViewById(R.id.openApk)
        display = findViewById(R.id.display)
        display.setEnabled(false)
        back = findViewById(R.id.back_install)
    }

    // https://yourservice.com/swagger/index.html#/code码相关接口/get_robot_api_v1_device_code_getInfo
    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context = WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_BIND_CODE -> responseUpdateBindCodeView(msg.obj as String)

                SHOW_OPEN_APP -> {
                    showOpenApp(msg.obj)
                    deleteApkFile()
                }

                SHOW_INSTALLING -> showInstallIng()
                INSTALLING_APK -> installApk(currentDownloadFileInfo)
            }
        }
    }

    private fun showInstallIng() {
        display!!.visibility = VISIBLE
        install!!.visibility = GONE
        openApk!!.visibility = GONE
    }

    private fun showOpenApp(obj: Any) {
        display!!.visibility = GONE
        install!!.visibility = GONE
        openApk!!.visibility = VISIBLE
    }

    private fun responseUpdateBindCodeView(code: String) {
        downloadCode!!.text = code
    }

    private fun updateBindCodeView(code: String) {
        val message = Message()
        message.what = UPDATE_BIND_CODE
        message.obj = code
        updateViewHandler!!.handleMessage(message)
    }

    private fun installSuccess(packageName: String) {
        currentPackageName = packageName
        val message = Message()
        message.what = SHOW_OPEN_APP
        message.obj = packageName
        updateViewHandler!!.handleMessage(message)
    }

    private fun showInstalling() {
        val message = Message()
        message.what = SHOW_INSTALLING
        updateViewHandler!!.handleMessage(message)
    }

    private fun openUserAppView(packageName: String?) {
        changeMode(packageName)
        OpenAppCallback.instance.openApp(packageName)
        (mContext as InstallAppActivity?)!!.finish()
        // responseBack();
    }

    fun changeMode(mode: String?) {
        if (!TextUtils.isEmpty(mode)) {
            val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
            ModeChangeCmdCallback.instance.changeRobotMode(CHANGE_SHOW_MODE, data)
        }
    }

    private fun installApk(downloadFileInfo: DownloadFileInfo?) {
        Thread {
            val file = File(APP_DOWNLOAD_FILE_FOLDER, downloadFileInfo?.file_name)
            createUserApkInstallSession(file)
        }.start()
    }

    private var session: PackageInstaller.Session? = null

    private fun createUserApkInstallSession(file: File) {
        val packageInstaller = mContext!!.packageManager.packageInstaller
        // 创建安装会话
        try {
            // 创建会话参数
            val params = SessionParams(
                SessionParams.MODE_FULL_INSTALL
            )
            // 获取会话ID
            val sessionId = packageInstaller.createSession(params)
            Log.e("letianpai_appstore", "1_createInstallSession: ")
            // 打开会话
            session = packageInstaller.openSession(sessionId)
            Log.e("letianpai_appstore", "2_createInstallSession: ")
            // 为会话注册回调
            // registerSessionCallback();
            Log.e("letianpai_appstore", "3_createInstallSession: ")

            // 将APK文件写入会话
            writeApkToSession(file, session!!)
            Log.e("letianpai_appstore", "4_createInstallSession: ")
            // 提交会话，开始安装
            session!!.commit(PendingIntent.getBroadcast(mContext, 0, Intent(),
                PendingIntent.FLAG_IMMUTABLE).intentSender)
            Log.e("letianpai_appstore", "5_createInstallSession: ")
            // } catch (IOException | PackageManager.NameNotFoundException e) {
        } catch (e: Exception) {
            Log.e("letianpai_appstore", "6_createInstallSession: ")
            Log.e("letianpai", "Failed to create install session", e)
            // 是否需要再次上报安装失败？
        }
    }

    @Throws(IOException::class)
    private fun writeApkToSession(apkFile: File, session: PackageInstaller.Session) {
        try {
            FileInputStream(apkFile).use { inputStream ->
                session.openWrite("apk", 0, -1).use { outputStream ->
                    val buffer = ByteArray(4096)
                    var length: Int
                    while ((inputStream.read(buffer).also { length = it }) != -1) {
                        outputStream.write(buffer, 0, length)
                    }

                    // 提交会话，完成写入
                    session.fsync(outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun deleteApkFile() {
        Thread {
            if (currentDownloadFileInfo != null && !TextUtils.isEmpty(currentDownloadFileInfo?.file_name)) {
                val file = File(APP_DOWNLOAD_FILE_FOLDER, currentDownloadFileInfo?.file_name)
                val status = file.delete()
                Log.e("letianpai", "status: $status")
            }
        }.start()
    }

    companion object {
        private const val UPDATE_BIND_CODE = 1
        private const val SHOW_OPEN_APP = 2
        private const val SHOW_INSTALLING = 3
        private const val INSTALLING_APK = 5
        fun launchApp(context: Context, packageName: String?) {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(packageName!!)

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                // 如果找不到对应的应用程序
                // 可以进行一些错误处理操作，或者显示提示信息给用户
            }
        }

        const val CHANGE_SHOW_MODE: String = "changeShowModule"

        const val APP_DOWNLOAD_FILE_FOLDER: String =
            "/storage/emulated/0/Android/filedownloader/files/"
    }
}
