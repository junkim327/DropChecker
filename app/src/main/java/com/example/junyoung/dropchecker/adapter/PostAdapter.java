package com.example.junyoung.dropchecker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junyoung.dropchecker.Post;
import com.example.junyoung.dropchecker.activities.PostContentActivity;
import com.example.junyoung.dropchecker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;
    private String pageTitle;
    private static final String TAG = "RealtimeDatabasePost";

    public PostAdapter() {

    }

    public PostAdapter(List<Post> postList, String pageTitle) {
        this.postList = postList;
        this.pageTitle = pageTitle;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.textViewUserName.setText(post.getName());
        holder.textViewSubject.setText(post.getSubject());
        if (post.getUserPhotoUrlString() == null) {
            Picasso.with(holder.imageViewUserImage.getContext())
                    .load(R.drawable.if_user_male3_172627)
                    .into(holder.imageViewUserImage);
        } else {
            Picasso.with(holder.imageViewUserImage.getContext())
                    .load(post.getUserPhotoUrlString())
                    .into(holder.imageViewUserImage);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.textview_subject) TextView textViewSubject;
        @BindView(R.id.textview_username) TextView textViewUserName;
        @BindView(R.id.imageview_userImage) ImageView imageViewUserImage;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Context context = v.getContext();
            Intent intent = new Intent(context, PostContentActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString("pageTitle", pageTitle);
            bundle.putString("subject", postList.get(getAdapterPosition()).getSubject());
            bundle.putString("size", postList.get(getAdapterPosition()).getSize());
            bundle.putString("price", postList.get(getAdapterPosition()).getPrice());
            bundle.putString("content", postList.get(getAdapterPosition()).getContent());
            bundle.putString("userName", postList.get(getAdapterPosition()).getName());
            bundle.putString("userPhotoUrlString", postList.get(getAdapterPosition())
                    .getUserPhotoUrlString());
            intent.putExtras(bundle);

            context.startActivity(intent);
        }
    }
}
