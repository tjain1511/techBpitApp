package com.indianapp.techbpit.activities;

import static com.indianapp.techbpit.DateTimeUtils.getDayFromMillis;
import static com.indianapp.techbpit.DateTimeUtils.getFormattedDate;
import static com.indianapp.techbpit.DateTimeUtils.getMonthFromMillis;
import static com.indianapp.techbpit.DateTimeUtils.getYearFromMillis;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.BottomSheetFragment.BottomSheetAddTeamMembers;
import com.indianapp.techbpit.MemberAddedListener;
import com.indianapp.techbpit.MemberRemovedClickListener;
import com.indianapp.techbpit.R;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.adapters.TeamMembersAdapter;
import com.indianapp.techbpit.databinding.ActivityAddProjectBinding;
import com.indianapp.techbpit.model.ProjectRequest;
import com.indianapp.techbpit.model.ProjectResponse;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import retrofit2.Response;

public class AddProjectActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener, MemberAddedListener, MemberRemovedClickListener {
    private ActivityAddProjectBinding binding;
    private ArrayList<UserModel> teamMembers = new ArrayList<>();
    private TeamMembersAdapter adapter;
    private SortedSet<UserModel> teamMembersSet = new TreeSet<UserModel>(new Comparator<UserModel>() {
        @Override
        public int compare(UserModel userModel1, UserModel userModel2) {
            return userModel1.username.toLowerCase().compareTo(userModel2.username.toLowerCase());
        }
    });

    private ProjectResponse project;
    private boolean isEditFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentData();
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        if (!isEditFlow) {
            teamMembers.add(SharedPrefHelper.getUserModel(this));
            setupTeamMembersUI();
            binding.tvStartDate.setOnClickListener(v -> setupDatePickerDialog(binding.tvStartDate));
            binding.tvEndDate.setOnClickListener(v -> setupDatePickerDialog(binding.tvEndDate));
            binding.btnPost.setOnClickListener(v -> postProject());
            binding.ivAddMember.setOnClickListener(v -> {
                BottomSheetAddTeamMembers bottomSheetAddTeamMembers = new BottomSheetAddTeamMembers();
                bottomSheetAddTeamMembers.setListener(this);
                bottomSheetAddTeamMembers.setTeamMembers(teamMembersSet);
                bottomSheetAddTeamMembers.show(getSupportFragmentManager(), "BottomSheetStartNewChat");

            });
        } else {
            binding.tvHeading.setText("Edit Project");
            binding.btnPost.setOnClickListener(v -> updateProject());
            prefillData();
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("project_data")) {
            project = (ProjectResponse) getIntent().getSerializableExtra("project_data");
        }
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("project_data")) {
            isEditFlow = getIntent().getBooleanExtra("project_edit", false);
        }

    }


    private void setupTeamMembersUI() {
        adapter = new TeamMembersAdapter(this, teamMembers);
        adapter.setListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvTeamMembers.setLayoutManager(layoutManager);
        binding.rvTeamMembers.setAdapter(adapter);
    }

    private void updateTeamMemberList() {
        teamMembers.clear();
        teamMembers.add(SharedPrefHelper.getUserModel(this));
        teamMembers.addAll(teamMembersSet);
        adapter.notifyDataSetChanged();
    }

    private boolean validate() {
        boolean valid = true;
        if (TextUtils.isEmpty(binding.edtTitle.getText())) {
            binding.edtTitle.setError("Enter Title");
            valid = false;
        }
        if (TextUtils.isEmpty(binding.edtDescription.getText())) {
            binding.edtDescription.setError("Enter Description");
            valid = false;
        }
        if (TextUtils.isEmpty(binding.edtGitLink.getText())) {
            binding.edtGitLink.setError("Enter Git Link");
            valid = false;
        }
        if (TextUtils.isEmpty(binding.edtProjectLink.getText())) {
            binding.edtProjectLink.setError("Enter Project Link");
            valid = false;
        }
        if (TextUtils.isEmpty(binding.tvStartDate.getText())) {
            binding.tvStartDate.setError("Enter Start Date");
            valid = false;
        }
        if (TextUtils.isEmpty(binding.tvEndDate.getText())) {
            binding.tvEndDate.setError("Enter End Date");
            valid = false;
        }
        return valid;
    }

    private void postProject() {
        if (validate()) {
            binding.btnPost.setEnabled(false);
            binding.btnPost.setText("Saving ...");
            try {
                RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_POST_USER_PROJECTS, new BaseData<>(createProjectData()), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProject() {

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


    private void prefillData() {
        Picasso.get().load(project.image).into(binding.ivProjectImage);
        binding.edtTitle.setText(project.title);
        binding.edtDescription.setText(project.description);
        binding.edtGitLink.setText(project.gitLink);
        binding.edtProjectLink.setText(project.hostedLink);
        if (!TextUtils.isEmpty(project.duration)) {
            String dates[] = project.duration.split(" - ");
            binding.tvStartDate.setText(dates[0]);
            binding.tvEndDate.setText(dates[1]);
        }

    }

    private ProjectRequest createProjectData() {
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.id = SharedPrefHelper.getUserModel(this)._id;
        projectRequest.image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQkaSshoEf84h4kdxb8QnrSAEznAm5DAa7VmQ&usqp=CAU";
        if (!TextUtils.isEmpty(binding.edtTitle.getText())) {
            projectRequest.title = String.valueOf(binding.edtTitle.getText());
        }
        if (!TextUtils.isEmpty(binding.edtDescription.getText())) {
            projectRequest.description = String.valueOf(binding.edtDescription.getText());
        }
        if (!TextUtils.isEmpty(binding.edtGitLink.getText())) {
            projectRequest.gitLink = String.valueOf(binding.edtGitLink.getText());
        }
        if (!TextUtils.isEmpty(binding.edtProjectLink.getText())) {
            projectRequest.hostedLink = String.valueOf(binding.edtProjectLink.getText());
        }
        if (!TextUtils.isEmpty(binding.tvStartDate.getText()) && !TextUtils.isEmpty(binding.tvEndDate.getText())) {
            projectRequest.duration = String.valueOf(binding.tvStartDate.getText()) + " - " + String.valueOf(binding.tvEndDate.getText());
        }
//        projectResponse.teamMembers = new ArrayList<UserModel>();
//        projectResponse.teamMembers.addAll(teamMembersSet);
        return projectRequest;
    }

    private void setupDatePickerDialog(TextView textView) {
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
                    textView.setText(getFormattedDate(date_ship_millis));
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

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> data, Response<?> response) {
        if (commands == RESTController.RESTCommands.REQ_POST_USER_PROJECTS) {
            if (response.isSuccessful()) {
                onBackPressed();
            } else {
                binding.btnPost.setEnabled(true);
                binding.btnPost.setText("SAVE");
            }
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {
        if (commands == RESTController.RESTCommands.REQ_POST_USER_PROJECTS) {
            binding.btnPost.setEnabled(true);
            binding.btnPost.setText("SAVE");
        }
    }

    @Override
    public void onDoneClicked(SortedSet<UserModel> addedMembers) {
        teamMembersSet = addedMembers;
        updateTeamMemberList();
    }

    @Override
    public void onRemovedClick(UserModel userModel) {
        teamMembersSet.remove(userModel);
        updateTeamMemberList();
    }
}