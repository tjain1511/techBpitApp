package com.indianapp.techbpit.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.gson.Gson;
import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.BottomSheetFragment.BottomSheetImageSource;
import com.indianapp.techbpit.BottomSheetImageSourceListener;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.SocketClient;
import com.indianapp.techbpit.adapters.ChatAdapter;
import com.indianapp.techbpit.databinding.ActivityMainBinding;
import com.indianapp.techbpit.model.GroupMessageRequest;
import com.indianapp.techbpit.model.MessageModel;
import com.indianapp.techbpit.model.MessageRequest;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener, BottomSheetImageSourceListener {
    private static final int PERMISSION_CODE = 1;
    private static final int ATTACHMENT_CHOICE_TAKE_PHOTO = 0x1002;
    private static final int REQUEST_FILE_DOCUMENT = 0x001;
    private static final long DELAY = 1000;
    private static final int ATTACHMENT_CHOICE_CHOOSE_IMAGE = 0x1001;
    private final ArrayList<MessageModel> messages = new ArrayList<>();
    private final Map config = new HashMap();
    private final Handler handler = new Handler();
    private long last_text_edit = 0;
    private Socket socket;
    private ActivityMainBinding binding;
    private ChatAdapter adapter;
    private final Emitter.Listener onNewMessage = new Emitter.Listener() {
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
    private UserModel receiverUser;
    private UserModel mySelf;
    private String groupId;
    private Uri filePath;
    private boolean isGrpChat;
    private final Emitter.Listener isTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        if (jsonObject.getBoolean("status")) {
                            if (isGrpChat) {
                                binding.tvIsTyping.setText(jsonObject.getString("senderName") + " is Typing..");
                            }
                            binding.tvIsTyping.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvIsTyping.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private final Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + DELAY - 500)) {
                String receiverId;
                if (isGrpChat) {
                    receiverId = groupId;
                } else {
                    receiverId = receiverUser._id;
                }
                socket.emit("isTyping", mySelf._id, receiverId, false, isGrpChat, mySelf.username, new Ack() {
                    @Override
                    public void call(Object... args) {
                    }
                });
            }
        }
    };
    private String messageEvent;
    private String typingEvent;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            RESTController.getInstance(this).clearPendingApis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SocketClient.setUserId(mySelf._id);
        socket = SocketClient.getSocket(this);
        socket.emit("join-room", groupId);

        if (!isGrpChat) {
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.sender = mySelf._id;
            messageRequest.receiver = receiverUser._id;
            try {
                RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_GET_MESSAGES, new BaseData<>(messageRequest), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GroupMessageRequest groupMessageRequest = new GroupMessageRequest();
            groupMessageRequest.groupId = groupId;
            try {
                RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_GET_GROUP_MESSAGES, new BaseData<>(groupMessageRequest), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mySelf = SharedPrefHelper.getUserModel(this);
        setupIsGrpChat();
        configCloudinary();
        setOnClickListener();
        initRecyclerView();
        binding.clHeader.setOnClickListener(v -> {
            if (isGrpChat) {
                Intent intent = new Intent(this, GroupDetailActivity.class);
                intent.putExtra("group_id", groupId);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ThirdPersonProfileActivity.class);
                intent.putExtra("third_person", receiverUser);
                startActivity(intent);
            }
        });
    }

    private void setupIsGrpChat() {
        isGrpChat = getIntent().getBooleanExtra("is_grp_chat", false);
        if (!isGrpChat) {
            receiverUser = (UserModel) getIntent().getExtras().getSerializable("current_user");
            messageEvent = receiverUser._id + "-msg";
            typingEvent = receiverUser._id + "-isTyping";
            binding.txt.setText(receiverUser.username);
            Picasso.get().load(receiverUser.imageUrl).into(binding.imgC);
        } else {
            groupId = getIntent().getStringExtra("group_id");
            messageEvent = groupId + "-msg";
            typingEvent = groupId + "-isTyping";
            binding.txt.setText(getIntent().getStringExtra("group_name"));
            Picasso.get().load(getIntent().getStringExtra("group_image")).into(binding.imgC);
        }
    }

    private void setOnClickListener() {
        binding.imageView6.setOnClickListener(v -> {
            try {
                if (!isGrpChat)
                    attemptSend(getMessageFromTextBox(), "direct-message", "");
                else
                    attemptGrpSend(getMessageFromTextBox(), "group-message", "");
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
            Toast.makeText(ChatActivity.this, "Uploading", Toast.LENGTH_SHORT).show();
            binding.progress.setVisibility(View.VISIBLE);
            uploadToCloudinary(filePath);
        });
    }

    private void initData(ArrayList<MessageModel> oldMessages) {
        filterMessageModel(oldMessages);
        binding.pbChatsLoading.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        listenEvents();
        setupMessageBoxTextWatcher();

    }

    private void filterMessageModel(ArrayList<MessageModel> oldMessages) {
        ArrayList<MessageModel> messageModels = new ArrayList<>();
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault());
        int index = 0;
        for (MessageModel messageModel : oldMessages) {
            MessageModel messageUIModel = new MessageModel();
            Date time = new Date(Long.valueOf(messageModel.timestamp));
            if (index == 0) {
                messageUIModel.date = sfd.format(time);
                messageModels.add(messageUIModel);
            } else {
                //check if date diff from above one and add message
                Date time1 = new Date(Long.valueOf(oldMessages.get(index - 1).timestamp));
                if (!sfd.format(time).equalsIgnoreCase(sfd.format(time1))) {
                    messageUIModel.date = sfd.format(time);
                    messageModels.add(messageUIModel);
                }
            }
            messageModels.add(messageModel);
            index++;
        }
        messages.clear();
        messages.addAll(messageModels);
    }

    private void listenEvents() {
        socket.off(messageEvent);
        socket.on(messageEvent, onNewMessage);
        socket.off(typingEvent);
        socket.on(typingEvent, isTyping);
    }

    private void setupMessageBoxTextWatcher() {
        binding.msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable s) {

                last_text_edit = System.currentTimeMillis();
                handler.postDelayed(input_finish_checker, DELAY);
                String receiverId;
                if (isGrpChat) {
                    receiverId = groupId;
                } else {
                    receiverId = receiverUser._id;
                }
                socket.emit("isTyping", mySelf._id, receiverId, true, isGrpChat, mySelf.username, new Ack() {
                    @Override
                    public void call(Object... args) {
                    }
                });

            }
        });
    }

    private String getMessageFromTextBox() {
        String message = binding.msg.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "message can't be empty", Toast.LENGTH_SHORT).show();
            return null;
        }
        return message;
    }

    private void attemptSend(String message, String type, String imageUrl) throws JSONException {
//"direct-message","group-message","direct-message-with-image","group-message-with-image","session-post"
        MessageModel messageModel = createMessageModel(message, imageUrl, receiverUser._id);
        messages.add(messageModel);
        adapter.notifyItemChanged(messages.size() - 1);
        binding.rvChat.smoothScrollToPosition(adapter.getItemCount());
        binding.msg.setText("");
        JSONObject jsonObject = createMessageJsonObject(messageModel, type);
        socket.emit("msg", jsonObject, receiverUser._id, new Ack() {
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

    private void attemptGrpSend(String message, String type, String imageUrl) throws JSONException {
        MessageModel messageModel = createMessageModel(message, imageUrl, groupId);
        messages.add(messageModel);
        adapter.notifyItemChanged(messages.size() - 1);
        binding.rvChat.smoothScrollToPosition(adapter.getItemCount());
        binding.msg.setText("");
        JSONObject jsonObject = createMessageJsonObject(messageModel, type);
        socket.emit("grp-msg", jsonObject, groupId, new Ack() {
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

    private MessageModel createMessageModel(String message, String imageUrl, String receiverId) {
        MessageModel messageModel = new MessageModel();
        messageModel.message = message;
        messageModel.sender = mySelf._id;
        messageModel.receiver = receiverId;
        messageModel.timestamp = String.valueOf(System.currentTimeMillis());
        messageModel.isSent = false;
        messageModel.imageUrl = imageUrl;
        return messageModel;
    }

    private JSONObject createMessageJsonObject(MessageModel message, String type) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("sender", message.sender);
        jsonObject.put("message", message.message);
        jsonObject.put("receiver", message.receiver);
        jsonObject.put("timestamp", message.timestamp);
        jsonObject.put("imageUrl", message.imageUrl);
        return jsonObject;
    }

//    private void dispatchFileSelectionIntent() {
//        if (isFinishing() || isDestroyed()) {
//            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("/");
//        String[] mimeTypes = {"image/jpeg", "image/png", "application/pdf", "application/msword"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        startActivityForResult(intent, REQUEST_FILE_DOCUMENT);
//    }

    private void initRecyclerView() {
        adapter = new ChatAdapter(this, messages, SharedPrefHelper.getUserModel(this)._id);
        binding.rvChat.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChat.setLayoutManager(linearLayoutManager);
    }

    private void configCloudinary() {
        config.put("cloud_name", "dmigta0dz");
        config.put("api_key", "975872631164455");
        config.put("api_secret", "osxUk6znAuPhxluWSL3UC2_Qf4o");
        try {
            MediaManager.init(ChatActivity.this, config);
        } catch (Exception e) {

        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (ChatActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchCameraIntent();
        } else {
            ActivityCompat.requestPermissions(
                    ChatActivity.this,
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

            } else {
                Toast.makeText(ChatActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
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

    private void dispatchGalleryIntent() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        startActivityForResult(chooserIntent, ATTACHMENT_CHOICE_CHOOSE_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
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
            } catch (Exception e) {
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
        Log.d("filepath", String.valueOf(filePath));
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(
                    filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bmp = BitmapFactory.decodeStream(imageStream);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {

            e.printStackTrace();
        }
        MediaManager.get().upload(byteArray).callback(new UploadCallback() {
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
                try {
                    if (isGrpChat) {
                        attemptGrpSend("", "group-message-with-image", resultData.get("url").toString());
                    } else {
                        attemptSend("", "direct-message-with-image", resultData.get("url").toString());
                    }
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                binding.imgSendBtn.setClickable(true);
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                binding.imgSendBtn.setClickable(true);
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }
        }).dispatch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        socket.off(typingEvent);
//        socket.off(messageEvent);
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        if (response.isSuccessful()) {
            initData((ArrayList<MessageModel>) response.body());
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> request, Throwable t) {

    }

    @Override
    public void onSourceClicked(BottomSheetImageSource.SourceType sourceType) {
        if (sourceType == BottomSheetImageSource.SourceType.CAMERA) {
            dispatchCameraIntent();
        } else if (sourceType == BottomSheetImageSource.SourceType.GALLERY) {
            dispatchGalleryIntent();
        }
    }
}