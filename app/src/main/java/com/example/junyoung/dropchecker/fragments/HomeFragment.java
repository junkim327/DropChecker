package com.example.junyoung.dropchecker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.junyoung.dropchecker.HomeFeedData;
import com.example.junyoung.dropchecker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment{
    private static final String TAG = "HomeFragment";
    private String uid;

    private Query query;
    private FirebaseUser user;
    private FirebaseRecyclerAdapter<HomeFeedData, HomeFeedHolder> adapter;

    @BindView(R.id.recyclerview_home) RecyclerView recyclerView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        query =  FirebaseDatabase.getInstance().getReference().child("homeFeed");
        if (user != null) {
            uid = user.getUid();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        FirebaseRecyclerOptions<HomeFeedData> options =
                new FirebaseRecyclerOptions.Builder<HomeFeedData>()
                        .setQuery(query, HomeFeedData.class)
                        .build();

        // Code adapted from FirebaseUI-Android example:
        // https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md
        adapter = new FirebaseRecyclerAdapter<HomeFeedData, HomeFeedHolder>(options) {
            @Override
            protected void onBindViewHolder(final HomeFeedHolder holder, int position, HomeFeedData model) {
                Log.d(TAG, "onBindViewHolder: start binding.");
                if (user == null) {
                    holder.toggleButtonLike.setEnabled(false);
                }

                //set checked if the user is in the list of users who liked this current post.
                if (model.getUsersWhoLiked() == null) {
                    //do nothing
                } else if (model.getUsersWhoLiked().get(uid) == null) {
                    holder.toggleButtonLike.setChecked(false);
                } else if (model.getUsersWhoLiked().get(uid)) {
                    holder.toggleButtonLike.setChecked(true);
                }

                holder.textViewTitle.setText(model.getTitle());
                holder.textViewReleaseDate.setText(model.getReleaseDate());
                Glide.with(holder.imageViewMainImage.getContext())
                        .load(model.getimageUrl())
                        .apply(new RequestOptions().fitCenter())
                        .into(holder.imageViewMainImage);
                holder.toggleButtonLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DatabaseReference usersWhoLikedRef = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("homeFeed")
                                .child(String.valueOf(holder.getAdapterPosition()))
                                .child("usersWhoLiked");
                        if (isChecked) {
                            usersWhoLikedRef.child(user.getUid()).setValue(true);
                        } else {
                            usersWhoLikedRef.child(user.getUid()).removeValue();
                        }
                    }
                });
            }

            @Override
            public HomeFeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d(TAG, "onCreateViewHolder: start creating the view holder.");
                
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_cardview, parent, false);

                return new HomeFeedHolder(view);
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class HomeFeedHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_home)
        ImageView imageViewMainImage;
        @BindView(R.id.togglebutton_home)
        ToggleButton toggleButtonLike;
        @BindView(R.id.textview_home_title)
        TextView textViewTitle;
        @BindView(R.id.textview_home_releasedate) TextView textViewReleaseDate;

        public HomeFeedHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
