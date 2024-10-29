package com.letianpai.robot.installer.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import com.letianpai.robot.installer.callback.ModeChangeCmdCallback
import com.letianpai.robot.installer.callback.ModeChangeCmdCallback.ModeChangeCommandListener
import com.renhejia.robot.letianpaiservice.ILetianpaiService

class InstallerService : Service() {
    private var iLetianpaiService: ILetianpaiService? = null
    private var mContext: Context? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this@InstallerService
        connectService()
        addModeChangeListeners()
    }

    //链接服务端
    private fun connectService() {
        val intent = Intent()
        intent.setPackage("com.renhejia.robot.letianpaiservice")
        intent.setAction("android.intent.action.LETIANPAI")
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e("letianpai", "========== GeeUIDesktopService onServiceConnected() ==========")
            Log.d("letianpai", "InstallerService 完成AIDLService绑定服务")
            iLetianpaiService = ILetianpaiService.Stub.asInterface(service)
            //            try {
//                iLetianpaiService.registerLCCallback(ltpLongConnectCallback);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d("", "乐天派 无法绑定aidlserver的AIDLService服务")
        }
    }

    private fun addModeChangeListeners() {
        ModeChangeCmdCallback.instance.setModeChangeCommandListener { command, data ->
            if (iLetianpaiService != null && !TextUtils.isEmpty(command) && !TextUtils.isEmpty(
                    data
                )
            ) {
                Log.e("letianpai", "changeMode_data_command: $command")
                Log.e("letianpai", "changeMode_data_data: $data")
                try {
                    iLetianpaiService!!.setLongConnectCommand(command, data)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
