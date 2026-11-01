package com.sysnova.cbfdailyproductionapp.Activity;

import static com.sysnova.cbfdailyproductionapp.Activity.Login.employeecode;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.employeename;
import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sysnova.cbfdailyproductionapp.Database.DatabaseHelper;
import com.sysnova.cbfdailyproductionapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Menu extends AppCompatActivity {
    Button dataentrybtn, logoutbtn, cng_pass, sendreports, geolocbtn, farmerinfobtn, IFTF_Request_btn,IFTF_Send_btn,
            IFTF_Received_btn;
    DatabaseHelper mDatabaseHelper;
    TextView nametextvw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

   //     getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        dataentrybtn = findViewById(R.id.data_entrybutton_id);
        nametextvw = findViewById(R.id.name_textvw);
        logoutbtn = findViewById(R.id.log_out);
        cng_pass = findViewById(R.id.change_password);
        sendreports = findViewById(R.id.send_button_id);
        geolocbtn = findViewById(R.id.farmer_geoloc_button_id);
        farmerinfobtn = findViewById(R.id.farmer_info_button_id);
        IFTF_Request_btn = findViewById(R.id.IFTF_Request_button_id);
        IFTF_Send_btn= findViewById(R.id.IFTF_Send_button_id);
        IFTF_Received_btn=findViewById(R.id.IFTF_Received_button_id);

        nametextvw.setText(employeename+"\n"+employeecode);

        mDatabaseHelper = new DatabaseHelper(this);

        clickDataEntryButton();
        clicksendreportsButton();
        clicklogout();
        clickChangePassword();
        clickgolocbtn();
        clickfarmerinfobtn();
        clickiftfreqbtn();
        clickiftfsendbtn();
        clickiftfrecbtn();
    }

    private void clickDataEntryButton() {
        dataentrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityintent = new Intent(Menu.this, DataEntryActivity.class);
                startActivity(activityintent);
            }
        });
    }
    private void clicksendreportsButton() {
        sendreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityintent = new Intent(Menu.this, ReportList.class);
                startActivity(activityintent);
            }
        });
    }

    private void clickChangePassword() {
        cng_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityintent = new Intent(Menu.this, ChangePassword.class);
                startActivity(activityintent);
            }
        });
    }
    private void clicklogout() {
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent activityintent = new Intent(Menu.this, Login.class);
                startActivity(activityintent);
            }
        });
    }

    private void clickgolocbtn() {
        geolocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityintent = new Intent(Menu.this, GeoLocationCollection.class);
                startActivity(activityintent);
            }
        });
    }
    private void clickfarmerinfobtn() {
        farmerinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityintent = new Intent(Menu.this, FarmerInfoActivity.class);
                startActivity(activityintent);
            }
        });
    }
    private void clickiftfreqbtn(){
        IFTF_Request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Menu.this,IFTFRequestActivity.class);
                startActivity(intent);
            }
        });
    }
    private void clickiftfsendbtn(){
        IFTF_Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Menu.this, IFTFRequestSendActivity.class);
                startActivity(intent);
            }
        });
    }
    private void clickiftfrecbtn(){
        IFTF_Received_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Menu.this, IFTFRequestReceivedActivity.class);
                startActivity(intent);
            }
        });
    }


    public boolean internetIsConnected() {
        try {
            String command = "ping -w 1 -c 1 172.16.253.10";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkVPN(){
        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {}
        return networkList.contains("tun0");
    }
    public void ShowAlert(String title,String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(Menu.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.image);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="",a="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null) {
                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e) {}
        return a;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i=new Intent(Menu.this, Login.class);
        startActivity(i);
    }
    public void toastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}