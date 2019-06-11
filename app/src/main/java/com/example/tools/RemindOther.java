package com.example.tools;

import android.os.Message;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by 何伟昌 on 2019/5/13.
 */
public class RemindOther extends WebSocketClient {

    private static volatile RemindOther socketClient = null;

    public RemindOther(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    public static RemindOther getWebSocketClient(URI serverUri) {
        if (socketClient == null) {
            synchronized (RemindOther.class) {
                if (socketClient == null) {
                    socketClient = new RemindOther(serverUri);
                }
            }
        }
        return socketClient;
    }

    public static RemindOther getWebSocketClient() {
        synchronized (RemindOther.class) {
            return socketClient;
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("RemindOther", "onOpen()");
    }

    @Override
    public void onMessage(String message) {

        Message mes = new Message();
        Log.d("RemindOther", "message: " + mes);
        mes.what = 1;
        CP.NotiHanler.sendMessage(mes);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("RemindOther", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.e("RemindOther", "onError()");
        ex.printStackTrace();
    }

}
