package com.example.tools;

import android.os.AsyncTask;
import android.util.Log;

import org.java_websocket.enums.ReadyState;

import java.net.URI;

/**
 * Created by 何伟昌 on 2019/5/13.
 */
public class BindServer extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            String WS_URL = "ws://10.0.2.2:8080/xmtest_server/RemindOthers/";
            String userpri = "1";
            URI uri = new URI(WS_URL + userpri);
            RemindOther client = RemindOther.getWebSocketClient(uri);
            client.connect();
            while (!client.getReadyState().equals(ReadyState.OPEN)){}
            Log.i("BindServer", "连接服务器成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}