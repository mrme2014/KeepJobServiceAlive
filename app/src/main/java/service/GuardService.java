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

public class GuardService extends Service {
    private ServiceConnection mServiceConnection;
    private MessageBind mMessageBind;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mServiceConnection == null) {
            mServiceConnection = new MessageServiceConnection();
        }

        if (mMessageBind == null) {
            mMessageBind = new MessageBind();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GuardService.this.bindService(new Intent(GuardService.this, MessageService.class),
                mServiceConnection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessageBind;
    }

    @Override
    public void onDestroy() {
        Log.e("guard_service", "onDestroy: guard_service");
        if (mServiceConnection != null) unbindService(mServiceConnection);
        super.onDestroy();
    }

    private class MessageBind extends ProcessConnection.Stub {

    }

    private class MessageServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            Toast.makeText(GuardService.this, "建立连接", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接
            Toast.makeText(GuardService.this, "断开连接", Toast.LENGTH_LONG).show();
            Intent guardIntent = new Intent(GuardService.this, MessageService.class);
            // 发现断开我就从新启动和绑定
            startService(guardIntent);
            GuardService.this.bindService(guardIntent,
                    mServiceConnection, Context.BIND_IMPORTANT);
        }
    }
}
