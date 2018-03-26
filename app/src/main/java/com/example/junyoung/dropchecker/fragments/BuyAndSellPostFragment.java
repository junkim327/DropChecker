package com.example.junyoung.dropchecker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.junyoung.dropchecker.activities.MakePostActivity;
import com.example.junyoung.dropchecker.Post;
import com.example.junyoung.dropchecker.R;
import com.example.junyoung.dropchecker.adapter.PostAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyAndSellPostFragment extends Fragment implements View.OnClickListener {
    private List<Post> postList;
    private String pageTitle;
    private PostAdapter postAdapter;
    private DatabaseReference postRef;
    private static final String TAG = "BuyAndSellPostFragment";

    @BindView(R.id.recyclerview_post) RecyclerView recyclerView;
    @BindView(R.id.button_post) FloatingActionButton floatingActionButtonPost;

    public static BuyAndSellPostFragment newInstance(String pageTitle) {
        BuyAndSellPostFragment fragment = new BuyAndSellPostFragment();

        Bundle args = new Bundle();
        args.putString("pageTitle", pageTitle);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageTitle = getArguments().getString("pageTitle");
        if (pageTitle != null) {
            Log.v(TAG, "The String that I received is: " + pageTitle);

            if (pageTitle.equals(this.getString(R.string.want_to_buy_post_page_title))) {
                postRef = FirebaseDatabase.getInstance().getReference().child("buyingPosts");
            } else if (pageTitle.equals(this.getString(R.string.want_to_sell_post_page_title))) {
                postRef = FirebaseDatabase.getInstance().getReference().child("posts");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_post, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, pageTitle);

        floatingActionButtonPost.setOnClickListener(this);
        getDataFromFirebase();

        return view;
    }

    private void getDataFromFirebase() {
        postRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                postList.add(dataSnapshot.getValue(Post.class));
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_post) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), floatingActionButtonPost);
            popupMenu.getMenuInflater().inflate(R.menu.menu_buyandsell, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getActivity(), MakePostActivity.class);
                    switch (item.getItemId()) {
                        case R.id.item_buy:
                            intent.putExtra("databaseNodeName", "buyingPosts");
                            startActivity(intent);
                            return true;
                        case R.id.item_sell:
                            intent.putExtra("databaseNodeName", "posts");
                            startActivity(intent);
                            return true;
                        default:
                            return false;
                    }
                }
            });
        }
    }
}
