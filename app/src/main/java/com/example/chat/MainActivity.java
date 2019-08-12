package com.example.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.chat.Fragment.HomeFragment;
import com.example.chat.Fragment.ProfileFragment;
import com.example.chat.Fragment.SearchFragment;
import com.example.chat.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    Fragment selctedfragment=null;
    FirebaseUser firebaseuser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //profile_pic=findViewById(R.id.profile_pic);
        bottomNavigationView=findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        fab=findViewById(R.id.fab);
        }
        private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.home:
                                selctedfragment=new HomeFragment();
                                break;

                            case R.id.search:
                                selctedfragment=new SearchFragment();
                                break;

                            case R.id.person:
                                SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                                editor.putString("profileid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                                editor.apply();
                                selctedfragment=
                                selctedfragment=new ProfileFragment();
                                break;
                        }
                        if(selctedfragment!=null)
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selctedfragment).commit();
                        }
                        return true;
                    }
                };


}