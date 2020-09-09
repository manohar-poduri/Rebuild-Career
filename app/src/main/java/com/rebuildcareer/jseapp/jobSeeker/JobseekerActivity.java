package com.rebuildcareer.jseapp.jobSeeker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rebuildcareer.jseapp.MainActivity;
import com.rebuildcareer.jseapp.R;
import com.rebuildcareer.jseapp.UserClass.Member;
import com.rebuildcareer.jseapp.employe.EmployerActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class JobseekerActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private EditText edtFullName, edtAge, edtDob, edtAadharNumber, edtEmailAddress, edtContactNumber, edtQualification, edtExperience, edtJobProfile, edtKeyWordJobProfile, edtOtherReason, edtExpectedSalary, edtCurrentLocation, edtPreferredLocation;
    private RadioButton rbNoJob, rbNoSalary, rbOthers, rbYes, rbNo, radio2, radio1;
    private Button btnSubmit;
    DatabaseReference databaseReference;
    private Member member;
    private Toolbar toolbar;
    private ScrollView scrollViewForJobSeekers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker);

        setTitle("Job Seeker");

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);

        scrollViewForJobSeekers = findViewById(R.id.scrollViewForJobSeekers);

        edtFullName = findViewById(R.id.edtFullName);
        edtAge = findViewById(R.id.edtAge);
        edtDob = findViewById(R.id.edtDob);
        edtAadharNumber = findViewById(R.id.edtAadharNumber);
        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        edtContactNumber = findViewById(R.id.edtContactNumber);
        edtQualification = findViewById(R.id.edtQualification);
        edtExperience = findViewById(R.id.edtExperience);
        edtJobProfile = findViewById(R.id.edtJobProfile);
        edtKeyWordJobProfile = findViewById(R.id.edtKeyWordJobProfile);
        edtOtherReason = findViewById(R.id.edtOtherReason);
        edtExpectedSalary = findViewById(R.id.edtExpectedSalary);
        edtCurrentLocation = findViewById(R.id.edtCurrentLocation);
        edtPreferredLocation = findViewById(R.id.edtPreferredLocation);

        rbNoJob = findViewById(R.id.rbNoJob);
        rbNoSalary = findViewById(R.id.rbNoSalary);
        rbOthers = findViewById(R.id.rbOthers);
        rbYes= findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);
        btnSubmit = findViewById(R.id.btnSubmit);

        rbOthers.setOnCheckedChangeListener(this);
        rbYes.setOnCheckedChangeListener(this);



        member = new Member();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("application");


//        btnSubmit.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(JobseekerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        edtDob.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtFullName.getText().toString().equals("") || edtAge.getText().toString().equals("") || edtDob.getText().toString().equals("") || edtAadharNumber.getText().toString().equals("") || edtEmailAddress.getText().toString().equals("") || edtContactNumber.getText().toString().equals("") ||edtQualification.getText().toString().equals("") || edtExperience.getText().toString().equals("")
                 || edtJobProfile.getText().toString().equals("") || edtKeyWordJobProfile.getText().toString().equals("") || edtExpectedSalary.getText().toString().equals("") || edtCurrentLocation.getText().toString().equals("") || rbNoJob.getText().toString().equals("") || rbNoSalary.getText().toString().equals("") || rbOthers.getText().toString().equals("") || rbYes.getText().toString().equals("") || rbNo.getText().toString().equals("")){

                } else {

                    insertingData();

                }

            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout){

            FirebaseAuth.getInstance().signOut();
            finishAffinity();
            System.exit(0);
            FancyToast.makeText(JobseekerActivity.this,"You have logged out successful!!",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertingData(){
        member.setFullName(edtFullName.getText().toString());
        member.setAge(String.valueOf(Integer.parseInt(edtAge.getText().toString())));
        member.setDob(edtDob.getText().toString());
        member.setAadharNumber(edtAadharNumber.getText().toString());
        member.setEmailAddress(edtEmailAddress.getText().toString());
        member.setContactNumber(edtContactNumber.getText().toString());
        member.setQualification(edtQualification.getText().toString());
        member.setExperience(edtExperience.getText().toString());
        member.setJobProfile(edtJobProfile.getText().toString());
        member.setKeywordJobProfile(edtKeyWordJobProfile.getText().toString());
        member.setExpectedSalary(edtExpectedSalary.getText().toString());
        member.setCurrentLocation(edtCurrentLocation.getText().toString());


        databaseReference.push().setValue(member);


        FancyToast.makeText(JobseekerActivity.this,"Thanks for submitting your details!!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();


        final PrettyDialog prettyDialog = new PrettyDialog(JobseekerActivity.this);
        prettyDialog.setTitle(member.getFullName())
                .setMessage(member.getFullName() + "\n" + member.getContactNumber() + "\n" + member.getEmailAddress() + "\n" + member.getQualification())
                .setIcon(R.drawable.pdlg_icon_info)
                .setMessageColor(R.color.colorAccent)
                .addButton("OK", R.color.pdlg_color_red, R.color.pdlg_color_white, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {

                                prettyDialog.dismiss();


                            }
                        }
                )
                .show();

        edtFullName.setText(edtFullName.getText().toString());
        edtAge.setText(edtAge.getText().toString());
        edtDob.setText(edtDob.getText().toString());
        edtAadharNumber.setText(edtAadharNumber.getText().toString());
        edtEmailAddress.setText(edtEmailAddress.getText().toString());
        edtContactNumber.setText(edtContactNumber.getText().toString());
        edtQualification.setText(edtQualification.getText().toString());
        edtExperience.setText(edtExperience.getText().toString());
        edtJobProfile.setText(edtJobProfile.getText().toString());
        edtKeyWordJobProfile.setText(edtKeyWordJobProfile.getText().toString());
        edtOtherReason.setText(edtOtherReason.getText().toString());
        edtExpectedSalary.setText(edtExpectedSalary.getText().toString());
        edtCurrentLocation.setText(edtCurrentLocation.getText().toString());
        edtPreferredLocation.setText(edtPreferredLocation.getText().toString());


    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerActivity.this);
        builder.setMessage("If you want to LOGOUT click YES");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                JobseekerActivity.super.onBackPressed();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(JobseekerActivity.this, MainActivity.class));
                FancyToast.makeText(JobseekerActivity.this,"You have Logged out successful!!",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch(buttonView.getId()){

            case R.id.rbYes:

                if (isChecked){
                    edtPreferredLocation.setVisibility(View.VISIBLE);
                    member.setPreferredLocation(edtPreferredLocation.getText().toString());
                } else {
                    edtPreferredLocation.setVisibility(View.GONE);
                    member.setPreferredLocationNo(rbNo.getText().toString());
                }
                break;

            case R.id.rbOthers:
                if (isChecked){
                    edtOtherReason.setVisibility(View.VISIBLE);
                    member.setOtherReason(edtOtherReason.getText().toString());
                } else if(rbNoJob.isChecked()){
                    edtOtherReason.setVisibility(View.GONE);
                    member.setNoJob(rbNoJob.getText().toString());
                } else {
                    edtOtherReason.setVisibility(View.GONE);
                    member.setNoSalary(rbNoSalary.getText().toString());
                }
        }

    }
}
