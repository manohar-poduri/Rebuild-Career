package com.rebuildcareer.jseapp.employe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rebuildcareer.jseapp.R;
import com.rebuildcareer.jseapp.UserClass.Message;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.concurrent.TimeUnit;

public class CompanyDetailsEmployer extends AppCompatActivity {

    EditText edtCompanyName, edtLocation, edtPersonName, edtEmail, edtPhone, edtEmployerOTP;
    Button btnVerifyOtp, btnEmployerSignUp;
    Message message;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    String verificationCode;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details_employer);

        setTitle("Company Details");

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);

        edtCompanyName = findViewById(R.id.edtCompanyName);
        edtLocation = findViewById(R.id.edtLocation);
        edtPersonName = findViewById(R.id.edtPersonName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        message = new Message();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Company Details");

        firebaseAuth = FirebaseAuth.getInstance();

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String companyName = edtCompanyName.getText().toString();
                String location = edtLocation.getText().toString();
                String personName = edtPersonName.getText().toString();
                String emailId = edtEmail.getText().toString();
                String phone = edtPhone.getText().toString();

                if (companyName.equals("") || location.equals("") || personName.equals("") || emailId.equals("") || phone.equals("")) {

                    edtCompanyName.setError("Company Name..");
                    edtCompanyName.requestFocus();

                    edtLocation.setError("Location...");
                    edtLocation.requestFocus();

                    edtPersonName.setError("Name...");
                    edtPersonName.requestFocus();

                    edtEmail.setError("Email ID..");
                    edtEmail.requestFocus();

                    edtPhone.setError("Phone...");
                    edtPhone.requestFocus();
                } else {

                    message.setCompanyName(edtCompanyName.getText().toString());
                    message.setLocation(edtLocation.getText().toString());
                    message.setPersonName(edtPersonName.getText().toString());
                    message.setEmailId(edtEmail.getText().toString());
                    message.setPhoneNumber(edtPhone.getText().toString());

                    databaseReference.push().setValue(message);

                    FancyToast.makeText(CompanyDetailsEmployer.this, "OTP sent to Your " + edtPhone.getText().toString(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();


                    FancyToast.makeText(CompanyDetailsEmployer.this, "Thanks for submitting your details!!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();


//                    startActivity(new Intent(CompanyDetailsEmployer.this, EmployeeSignUp.class));


                    AlertDialog.Builder builder = new AlertDialog.Builder(CompanyDetailsEmployer.this, R.style.CustomDialog);
                    View view1 = getLayoutInflater().inflate(R.layout.otp_alert_box, null);
                    edtEmployerOTP = view1.findViewById(R.id.edtEmployerOTP);
                    btnEmployerSignUp = view1.findViewById(R.id.btnEmployerSignUp);

                    sendVerificationCode(edtPhone.getText().toString());

                    btnEmployerSignUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String code = edtEmployerOTP.getText().toString().trim();

                            if (code.isEmpty() || code.length() < 6) {

                                edtEmployerOTP.setError("Enter OTP..");
                                edtEmployerOTP.requestFocus();
                                return;

                            }

                            verify(code);
                        }
                    });

                    builder.setView(view1);
                    builder.setCancelable(true);
                    builder.create();
                    builder.show();


                }
            }

        });

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

            Toast.makeText(CompanyDetailsEmployer.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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

                    Intent intent = new Intent(getApplicationContext(),MessageEmployer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    FancyToast.makeText(getApplicationContext(), "OTP is Verified!!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                } else {

                    FancyToast.makeText(getApplicationContext(), task.getException().getMessage(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                }
            }
        });


    }

}

