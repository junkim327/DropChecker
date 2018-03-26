package com.example.junyoung.dropchecker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.junyoung.dropchecker.activities.FullImageViewActivity;
import com.example.junyoung.dropchecker.ProductInfo;
import com.example.junyoung.dropchecker.R;
import com.example.junyoung.dropchecker.adapter.ImageAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductImageViewFragment extends Fragment {
    private List<ProductInfo> productInfoList;

    private DatabaseReference productInfoRef;

    @BindView(R.id.gridview_productview) GridView gridView;

    public static ProductImageViewFragment newInstance(String pageTitle) {
        ProductImageViewFragment productImageViewFragment = new ProductImageViewFragment();

        Bundle args = new Bundle();
        args.putString("pageTitle", pageTitle);
        productImageViewFragment.setArguments(args);

        return productImageViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pageTitle = getArguments().getString("pageTitle");
        Log.v("pageTitle", "PageTilte: " + pageTitle);
        productInfoRef = FirebaseDatabase.getInstance().getReference().child("productImages")
                .child(pageTitle).child("Week 17");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productview, container, false);
        ButterKnife.bind(this, view);

        productInfoList = new ArrayList<>();
        //gridView.setAdapter(new ImageAdapter(getActivity(), productInfoList));

        productInfoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                productInfoList.add(dataSnapshot.getValue(ProductInfo.class));
                gridView.setAdapter(new ImageAdapter(getActivity(), productInfoList));
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FullImageViewActivity.class);
                ProductInfo productInfo = (ProductInfo) parent.getItemAtPosition(position);
                intent.putExtra("color", productInfo.getColor());
                intent.putExtra("price", productInfo.getPrice());
                intent.putExtra("productName", productInfo.getProductName());
                intent.putExtra("imageUrlString", productInfo.getImageUrlString());
                startActivity(intent);
            }
        });

        return view;
    }
}
