package com.rebuildcareer.jseapp.employe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class EmployeeSignUp extends AppCompatActivity {


    EditText edtEmployerEmail, edtEmployerPhone, edtEmployerOTP, edtEmployerPassword;
    Button btnEmployerOTP, btnEmployerSignUp;
    ProgressBar eProgressBar;
    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    private String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_sign_up);

        setTitle("SignUp");

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);


        edtEmployerEmail = findViewById(R.id.edtEmployeeEmail);
        edtEmployerPhone = findViewById(R.id.edtEmployerPhone);
        btnEmployerOTP = findViewById(R.id.btnEmployerOTP);
        eProgressBar = findViewById(R.id.employerProgressBar);


        firebaseAuth = FirebaseAuth.getInstance();

        btnEmployerOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String email = edtEmployerEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    FancyToast.makeText(EmployeeSignUp.this, "Please enter email!!", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    return;
                }



                if (edtEmployerPhone.getText().toString().equals("")) {
                    FancyToast.makeText(EmployeeSignUp.this, "Please enter Phone Number!!", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    return;
                }



                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeSignUp.this);
                View view1 = getLayoutInflater().inflate(R.layout.otp_alert_box, null);
                edtEmployerOTP = view1.findViewById(R.id.edtEmployerOTP);
                btnEmployerSignUp = view1.findViewById(R.id.btnEmployerSignUp);

                sendVerificationCode(edtEmployerPhone.getText().toString());

                btnEmployerSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String OTP = edtEmployerOTP.getText().toString().trim();

                        if (OTP.length() < 6 || OTP.length() > 6) {

                            FancyToast.makeText(getApplicationContext(), "Please enter the correct OTP", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                        } else if (OTP.equals("")) {

                            FancyToast.makeText(getApplicationContext(), "Please enter the correct OTP", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        } else  {
                            startActivity(new Intent(getApplicationContext(), MessageEmployer.class));
                        }


                    }
                });
                builder.setView(view1);
                builder.create();
                builder.show();

                String phone = edtEmployerPhone.getText().toString().trim();


                if (phone.isEmpty()) {

                    edtEmployerPhone.setError("Phone Number");
                    edtEmployerPhone.requestFocus();
                    return;
                }

                FancyToast.makeText(EmployeeSignUp.this, "OTP sent to Your " + edtEmployerPhone.getText().toString(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

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

                    Toast.makeText(EmployeeSignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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


                            FancyToast.makeText(getApplicationContext(), "OTP is Verified!!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        } else {

                            FancyToast.makeText(getApplicationContext(), task.getException().getMessage(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                        }
                    }
                });


            }
        });
    }
}

