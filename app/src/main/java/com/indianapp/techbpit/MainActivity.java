package com.indianapp.techbpit;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.gson.Gson;
import com.indianapp.techbpit.databinding.ActivityMainBinding;
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

public class MainActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener{
    private Socket socket;
    private ArrayList<MessageModel> messages = new ArrayList<>();
    private ActivityMainBinding binding;
    private MessageAdapter adapter;
    private UserModel currentUser;
    private SharedPreferences mPrefs;
    Map config = new HashMap();


    private static final int PERMISSION_CODE = 1;
    private static final int ATTACHMENT_CHOICE_TAKE_PHOTO = 0x1002;
    private static final int REQUEST_FILE_DOCUMENT = 0x001;
    private Uri filePath;

    @Override
    protected void onResume() {
        super.onResume();
        SocketClient.setUserId(mPrefs.getString("my_email", ""));
        socket = SocketClient.getSocket(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mPrefs = this.getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
        getWindow().setStatusBarColor(Color.parseColor("#4169ef"));
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        currentUser = (UserModel) getIntent().getBundleExtra("bundle").getSerializable("current_user");
        binding.txt.setText(currentUser.username);
        Picasso.get().load(currentUser.imageUrl).into(binding.imgC);
        SocketClient.setUserId(mPrefs.getString("my_email", ""));
        socket = SocketClient.getSocket(this);
        Log.i("current_user", currentUser.email);

        socket.on("disconnect", onDisconnect);
        configCloudinary();
        setOnClickListener();
        initRecyclerView();
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.senderId = mPrefs.getString("my_email", "");
        messageRequest.receiverId = currentUser.email;
        try {
            RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_GET_MESSAGES, new BaseData<>(messageRequest), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnClickListener() {
        binding.imageView6.setOnClickListener(v -> {
            try {
                attemptSend();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        binding.imageView4.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.imageButton.setOnClickListener(v -> {
            requestPermission();
        });
        binding.imgSendBtn.setOnClickListener(v -> {
            binding.imgSendBtn.setClickable(false);
            Toast.makeText(MainActivity.this, "Uploading", Toast.LENGTH_SHORT).show();
            binding.progress.setVisibility(View.VISIBLE);
            uploadToCloudinary(filePath);
        });
    }

    private void attemptSend() throws JSONException {
        String userId = currentUser.email;
        String message = binding.msg.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "message can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "receiverId can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        MessageModel messageModel = new MessageModel();
        messageModel.message = message;
        messageModel.sender = mPrefs.getString("my_email", "");
        messageModel.receiver = userId;
        messageModel.timestamp = String.valueOf(System.currentTimeMillis());
        messageModel.isSent = false;
        messages.add(messageModel);
        adapter.notifyItemChanged(messages.size() - 1);
        binding.rvChat.smoothScrollToPosition(adapter.getItemCount());
        binding.msg.setText("");
        Log.i("sender", messageModel.sender);
        Log.i("receiver", messageModel.receiver);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sender", messageModel.sender);
        jsonObject.put("message", messageModel.message);
        jsonObject.put("receiver", messageModel.receiver);
        jsonObject.put("timestamp", messageModel.timestamp);
        socket.emit("msg", jsonObject, userId, new Ack() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                try {
                    Log.i("emitted", response.getString("status"));
                    messageModel.isSent = true;
                    binding.rvChat.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initRecyclerView() {
        adapter = new MessageAdapter(messages, mPrefs.getString("my_email", ""));
        binding.rvChat.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChat.setLayoutManager(linearLayoutManager);
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.i("messafge", "sdkfh");
        }
    };
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Gson gson = new Gson();
                    MessageModel messageModel = gson.fromJson(String.valueOf(jsonObject), MessageModel.class);

                    messages.add(messageModel);
                    adapter.notifyItemChanged(messages.size() - 1);
                    binding.rvChat.smoothScrollToPosition(adapter.getItemCount());

                }
            });
        }
    };

    private void configCloudinary() {
        config.put("cloud_name", "dmigta0dz");
        config.put("api_key", "975872631164455");
        config.put("api_secret", "osxUk6znAuPhxluWSL3UC2_Qf4o");
        try {
            MediaManager.init(MainActivity.this, config);
        } catch (Exception e) {

        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchCameraIntent();
        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchCameraIntent();
            } else {
                Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchCameraIntent() {
        if (isFinishing() || isDestroyed()) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String fileName = "techBpit" + timeStamp;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        filePath = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
        startActivityForResult(intent, ATTACHMENT_CHOICE_TAKE_PHOTO);
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_CANCELED)
            return;
        if (requestCode == ATTACHMENT_CHOICE_TAKE_PHOTO) {
            binding.chatActivity.setVisibility(View.GONE);
            binding.imgUpload.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.selectedImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_FILE_DOCUMENT) {
            try {
                if (resultCode == RESULT_OK && null != intent) {
                    filePath = intent.getData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadToCloudinary(Uri filePath) {
        Log.d("A", "sign up uploadToCloudinary- ");
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                binding.imgSendBtn.setClickable(true);
                binding.progress.setVisibility(View.GONE);
                binding.chatActivity.setVisibility(View.VISIBLE);
                binding.imgUpload.setVisibility(View.GONE);
                MessageModel messageModel = new MessageModel();
                messageModel.message = resultData.get("url").toString();
                messageModel.sender = mPrefs.getString("my_email", "viratkohli@gmail.com");
                messageModel.receiver = currentUser.email;
                messageModel.timestamp = String.valueOf(System.currentTimeMillis());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("sender", messageModel.sender);
                    jsonObject.put("message", messageModel.message);
                    jsonObject.put("receiver", messageModel.receiver);
                    jsonObject.put("timestamp", messageModel.timestamp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("msg", jsonObject, currentUser.email, new Ack() {
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
                messages.add(messageModel);
                adapter.notifyItemChanged(messages.size() - 1);
                binding.rvChat.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                binding.imgSendBtn.setClickable(true);
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                binding.imgSendBtn.setClickable(true);
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }
        }).dispatch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, Call<?> request, Response<?> response) {

        ArrayList<MessageModel> oldMessages = (ArrayList<MessageModel>) response.body();
        Log.i("response",String.valueOf(messages.size()));
        messages.addAll(oldMessages);
        adapter.notifyDataSetChanged();
        String event_Name = currentUser.email + "-msg";
        socket.on(event_Name, onNewMessage);
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, Call<?> request, Throwable t) {

    }
}