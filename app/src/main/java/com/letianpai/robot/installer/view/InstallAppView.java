package com.letianpai.robot.installer.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

//import com.letianpai.robot.downloader.consts.AppStoreConsts;
//import com.letianpai.robot.downloader.parser.DownloadFileInfo;
import com.letianpai.robot.installer.R;
import com.letianpai.robot.installer.activity.InstallAppActivity;
import com.letianpai.robot.installer.callback.AppStatusUpdateCallback;
import com.letianpai.robot.installer.callback.ModeChangeCmdCallback;
import com.letianpai.robot.installer.callback.OpenAppCallback;
import com.letianpai.robot.installer.consts.AppStoreConsts;
import com.letianpai.robot.geeuilocaldownloader.parser.DownloadFileInfo;
import com.letianpai.robot.widget.back.BackButton;
import com.letianpai.robot.widget.button.SettingsButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

/**
 * @author liujunbin
 */
public class InstallAppView extends LinearLayout {

    private Context mContext;
    private SettingsButton install;
    private SettingsButton openApk;
    private SettingsButton display;
    private TextView apkName;
    private BackButton back;
    private static final int UPDATE_BIND_CODE = 1;
    private static final int SHOW_OPEN_APP = 2;
    private static final int SHOW_INSTALLING = 3;
    private static final int INSTALLING_APK = 5;
    private TextView downloadCode;
    private UpdateViewHandler updateViewHandler;
    private String currentPackageName;
    private DownloadFileInfo currentDownloadFileInfo;

    public InstallAppView(Context context) {
        super(context);
        initViews(context);
    }

