package com.rebuildcareer.jseapp.employe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rebuildcareer.jseapp.MainActivity;
import com.rebuildcareer.jseapp.R;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MessageEmployer extends AppCompatActivity {

    TextView txtMessage,personName;

    DatabaseReference databaseReference;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_employer);

        setTitle("Mail Details");

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);

        txtMessage = findViewById(R.id.txtMessage);
//        personName = findViewById(R.id.personName);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Company Details");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*Message message = dataSnapshot.getValue(Message.class);
                String name = message.getPersonName();

                personName.setText(name);

                Toast.makeText(MessageEmployer.this, name, Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {


            }
        };

        ss.setSpan(clickableSpan,24,50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtMessage.setText(ss);
        txtMessage.setMovementMethod(LinkMovementMethod.getInstance());*/



    }

    @Override
    public void onBackPressed() {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MessageEmployer.this);
        builder.setMessage("If you want to LOGOUT click YES");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MessageEmployer.super.onBackPressed();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MessageEmployer.this, MainActivity.class));
                FancyToast.makeText(MessageEmployer.this, "You have Logged out successful!!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

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
        }
        return super.onOptionsItemSelected(item);
    }
}


