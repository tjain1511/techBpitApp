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
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ActivitySetupProfileBinding;
import com.indianapp.techbpit.model.SetupProfileRequest;
import com.indianapp.techbpit.model.SetupProfileRequestItem;
import com.indianapp.techbpit.model.SocialPlatform;

import java.util.ArrayList;

import retrofit2.Response;

public class SetupProfileActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    private static final String[] COUNTRIES = new String[]{
            "Android", "Android Development", "Node", "SQL", "Django", "React", "MongoDb"
    };
    private ActivitySetupProfileBinding binding;
    private int yearOfStudy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
    }

    private SetupProfileRequestItem createProfileData() {
        SetupProfileRequestItem setupProfileRequestItem = new SetupProfileRequestItem();
//        setupProfileRequestItem.id = SharedPrefHelper.getUserModel(this)._id;
        setupProfileRequestItem.username = String.valueOf(binding.edtFullName.getText());
        setupProfileRequestItem.about = String.valueOf(binding.edtAbout.getText());
        setupProfileRequestItem.state = String.valueOf(binding.edtState.getText());
        setupProfileRequestItem.city = String.valueOf(binding.edtCity.getText());
        setupProfileRequestItem.skills = createSkillsList();
        if (yearOfStudy != 0) {
            setupProfileRequestItem.yearOfStudy = binding.spnrSelectYear.getSelectedItem().toString();
        }
        setupProfileRequestItem.socialLinks = createSocialPlatform();
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
            if (!TextUtils.isEmpty(skills[i]))
                skillsList.add(skills[i].trim());
        }
        return skillsList;
    }

    private void setupSpinner() {
        String[] modes = {"- Select year of study -", "1st Year", "2nd Year", "3rd Year", "4th Year"};
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
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }
}