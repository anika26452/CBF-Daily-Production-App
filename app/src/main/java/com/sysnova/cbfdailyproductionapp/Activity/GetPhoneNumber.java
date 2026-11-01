package com.sysnova.cbfdailyproductionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.View;
import android.widget.Button;

import com.sysnova.cbfdailyproductionapp.R;

import java.util.List;

public class GetPhoneNumber extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        btn = findViewById(R.id.btn);

        String[] permission = {
                Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, 102);
        }
    }
    public void GetNumber(View v) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetPhoneNumber.this, new String[]{Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.READ_PHONE_STATE}, 0);
            return;
        }
        List<SubscriptionInfo> subsInfoList = (List<SubscriptionInfo>) subscriptionManager.getActiveSubscriptionInfoList();
        StringBuilder number = new StringBuilder();
        for (SubscriptionInfo subscriptionInfo : subsInfoList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                number.append(subscriptionManager.getPhoneNumber(subscriptionInfo.getSubscriptionId())).append("\n");
            } else {
                number.append(subscriptionInfo.getCarrierName()).append("\n");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (subscriptionInfo != null) {
                        String t = subscriptionInfo.getNumber();
                        System.out.println(t);
                    }
                }
            }

        }
        String tt = number.toString();
        btn.setText("Mobile Number = " + number.toString());
    }
}