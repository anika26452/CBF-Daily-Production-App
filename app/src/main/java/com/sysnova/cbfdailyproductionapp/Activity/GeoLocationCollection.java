package com.sysnova.cbfdailyproductionapp.Activity;

import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sysnova.cbfdailyproductionapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;


public class GeoLocationCollection extends AppCompatActivity {

    EditText farmerCodeedit, villageedit, post_ofcedit, post_code, thanaedit, districsedit;
    String farmerCode ="", farmername="", farmeraddress="", villagetext="", postofctext="", postcodetext="", thanatext="", districttext="";
    GPSTracker gps;
    TextView latvw, longvw, namevw, addressvw;
    ProgressDialog pd;
    double latitude,  longitude;
    Button set_loc_btn;
    ImageView fcodevw;
    private MapView mapView;
    Marker startMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE));

        setContentView(R.layout.activity_geo_location_collection);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        farmerCodeedit = findViewById(R.id.code_id);
        latvw = findViewById(R.id.lat_text);
        longvw = findViewById(R.id.long_text);
        set_loc_btn = findViewById(R.id.set_loc_btn_id);
        namevw = findViewById(R.id.farmers_name);
        addressvw = findViewById(R.id.farmer_address);
        fcodevw = findViewById(R.id.fcode_btnvw);
        villageedit = findViewById(R.id.village_id);
        post_ofcedit = findViewById(R.id.post_ofc_id);
        post_code = findViewById(R.id.postcode_id);
        thanaedit = findViewById(R.id.thana_id);
        districsedit = findViewById(R.id.district_id);

        set_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                villagetext = villageedit.getText().toString();
                postofctext = post_ofcedit.getText().toString();
                postcodetext = post_code.getText().toString();
                thanatext = thanaedit.getText().toString();
                districttext = districsedit.getText().toString();

                boolean vpn = true;
                if(vpn) {
                    AsyncUpdateLocationtwo asyncSendToServer = new AsyncUpdateLocationtwo();
                    asyncSendToServer.execute();
                }else{
                    showAlert("Warning","Please Check Vpn is Connected");
                }
            }
        });
    }

    public void loadfarmer(View view){
        farmerCode = farmerCodeedit.getText().toString();

        AsyncSyncDetail asyncSyncBatch = new AsyncSyncDetail();
        asyncSyncBatch.execute();
    }

    public void loadlocation(){

        gps = new GPSTracker(GeoLocationCollection.this);


        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if(latitude<=0){
                toastMessage("Latitude and longitude not found , try again");
                return;
            }
            latvw.setText(String.valueOf(latitude));
            longvw.setText(String.valueOf(longitude));

            GeoPoint startPoint = new GeoPoint(latitude, longitude);
            IMapController mapController = mapView.getController();
            mapController.setZoom(15.0);
            mapController.setCenter(startPoint);

            if (startMarker != null) {
                mapView.getOverlays().remove(startMarker);
            }

            startMarker = new Marker(mapView);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setTitle("You are here");
            mapView.getOverlays().add(startMarker);

        } else {
            gps.showSettingsAlert();
        }
    }

    private final class AsyncSyncDetail extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(GeoLocationCollection.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Details are Loading ... ...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            boolean res = false;
            String result = "";
            HttpURLConnection con = null;

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "farmerdetail"));
                list.add(new BasicNameValuePair("farmercode", farmerCode));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);

                if (s.isEmpty() || !s.startsWith("[")){
                    result=s;
                    return result;
                }

                ArrayList list4=new ArrayList();

                String[] parts = s.split("_");
                for(int i=0;i<parts.length;i++){
                    Vector line1=new Vector();

                    String[] parts1 = parts[i].split(",");
                    for(int j=0;j<parts1.length;j++){
                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                        value=value.replaceAll("[\\[\\]]","");
                        line1.add(value);

                    }
                    list4.add(line1);
                }
                for(int i1=0;i1<list4.size();i1++){
                    Vector line= (Vector) list4.get(i1);
                    farmername=(String) line.get(0);
                    farmeraddress=(String) line.get(1);

                    res = true;
                }
                if (res){
                    result = "Sync-Successful";
                }else{
                    result = "No batch Found";
                }
            }catch (Exception e){
                result = e.getMessage();
            }finally {
                if(con!= null) {
                    con.disconnect();
                    con = null;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try{
                if(s.equals("Sync-Successful")){
                    namevw.setText(farmername);
                    addressvw.setText(farmeraddress.replace('-', ','));
                    loadlocation();
                }else {
                    toastMessage("No Farmer Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private final class AsyncUpdateLocation extends AsyncTask<String,String,String> {

        String batchId = "";
        String result = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "Fail";
            HttpURLConnection con = null;
            boolean isCommit = false;

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/InsertData")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/InsertData");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "iftfreqrecsendtoserver"));
                list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
                list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
                list.add(new BasicNameValuePair("villagetext", villagetext));
                list.add(new BasicNameValuePair("postofctext", postofctext));
                list.add(new BasicNameValuePair("postcodetext", postcodetext));
                list.add(new BasicNameValuePair("thanatext", thanatext));
                list.add(new BasicNameValuePair("districttext", districttext));
                list.add(new BasicNameValuePair("farmerCode", farmerCode));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);

                if (s.isEmpty() || !s.equals("Successful")){
                    result=s;
                    return result;
                } else if(s.equals("Successful")){
                    result=  "Updated Successfully";
                }
            }catch (Exception ex){
                result = ex.getMessage();
            }finally {
                if(con != null){
                    con.disconnect();
                    con = null;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("Updated Successfully")){
                    toastMessage("Location Saved Successfully!");
                    farmerCodeedit.setText("");
                    namevw.setText("");
                    addressvw.setText("");
                    latvw.setText("");
                    longvw.setText("");
                    villageedit.setText("");
                    post_ofcedit.setText("");
                    post_code.setText("");
                    thanaedit.setText("");
                    districsedit.setText("");
                    set_loc_btn.setEnabled(true);
                }else {
                    showAlert("Error",s);
                    namevw.setText("");
                    addressvw.setText("");
                    farmerCodeedit.setText("");
                    latvw.setText("");
                    longvw.setText("");
                    set_loc_btn.setEnabled(true);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private final class AsyncUpdateLocationtwo extends AsyncTask<String,String,String> {

        String batchId = "";
        String result = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "Fail";
            boolean isCommit = false;

            try{
                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesOrder")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesOrder");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "salesorder"));
                list.add(new BasicNameValuePair("farmerCode", farmerCode));
                list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
                list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
                list.add(new BasicNameValuePair("villagetext",villagetext));
                list.add(new BasicNameValuePair("postofctext", postofctext));
                list.add(new BasicNameValuePair("postcodetext", postcodetext));
                list.add(new BasicNameValuePair("thanatext", thanatext));
                list.add(new BasicNameValuePair("districttext", districttext));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);

                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                } else {
                    return "Success";
                }

            } catch (Exception e){
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("complete")){
                    toastMessage("Location Saved Successfully!");
                    farmerCodeedit.setText("");
                    namevw.setText("");
                    addressvw.setText("");
                    latvw.setText("");
                    longvw.setText("");
                    villageedit.setText("");
                    post_ofcedit.setText("");
                    post_code.setText("");
                    thanaedit.setText("");
                    districsedit.setText("");
                    set_loc_btn.setEnabled(true);
                }else {
                    showAlert("Error",s);
                    namevw.setText("");
                    addressvw.setText("");
                    farmerCodeedit.setText("");
                    latvw.setText("");
                    longvw.setText("");
                    set_loc_btn.setEnabled(true);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public boolean checkVPN(){

        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {
        }

        return networkList.contains("tun0");
    }

    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(GeoLocationCollection.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(GeoLocationCollection.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading.....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        pd.show();
    }

    private void dismissProgressDialog(){
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        Intent intent = new Intent(GeoLocationCollection.this,Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="",a="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null){
                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e){}
        return a;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume(); // needed for compass, my location overlays, etc.
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();  // needed for compass, my location overlays, etc.
    }
}
