package com.example.junyoung.dropchecker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.junyoung.dropchecker.ProductInfo;
import com.example.junyoung.dropchecker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<ProductInfo> productInfoList;

    public ImageAdapter(Context context, List<ProductInfo> productInfoList) {
        this.context = context;
        this.productInfoList = productInfoList;
    }

    @Override
    public int getCount() {
        return productInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return productInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(540, 540));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 1, 0, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        Log.v("imageUrl", "imageUrl: " + productInfoList.get(position).getImageUrlString());
        Glide.with(context)
                .load(productInfoList.get(position).getImageUrlString())
                .apply(new RequestOptions().fitCenter())
                .into(imageView);

        return imageView;
    }
}
