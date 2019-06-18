package com.example.busapp20;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import Config.Config;       // We store here our Key

//  ******************************************************************************************* //
/// ***** TOPUP ACTIVITY USES PAYPAL APIS TO ALLOW USERS TO LOAD MONEY ON THEIR e-WALLET.       //
///                           THIS IS PAYPAL IMPLEMENTATION IN - APP   ************************ //
//  ******************************************************************************************* //

public class TopupActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration() // PAYPAL object initialization
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)   // We are using sandbox because we don't want to actually pay during demos
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    EditText edtAmount;

    String amount = "";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));

        /// It makes impossible to get back to a previous state of this activity when you move focus.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ///START Paypal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btnPayNow = findViewById(R.id.btnPayNow);
        edtAmount = findViewById(R.id.edtAmount);


        /// Setting TopUp Button Listener to start Payment process
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = edtAmount.getText().toString();

                // Check if you are trying to topUp a negative amount.
                if (amount.isEmpty() || Integer.valueOf(amount) < 1) {
                    AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(TopupActivity.this);
                    AlertBuilder.setMessage("Please insert a valid amount.");
                    AlertBuilder.setTitle("Error");
                    AlertBuilder.setCancelable(true);
                    AlertBuilder.setPositiveButton("Close", new DialogInterface
                            .OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = AlertBuilder.create();
                    alertDialog.show();
                } else {

                    // If you are clever enough not to try lose your money you can go ahead.
                    processPayment();

                }
            }
        });
    }

    private void processPayment() {
        PayPalPayment paypalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "EUR",
                "Put money in your wallet", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, paypalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    /// We check if everything has gone all right!
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                /// Code to get our previous balance from SharedPreferences
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                float myPrevBalance = prefs.getFloat("Balance", 0);
                //

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {     // We make sure we get infos from PPal!
                    try {
                        // We create a JSON to move our infos
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        /// Updating Balance on SharedPreferences
                        float newBalance = myPrevBalance + new Float(Double.valueOf(amount));
                        float newFloatBalance = Float.valueOf(newBalance);

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putFloat("Balance", newFloatBalance);
                        editor.apply();
                        // New Data Applied

                        /// An activity with your payment data is shown.
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );
                    } catch (JSONException e) {
                        // Exception handling
                        e.printStackTrace();
                        // We print StackTrace in case of errors
                    }
                }
                finish();
            } else if (resultCode == Activity.RESULT_CANCELED)  // IF USER ABORTS?
                Toast.makeText(this, "Ricarica Cancellata", Toast.LENGTH_SHORT).show();     //Toast
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)     //IF DATA IS INVALID?
            Toast.makeText(this, "Ricarica Fallita", Toast.LENGTH_SHORT).show();            //Toast
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}