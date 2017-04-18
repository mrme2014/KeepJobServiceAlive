package service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.mrs.keepjobservicealive.ProcessConnection;


/**
 * Created by mrs on 2017/4/18.
 */

public class MessageService extends Service {

    private static final String TAG = "MessageService";

    private MessageServiceConnection mServiceConnection;
    private MessageBind mMessageBind;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.e(TAG, "等待接收消息");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        if (mServiceConnection == null) {
            mServiceConnection = new MessageServiceConnection();
        }

        if (mMessageBind == null) {
            mMessageBind = new MessageBind();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MessageService.this.bindService(new Intent(MessageService.this, GuardService.class),
                mServiceConnection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: message_service");
        if (mServiceConnection != null) unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessageBind;
    }

    private class MessageBind extends ProcessConnection.Stub {

    }

    private class MessageServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            Toast.makeText(MessageService.this, "建立连接", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接
            Toast.makeText(MessageService.this, "断开连接", Toast.LENGTH_LONG).show();
            Intent guardIntent = new Intent(MessageService.this, GuardService.class);
            // 发现断开我就从新启动和绑定
            startService(guardIntent);
            MessageService.this.bindService(guardIntent,
                    mServiceConnection, Context.BIND_IMPORTANT);
        }
    }
}
