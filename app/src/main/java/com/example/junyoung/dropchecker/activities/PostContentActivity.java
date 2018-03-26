package com.example.junyoung.dropchecker.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junyoung.dropchecker.R;
import com.example.junyoung.dropchecker.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostContentActivity extends AppCompatActivity {
    private static final int PAYPAL_REQUEST_CODE = 888;
    private String price;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    private String amount;

    @BindView(R.id.button_postcontent_purchase) Button buttonPurchase;
    @BindView(R.id.textview_postcontent_size) TextView textViewSize;
    @BindView(R.id.textview_postcontent_price) TextView textViewPrice;
    @BindView(R.id.textview_postcontent_title) TextView textViewTitle;
    @BindView(R.id.textview_postcontent_content) TextView textViewContent;
    @BindView(R.id.textview_postcontent_username) TextView textViewUserName;
    @BindView(R.id.imageview_postcontent_userphoto) ImageView imageViewUserPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcontent_sell);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String pageTitle = bundle.getString("pageTitle");
            String content = bundle.getString("content");
            String size = bundle.getString("size");
            price = bundle.getString("price");
            String subject = bundle.getString("subject");
            String userName = bundle.getString("userName");
            String userPhotoUrlString = bundle.getString("userPhotoUrlString");

            if (pageTitle.equals("BUY")) {
                buttonPurchase.setVisibility(View.GONE);
            }
            textViewSize.setText(size);
            textViewPrice.setText(price);
            textViewTitle.setText(subject);
            textViewContent.setText(content);
            textViewUserName.setText(userName);
            if (userPhotoUrlString == null) {
                Picasso.with(this).load(R.drawable.if_user_male3_172627).into(imageViewUserPhoto);
            }
            Picasso.with(this).load(userPhotoUrlString).into(imageViewUserPhoto);
        }

        Intent payPalIntent = new Intent(this, PayPalService.class);
        payPalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        this.startService(payPalIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(this, PayPalService.class));
    }

    @OnClick(R.id.button_postcontent_purchase)
    public void purchase() {
        processPayment();
    }

    private void processPayment() {
        amount = price.replaceAll("[^0-9]", "");
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),
                "USD",
                "Purchasing Clothes",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, PaymentDetailsActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}
