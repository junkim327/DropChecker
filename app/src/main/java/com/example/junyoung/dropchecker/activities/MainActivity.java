package com.example.junyoung.dropchecker.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.junyoung.dropchecker.R;
import com.example.junyoung.dropchecker.fragments.DropListFragment;
import com.example.junyoung.dropchecker.fragments.HomeFragment;
import com.example.junyoung.dropchecker.fragments.PostFragment;
import com.example.junyoung.dropchecker.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseUser user;

    @BindView(R.id.navigationview_bottom) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setupBottomNavigation();
        bottomNavigationView.setSelectedItemId(R.id.item_home);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        final String fragmentTitle;
                        switch (item.getItemId()) {
                            case R.id.item_home:
                                Log.v(TAG, "Home is selected.");

                                fragment = HomeFragment.newInstance();
                                fragmentTitle = "Featured";
                                break;
                            case R.id.item_schedules:
                                Log.v(TAG, "DropList is selected");

                                fragment = DropListFragment.newInstance();
                                fragmentTitle = "DropList";
                                break;
                            case R.id.item_post:
                                Log.v(TAG, "Post is selected");

                                if (user == null) {
                                    createDialogForGuest();
                                }
                                fragment = PostFragment.newInstance();
                                fragmentTitle = "Post";
                                break;
                            case R.id.item_profile:
                                Log.v(TAG, "Profile is selected");

                                if (user == null) {
                                    createDialogForGuest();
                                }
                                fragment = ProfileFragment.newInstance();
                                fragmentTitle = "Profile";
                                break;
                            default:
                                return false;
                        }

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout_main, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(fragmentTitle);
                        }

                        return true;
                    }
                });
    }

    private void createDialogForGuest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Hello World").setTitle("Log In");

        builder.show();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
