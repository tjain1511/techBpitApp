package com.indianapp.techbpit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.indianapp.techbpit.databinding.ActivityGroupBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class GroupActivity extends AppCompatActivity {
    private ActivityGroupBinding binding;
    private MessageAdapter adapter;
    private ArrayList<String> messages = new ArrayList<>();
    private Socket socket;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        socket = SocketClient.getSocket(this);
        groupId = getIntent().getExtras().getString("GroupId");
        if (TextUtils.isEmpty(groupId)) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            Log.i("ahevbdf", groupId);
            joinRoom();
        }
        socket.on(groupId + "-msg", onNewMessage);
        initRecyclerView();
        setOnClickListener();
    }

    private void initRecyclerView() {
        adapter = new MessageAdapter(messages);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
    }

    private void joinRoom() {
        socket.emit("join-room", groupId);
    }

    private void setOnClickListener() {
        binding.sendBtn.setOnClickListener(v -> {
            attemptSend();
        });
    }

    private void attemptSend() {
        String message = binding.msgBox.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "message can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        messages.add(message);
        adapter.notifyDataSetChanged();
        binding.msgBox.setText("");
        socket.emit("grp-msg", message, groupId, new Ack() {
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}