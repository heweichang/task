package com.example.tools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by 何伟昌 on 2019/5/13.
 */
public class RemindService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void open(Context context){
        Intent intent = new Intent(context,RemindService.class);
        context.startService(intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BindServer bindServer = new BindServer();
        bindServer.execute("");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RemindOther.getWebSocketClient().close();

    }

}
