package com.example.chat.Fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chat.Model.User;
import com.example.chat.R;
import com.example.chat.UserAdapder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapder userAdapder;
    private List<User>mUser;

    EditText search_bar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView=view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_bar=view.findViewById(R.id.seacrch_bar);
        mUser=new ArrayList<>();
        userAdapder=new UserAdapder(getContext(),mUser);
        recyclerView.setAdapter(userAdapder);
        readuses();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
public void searchUser(String s) {
    Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("username").startAt(s).endAt(s + "\uf8ff");

    query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mUser.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                User user = (User) snapshot.getValue(User.class);
                mUser.add(user);
            }
            userAdapder.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
    private void readuses()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_bar.getText().toString().equals(""))
                {
                    mUser.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        User user = (User) snapshot.getValue(User.class);
                        mUser.add(user);
                    }


                    userAdapder.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
