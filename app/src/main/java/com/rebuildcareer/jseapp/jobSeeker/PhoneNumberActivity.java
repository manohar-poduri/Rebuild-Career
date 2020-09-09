package com.rebuildcareer.jseapp.jobSeeker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rebuildcareer.jseapp.MainActivity;
import com.rebuildcareer.jseapp.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {


    private EditText edtPhoneNumber, edtJobSeekerOTP;
    private Button btnSendOTP, btnJobSeekerSignUp;
    Toolbar toolbar;
    private String verificationCode;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        setTitle("Phone Verification");

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);


        firebaseAuth = FirebaseAuth.getInstance();

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = edtPhoneNumber.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10 || number.length() > 13){

                    edtPhoneNumber.setError("Enter valid Phone Number");
                    edtPhoneNumber.requestFocus();
                    return;
                }

                FancyToast.makeText(PhoneNumberActivity.this,"OTP sent to Your " + edtPhoneNumber.getText().toString(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                sendVerificationCode(number);

                AlertDialog.Builder builder = new AlertDialog.Builder(PhoneNumberActivity.this,R.style.CustomDialog);
                View view = getLayoutInflater().inflate(R.layout.job_seeker_otp_alert_box,null);
                edtJobSeekerOTP = view.findViewById(R.id.edtJobSeekerOTP);
                btnJobSeekerSignUp = view.findViewById(R.id.btnJobSeekerSignUp);

                btnJobSeekerSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {

                        String code = edtJobSeekerOTP.getText().toString().trim();

                        if (code.isEmpty() || code.length() < 6) {

                            edtJobSeekerOTP.setError("Enter OTP..");
                            edtJobSeekerOTP.requestFocus();
                            return;

                        }

                        verify(code);

                    }
                });
                builder.setView(view);
                builder.create().show();;

            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            Intent intent = new Intent(PhoneNumberActivity.this,JobseekerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }


    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {

//                progressBar.setVisibility(View.VISIBLE);
                verify(code);

            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(PhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verify(String code) {


        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        SIgnInWithCredentials(credential);

    }

    private void SIgnInWithCredentials(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Intent intent = new Intent(getApplicationContext(),JobSeekerTermsAndConditions.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    FancyToast.makeText(getApplicationContext(),"OTP is Verified!!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                } else {

                    finish();
                    FancyToast.makeText(getApplicationContext(),task.getException().getMessage(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                }
            }
        });


    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(PhoneNumberActivity.this, MainActivity.class));

        super.onBackPressed();
    }
}
