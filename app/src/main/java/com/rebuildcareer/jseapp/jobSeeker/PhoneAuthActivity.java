package com.rebuildcareer.jseapp.jobSeeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rebuildcareer.jseapp.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private EditText edtOTP;
    private Button btnVerifyOTP, btnOpenApplication;
//    private TextView txtTermsAndConditions;
    private String verificationCode;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private Toolbar toolbar;
//    LinearLayout linearLayout;
//    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        setTitle("OTP Verification");

        firebaseAuth = FirebaseAuth.getInstance();

        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        sendVerificationCode(phoneNumber);

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);
        edtOTP = findViewById(R.id.edtOTP);
//        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        btnOpenApplication = findViewById(R.id.btnOpenApplication);
//        txtTermsAndConditions = findViewById(R.id.termsAndConditions);

        //  edtOTP.animate().translationY(100).setDuration(1500);
        //btnVerifyOTP.animate().translationY(100).setDuration(1500);
        //btnOpenApplication.animate().translationY(100).setDuration(1500);

        btnOpenApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FancyToast.makeText(PhoneAuthActivity.this,"Please read the terms and conditions!!",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();


//                FancyToast.makeText(PhoneAuthActivity.this,"Please enter the correct OTP!!",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
            }
        });


        /*txtTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(PhoneAuthActivity.this);
                View view = getLayoutInflater().inflate(R.layout.terms_and_conditions, null);
                txtTermsAndConditions = view.findViewById(R.id.termsAndConditions);
                builder.setTitle("Terms and Conditions");
                builder.setMessage("I fully understand that\n" +
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
                builder.create();
                builder.show();
                builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

            }
        });
*/
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

            Toast.makeText(PhoneAuthActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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


                    FancyToast.makeText(PhoneAuthActivity.this,"OTP is Verified!!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                } else {

                    FancyToast.makeText(PhoneAuthActivity.this,task.getException().getMessage(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                }
            }
        });


    }


    public void itemClicked(View view){

        final CheckBox checkBox = (CheckBox) view;


        if (checkBox.isChecked()){

            FancyToast.makeText(this,"You have read all the terms and conditions!!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

            btnOpenApplication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String code = edtOTP.getText().toString().trim();

                    if (code.isEmpty() || code.length() < 6) {

                        edtOTP.setError("Enter OTP..");
                        edtOTP.requestFocus();
                        return;
                    }

                    verify(code);



                    if (checkBox.isChecked()) {

//                        verify(code);

                        FancyToast.makeText(PhoneAuthActivity.this,"Application opened!!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        Intent intent = new Intent(PhoneAuthActivity.this, JobseekerActivity.class);
                        startActivity(intent);

                    }
                }
            });
        }

    }


}
