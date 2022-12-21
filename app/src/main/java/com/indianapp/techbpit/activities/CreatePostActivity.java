package com.indianapp.techbpit.activities;

import static com.indianapp.techbpit.DateTimeUtils.getAmPmFromMillis;
import static com.indianapp.techbpit.DateTimeUtils.getDayFromMillis;
import static com.indianapp.techbpit.DateTimeUtils.getFormattedDateSimple;
import static com.indianapp.techbpit.DateTimeUtils.getFormattedTimeSimple;
import static com.indianapp.techbpit.DateTimeUtils.getHourFromMillis;
import static com.indianapp.techbpit.DateTimeUtils.getMinuteFromMillis;
import static com.indianapp.techbpit.DateTimeUtils.getMonthFromMillis;
import static com.indianapp.techbpit.DateTimeUtils.getYearFromMillis;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.BottomSheetFragment.BottomSheetImageSource;
import com.indianapp.techbpit.BottomSheetFragment.BottomSheetPostType;
import com.indianapp.techbpit.BottomSheetImageSourceListener;
import com.indianapp.techbpit.R;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ActivityCreatePostBinding;
import com.indianapp.techbpit.model.SocialPostRequest;
import com.indianapp.techbpit.model.SocialPostRequestItem;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener, BottomSheetImageSourceListener {
    private static final int PERMISSION_CODE = 1;
    private static final int ATTACHMENT_CHOICE_TAKE_PHOTO = 0x1002;
    private static final int REQUEST_FILE_DOCUMENT = 0x001;
    private static final int ATTACHMENT_CHOICE_CHOOSE_IMAGE = 0x1001;
    private static final int REQ_CODE_CHOOSE_IMAGE = 0x0301;
    private static final int REQ_CODE_OPEN_CAMERA = 0x0302;
    private Uri filePath;
    private String imageUrl;
    private ActivityCreatePostBinding binding;
    private BottomSheetPostType.PostType postType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Map config = new HashMap();
        config.put("cloud_name", "dmigta0dz");
        config.put("api_key", "975872631164455");
        config.put("api_secret", "osxUk6znAuPhxluWSL3UC2_Qf4o");
        try {
            MediaManager.init(this, config);
        } catch (Exception e) {

        }


        if (getIntent().getSerializableExtra("post_type") != null) {
            postType = (BottomSheetPostType.PostType) getIntent().getSerializableExtra("post_type");
        }
        if (postType != null) {
            setupPostType();
        }
        setupOnClickListener();
    }

    private void setupPostType() {
        binding.eventPost.rootLayout.setVisibility(View.GONE);
        binding.communityPost.rootLayout.setVisibility(View.GONE);
        binding.resourcePost.rootLayout.setVisibility(View.GONE);
        switch (postType) {
            case EVENT_POST:
                binding.eventPost.rootLayout.setVisibility(View.VISIBLE);
                binding.eventPost.tvEventTime.setText(getFormattedTimeSimple(System.currentTimeMillis()));
                binding.eventPost.tvEventDate.setText(getFormattedDateSimple(System.currentTimeMillis()));
                setupModeSpinner();
                break;
            case RESOURCE_POST:
                binding.resourcePost.rootLayout.setVisibility(View.VISIBLE);
                break;
            case COMMUNITY_POST:
                binding.communityPost.rootLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setupOnClickListener() {
        binding.ivPostImage.setOnClickListener(v -> launchBottomSheetImageSourceFragment());
        binding.eventPost.llEventDate.setOnClickListener(V -> setupDatePickerDialog());
        binding.eventPost.llEventTime.setOnClickListener(v -> setupTimePickerDialog());
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        binding.ivCancel.setOnClickListener(v -> onBackPressed());
        binding.btnPost.setOnClickListener(v -> sendPost());
    }

    private void sendPost() {
        SocialPostRequest socialPostRequest = new SocialPostRequest();
        SocialPostRequestItem socialPostRequestItem = new SocialPostRequestItem();
        socialPostRequestItem.author = SharedPrefHelper.getUserModel(this)._id;
        socialPostRequestItem.timestamp = String.valueOf(System.currentTimeMillis());
        socialPostRequestItem.postType = postType.label;
        socialPostRequestItem.groupId = "639f0f280b4f83601bba47b1";
        socialPostRequestItem.imageUrl = imageUrl;
        socialPostRequestItem.eventDate = (String) binding.eventPost.tvEventDate.getText();
        socialPostRequestItem.eventTime = (String) binding.eventPost.tvEventTime.getText();
        if (binding.eventPost.spnrSelectMode.getSelectedItem() != null)
            socialPostRequestItem.mode = (String) binding.eventPost.spnrSelectMode.getSelectedItem().toString().toLowerCase();
        socialPostRequestItem.organizer = String.valueOf(binding.eventPost.organizerName.getText());
        socialPostRequestItem.topic = String.valueOf(binding.title.getText());
        socialPostRequestItem.description = String.valueOf(binding.description.getText());
        socialPostRequestItem.resourceTime = String.valueOf(binding.resourcePost.tvReadTime.getText());
        socialPostRequestItem.venue = String.valueOf(binding.eventPost.meetingVenue.getText());
        if (postType.equals(BottomSheetPostType.PostType.EVENT_POST)) {
            socialPostRequestItem.link = String.valueOf(binding.eventPost.meetingLink.getText());
        } else if (postType.equals(BottomSheetPostType.PostType.RESOURCE_POST)) {
            socialPostRequestItem.link = String.valueOf(binding.resourcePost.tvResourceLink.getText());
        }

        socialPostRequest.post = socialPostRequestItem;


        BaseData<SocialPostRequest> baseData = new BaseData<>(socialPostRequest);
        try {
            RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_POST_POST, baseData, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupModeSpinner() {
        String[] modes = {"Offline", "Online"};
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                modes);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        binding.eventPost.spnrSelectMode.setAdapter(ad);
        binding.eventPost.spnrSelectMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    binding.eventPost.meetingLink.setVisibility(View.GONE);
                    binding.eventPost.meetingVenue.setVisibility(View.VISIBLE);
                } else {
                    binding.eventPost.meetingLink.setVisibility(View.VISIBLE);
                    binding.eventPost.meetingVenue.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getYearFromMillis(System.currentTimeMillis()));
        cal.set(Calendar.MONTH, getMonthFromMillis(System.currentTimeMillis()));
        cal.set(Calendar.DAY_OF_MONTH, getDayFromMillis(System.currentTimeMillis()));

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long date_ship_millis = calendar.getTimeInMillis();
                    binding.eventPost.tvEventDate.setText(getFormattedDateSimple(date_ship_millis));
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setMinDate(Calendar.getInstance());
        datePicker.setAccentColor(getColor(R.color.colorPrimary));
        datePicker.show(getSupportFragmentManager(), "DatePickerDialog");
    }

    private void setupTimePickerDialog() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, getHourFromMillis(System.currentTimeMillis()));
        cal.set(Calendar.MINUTE, getMinuteFromMillis(System.currentTimeMillis()));
        cal.set(Calendar.AM_PM, getAmPmFromMillis(System.currentTimeMillis()));

        TimePickerDialog timePicker = TimePickerDialog.newInstance(
                (view, hour, minute, am_pm) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.AM_PM, am_pm);
                    long date_ship_millis = calendar.getTimeInMillis();
                    binding.eventPost.tvEventTime.setText(getFormattedTimeSimple(date_ship_millis));
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        );
        timePicker.setThemeDark(false);
        timePicker.setAccentColor(getColor(R.color.colorPrimary));
        timePicker.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission
                (this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            dispatchCameraIntent();
        } else {
            ActivityCompat.requestPermissions(
                    this,
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
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_CANCELED)
            return;
        if (requestCode == ATTACHMENT_CHOICE_TAKE_PHOTO) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.ivPostImage.setImageBitmap(bitmap);
                binding.pbUploading.setVisibility(View.VISIBLE);
                uploadToCloudinary(filePath);
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

        if (requestCode == ATTACHMENT_CHOICE_CHOOSE_IMAGE) {
            try {
                ContentResolver cr = getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, intent.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                binding.ivPostImage.setImageBitmap(bitmap);
                binding.pbUploading.setVisibility(View.VISIBLE);
                uploadToCloudinary(intent.getData());
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
                imageUrl = resultData.get("url").toString();
                binding.pbUploading.setVisibility(View.GONE);

            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
//                Toast.makeText(this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
//                Toast.makeText(this, error.getDescription(), Toast.LENGTH_SHORT).show();
            }
        }).dispatch();
    }

    public void launchBottomSheetImageSourceFragment() {
        BottomSheetImageSource bottomSheetImageSourceFragment = new BottomSheetImageSource(this);
        bottomSheetImageSourceFragment.show(getSupportFragmentManager(), "BottomSheetImageSourceFragmentBase");
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> data, Response<?> response) {
        if (response.isSuccessful()) {
            Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }

    private boolean hasPermissions(int requestCode, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final List<String> missingPermissions = new ArrayList<>();
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(permission);
                }
            }
            if (missingPermissions.size() == 0) {
                return true;
            } else {
                requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), requestCode);
                return false;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onSourceClicked(BottomSheetImageSource.SourceType sourceType) {
        if (sourceType == BottomSheetImageSource.SourceType.GALLERY) {

            if (!hasPermissions(REQ_CODE_CHOOSE_IMAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }

            dispatchGalleryIntent();
        } else if (sourceType == BottomSheetImageSource.SourceType.CAMERA) {

            if (!hasPermissions(REQ_CODE_OPEN_CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                return;
            }

            dispatchCameraIntent();
        }
    }

    private void dispatchGalleryIntent() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        startActivityForResult(chooserIntent, ATTACHMENT_CHOICE_CHOOSE_IMAGE);
    }
}