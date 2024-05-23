package com.letianpai.robot.installer.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.letianpai.robot.installer.callback.ModeChangeCmdCallback;
import com.renhejia.robot.letianpaiservice.ILetianpaiService;

public class InstallerService extends Service {

    private ILetianpaiService iLetianpaiService;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = InstallerService.this;
        connectService();
        addModeChangeListeners();
    }

    //链接服务端
    private void connectService() {
        Intent intent = new Intent();
        intent.setPackage("com.renhejia.robot.letianpaiservice");
        intent.setAction("android.intent.action.LETIANPAI");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("letianpai","========== GeeUIDesktopService onServiceConnected() ==========");
            Log.d("letianpai", "InstallerService 完成AIDLService绑定服务");
            iLetianpaiService = ILetianpaiService.Stub.asInterface(service);
//            try {
//                iLetianpaiService.registerLCCallback(ltpLongConnectCallback);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("", "乐天派 无法绑定aidlserver的AIDLService服务");
        }
    };

    private void addModeChangeListeners() {
        ModeChangeCmdCallback.getInstance().setModeChangeCommandListener(new ModeChangeCmdCallback.ModeChangeCommandListener() {
            @Override
            public void onRobotModeChange(String command, String data) {
                if (iLetianpaiService!= null && !TextUtils.isEmpty(command) && !TextUtils.isEmpty(data)){
                    Log.e("letianpai","changeMode_data_command: "+ command);
                    Log.e("letianpai","changeMode_data_data: "+ data);
                    try {
                        iLetianpaiService.setLongConnectCommand(command,data);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
