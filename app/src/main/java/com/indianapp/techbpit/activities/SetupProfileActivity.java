package com.indianapp.techbpit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.utils.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ActivitySetupProfileBinding;
import com.indianapp.techbpit.model.SetupProfileRequest;
import com.indianapp.techbpit.model.SetupProfileRequestItem;
import com.indianapp.techbpit.model.SocialPlatform;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Response;

public class SetupProfileActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    private static final String[] COUNTRIES = new String[]{
            "Android", "Android Development", "Node", "SQL", "Django", "React", "MongoDb"
    };
    UserModel userModel;
    String[] modes = {"- Select year of study -", "1st Year", "2nd Year", "3rd Year", "4th Year"};
    private ActivitySetupProfileBinding binding;
    private int yearOfStudy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, COUNTRIES);
        binding.multiViewSkills.setAdapter(adapter);
        binding.multiViewSkills.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        binding.cvAddSocial.setOnClickListener(v -> {
            binding.cvAddSocial.setVisibility(View.GONE);
            binding.addLinkCard.getRoot().setVisibility(View.VISIBLE);
        });
        binding.addLinkCard.getRoot().setOnClickListener(v -> {
            binding.cvAddSocial.setVisibility(View.VISIBLE);
            binding.addLinkCard.getRoot().setVisibility(View.GONE);
        });
        setupSpinner();
        binding.btnSetupProfile.setOnClickListener(v -> {
            SetupProfileRequest setupProfileRequest = new SetupProfileRequest();
            setupProfileRequest.id = SharedPrefHelper.getUserModel(this)._id;
            setupProfileRequest.item = createProfileData();
            try {
                RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_PATCH_UPDATE_USER_PROFILE, new BaseData<>(setupProfileRequest), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            binding.btnSetupProfile.setEnabled(false);
            binding.btnSetupProfile.setText("Updating...");

        });

        binding.imageView8.setOnClickListener(v -> {
            onBackPressed();
        });

        if (userModel != null) {
            prefillData();
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras().containsKey("profile_data")) {
            userModel = (UserModel) getIntent().getExtras().getSerializable("profile_data");
        }
    }

    private void prefillData() {
        if (!TextUtils.isEmpty(userModel.imageUrl)) {
            Picasso.get().load(userModel.imageUrl).into(binding.ivProfilePicture);
        }
        if (!TextUtils.isEmpty(userModel.username))
            binding.edtFullName.setText(userModel.username);
        if (!TextUtils.isEmpty(userModel.state))
            binding.edtState.setText(userModel.state);
        if (!TextUtils.isEmpty(userModel.city))
            binding.edtCity.setText(userModel.city);
        if (!TextUtils.isEmpty(userModel.about))
            binding.edtAbout.setText(userModel.about);
        binding.multiViewSkills.setText(getSkills());
        if (!TextUtils.isEmpty(userModel.yearOfStudy))
            binding.spnrSelectYear.setSelection(getYearOfStudy());
    }

    private String getSkills() {
        String skills = "";
        for (String skill : userModel.skills) {
            skills += skill + ", ";
        }
        return skills;
    }

    private int getYearOfStudy() {
        int idx = 0;
        for (String year : modes) {
            if (year.equalsIgnoreCase(userModel.yearOfStudy)) {
                return idx;
            }
            idx++;
        }
        return idx;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            RESTController.getInstance(this).clearPendingApis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private SetupProfileRequestItem createProfileData() {
        SetupProfileRequestItem setupProfileRequestItem = new SetupProfileRequestItem();
        if (!TextUtils.isEmpty(binding.edtFullName.getText())) {
            setupProfileRequestItem.username = String.valueOf(binding.edtFullName.getText());
        }
        if (!TextUtils.isEmpty(binding.edtAbout.getText())) {
            setupProfileRequestItem.about = String.valueOf(binding.edtAbout.getText());
        }
        if (!TextUtils.isEmpty(binding.edtState.getText())) {
            setupProfileRequestItem.state = String.valueOf(binding.edtState.getText());
        }
        if (!TextUtils.isEmpty(binding.edtCity.getText())) {
            setupProfileRequestItem.city = String.valueOf(binding.edtCity.getText());
        }
        if (!createSkillsList().isEmpty()) {
            setupProfileRequestItem.skills = createSkillsList();
        }
        if (yearOfStudy != 0) {
            setupProfileRequestItem.yearOfStudy = binding.spnrSelectYear.getSelectedItem().toString();
        }
        if (!createSocialPlatform().isEmpty()) {
            setupProfileRequestItem.socialLinks = createSocialPlatform();
        }
        setupProfileRequestItem.image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNzU1Ajo-RClF42aO2dvjvDRFcjPtvNuPUwTU92WutY5sB_8xV4vLcdoqEjn2vtd-_-MA&usqp=CAU";
        return setupProfileRequestItem;
    }

    private ArrayList<SocialPlatform> createSocialPlatform() {
        ArrayList<SocialPlatform> socialPlatformArrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(binding.addLinkCard.edtLinkedinLink.getText()))
            socialPlatformArrayList.add(createSocialPlatform("https://cdn-icons-png.flaticon.com/512/174/174857.png",
                    String.valueOf(binding.addLinkCard.edtLinkedinLink.getText())));

        if (!TextUtils.isEmpty(binding.addLinkCard.edtGithubLink.getText()))
            socialPlatformArrayList.add(createSocialPlatform("https://play-lh.googleusercontent.com/PCpXdqvUWfCW1mXhH1Y_98yBpgsWxuTSTofy3NGMo9yBTATDyzVkqU580bfSln50bFU",
                    String.valueOf(binding.addLinkCard.edtGithubLink.getText())));

        if (!TextUtils.isEmpty(binding.addLinkCard.edtLeetcodeLink.getText()))
            socialPlatformArrayList.add(createSocialPlatform("https://leetcode.com/static/images/LeetCode_logo_rvs.png",
                    String.valueOf(binding.addLinkCard.edtLeetcodeLink.getText())));

        if (!TextUtils.isEmpty(binding.addLinkCard.edtCodeforcesLink.getText()))
            socialPlatformArrayList.add(createSocialPlatform("https://play-lh.googleusercontent.com/EkSlLWf2-04k5Y5F_MDLqoXPdo0TyZX3zKdCfsEUDqVB7INUypTOd6AVmkE_X7ej3JuR",
                    String.valueOf(binding.addLinkCard.edtCodeforcesLink.getText())));

        if (!TextUtils.isEmpty(binding.addLinkCard.edtCodechefLink.getText()))
            socialPlatformArrayList.add(createSocialPlatform("https://pbs.twimg.com/profile_images/1477930785537605633/ROTVNVz7_400x400.jpg",
                    String.valueOf(binding.addLinkCard.edtCodechefLink.getText())));

        if (!TextUtils.isEmpty(binding.addLinkCard.edtPersonalWebsite.getText()))
            socialPlatformArrayList.add(createSocialPlatform("https://www.freepnglogos.com/uploads/logo-website-png/logo-website-file-globe-icon-svg-wikimedia-commons-21.png",
                    String.valueOf(binding.addLinkCard.edtPersonalWebsite.getText())));
        return socialPlatformArrayList;
    }

    private SocialPlatform createSocialPlatform(String url, String link) {
        SocialPlatform socialPlatform = new SocialPlatform();
        socialPlatform.platformImg = url;
        socialPlatform.platformLink = link;
        return socialPlatform;
    }

    private ArrayList<String> createSkillsList() {
        String skillsString = String.valueOf(binding.multiViewSkills.getText());
        String skills[] = skillsString.split(",");
        ArrayList<String> skillsList = new ArrayList<>();
        for (int i = 0; i < skills.length; i++) {
            if (!TextUtils.isEmpty(skills[i].trim()))
                skillsList.add(skills[i].trim());
        }
        return skillsList;
    }

    private void setupSpinner() {
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                modes);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spnrSelectYear.setAdapter(ad);
        binding.spnrSelectYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        yearOfStudy = 0;
                        break;
                    case 1:
                        yearOfStudy = 1;
                        break;
                    case 2:
                        yearOfStudy = 2;
                        break;
                    case 3:
                        yearOfStudy = 3;
                        break;
                    case 4:
                        yearOfStudy = 4;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> data, Response<?> response) {
        if (response.isSuccessful()) {
            binding.btnSetupProfile.setText("Updated Successfully");
            onBackPressed();
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }
}