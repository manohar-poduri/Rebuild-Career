package com.rebuildcareer.jseapp.employe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rebuildcareer.jseapp.MainActivity;
import com.rebuildcareer.jseapp.R;
import com.rebuildcareer.jseapp.UserClass.Member;
import com.rebuildcareer.jseapp.UserClass.UserAdapter;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;


public class EmployerActivity extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Member> members;
    UserAdapter userAdapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

        setTitle("Employer");

        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
//        spinner = findViewById(R.id.spinner);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        members = new ArrayList<Member>();
        recyclerView.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("application");
        databaseReference.addValueEventListener(valueEventListener);


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                Member mMember = dataSnapshot1.getValue(Member.class);
                members.add(mMember);
            }

            userAdapter = new UserAdapter(EmployerActivity.this, members);
            recyclerView.setAdapter(userAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EmployerActivity.this);
        builder.setMessage("If you want to LOGOUT click YES");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EmployerActivity.super.onBackPressed();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EmployerActivity.this,MainActivity.class));
                FancyToast.makeText(EmployerActivity.this, "You have Logged out successful!!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
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
}
