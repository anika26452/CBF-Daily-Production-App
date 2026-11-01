package com.sysnova.cbfdailyproductionapp.Activity;

import static com.sysnova.cbfdailyproductionapp.Activity.Login.ad_user_id;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.backdays;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.uName;
import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ReportDetailsActivity extends AppCompatActivity {

    int ID;
    String userID, selectedMortReason, noofbirds, totalbirds, diseasename;
    TextView farmerCodeEditText,birdNumberEditText,birdAgeEditText,feedAmtEditText1,feedAmtEditText2,feedAmtEditText3,
            mortalityEditText,suggestionEditText,visitDateEditText,productionDateEditText,transitMortalityEditText,
            reportCreatedTextView, mortalityreasonvw, noofbirdsvw, totalwtvw, avrwtvw, diseasevw, farmernamevw;
    Button confirmReportBtn,deleteReportBtn;
    Spinner batchCodeSpinner,feedCodeSpinner1,feedCodeSpinner2,feedCodeSpinner3;
    LinearLayout translayoutvw, nobirdlinearlayoutvw;

    DatabaseHelper sqliteDbHelper;

    String farmerCode="", farmerName="", batchCode="",visitDate = "",inTime="",birdNumber="",birdAge="",birdMortality="",birdWeight="",feedCode1="",feedCode2="",feedCode3="",feedQty1="",
            feedQty2="",feedQty3="",outTime="",suggestion = "",productionDate = "",reportCreated = "",transitMortality = "",c_bpartner_id = "",
            latitude = "", longitude = "";

    int batch_id = 0;

    String feedId1 ="0",feedId2 ="0",feedId3 ="0";
    List<String> feedCodeArrayList = new ArrayList<>();
    List<String> feedQtyArrayList = new ArrayList<>();
    List<String> batchArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

        farmerCodeEditText = findViewById(R.id.report_farmer_code);

        batchCodeSpinner = findViewById(R.id.report_batch_list_spinner);

        birdNumberEditText  = findViewById(R.id.report_bird_number);
        birdAgeEditText  = findViewById(R.id.report_bird_age);
        feedCodeSpinner1  = findViewById(R.id.report_feed_list_spinner1);
        feedCodeSpinner2  = findViewById(R.id.report_feed_list_spinner2);
        feedCodeSpinner3  = findViewById(R.id.report_feed_list_spinner3);
        feedAmtEditText1  = findViewById(R.id.report_feed_amt_1);
        feedAmtEditText2  = findViewById(R.id.report_feed_amt_2);
        feedAmtEditText3  = findViewById(R.id.report_feed_amt_3);
        mortalityEditText  = findViewById(R.id.report_bird_mortality_amt);
        suggestionEditText  = findViewById(R.id.report_suggestion);
        diseasevw = findViewById(R.id.disease);
        visitDateEditText = findViewById(R.id.report_visitDate);
        productionDateEditText = findViewById(R.id.report_productonDate);
        reportCreatedTextView  = findViewById(R.id.reportCreatedDateTextView_id);
        transitMortalityEditText =findViewById(R.id.report_bird_transit_mortality_amt);
        confirmReportBtn = findViewById(R.id.report_confirm_btn_id);
        deleteReportBtn  = findViewById(R.id.report_delete_btn_id);
        translayoutvw = findViewById(R.id.translayout);
        mortalityreasonvw = findViewById(R.id.mortalityreason);
        noofbirdsvw = findViewById(R.id.noofbirds);
        totalwtvw = findViewById(R.id.total_weight);
        avrwtvw = findViewById(R.id.avg_weight);
        farmernamevw = findViewById(R.id.report_farmer_name);
        nobirdlinearlayoutvw = findViewById(R.id.nobirdlinearlayout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            ID =(int) bundle.get("ID");
            userID = bundle.get("userID").toString();
        }

        sqliteDbHelper = new DatabaseHelper(this);

        buttonClick();
        loadReportData();
    }

    private void loadReportData(){

        List<String> feedCodeArrayList1 = new ArrayList<>();
        List<String> feedCodeArrayList2 = new ArrayList<>();
        List<String> feedCodeArrayList3 = new ArrayList<>();

        batchArrayList.clear();
        feedCodeArrayList.clear();
        feedQtyArrayList.clear();
        Cursor data = sqliteDbHelper.getReportDetails(ID);

        while(data.moveToNext()){

             farmerCode = data.getString(2);
             farmerName = data.getString(3);
             batchCode = data.getString(4);
             batchArrayList.add(data.getString(4));
             visitDate = data.getString(5);
             birdNumber = data.getString(6);
             birdAge = data.getString(7);
             birdMortality = data.getString(8);
             birdWeight = data.getString(9);
             feedCode1 = data.getString(10);
             feedCodeArrayList1.add(data.getString(10));
             feedCode2 = data.getString(11);
             feedCodeArrayList2.add(data.getString(11));
             feedCode3 = data.getString(12);
             feedCodeArrayList3.add(data.getString(12));
             feedQty1 = data.getString(13);
             feedQty2 = data.getString(14);
             feedQty3 = data.getString(15);
             inTime   = data.getString(16);   //actually batch_id
             outTime = data.getString(17);   //placementdate actullay
             suggestion = data.getString(18);
             productionDate = data.getString(19);
             reportCreated = data.getString(20);
             transitMortality      = data.getString(21);
             c_bpartner_id      = data.getString(22);
             latitude = data.getString(26);  //added  by Anika 21-10-2023
             longitude = data.getString(27);  //added  by Anika 21-10-2023
             selectedMortReason = data.getString(28);
             noofbirds = data.getString(29);
             totalbirds = data.getString(30);
             diseasename = data.getString(31);

             Cursor data1 = sqliteDbHelper.getFeedIdByCode(feedCode1);
             while(data1.moveToNext()){
                feedId1 = data1.getString(0);
             }

             Cursor data2 = sqliteDbHelper.getFeedIdByCode(feedCode2);
             while(data2.moveToNext()){
                feedId2 = data2.getString(0);
             }

             Cursor data3 = sqliteDbHelper.getFeedIdByCode(feedCode3);
             while(data3.moveToNext()){
                feedId3 = data3.getString(0);
             }

             Cursor cursor = sqliteDbHelper.getBatchID(batchCode);
             while (cursor.moveToNext()){
                 batch_id = cursor.getInt(0);
             }

             feedCodeArrayList.add(feedId1);
             feedCodeArrayList.add(feedId2);
             feedCodeArrayList.add(feedId3);
             feedQtyArrayList.add(feedQty1);
             feedQtyArrayList.add(feedQty2);
             feedQtyArrayList.add(feedQty3);
        }


        farmerCodeEditText.setText(farmerCode);
        farmernamevw.setText(farmerName);
        loadBatchSpinner(batchArrayList);
        visitDateEditText.setText(visitDate);

        transitMortalityEditText.setText(transitMortality);

        if(transitMortality.equals("0")){
            translayoutvw.setVisibility(View.GONE);
        }else{
            translayoutvw.setVisibility(View.VISIBLE);
        }
        birdNumberEditText.setText(birdNumber);
        birdAgeEditText.setText(birdAge);

        loadFeedSpinner1(feedCodeArrayList1);
        loadFeedSpinner2(feedCodeArrayList2);
        loadFeedSpinner3(feedCodeArrayList3);
        feedAmtEditText1.setText(feedQty1);
        feedAmtEditText2.setText(feedQty2);
        feedAmtEditText3.setText(feedQty3);
        mortalityEditText.setText(birdMortality);
        suggestionEditText.setText(suggestion);

        productionDateEditText.setText(outTime);
        mortalityreasonvw.setText(selectedMortReason);
        reportCreatedTextView.setText("Report Created By : "+userID+"\nCreated Time : "+reportCreated);
        noofbirdsvw.setText(noofbirds);
        totalwtvw.setText(totalbirds);
        diseasevw.setText(diseasename);

        if(noofbirds.equals("")){
            nobirdlinearlayoutvw.setVisibility(View.GONE);
        }else{
            nobirdlinearlayoutvw.setVisibility(View.VISIBLE);
        }

        if(!totalbirds.equals("") && !noofbirds.equals("")){
            Double totalwtint  = Double.valueOf(totalbirds);
            int noofbirdsint = Integer.parseInt(noofbirds);

            Double avdwtint = totalwtint/Double.valueOf(noofbirdsint);

            avrwtvw.setText( String.format(Locale.US, "%.2f", Math.round(avdwtint * 100.0) / 100.0));
        }
    }


    private void loadBatchSpinner(List<String> arrayList1){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,arrayList1);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchCodeSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void loadFeedSpinner1(List<String> feedArrayList1){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,feedArrayList1);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedCodeSpinner1.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        if(feedArrayList1.size()>1){
            if(feedCode1 != null){
                int spinnerPosition = dataAdapter.getPosition(feedCode1);
                feedCodeSpinner1.setSelection(spinnerPosition);
            }
        }
    }

    private void loadFeedSpinner2(List<String> feedArrayList2){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,feedArrayList2);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedCodeSpinner2.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        if(feedArrayList2.size() > 1){
            if(feedCode2 != null){
                int spinnerPosition = dataAdapter.getPosition(feedCode2);
                feedCodeSpinner2.setSelection(spinnerPosition);
            }
        }
    }

    private void loadFeedSpinner3(List<String> feedArrayList3){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,feedArrayList3);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedCodeSpinner3.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        if(feedArrayList3.size() > 1){
            if(feedCode3 != null){
                int spinnerPosition = dataAdapter.getPosition(feedCode3);
                feedCodeSpinner3.setSelection(spinnerPosition);
            }
        }
    }

    private void buttonClick(){

        confirmReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   confirmReportBtn.setEnabled(false);

                String batchCode = batchCodeSpinner.getSelectedItem().toString();

                ArrayList fList = new ArrayList();
                ArrayList fAmt = new ArrayList();

                String feedCode11 = feedCodeSpinner1.getSelectedItem().toString();
                String feedCode21 = feedCodeSpinner2.getSelectedItem().toString();
                String feedCode31 = feedCodeSpinner3.getSelectedItem().toString();
                String feedAmt11 = feedAmtEditText1.getText().toString();
                String feedAmt21 =  feedAmtEditText2.getText().toString();
                String feedAmt31 = feedAmtEditText3.getText().toString();

                if(!(feedCode11.equals("-- Select Feed --"))){
                    fList.add(feedCode11);
                    fAmt.add(feedAmt11);
                }
                if(!(feedCode21.equals("-- Select Feed --"))){
                    fList.add(feedCode21);
                    fAmt.add(feedAmt21);
                }
                if(!(feedCode31.equals("-- Select Feed --"))){
                    fList.add(feedCode31);
                    fAmt.add(feedAmt31);
                }

                for(int i = 0; i < fList.size(); i++){
                    AsyncLoadFeed task = new AsyncLoadFeed();
                    task.execute(batchCode,fList.get(i).toString(),fAmt.get(i).toString());
                }

                if(feedCode1.equals("-- Select Feed --")){
                    feedCode1 = "";
                }
                if(feedCode2.equals("-- Select Feed --")){
                    feedCode2 = "";
                }
                if(feedCode3.equals("-- Select Feed --")){
                    feedCode3 = "";
                }

         //       AsyncSendToServer asyncSendToServer = new AsyncSendToServer();
           //     asyncSendToServer.execute();

                //nicher line gula on korte hobe on krte hobe
                SendToServer asyncSendToServer = new SendToServer();
                asyncSendToServer.execute();

            }
        });

        deleteReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReport();
            }
        });
    }

    private void updateReport(){
        String farmerCode = farmerCodeEditText.getText().toString();
        String batchCode = batchCodeSpinner.getSelectedItem().toString();
        String birdNum = birdNumberEditText.getText().toString();
        String birdAge =  birdAgeEditText.getText().toString();
        String feedCode1 =  feedCodeSpinner1.getSelectedItem().toString();
        String feedCode2 =  feedCodeSpinner2.getSelectedItem().toString();
        String feedCode3 =  feedCodeSpinner3.getSelectedItem().toString();
        String feedAmt1 =   feedAmtEditText1.getText().toString();
        String feedAmt2 =   feedAmtEditText2.getText().toString();
        String feedAmt3 =   feedAmtEditText3.getText().toString();
        String mortality =  mortalityEditText.getText().toString();
        String weight    =  avrwtvw.getText().toString();
        String suggestion = suggestionEditText.getText().toString();
        boolean update = sqliteDbHelper.updateReport(String.valueOf(ID),farmerCode,batchCode,inTime,birdNum,birdAge,feedCode1,feedCode2

        ,feedCode3,feedAmt1,feedAmt2,feedAmt3,mortality,weight,suggestion,outTime);

        if(update){
            loadReportData();
        }
    }

    private void deleteReport(){
        sqliteDbHelper.deleteReport(ID);

        finish();
        Intent intent = new Intent(ReportDetailsActivity.this,ReportList.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        Intent intent = new Intent(ReportDetailsActivity.this,ReportList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    private final class AsyncLoadFeed extends AsyncTask<String,String,String> {

        String feedID = "";
        int m_feed_ID = 0;
        String batchId = "", result = "";
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pd == null){
                pd = new ProgressDialog(ReportDetailsActivity.this);
                pd.setTitle("Please Wait");
                pd.setMessage("Loading.....");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            }
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection con = null;
            String result = "";
            boolean res = false;
            List<String> feedArrayList = new ArrayList<>();
            feedArrayList.add("Select Feed");
            double feedQty = 0;

            try{
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "ReportLoadFeed"));
                list.add(new BasicNameValuePair("batchCode", strings[0]));
                list.add(new BasicNameValuePair("feedCode", strings[1]));

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
                    feedID    = (String) line.get(0);
                    feedQty = Double.parseDouble((String) line.get(1));
                    m_feed_ID   = Integer.parseInt((String) line.get(2));

                    res = true;
                }

                if(res){
                    result = "Feed-Sync-Successful";
                }
                if(Double.parseDouble(strings[2].toString()) > feedQty ){
                    result ="feed not found";
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
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            try{
                if(s.equals("feed not found")){
                    toastMessage("No available feed");
                    return;
                }else {

                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
/*
    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

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
            PostgresqlConnection postgresqlConnection = new PostgresqlConnection();
            Connection connection = postgresqlConnection.getConn();
            boolean isCommit = false;
            if(connection != null){
                try{
                    connection.setAutoCommit(false);

                    Statement statement1 = connection.createStatement();
                    int presentAge = Integer.parseInt(birdAge);
                    String getLastAge = "select birdage from t_cbfdailyData where batch = '"+String.valueOf(inTime)+"' order by birdage desc limit 1";
                    ResultSet resultSet = statement1.executeQuery(getLastAge);
                    if(resultSet.next()){
                        int previousAge = resultSet.getInt("birdage");
                        if((presentAge - previousAge) == 1){
                            //lat & long added  by Anika 21-10-2023
                            String inset_query = "insert into t_cbfdailyData (ad_user_id,bpcode,batch,intime,birdnumber,birdage,mortality,feedCode1,feedCode2,feedCode3,feedQty1,feedQty2,feedQty3,outtime,suggestion,visitdate,productiondate,transitmortality,appversion,userid,latitude, longitude) " +
                                    "values ('" + ad_user_id + "','" + c_bpartner_id + "','" + String.valueOf(inTime) + "','','" + birdNumber + "','" + birdAge + "','" + birdMortality + "','"  + feedId1 + "','" + feedId2 + "','" + feedId3 + "','" + feedQty1 + "','" + feedQty2 + "','" + feedQty3 + "','" + outTime + "','" + suggestion + "','" + visitDate + "','" + productionDate + "','" + transitMortality + "','"+getResources().getString(R.string.app_v)+"','"+uName+"','"+latitude+"','"+longitude+"')";

                            int res = statement1.executeUpdate(inset_query);
                            if (res == 1) {
                                result = "Insertion-Successful";

                                for (int i = 0; i < feedCodeArrayList.size(); i++) {
                                    //  System.out.println(feedQtyArrayList.get(i)+"====="+ feedCodeArrayList.get(i));
                                    String getM_batchLine_id = "select m_batchline_id from m_batchline where m_batch_id = '" + inTime + "' and m_product_id = '" + feedCodeArrayList.get(i) + "'  order by qtyonhand desc limit 1";
                                    ResultSet resultSet0 = statement1.executeQuery(getM_batchLine_id);
                                    if (resultSet0.next()) {
                                        int m_batchline_id = resultSet0.getInt("m_batchline_id");
                                        // System.out.println(Integer.parseInt(feedQtyArrayList.get(i))+"====="+m_batchline_id);
                                        String updateFeedQty = "update m_batchline set qtyonhand = qtyonhand - '" + Integer.parseInt(feedQtyArrayList.get(i)) + "' where m_batchline_id = '" + m_batchline_id + "'";
                                        int update = statement1.executeUpdate(updateFeedQty);
                                        if (update > 0) {
                                            result = "updated";
                                            isCommit = true;
                                        }else{
                                            isCommit = false;
                                            break;
                                        }
                                    }
                                }
                            }

                            if(isCommit){
                                connection.commit();
                                result = "complete";
                            }else {
                                try {
                                    connection.rollback();
                                } catch (SQLException e) {
                                    connection.rollback();
                                }
                            }
                        }else{
                            result = "Please Enter Previous Age Data";
                        }
                    }else if(birdAge.equals("1")){

                        //lat & long added  by Anika 21-10-2023

                        String insert_query = "insert into t_cbfdailyData (ad_user_id,bpcode,batch,intime,birdnumber,birdage,mortality,feedCode1,feedCode2,feedCode3,feedQty1,feedQty2,feedQty3,outtime,suggestion,visitdate,productiondate,transitmortality,appversion,userid,latitude, longitude) " +
                                "values ('" + ad_user_id + "','" + c_bpartner_id + "','" + String.valueOf(batch_id) + "','" + inTime + "','" + birdNumber + "','" + birdAge + "','" + birdMortality + "',0,'" + feedId1 + "','" + feedId2 + "','" + feedId3 + "','" + feedQty1 + "','" + feedQty2 + "','" + feedQty3 + "','" + outTime + "','" + suggestion + "','" + visitDate + "','" + productionDate + "','" + transitMortality + "','"+getResources().getString(R.string.app_v)+"','"+uName+"','"+latitude+"','"+longitude+"')";

                        int res = statement1.executeUpdate(insert_query);

                        if (res == 1) {
                            result = "Insertion-Successful";

                            for (int i = 0; i < feedCodeArrayList.size(); i++) {
                                String getM_batchLine_id = "select m_batchline_id from m_batchline where m_batch_id = '" + batch_id + "' and m_product_id = '" + feedCodeArrayList.get(i) + "'  order by qtyonhand desc limit 1";
                                ResultSet resultSet0 = statement1.executeQuery(getM_batchLine_id);
                                if (resultSet0.next()) {
                                    int m_batchline_id = resultSet0.getInt("m_batchline_id");
                                    String updateFeedQty = "update m_batchline set qtyonhand = qtyonhand - '" + Integer.parseInt(feedQtyArrayList.get(i)) + "' where m_batchline_id = '" + m_batchline_id + "'";
                                    int update = statement1.executeUpdate(updateFeedQty);
                                    if (update > 0) {
                                        result = "updated";
                                        isCommit = true;
                                    }else{
                                        isCommit = false;
                                        break;
                                    }
                                }
                            }
                        }

                        if(isCommit){
                            connection.commit();
                            result = "complete";
                        }else {
                            try {
                                connection.rollback();
                            } catch (SQLException e) {
                                connection.rollback();
                            }
                        }
                    }else{
                        result = "Enter Day One Data First";
                    }

                }catch (SQLException ex){
                    result = ex.getMessage();
                    try {
                        if (connection != null) {
                            connection.rollback();
                        }
                    }catch (Exception ex2){
                        result = ex2.getMessage();
                    }
                }finally {
                    if(connection != null){
                        try{
                            connection.close();
                            if(result == "complete"){
                                result = "complete";
                            }
                            if(result.equals("Please Enter Previous Age Data")){
                                result = "Please Enter Previous Age Data";
                            }
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
                if(s.equals("complete")){
                    toastMessage("Report Saved Successfully!");
                    deleteReport();
                }else {
                    showAlert("Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    */

    private final class SendToServer extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pd == null){
                pd = new ProgressDialog(ReportDetailsActivity.this);
                pd.setTitle("Please Wait");
                pd.setMessage("Loading.....");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            }
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "Fail";
            HttpURLConnection con = null;
            try{
                String headerString = "";
                for (Object s : feedCodeArrayList) {
                    if (headerString.isEmpty()) {
                        headerString =headerString+s;
                    } else {
                        headerString =headerString+"_" + s;
                    }
                }

                String lineString = "";
                for(Object s: feedQtyArrayList) {
                    if (lineString.isEmpty()) {
                        lineString =lineString+s;
                    } else {
                        lineString =lineString+"_" + s;
                    }
                }

                con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesStatusSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesStatusSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
             //   list.add(new BasicNameValuePair("type", "salesorder"));
                list.add(new BasicNameValuePair("batch", inTime));
                list.add(new BasicNameValuePair("ad_user_id", ad_user_id));
                list.add(new BasicNameValuePair("c_bpartner_id", c_bpartner_id));
                list.add(new BasicNameValuePair("birdNumber", birdNumber));
                list.add(new BasicNameValuePair("birdAge", birdAge));
                list.add(new BasicNameValuePair("birdMortality", birdMortality));
                list.add(new BasicNameValuePair("feedId1", feedId1));
                list.add(new BasicNameValuePair("feedId2", feedId2));
                list.add(new BasicNameValuePair("feedId3", feedId3));
                list.add(new BasicNameValuePair("feedQty1", feedQty1));
                list.add(new BasicNameValuePair("feedQty2", feedQty2));
                list.add(new BasicNameValuePair("feedQty3", feedQty3));
                list.add(new BasicNameValuePair("outTime", outTime));
                list.add(new BasicNameValuePair("suggestion", suggestion));
                list.add(new BasicNameValuePair("visitDate", visitDate));
                list.add(new BasicNameValuePair("productionDate", productionDate));
                list.add(new BasicNameValuePair("transitMortality", transitMortality));
                list.add(new BasicNameValuePair("app_v", getResources().getString(R.string.app_v)));
                list.add(new BasicNameValuePair("uName", uName));
                list.add(new BasicNameValuePair("latitude", latitude));
                list.add(new BasicNameValuePair("longitude", longitude));
                list.add(new BasicNameValuePair("selectedMortReason", selectedMortReason));
                list.add(new BasicNameValuePair("diseasename", diseasename));
                list.add(new BasicNameValuePair("backdays", String.valueOf(backdays)));
                list.add(new BasicNameValuePair("feedCodeArrayList",headerString));
                list.add(new BasicNameValuePair("feedQtyArrayList", lineString));

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
            }catch (Exception ex){
                    result = ex.getMessage();

            }finally {
                if(con != null){
                    try{
                        con = null;
                        if(result == "complete"){
                            result = "complete";
                        }
                        if(result.equals("Please Enter Previous Age Data")){
                            result = "Please Enter Previous Age Data";
                        }
                    }catch (Exception ex){
                        result = ex.getMessage();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            try{
                if(s.equals("complete")){
                    toastMessage("Report Saved Successfully!");
                    deleteReport();
                }else {
                    showAlert("Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
/*
    private final class AsyncCheckHistory extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "", gotresult="";
            HttpURLConnection con = null;

            try {
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "checkhistory"));
                list.add(new BasicNameValuePair("batchId", String.valueOf(batch_id)));
                list.add(new BasicNameValuePair("visitdateval", visitDate));
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
                    gotresult = (String) line.get(0);

                    result = gotresult;
                }

                Cursor data = mDatabaseHelper.checkLastProductionEntry(batchId,String.valueOf(c_bpartnerID));
                while(data.moveToNext()){
                    birdage = data.getInt(0);
                    birdnumber = data.getInt(1);
                    productionDate = data.getString(2);
                    productionDatevw = productionDate;
                    mortality    = data.getInt(3);
                    transitmortality = data.getInt(4);
                    result = "Bird-Sync-Successful";
                }

                if(birdnumber==0 &&  (productionDate == null || productionDate.equals("") ) && birdage==0){
                    birdnumber = qtyonhand;
                    productionDate = movementdate;
                    productionDatevw = productionDate;
                    result = "First-Bird-Sync-Successful";
                }
            } catch (Exception e) {
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
            dismissProgressDialog();
            try{
                if(s.equals("complete")){
                    toastMessage(s);
                    deleteReport();
                }else {
                    showAlert("Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    */

    private void showProgressDialog(){

    }

    private void dismissProgressDialog(){

    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(ReportDetailsActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ReportDetailsActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void onClickDatePickerReport(View view) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.report_visitDate).getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (month));
                c.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                visitDateEditText.setText(sdf.format(c.getTime()));
                // visitDateEditText.setText(view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth());

            }
        }, day, month, year);
        //subtract -3000 to select date before current date
        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        c.add(Calendar.MONTH, -1);
        datePicker.getDatePicker().setSpinnersShown(true);
        datePicker.getDatePicker().setCalendarViewShown(false);
        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());


        datePicker.updateDate(year,month,day);
        datePicker.setTitle("Select Date");
        datePicker.show();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//        Intent intent = new Intent(ReportDetailsActivity.this,LoginActivity.class);
//        startActivity(intent);
//    }

    public boolean checkVPN(){

        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {
            //Timber.d("isVpnUsing Network List didn't received");
        }

        return networkList.contains("tun0");
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
