package com.example.junyoung.dropchecker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junyoung.dropchecker.activities.LoginActivity;
import com.example.junyoung.dropchecker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment{
    private String userName;
    private Uri userPhotoUri;
    private static final String TAG = "ProfileFragment";


    private FirebaseAuth auth;

    @BindView(R.id.button_profile_logout) Button buttonLogout;
    @BindView(R.id.textview_profile) TextView textViewUserName;
    @BindView(R.id.imageview_profile) ImageView imageViewUserPhoto;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userName = user.getDisplayName();
            userPhotoUri = user.getPhotoUrl();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        Picasso.with(getActivity()).load(userPhotoUri).into(imageViewUserPhoto);
        textViewUserName.setText(userName);

        return view;
    }

    @OnClick(R.id.button_profile_logout)
    public void signOut() {
        auth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
