package com.indianapp.techbpit.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.gson.Gson;
import com.indianapp.techbpit.BaseData;
import com.indianapp.techbpit.ImageUtils;
import com.indianapp.techbpit.RESTController;
import com.indianapp.techbpit.SocketClient;
import com.indianapp.techbpit.adapters.MessageAdapter;
import com.indianapp.techbpit.databinding.ActivityGroupBinding;
import com.indianapp.techbpit.databinding.ActivityMainBinding;
import com.indianapp.techbpit.model.MessageModel;
import com.indianapp.techbpit.model.MessageRequest;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity  {
//        private ActivityGroupBinding binding;
//    private MessageAdapter adapter;
//    private ArrayList<String> messages = new ArrayList<>();
//    private Socket socket;
//    private String groupId;
//    Map config = new HashMap();
//
//
//    private static final int PERMISSION_CODE = 1;
//    private static final int ATTACHMENT_CHOICE_TAKE_PHOTO = 0x1002;
//    private static final int REQUEST_FILE_DOCUMENT = 0x001;
//    private Uri filePath;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityGroupBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        socket = SocketClient.getSocket(this);
//        groupId = getIntent().getExtras().getString("GroupId");
//        if (TextUtils.isEmpty(groupId)) {
//            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
//            onBackPressed();
//        } else {
//            Log.i("ahevbdf", groupId);
//            joinRoom();
//        }
//        socket.on(groupId + "-msg", onNewMessage);
//        initRecyclerView();
//        setOnClickListener();
//        configCloudinary();
//    }
//
//    private void initRecyclerView() {
//        adapter = new MessageAdapter(messages);
//        binding.rv.setLayoutManager(new LinearLayoutManager(this));
//        binding.rv.setAdapter(adapter);
//    }
//
//    private void joinRoom() {
//        socket.emit("join-room", groupId);
//    }
//
//    private void setOnClickListener() {
//        binding.sendBtn.setOnClickListener(v -> {
//            attemptSend();
//        });
//
//        binding.uploadBtn.setOnClickListener(v -> {
//            requestPermission();
//        });
//
//        binding.imgSendBtn.setOnClickListener(v -> {
//            uploadToCloudinary(filePath);
//        });
//    }
//
//    private void attemptSend() {
//        String message = binding.msgBox.getText().toString().trim();
//        if (TextUtils.isEmpty(message)) {
//            Toast.makeText(this, "message can't be empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        messages.add(message);
//        adapter.notifyDataSetChanged();
//        binding.msgBox.setText("");
//        socket.emit("grp-msg", message, groupId, new Ack() {
//            @Override
//            public void call(Object... args) {
//                JSONObject response = (JSONObject) args[0];
//                try {
//                    Log.i("emitted", response.getString("status"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private Emitter.Listener onNewMessage = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String message = (String) args[0];
//                    messages.add(message);
//                    adapter.notifyDataSetChanged();
//                }
//            });
//        }
//    };
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//
//    private void configCloudinary() {
//        config.put("cloud_name", "dmigta0dz");
//        config.put("api_key", "975872631164455");
//        config.put("api_secret", "osxUk6znAuPhxluWSL3UC2_Qf4o");
//        MediaManager.init(GroupActivity.this, config);
//    }
//
//    private void requestPermission() {
//        if (ContextCompat.checkSelfPermission
//                (GroupActivity.this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            dispatchCameraIntent();
//        } else {
//            ActivityCompat.requestPermissions(
//                    GroupActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    PERMISSION_CODE
//            );
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                dispatchCameraIntent();
//            } else {
//                Toast.makeText(GroupActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void dispatchCameraIntent() {
//        if (isFinishing() || isDestroyed()) {
//            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
//        String fileName = "techBpit" + timeStamp;
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, fileName);
//        filePath = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
//        startActivityForResult(intent, ATTACHMENT_CHOICE_TAKE_PHOTO);
//    }
//
//    private void dispatchFileSelectionIntent() {
//        if (isFinishing() || isDestroyed()) {
//            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("/");
//        String[] mimeTypes = {"image/jpeg", "image/png", "application/pdf", "application/msword"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        startActivityForResult(intent, REQUEST_FILE_DOCUMENT);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        if (resultCode == RESULT_CANCELED)
//            return;
//        if (requestCode == ATTACHMENT_CHOICE_TAKE_PHOTO) {
//            binding.grpChat.setVisibility(View.GONE);
//            binding.imgUpload.setVisibility(View.VISIBLE);
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                binding.selectedImg.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (requestCode == REQUEST_FILE_DOCUMENT) {
//            try {
//                if (resultCode == RESULT_OK && null != intent) {
//                    filePath = intent.getData();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void uploadToCloudinary(Uri filePath) {
//        Log.d("A", "sign up uploadToCloudinary- ");
//        MediaManager.get().upload(filePath).callback(new UploadCallback() {
//            @Override
//            public void onStart(String requestId) {
//                Toast.makeText(GroupActivity.this, "Uploading", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onProgress(String requestId, long bytes, long totalBytes) {
//                binding.progress.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onSuccess(String requestId, Map resultData) {
//                binding.progress.setVisibility(View.GONE);
//                binding.grpChat.setVisibility(View.VISIBLE);
//                binding.imgUpload.setVisibility(View.GONE);
//                socket.emit("grp-msg", resultData.get("url").toString(), groupId, new Ack() {
//                    @Override
//                    public void call(Object... args) {
//                        JSONObject response = (JSONObject) args[0];
//                        try {
//                            Log.i("emitted", response.getString("status"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                messages.add(resultData.get("url").toString());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(String requestId, ErrorInfo error) {
//                Toast.makeText(GroupActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onReschedule(String requestId, ErrorInfo error) {
//                Toast.makeText(GroupActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
//            }
//        }).dispatch();
//    }

}