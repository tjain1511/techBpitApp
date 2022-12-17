package com.indianapp.techbpit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketClient {
    private static Socket socket;
    private static String url = "https://techbpit-tjhkw.run-ap-south1.goorm.io/";
    private static String myId;

    private SocketClient() {
    }

    public static Socket getSocket(Context ctx) {

        if (socket == null || !socket.isActive()) {
            if (socket != null) {
                Log.i("isActive", String.valueOf(socket.isActive()));
            }
            initializeSocket(ctx);
        }
        return socket;
    }

    public static void setUserId(String userId) {
        myId = userId;
    }

    private static void initializeSocket(Context ctx) {
        if (socket == null || !socket.isActive()) {
            try {
                IO.Options mOptions = new IO.Options();
                mOptions.query = "userId=" + myId;
                socket = IO.socket(url, mOptions);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            socket.connect();
            Log.i("connected", String.valueOf(socket.connected()));
        }
    }
}
