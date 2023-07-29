package com.app.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.app.chatbot.Model.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView tvName, tvEmail, tvDate;
    FirebaseUser user;
    FirebaseDatabase fData;
    DatabaseReference dReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_user);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_user:
                    return true;
                case R.id.bottom_home:
                    startActivity(new Intent(ProfileActivity.this, HomeMenuActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.bottom_reminder:
                    startActivity(new Intent(ProfileActivity.this, AlarmActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.bottom_bot:
                    startActivity(new Intent(ProfileActivity.this, ChatBotActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });

        auth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        dReference = fData.getReference("Registered Users");

        button = findViewById(R.id.logout1);
        tvName = findViewById(R.id.nameProfile1);
        tvEmail = findViewById(R.id.emailProfile1);
        user = auth.getCurrentUser();
        
        if (user == null) {
            Intent intent = new Intent(ProfileActivity.this, Login_user.class);
            startActivity(intent);
            finish();
        } else {
            tvEmail.setText(user.getEmail());
            showUserProfile(user);
        }
        button.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(ProfileActivity.this, Login_user.class);
            startActivity(intent);
            finish();
        });
    }

    private void showUserProfile(FirebaseUser fUser) {
        String userId = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails uDetail = snapshot.getValue(UserDetails.class);
                if (uDetail != null){
                    String test = uDetail.getUserName();
                    tvName.setText(test);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}