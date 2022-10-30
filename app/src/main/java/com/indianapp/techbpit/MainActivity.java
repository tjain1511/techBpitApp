package com.indianapp.techbpit;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianapp.techbpit.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private Socket socket;
    private String url = "https://techbpitbackend.onrender.com/";
    private ArrayList<String> messages = new ArrayList<>();
    private ActivityMainBinding binding;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        socket = SocketClient.getSocket(this);
        socket.on("msg", onNewMessage);
        setOnClickListener();
        initRecyclerView();
    }

    private void setOnClickListener() {
        binding.button.setOnClickListener(v -> {
            attemptSend();
        });
    }

    private void attemptSend() {
        String userId = String.valueOf(binding.receiverId.getText());
        String message = binding.messageBox.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "message can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "receiverId can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        messages.add(message);
        adapter.notifyDataSetChanged();
        binding.messageBox.setText("");
        socket.emit("msg", message, userId, new Ack() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                try {
                    Log.i("emitted", response.getString("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initRecyclerView() {
        adapter = new MessageAdapter(messages);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    messages.add(message);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off();
    }
}