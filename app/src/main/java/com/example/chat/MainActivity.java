package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseuser;
    DatabaseReference referance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        getSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");


        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        referance= FirebaseDatabase.getInstance().getReference("User").child(firebaseuser.getUid());
        referance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default"))
                {
                    profile_image.setImageResource(R.drawable.ic_launcher_foreground);
                }
                else
                {
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSupportActionBar(Toolbar toolbar) {
    }
}