    public InstallAppView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public InstallAppView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public InstallAppView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context) {
        this.mContext = context;
        inflate(context, R.layout.install_app, this);
        initView();
        fillData();
        addListeners();
    }

    private void fillData() {
        currentDownloadFileInfo = ((InstallAppActivity) mContext).getDownloadFileInfo();
    }

    private void addListeners() {
        install.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadFileInfo downloadFileInfo = ((InstallAppActivity) mContext).getDownloadFileInfo();
                if (downloadFileInfo != null) {
                    showInstalling();
                    // DownloadCallback.getInstance().installDownloadApp(downloadFileInfo);
                    installApk(downloadFileInfo);
                    AppStatusUpdateCallback.getInstance().updateApkStatus(downloadFileInfo,
                            AppStoreConsts.APP_STORE_STATUS_INSTALLING);
                }
            }
        });
        openApk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // launchApp(mContext,currentPackageName);
                openUserAppView(currentPackageName);

            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("letianpai", "back =========");
                ((InstallAppActivity) mContext).finish();
                ;
            }
        });
        AppStatusUpdateCallback.getInstance()
                .setAppStatusUpdateListener(new AppStatusUpdateCallback.AppStatusUpdateListener() {

                    @Override
                    public void appUpdateStatusFinished(DownloadFileInfo downloadFileInfo, int status) {

                    }

                    @Override
                    public void appDownloadProgress(DownloadFileInfo downloadFileInfo, int progress) {

                    }

                    @Override
                    public void appInstallSuccess(String packageName, int status) {
                        installSuccess(packageName);
                        // TODO 此处增加删除内存的apk下载列表的逻辑

                    }

                    @Override
                    public void appUninstall(String packageName, int status) {

                    }
                });

    }

    private void initView() {
        updateViewHandler = new UpdateViewHandler(mContext);
        apkName = findViewById(R.id.apk_name);
        DownloadFileInfo downloadFileInfo = ((InstallAppActivity) mContext).getDownloadFileInfo();
        if ((downloadFileInfo != null) && !TextUtils.isEmpty(downloadFileInfo.getFile_name())) {
            apkName.setText(downloadFileInfo.getFile_name());
        }

        install = findViewById(R.id.install);
        openApk = findViewById(R.id.openApk);
        display = findViewById(R.id.display);
        display.setEnabled(false);
        back = findViewById(R.id.back_install);
    }

    // https://yourservice.com/swagger/index.html#/code码相关接口/get_robot_api_v1_device_code_getInfo

    private class UpdateViewHandler extends Handler {
        private final WeakReference<Context> context;

        public UpdateViewHandler(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case UPDATE_BIND_CODE:
                    responseUpdateBindCodeView((String) msg.obj);

                    break;
                case SHOW_OPEN_APP:
                    showOpenApp(msg.obj);
                    deleteApkFile();

                    break;
                case SHOW_INSTALLING:
                    showInstallIng();
                    break;
                case INSTALLING_APK:
                    installApk(currentDownloadFileInfo);
                    break;

            }
        }
    }

    private void showInstallIng() {
        display.setVisibility(View.VISIBLE);
        install.setVisibility(View.GONE);
        openApk.setVisibility(View.GONE);
    }

    private void showOpenApp(Object obj) {
        display.setVisibility(View.GONE);
        install.setVisibility(View.GONE);
        openApk.setVisibility(View.VISIBLE);
    }

    private void responseUpdateBindCodeView(String code) {
        downloadCode.setText(code);
    }

    private void updateBindCodeView(String code) {
        Message message = new Message();
        message.what = UPDATE_BIND_CODE;
        message.obj = code;
        updateViewHandler.handleMessage(message);
    }

    private void installSuccess(String packageName) {
        currentPackageName = packageName;
        Message message = new Message();
        message.what = SHOW_OPEN_APP;
        message.obj = packageName;
        updateViewHandler.handleMessage(message);
    }

    private void showInstalling() {
        Message message = new Message();
        message.what = SHOW_INSTALLING;
        updateViewHandler.handleMessage(message);
    }

    public static void launchApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // 如果找不到对应的应用程序
            // 可以进行一些错误处理操作，或者显示提示信息给用户
        }
    }

    private void openUserAppView(String packageName) {
        changeMode(packageName);
        OpenAppCallback.getInstance().openApp(packageName);
        ((InstallAppActivity) mContext).finish();
        // responseBack();
    }

    public static final String CHANGE_SHOW_MODE = "changeShowModule";

    public void changeMode(String mode) {
        if (!TextUtils.isEmpty(mode)) {
            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
            ModeChangeCmdCallback.getInstance().changeRobotMode(CHANGE_SHOW_MODE, data);
        }
    }

    public static final String APP_DOWNLOAD_FILE_FOLDER = "/storage/emulated/0/Android/filedownloader/files/";

    private void installApk(DownloadFileInfo downloadFileInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(APP_DOWNLOAD_FILE_FOLDER, downloadFileInfo.getFile_name());
                createUserApkInstallSession(file);
            }
        }).start();

    }

    private PackageInstaller.Session session;

    private void createUserApkInstallSession(File file) {
        PackageInstaller packageInstaller = mContext.getPackageManager().getPackageInstaller();
        // 创建安装会话
        try {
            // 创建会话参数
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            // 获取会话ID
            int sessionId = packageInstaller.createSession(params);
            Log.e("letianpai_appstore", "1_createInstallSession: ");
            // 打开会话
            session = packageInstaller.openSession(sessionId);
            Log.e("letianpai_appstore", "2_createInstallSession: ");
            // 为会话注册回调
            // registerSessionCallback();
            Log.e("letianpai_appstore", "3_createInstallSession: ");

            // 将APK文件写入会话
            writeApkToSession(file, session);
            Log.e("letianpai_appstore", "4_createInstallSession: ");
            // 提交会话，开始安装
            session.commit(PendingIntent.getBroadcast(mContext, 0, new Intent(), 0).getIntentSender());
            Log.e("letianpai_appstore", "5_createInstallSession: ");
            // } catch (IOException | PackageManager.NameNotFoundException e) {
        } catch (Exception e) {

            Log.e("letianpai_appstore", "6_createInstallSession: ");
            Log.e("letianpai", "Failed to create install session", e);
            // 是否需要再次上报安装失败？
        }
    }

    private void writeApkToSession(File apkFile, PackageInstaller.Session session) throws IOException {
        try (
                InputStream inputStream = new FileInputStream(apkFile);
                OutputStream outputStream = session.openWrite("apk", 0, -1)) {

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            // 提交会话，完成写入
            session.fsync(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteApkFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (currentDownloadFileInfo != null && !TextUtils.isEmpty(currentDownloadFileInfo.getFile_name())) {
                    File file = new File(APP_DOWNLOAD_FILE_FOLDER, currentDownloadFileInfo.getFile_name());
                    boolean status = file.delete();
                    Log.e("letianpai", "status: " + status);
                }

            }
        }).start();

    }

}
