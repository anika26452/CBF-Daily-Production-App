package com.sysnova.cbfdailyproductionapp.Activity;

import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.cbfdailyproductionapp.Adapter.BatchDetailsRecyclerViewAdapter;
import com.sysnova.cbfdailyproductionapp.Adapter.FeedDetailsRecyclerViewAdapter;
import com.sysnova.cbfdailyproductionapp.Database.DatabaseHelper;

import com.sysnova.cbfdailyproductionapp.Model.ViewBatchDetailsModal;
import com.sysnova.cbfdailyproductionapp.Model.ViewFeedDetailsModal;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class FarmerInfoActivity extends AppCompatActivity {

    ProgressDialog pd;
    EditText farmercodeedittext;
    String farmercodeval, farmer_name;
    List<String> batchidArrayList = new ArrayList<>();
    HashMap<String,Integer> hashBatchcodeID = new HashMap<>();
    DatabaseHelper sqliteDbHelper;
    TextView farmername, birdqty, startdate;
    Spinner batchListSpinner;
    RecyclerView recyclerView, feedrecycleview;
    int batchId, abc= 0;
    ArrayList<ViewBatchDetailsModal> batchdetailsmodal = new ArrayList<>();
    ArrayList<ViewFeedDetailsModal> feeddetailsmodal = new ArrayList<>();
    TextView tmortalityvw, tfeed1vw, tfeed2vw, tfeed3vw;
    LinearLayout feedsmrylayout;
    private static final DecimalFormat decfor = new DecimalFormat("0");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_info);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

        recyclerView = findViewById(R.id.bird_list_recyclerview);
        farmercodeedittext = findViewById(R.id.farmers_code_id);
        farmername = findViewById(R.id.fname);
        batchListSpinner = findViewById(R.id.batch_idlist_spinner);
        birdqty = findViewById(R.id.bqty);
        startdate = findViewById(R.id.sdate);
        tmortalityvw = findViewById(R.id.totalmortality);
        tfeed1vw = findViewById(R.id.totalfeed1);
        tfeed2vw = findViewById(R.id.totalfeed2);
        tfeed3vw = findViewById(R.id.totalfeed3);
        feedsmrylayout = findViewById(R.id.feedsummarylayout);
        feedrecycleview = findViewById(R.id.feed_list_recyclerview);

        batchListSpinner.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
        sqliteDbHelper = new DatabaseHelper(this);
    }

    public void loadFarmerInfo(View view){
        farmercodeval = farmercodeedittext.getText().toString();
        batchidArrayList.clear();
        hashBatchcodeID.clear();
        sqliteDbHelper.deleteBatchCodeTable();
        initRecyclerView();
        initRecyclerView2();

        boolean vpn = true;
        if(vpn) {
            AsyncSyncBatchCode asyncSyncBatch = new AsyncSyncBatchCode();
            asyncSyncBatch.execute();
        }else{
            showAlert("Warning","Please Check Vpn is Connected");
        }
    }
