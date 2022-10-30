package com.indianapp.techbpit;

import android.content.Context;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketClient {
    private static Socket socket;
    private static String url = "https://techbpitbackend.onrender.com/";
    private static String myId;

    private SocketClient() {
    }

    public static Socket getSocket(Context ctx) {
        if (socket == null) {
            initializeSocket(ctx);
        }
        return socket;
    }

    public static void setUserId(String userId) {
        myId = userId;
    }

    private static void initializeSocket(Context ctx) {
        if (socket == null) {
            try {
                IO.Options mOptions = new IO.Options();
                mOptions.query = "userId=" + myId;
                socket = IO.socket(url, mOptions);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
//            socket.on("msg", onNewMessage);
            socket.connect();
            Toast.makeText(ctx, "Socket connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx, "Socket already initialized", Toast.LENGTH_SHORT).show();
        }
    }
}
