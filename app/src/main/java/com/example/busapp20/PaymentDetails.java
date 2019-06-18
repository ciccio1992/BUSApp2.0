package com.example.busapp20;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


///   Activity shown on successful payment reporting data about the transaction.
public class PaymentDetails extends AppCompatActivity {

    TextView txtId, txtAmount, txtStatus;
    Button btPaymentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);
        btPaymentHome = findViewById(R.id.btPaymentHome);


        //Get Intent
        Intent intent = getIntent();


        /// We get our JSON from Topup Activity
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btPaymentHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    /// Show payment data on UI. FYI
    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText(paymentAmount + " € ");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
