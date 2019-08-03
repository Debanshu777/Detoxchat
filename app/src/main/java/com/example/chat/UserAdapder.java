package com.example.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat.Fragment.ProfileFragment;
import com.example.chat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapder extends RecyclerView.Adapter<UserAdapder.Viewholder> {

    private Context mContext;
    private List<User> mUser;

    private FirebaseUser firebaseuser;

    public UserAdapder(Context mContext, List<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public UserAdapder.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapder.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapder.Viewholder holder, int position) {

        firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        final User user=mUser.get(position);
        holder.follow.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsername());
        holder.fullaname.setText(user.getFullname());
        Glide.with(mContext).load(user.getImageURL()).into(holder.image_profile);
        isFollowing(user.getId(),holder.follow);
        if(user.getId().equals(FirebaseAuth.getInstance()))
        {
            holder.follow.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= (SharedPreferences.Editor) mContext.getSharedPreferences("PREPS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",user.getId());
                editor.apply();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.follow.getText().toString().equals("Follow"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseuser.getUid())
                            .child("Following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("Following").child(firebaseuser.getUid()).setValue(true);
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseuser.getUid())
                            .child("Following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("Following").child(firebaseuser.getUid()).removeValue();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView fullaname;
        public CircleImageView image_profile;
        public Button follow;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            fullaname=itemView.findViewById(R.id.fullname);
            image_profile=itemView.findViewById(R.id.image_profile);
            follow=itemView.findViewById(R.id.follow);
        }
    }
    private void isFollowing(final String userId, final Button button){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseuser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userId).exists())
                {
                    button.setText("Following");

                }
                else
                {
                    button.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