/*
    private final class AsyncSyncBatchCode extends AsyncTas<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            PostgresqlConnection postgresqlConnection = new PostgresqlConnection();
            Connection connection = postgresqlConnection.getConn();
            if(connection != null){
                try{
                    Statement statement = connection.createStatement();
                    String getQuestion_query = "select coalesce((select bpname from t_customer_kfg tck where bpcode = mb.bpcode limit 1), '') as farmer_name, value, m_batch_id  from m_batch mb where bpcode = '"+farmercodeval+"' "; //and isclosed = 'N'
                    ResultSet resultSet = statement.executeQuery(getQuestion_query);
                    while (resultSet.next()){
                        String batch    = resultSet.getString("value");
                        int batchID     = resultSet.getInt("m_batch_id");
                        farmer_name    = resultSet.getString("farmer_name");
                        hashBatchcodeID.put(batch,batchID);
                        batchidArrayList.add(batch);
                        sqliteDbHelper.insertIntoBatchCodeDetails(batch,batchID);
                        result = "Sync-Successful";
                    }

                }catch (SQLException ex){
                    result = ex.getMessage();
                }finally {
                    if(connection != null){
                        try{
                            connection.close();
                        }catch (SQLException ex){
                            result = ex.getMessage();
                        }
                    }
                }
            }else{
                result = "Check Port,hostname is correct";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("Sync-Successful")){
                    farmername.setText("Farmer's Name : "+farmer_name);
                    loadBatchSpinner();
                    getSelectedSpinnerItem();
                }else {
                    loadBatchSpinner();
                    toastMessage("No Batch Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    */

    private final class AsyncSyncBatchCode extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            HttpURLConnection con = null;
            boolean res = false;

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "farmerBatchCode"));
                list.add(new BasicNameValuePair("farmercodeval", farmercodeval));

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

                    String batch = (String) line.get(0);
                    int batchID  = Integer.parseInt((String) line.get(1));
                    farmer_name    = (String) line.get(2);

                    hashBatchcodeID.put(batch,batchID);
                    batchidArrayList.add(batch);

                    res = sqliteDbHelper.insertIntoBatchCodeDetails(batch,batchID);
                }
                if (res){
                    result = "Sync-Successful";
                }else{
                    result = "No batch Found";
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
                if(s.equals("Sync-Successful")){
                    farmername.setText("Farmer's Name : "+farmer_name);
                    loadBatchSpinner();
                    getSelectedSpinnerItem();
                }else {
                    loadBatchSpinner();
                    toastMessage("No Batch Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(FarmerInfoActivity.this);
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

    private void loadBatchSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,batchidArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchListSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void getSelectedSpinnerItem(){

        batchListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedBatchCode = parent.getSelectedItem().toString();
//                    batchName = parent.getSelectedItem().toString();
                    batchId = hashBatchcodeID.get(selectedBatchCode);
                //    toastMessage(selectedBatchCode+"========="+batchId);
                    AsyncLoadBird task1 = new AsyncLoadBird();
                    task1.execute();
                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(FarmerInfoActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(FarmerInfoActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
/*
    private final class AsyncLoadBird extends AsyncTas<String,String,String> {

        int Qty = 0;
        int Age = 0;
        String productionDate  = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            if(connection != null){
                try{
                    Statement statement = connection.createStatement();

                    String getDataFromCbfDailyData = "select birdnumber,productiondate from t_cbfdailyData where batch = '"+strings[0]+"' order by t_cbfdailyData_id limit 1";
                    ResultSet resultSet0 = statement.executeQuery(getDataFromCbfDailyData);
                    while (resultSet0.next()){
                        Qty        = resultSet0.getInt("birdnumber");
                        productionDate = resultSet0.getString("productiondate");
                        result = "Bird-Sync-Successful";

                        if(Qty == 0){
                            result = "Batch Closed";
                        }
                    }

                }catch (SQLException ex){
                    result = ex.getMessage();
                }finally {
                    if(connection != null){
                        try{
                            connection.close();
                        }catch (SQLException ex){
                            result = ex.getMessage();
                        }
                    }
                }
            }else{
                result = "Check Port,hostname is correct";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("Bird-Sync-Successful")){
                    toastMessage(s);
                    birdqty.setText("Bird QTY            : "+String.valueOf(Qty));
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse( productionDate ) );
                        cal.add( Calendar.DATE, 0 );
                        productionDate = sdf.format(cal.getTime());
                        startdate.setText("Start Date          : "+productionDate);
                    }catch (Exception ex){
                        toastMessage("Date Format Exception");
                    }
                    try {
                        sqliteDbHelper.deleteBatchDetails();
                    } catch (Exception e) {
                        System.out.println("Kuch to garbar hai");
                    }

                    AsyncLoadBatchDetails task1 = new AsyncLoadBatchDetails();
                    task1.execute(String.valueOf(batchId));
                } else {
                    toastMessage("No Bird Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    */

    private final class AsyncLoadBird extends AsyncTask<String,String,String> {

        int Qty = 0;
        int Age = 0;
        String productionDate  = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            HttpURLConnection con = null;
            boolean res = false;

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "farmerLoadBird"));
                list.add(new BasicNameValuePair("batchId", String.valueOf(batchId)));

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
                    Qty        = Integer.parseInt((String) line.get(0));
                    productionDate = (String) line.get(1);

                    res = true;

                }
                if (res){
                    result = "Bird-Sync-Successful";
                    if(Qty == 0){
                        result = "Batch Closed";
                    }
                }else{
                    result = "No bird Found";
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
                if(s.equals("Bird-Sync-Successful")){
                    toastMessage(s);
                    birdqty.setText("Bird QTY            : "+String.valueOf(Qty));
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse( productionDate ) );
                        cal.add( Calendar.DATE, 0 );
                        productionDate = sdf.format(cal.getTime());
                        startdate.setText("Start Date          : "+productionDate);
                    }catch (Exception ex){
                        toastMessage("Date Format Exception");
                    }
                    try {
                        sqliteDbHelper.deleteBatchDetails();
                    } catch (Exception e) {
                        System.out.println("Kuch to garbar hai");
                    }

                    AsyncLoadBatchDetails task1 = new AsyncLoadBatchDetails();
                    task1.execute();
                } else {
                    toastMessage("No Bird Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        Intent intent = new Intent(FarmerInfoActivity.this,Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    private final class AsyncLoadBatchDetails extends AsyncTask<String,String,String> {

        String batch  = "", productiondate = "",birdage = "", birdnumber= "", mortality= "", transitmortality="", feedqty1="", feedqty2="", feedqty3="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            boolean res = false;
            String result = "";
            HttpURLConnection con = null;
            if(!batchdetailsmodal.isEmpty()) {
                batchdetailsmodal.clear();
            }

            try{
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "LoadBatchDetails"));
                list.add(new BasicNameValuePair("batchId", String.valueOf(batchId)));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                String s = readResponse(httpResponse);

                if (s.isEmpty() || !s.startsWith("[")) {
                    result = s;
                    return result;
                }

                ArrayList list4 = new ArrayList();

                String[] parts = s.split("_");
                for (int i = 0; i < parts.length; i++) {
                    Vector line1 = new Vector();

                    String[] parts1 = parts[i].split(",");
                    for (int j = 0; j < parts1.length; j++) {
                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                        value = value.replaceAll("[\\[\\]]", "");
                        line1.add(value);

                    }
                    list4.add(line1);
                }

                for (int i1 = 0; i1 < list4.size(); i1++) {
                    Vector line = (Vector) list4.get(i1);

                    batch        = (String) line.get(0);
                    productiondate = (String) line.get(1);
                    birdage = (String) line.get(2);
                    birdnumber = (String) line.get(3);
                    mortality = (String) line.get(4);
                    transitmortality = (String) line.get(5);
                    feedqty1 = (String) line.get(6);
                    feedqty2 = (String) line.get(7);
                    feedqty3 = (String) line.get(8);

                    abc++;

                    res = sqliteDbHelper.insertIntoBatchDetailsTable(Double.valueOf(mortality)+Double.valueOf(transitmortality), Double.valueOf(feedqty1), Double.valueOf(feedqty2), Double.valueOf(feedqty3));
                    ViewBatchDetailsModal orderListModal = new ViewBatchDetailsModal(batch,productiondate,birdage,birdnumber,mortality, transitmortality, feedqty1, feedqty2, feedqty3);
                    batchdetailsmodal.add(orderListModal);
                }

                if(res){
                    result = "Insert Successfully";
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
                if(s.equals("Insert Successfully")){
                    initRecyclerView();
                    gettotalmortality();
                    feedsummary();
                } else {
                    toastMessage("No Bird Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        BatchDetailsRecyclerViewAdapter adapter = new BatchDetailsRecyclerViewAdapter(FarmerInfoActivity.this, batchdetailsmodal);
        recyclerView.setAdapter(adapter);
    }

    public void initRecyclerView2() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        feedrecycleview.setLayoutManager(linearLayoutManager);
        FeedDetailsRecyclerViewAdapter adapter = new FeedDetailsRecyclerViewAdapter(FarmerInfoActivity.this, feeddetailsmodal);
        feedrecycleview.setAdapter(adapter);
    }

    public void gettotalmortality(){
        Cursor cursor = sqliteDbHelper.getTotalMortality();
        if (cursor.moveToFirst()) {
            do {
                tmortalityvw.setText(cursor.getString(0));
                tfeed1vw.setText(cursor.getString(1));
                tfeed2vw.setText(cursor.getString(2));
                tfeed3vw.setText(cursor.getString(3));
            } while (cursor.moveToNext());

        } else {
            System.out.println("Not Found");
        }
    }

    public void feedsummary(){
        AsyncLoadFeedSummary task1 = new AsyncLoadFeedSummary();
        task1.execute();
    }

    private final class AsyncLoadFeedSummary extends AsyncTask<String,String,String> {

        String pcode  = "";
        Double issueqty= 0.0, remainingqty = 0.0,recieveqty = 0.0;
        List<String> feedArrayList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            boolean res = false;
            String result = "";
            HttpURLConnection con = null;
            if(!feeddetailsmodal.isEmpty()) {
                feeddetailsmodal.clear();
            }
            try{
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "LoadFeedSummary"));
                list.add(new BasicNameValuePair("batchId", String.valueOf(batchId)));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                String s = readResponse(httpResponse);

                if (s.isEmpty() || !s.startsWith("[")) {
                    result = s;
                    return result;
                }

                ArrayList list4 = new ArrayList();

                String[] parts = s.split("_");
                for (int i = 0; i < parts.length; i++) {
                    Vector line1 = new Vector();

                    String[] parts1 = parts[i].split(",");
                    for (int j = 0; j < parts1.length; j++) {
                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                        value = value.replaceAll("[\\[\\]]", "");
                        line1.add(value);

                    }
                    list4.add(line1);
                }

                for (int i1 = 0; i1 < list4.size(); i1++) {
                    Vector line = (Vector) list4.get(i1);

                    pcode        = (String) line.get(0);
                    remainingqty =Double.parseDouble((String) line.get(1));
                    recieveqty = Double.parseDouble((String) line.get(2));
                    issueqty = Double.parseDouble((String) line.get(3));
                    feedArrayList.add(pcode);

                    ViewFeedDetailsModal orderListModal = new ViewFeedDetailsModal(pcode,remainingqty,recieveqty,issueqty);
                    feeddetailsmodal.add(orderListModal);
                    res =true;
                }

                if(res){
                    result = "Inserted Successfully";
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
                if(s.equals("Inserted Successfully")){
                    initRecyclerView2();
                } else {
                    toastMessage("No Feed Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
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
        } catch (Exception e) {}
        return a;
    }
}



