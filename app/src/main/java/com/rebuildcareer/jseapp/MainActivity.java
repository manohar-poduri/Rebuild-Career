package com.rebuildcareer.jseapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.rebuildcareer.jseapp.employe.CompanyDetailsEmployer;
import com.rebuildcareer.jseapp.jobSeeker.PhoneNumberActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView jobSeeker, employer;
    Toolbar toolbar;
    TextView vacancies, comingUp;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        jobSeeker = (CardView) findViewById(R.id.jobSearch);
        employer = (CardView) findViewById(R.id.employer);
        vacancies = findViewById(R.id.vacancies);
        comingUp = findViewById(R.id.comingUp);

//        vacancies.animate().alpha(0).setDuration(5000);
//        comingUp.animate().alpha(0).setDuration(5000);

        Animation rotateAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        rotateAnimation.setFillAfter(true);
        vacancies.startAnimation(rotateAnimation);

        Animation rotateAnimation1 = AnimationUtils.loadAnimation(this,R.anim.rotate);
        rotateAnimation.setFillAfter(true);
        comingUp.startAnimation(rotateAnimation1);

        /*Animation rotateAnimation2 = AnimationUtils.loadAnimation(this,R.anim.rotate);
        rotateAnimation.setFillAfter(true);
        toolbar.startAnimation(rotateAnimation2);*/

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.jobSearch:
                Intent intent = new Intent(MainActivity.this, PhoneNumberActivity.class);
                startActivity(intent);
                break;
            case R.id.employer:
                Intent intent1 = new Intent(MainActivity.this, CompanyDetailsEmployer.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
