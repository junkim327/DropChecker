package com.example.junyoung.dropchecker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.junyoung.dropchecker.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentDetailsActivity extends AppCompatActivity {
    @BindView(R.id.textview_payment_details_paymentid) TextView textviewTextId;
    @BindView(R.id.textview_payment_details_amount) TextView textviewAmount;
    @BindView(R.id.textview_payment_details_status) TextView textviewStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shows the transaction result details.
     *
     * @param response the transaction result such as, transaction id and state.
     * @param paymentAmount the payment amount.
     */
    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            textviewTextId.setText(response.getString("id"));
            textviewStatus.setText(response.getString("state"));
            textviewAmount.setText(("$" + paymentAmount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
