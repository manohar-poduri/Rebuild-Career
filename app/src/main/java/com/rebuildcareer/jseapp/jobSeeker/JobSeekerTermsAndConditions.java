package com.rebuildcareer.jseapp.jobSeeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rebuildcareer.jseapp.R;
import com.shashank.sony.fancytoastlib.FancyToast;

public class JobSeekerTermsAndConditions extends AppCompatActivity {

    TextView txtTAC, txtTermsAndConditions;
    Button openApplication;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_terms_and_conditions);

        setTitle("Terms And Conditions");

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);

        txtTAC = findViewById(R.id.txtTAC);
        txtTermsAndConditions = findViewById(R.id.termsAndConditions);
        openApplication = findViewById(R.id.openApplication);

        txtTAC.setText("I fully understand that\n" +
                "•\tThere is no guarantee of job or job offer by filling this form\n" +
                "•\tI am availing this service at free of cost\n" +
                "•\tI am sharing my information at my willingness \n" +
                "•\tI am willing to accept offer by any company fulfilling my requirements as mentioned in the form\n" +
                "•\tI am accepting to remove my information from this database as soon as I received my first offer irrespective of my acceptance of the offer received by me\n" +
                "•\tI cannot make any claim against the company as I am providing my details at my willingness\n" +
                "•\tIts my responsibility to inform by email in case I got job offer from any source.\n" +
                "•\tI am accepting to receive promotional emails, job offers and any relevant communication by email.\n" +
                "•\tWhatever information provided in the form is fully accurate and true. \n" +
                "•\tIf the information provided in the form is found to be false prior to receive any offer, my information will be deleted from the database.\n" +
                "•\tIf the information provided in the form is found to be false after getting job offer, Company has the right to inform to the organization offering job.\n");


    }


    public boolean checkedClick(View view) {

        CheckBox checkBox = (CheckBox) view;

        if (checkBox.isChecked()){

//            startActivity(new Intent(JobSeekerTermsAndConditions.this,JobseekerActivity.class));

            FancyToast.makeText(JobSeekerTermsAndConditions.this,"You have read all the terms and conditions!!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

            openApplication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(JobSeekerTermsAndConditions.this,JobseekerActivity.class));
                }
            });

        } else {

            openApplication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FancyToast.makeText(JobSeekerTermsAndConditions.this,"Please read the terms and conditions!!",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();

                }
            });
        }
        return false;
    }


}
