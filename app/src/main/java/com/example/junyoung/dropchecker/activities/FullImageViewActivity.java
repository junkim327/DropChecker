package com.example.junyoung.dropchecker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.junyoung.dropchecker.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullImageViewActivity extends AppCompatActivity {
    @BindView(R.id.textview_fullimageview_color) TextView textViewColor;
    @BindView(R.id.textview_fullimageview_price) TextView textViewPrice;
    @BindView(R.id.textview_fullimageview_productname) TextView textViewProductName;
    @BindView(R.id.imageview_fullimageview) ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimageview);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String color = bundle.getString("color");
            String price = bundle.getString("price");
            String productName = bundle.getString("productName");
            String imageUrlString = bundle.getString("imageUrlString");

            textViewColor.setText(color);
            textViewPrice.setText(price);
            textViewProductName.setText(productName);
            Glide.with(this).load(imageUrlString).apply(new RequestOptions().fitCenter()).into(imageView);
        }
    }
}
