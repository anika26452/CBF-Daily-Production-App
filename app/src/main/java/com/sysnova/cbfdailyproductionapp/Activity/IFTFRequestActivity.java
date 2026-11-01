package com.sysnova.cbfdailyproductionapp.Activity;


import static com.sysnova.cbfdailyproductionapp.Activity.Login.ad_user_id;
import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.math.BigDecimal;
import java.net.HttpURLConnection;

import java.net.URL;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class IFTFRequestActivity extends AppCompatActivity {

    Spinner batchToListSpinner,batchListSpinner,whfrom_spinner,whto_spinner,Transportation_list_spinner
            ,TransferReason_list_spinner;
    Button saveButton, get_loc;
    ProgressDialog pd;
    EditText farmersCodeEditText, visitDateEditText,
            distance_km, avgWeightEditText, suggestionEditText
            , farmersCodeToEditText, farmersCodeByEditText, StarterQty_EditText, GrowerQty_EditText, FinisherQty_EditText, Grower_crumbleqty_Edittext;
    TextView lattxtvw, longtxtvw,
            farmernamevw,farmernamevwTo,farmernamevwBy, orgname_txt;
    List<String> batchArrayList = new ArrayList<>();
    List<String> batchArrayListTo = new ArrayList<>();

    String farmerCode="",batchName = null,batchNameTo = null,visitDate,distance,TransferCost,description,
            OrgName= "",farmerCodeTo="",farmerByCode="",farmerTo_name= "",
            farmerBy_name= "",latitude1="",org1= "",longitude1="",latitude2="",longitude2="" , org2 = "",farmer_name="", selectedBatchName="";
    CheckBox Starter_checkbox, Grower_checkbox, Finisher_checkbox, Grower_crumble_checkbox;
    int batch_id = 0,c_bpartnerID = 0,c_bpartnerIDTo=0,batchTo_id = 0,selectedBatchId=0, OrgID;
    DatabaseHelper sqliteDbHelper;
    HashMap<String,Integer> hashBatchID = new HashMap<>();
    HashMap<String,Integer> hashBatchIDTo = new HashMap<>();
    GPSTracker gps;
    double latitude,  longitude;
    LinearLayout lat_layout, long_layout;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date cur_date = new Date();
    String transType = "Van";
    String reasonType = "Farm Closed";
    public static final int Ra = 6371;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iftf_request);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

        orgname_txt= findViewById(R.id.org_name_txt);
        batchListSpinner = findViewById(R.id.BatchFrom_spinner);
        batchToListSpinner = findViewById(R.id.BatchTo_spinner);
        whfrom_spinner   = findViewById(R.id.whfrom_spinner);
        whto_spinner   = findViewById(R.id.whTo_spinner);
        whto_spinner   = findViewById(R.id.whTo_spinner);
        Transportation_list_spinner   = findViewById(R.id.Transportation_list_spinner);
        TransferReason_list_spinner=findViewById(R.id.TransferReason_list_spinner);
        saveButton = findViewById(R.id.save_btn_id);

        farmersCodeEditText = findViewById(R.id.farmers_code_id);
        farmersCodeToEditText = findViewById(R.id.farmersTo_code_id);
        farmersCodeByEditText = findViewById(R.id.TransferBy_code_id);

        visitDateEditText   = findViewById(R.id.visit_dateEditText_id);
        distance_km   = findViewById(R.id.distance_km);
        avgWeightEditText   = findViewById(R.id.TransferCost_id);
        suggestionEditText  = findViewById(R.id.suggestion_id);
        Starter_checkbox        = findViewById(R.id.Starter_checkbox);
        Grower_checkbox        = findViewById(R.id.Grower_checkbox);
        Finisher_checkbox        = findViewById(R.id.Finisher_checkbox);
        Grower_crumble_checkbox = findViewById(R.id. Grower_crumble_checkbox);
        StarterQty_EditText        = findViewById(R.id.Starterqty_id);
        GrowerQty_EditText        = findViewById(R.id.Growerqty_id);
        FinisherQty_EditText        = findViewById(R.id.Finisherqty_id);
        Grower_crumbleqty_Edittext = findViewById(R.id.Grower_crumbleqty_id);

        TransferReason_list_spinner.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
        Transportation_list_spinner.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
        batchToListSpinner.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
        batchListSpinner.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);

        lattxtvw = findViewById(R.id.lat_textvw);
        longtxtvw = findViewById(R.id.long_textvw);
        lat_layout = findViewById(R.id.lat_linearlayout);
        long_layout = findViewById(R.id.long_linearlayout);
        get_loc = findViewById(R.id.get_loc_btn);

        farmernamevw = findViewById(R.id.farmer_name_txt);
        farmernamevwTo = findViewById(R.id.farmerTo_name_txt);
        farmernamevwBy = findViewById(R.id.TransferBy_name_txt);

        orgname_txt.setEnabled(false);
        StarterQty_EditText.setEnabled(false);
        GrowerQty_EditText.setEnabled(false);
        FinisherQty_EditText.setEnabled(false);

        Starter_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            StarterQty_EditText.setFocusable(true);
                            StarterQty_EditText.requestFocus();
                            StarterQty_EditText.setEnabled(true);
                        } else {
                            StarterQty_EditText.setText("");
                            StarterQty_EditText.setEnabled(false);
                        }
                    }
                });

        Grower_crumble_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Grower_crumbleqty_Edittext.setFocusable(true);
                    Grower_crumbleqty_Edittext.requestFocus();
                    Grower_crumbleqty_Edittext.setEnabled(true);
                } else {
                    Grower_crumbleqty_Edittext.setText("");
                    Grower_crumbleqty_Edittext.setEnabled(false);
                }
            }
        });

        Grower_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GrowerQty_EditText.setFocusable(true);
                    GrowerQty_EditText.requestFocus();
                    GrowerQty_EditText.setEnabled(true);
                } else {
                    GrowerQty_EditText.setText("");
                    GrowerQty_EditText.setEnabled(false);
                }
            }
        });

        Finisher_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FinisherQty_EditText.setFocusable(true);
                    FinisherQty_EditText.requestFocus();
                    FinisherQty_EditText.setEnabled(true);
                } else {
                    FinisherQty_EditText.setText("");
                    FinisherQty_EditText.setEnabled(false);
                }
            }
        });
        sqliteDbHelper = new DatabaseHelper(this);
        sendToServer();
    }

    public void loadBatch(View view){

        farmersCodeEditText.setEnabled(false);
        farmerCode = farmersCodeEditText.getText().toString();
        batchArrayList.clear();
        hashBatchID.clear();

        sqliteDbHelper.deleteBatchTable();

        boolean vpn = true;
        if(vpn) {
            AsyncSyncName asyncSyncName = new AsyncSyncName();
            asyncSyncName.execute();
        }else{
            showAlert("Warning","Please Check Vpn is Connected");
        }
    }

    public void loadBatchTo(View view){

        farmersCodeToEditText.setEnabled(false);
        farmerCodeTo = farmersCodeToEditText.getText().toString();
        batchArrayListTo.clear();
        hashBatchIDTo.clear();
        distance_km.setText("");

        sqliteDbHelper.deleteBatchToTable();

        boolean vpn = true;
        if(vpn) {
            AsyncSyncNameTo asyncSyncNameTo = new AsyncSyncNameTo();
            asyncSyncNameTo.execute();
        }else{
            showAlert("Warning","Please Check Vpn is Connected");
        }
    }

    public void loadBatchBy(View view){

        farmersCodeByEditText.setEnabled(true);
        farmerByCode = farmersCodeByEditText.getText().toString();
        String farmerFrom=farmersCodeEditText.getText().toString();
        String farmerTo=farmersCodeToEditText.getText().toString();
        boolean vpn = true;

        boolean proceed=true;

        if(farmerByCode.equals(farmerFrom) || farmerByCode.equals(farmerTo)){
            proceed=true;
        }else {
            proceed = false;
        }

        if(vpn) {
            if(proceed){
                AsyncSyncNameBy asyncSyncNameBy = new AsyncSyncNameBy();
                asyncSyncNameBy.execute();
            }else{
                showAlert("Invalid Input", "Please Check Farmer Code To and Farmer Code From input.");
            }
        }else{
            showAlert("Warning","Please Check Vpn is Connected");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(IFTFRequestActivity.this,Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void sendToServer(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GPSTracker(IFTFRequestActivity.this);

                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                } else {
                    gps.showSettingsAlert();
                }

                if(farmerCode.equals("")||farmerCode.isEmpty()){
                    farmersCodeEditText.setError("Please Enter farmer code");
                    return;
                }

                if(farmerCodeTo.equals("")||farmerCodeTo.isEmpty()){
                    farmersCodeToEditText.setError("Please Enter Farmer To Code");
                    return;
                }

                if(farmerByCode.equals("")||farmerByCode.isEmpty()){
                    farmersCodeByEditText.setError("Please Enter TranferBy Code");
                    return;
                }

                if(batchListSpinner != null && batchListSpinner.getSelectedItem() != null) {
                    batchName = batchListSpinner.getSelectedItem().toString();
                    batch_id  = hashBatchID.get(batchName);
                }

                if(batchToListSpinner != null && batchToListSpinner.getSelectedItem() != null) {
                    batchNameTo = batchToListSpinner.getSelectedItem().toString();
                    batchTo_id  = hashBatchIDTo.get(batchNameTo);
                }

                visitDate    = visitDateEditText.getText().toString();

                distance    = distance_km.getText().toString(); //distance
                TransferCost    = avgWeightEditText.getText().toString();
                description   = suggestionEditText.getText().toString();
                boolean check =Starter_checkbox.isChecked();
                if(Starter_checkbox.isChecked()==false && Grower_crumble_checkbox.isChecked()==false && Grower_checkbox.isChecked()==false && Finisher_checkbox.isChecked()==false){
                    toastMessage("Please Select Any Starter/Grower/Finisher CheckBox");
                    return;
                }
                if(visitDate.isEmpty()){
                    visitDateEditText.requestFocus();
                    visitDateEditText.setError("Please Enter Transfer Date");
                    return;
                }

                if(batchName == null){
                    toastMessage("No Batch found");
                    return;
                }
                if(batchNameTo == null){
                    toastMessage("No Batch To found");
                    return;
                }
                if(distance.isEmpty()){
                    distance_km.setError("Please Enter Distance");
                    return;
                }if(TransferCost.isEmpty()){
                    avgWeightEditText.setError("Please Enter Transfer Cost");
                    return;
                }

                if(Transportation_list_spinner == null && Transportation_list_spinner.getSelectedItem() == null){
                    toastMessage("Please Select Transportation");
                    return;
                }
                if(TransferReason_list_spinner == null && TransferReason_list_spinner.getSelectedItem() == null){
                    toastMessage("Please Select TransferReason");
                    return;
                }

                /* anika on 2025-08-24 */
                if(!org1.equals(org2)){
                    toastMessage("Select farmers of same branch!");
                    return;
                }
                /* anika on 2025-08-24 */

                savealert();
            }
        });

    }



/*
    private final class AsyncSyncBatch extends AsyncTas<String,String,String> {

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
                    String getQuestion_query = "select c_bpartner_id,value,m_batch_id,m_batch.ad_org_id,\n" +
                            "(select name from ad_org where ad_org.ad_org_id= m_batch.ad_org_id order by ad_org.name FETCH FIRST 1 ROWS ONLY) orgname from m_batch  where bpcode = '"+farmerCode+"' and isclosed = 'N'";
                    ResultSet resultSet = statement.executeQuery(getQuestion_query);
                    while (resultSet.next()){
                        String batch    = resultSet.getString("value");
                        int batchID     = resultSet.getInt("m_batch_id");
                        c_bpartnerID    = resultSet.getInt("c_bpartner_id");
                        int OrgID     = resultSet.getInt("ad_org_id");
                        String OrgName     = resultSet.getString("orgname");
                        hashBatchID.put(batch,batchID);
                        batchArrayList.add(batch);
                        sqliteDbHelper.insertIntoBatchDetailsNew(batch,batchID,OrgID,OrgName);
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
                    toastMessage(s);
                    loadBatchSpinner();

                    //getSelectedSpinnerItem();
                    farmersCodeEditText.setEnabled(true);
                }else {
                    loadBatchSpinner();
                    toastMessage("No Batch Found");
                    farmersCodeEditText.setEnabled(true);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    */

    private final class AsyncSyncBatch extends AsyncTask<String,String,String> {

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
                list.add(new BasicNameValuePair("name", "iftfrequestsyncbatch"));
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
                    String batch=(String) line.get(0);
                    int batchID= Integer.parseInt((String) line.get(1));
                    c_bpartnerID = Integer.parseInt((String) line.get(2));
                    int OrgID=Integer.parseInt((String) line.get(3));
                    OrgName  =(String) line.get(4);

                    hashBatchID.put(batch,batchID);
                    batchArrayList.add(batch);
                    res = sqliteDbHelper.insertIntoBatchDetailsNew(batch,batchID,OrgID,OrgName);

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
                //    toastMessage(s);
                    loadBatchSpinner();

                    farmersCodeEditText.setEnabled(true);
                }else {
                    loadBatchSpinner();
                    toastMessage("No Batch Found");
                    farmersCodeEditText.setEnabled(true);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private final class AsyncSyncBatchTo extends AsyncTask<String,String,String> {

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
                list.add(new BasicNameValuePair("name", "iftfrequestsyncbatchTo"));
                list.add(new BasicNameValuePair("farmerCodeTo", farmerCodeTo));

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


                    String batch=(String) line.get(0);
                    int batchID =Integer.parseInt((String) line.get(1));
                    int OrgID = Integer.parseInt((String) line.get(2));
                    String OrgName=(String) line.get(3);
                    c_bpartnerIDTo  =Integer.parseInt((String) line.get(4));

                    hashBatchIDTo.put(batch,batchID);
                    batchArrayListTo.add(batch);
                    res = sqliteDbHelper.insertIntoBatchDetailstoNew(batch,batchID,OrgID,OrgName);
                }
                if (res){
                    result = "Sync-Successful";
                }else{
                    result = "No batch Found";
                }

            }catch (Exception ex){
                result = ex.getMessage();
            }finally {
                if (con != null) {
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
               //     toastMessage(s);
                    loadBatchToSpinner();

                    farmersCodeToEditText.setEnabled(true);
                }else {
                    loadBatchToSpinner();
                    toastMessage("No Batch Found");
                    farmersCodeToEditText.setEnabled(true);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
/*
    private final class AsyncSyncName extends AsyncTas<String,String,String> {

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
                    String getQuestion_query = "select bpname,latitude,longitude from t_customer_kfg where bpcode = '"+farmerCode+"'";
                    ResultSet resultSet = statement.executeQuery(getQuestion_query);
                    while (resultSet.next()){
                        farmer_name    = resultSet.getString("bpname");
                        latitude1    = resultSet.getString("latitude");
                        longitude1   = resultSet.getString("longitude");
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
                    farmernamevw.setText(farmer_name);
                    AsyncSyncBatch asyncSyncBatch = new AsyncSyncBatch();
                    asyncSyncBatch.execute();
                }else {
                    toastMessage("No Farmer Found");
                    farmersCodeEditText.setEnabled(true);
                    AsyncSyncBatch asyncSyncBatch = new AsyncSyncBatch();
                    asyncSyncBatch.execute();
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    */

    private final class AsyncSyncName extends AsyncTask<String,String,String> {

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
                list.add(new BasicNameValuePair("name", "iftfrequestsyncname"));
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
                    farmer_name=(String) line.get(0);
                    latitude1=(String) line.get(1);
                    longitude1 = (String) line.get(2);
                    org1 = (String) line.get(3);

                    res = true;
                }

                if (res){
                    result = "Sync-Successful";
                }else{
                    result = "No Farmer Found";
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
                    farmernamevw.setText(farmer_name);
                    AsyncSyncBatch asyncSyncBatch = new AsyncSyncBatch();
                    asyncSyncBatch.execute();
                }else {
                    toastMessage("No Farmer Found");
                    farmersCodeEditText.setEnabled(true);
                    AsyncSyncBatch asyncSyncBatch = new AsyncSyncBatch();
                    asyncSyncBatch.execute();
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
/*
    private final class AsyncSyncNameTo extends AsyncTas<String,String,String> {

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
                    String getQuestion_query = "select bpname,latitude,longitude from t_customer_kfg where bpcode = '"+farmerCodeTo+"'";
                    ResultSet resultSet = statement.executeQuery(getQuestion_query);
                    while (resultSet.next()){
                        farmerTo_name    = resultSet.getString("bpname");
                        latitude2    = resultSet.getString("latitude");
                        longitude2   = resultSet.getString("longitude");
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
                    farmernamevwTo.setText(farmerTo_name);
                    //2024-11-02 added by shanzida for KML distance calculation
                    if (latitude1 == null) {
                        latitude1 = "0.0";
                    }
                    if(longitude1== null) {
                        longitude1 = "0.0";
                    }
                    if(latitude2== null) {
                        latitude2 = "0.0";
                    }
                    if(longitude2== null) {
                        longitude2 = "0.0";
                    }
                    double dLat = Math.toRadians(Double.parseDouble(latitude2) - Double.parseDouble(latitude1));
                    double dLon = Math.toRadians(Double.parseDouble(longitude2) - Double.parseDouble(longitude1));
                    double latitude11 = Math.toRadians(Double.parseDouble(latitude1));
                    double latitude22 = Math.toRadians(Double.parseDouble(latitude2));

                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(latitude11) * Math.cos(latitude22) *
                                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                    double distance_kml = Ra * c;

                    if(distance_kml == 0.0){
                        distance_km.setEnabled(true);
                    }else{
                        DecimalFormat df = new DecimalFormat("#.00");
                        String formattedDistance = df.format(distance_kml);
                        distance_km.setText(formattedDistance);
                    }
                    //2024-11-02 end
                    AsyncSyncBatchTo asyncSyncBatchTo = new AsyncSyncBatchTo();
                    asyncSyncBatchTo.execute();
                }else {
                    toastMessage("No Farmer Found");
                    farmersCodeToEditText.setEnabled(true);
                    AsyncSyncBatchTo asyncSyncBatchTo = new AsyncSyncBatchTo();
                    asyncSyncBatchTo.execute();
                }

            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
*/

    private final class AsyncSyncNameTo extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            HttpURLConnection con = null;
            Boolean res = false;

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "iftfrequestsyncname"));
                list.add(new BasicNameValuePair("farmercode", farmerCodeTo));

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

                    farmerTo_name=(String) line.get(0);
                    latitude2=(String) line.get(1);
                    longitude2 = (String) line.get(2);
                    org2 = (String) line.get(3);

                    res = true;

                }
                if (res){
                    result = "Sync-Successful";
                }else{
                    result = "No Farmer Found";
                }

            }catch (Exception ex){
                result = ex.getMessage();
            } finally {
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
                    farmernamevwTo.setText(farmerTo_name);
                    if (latitude1 == null || latitude1.equals("null") || latitude1.equals("")) {
                        latitude1 = "0.0";
                    }
                    if(longitude1== null || longitude1.equals("null") || longitude1.equals("")) {
                        longitude1 = "0.0";
                    }
                    if(latitude2== null || latitude2.equals("null") || latitude2.equals("")) {
                        latitude2 = "0.0";
                    }
                    if(longitude2== null || longitude2.equals("null") || longitude2.equals("")) {
                        longitude2 = "0.0";
                    }
                    double dLat = Math.toRadians(Double.parseDouble(latitude2) - Double.parseDouble(latitude1));
                    double dLon = Math.toRadians(Double.parseDouble(longitude2) - Double.parseDouble(longitude1));
                    double latitude11 = Math.toRadians(Double.parseDouble(latitude1));
                    double latitude22 = Math.toRadians(Double.parseDouble(latitude2));

                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(latitude11) * Math.cos(latitude22) *
                                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                    double distance_kml = Ra * c;

                    if(distance_kml == 0.0){
                        distance_km.setEnabled(true);
                    }else{
                        DecimalFormat df = new DecimalFormat("#.00");
                        String formattedDistance = df.format(distance_kml);
                        distance_km.setText(formattedDistance);
                    }
                    AsyncSyncBatchTo asyncSyncBatchTo = new AsyncSyncBatchTo();
                    asyncSyncBatchTo.execute();
                }else {
                    toastMessage("No Farmer Found");
                    farmersCodeToEditText.setEnabled(true);
                    AsyncSyncBatchTo asyncSyncBatchTo = new AsyncSyncBatchTo();
                    asyncSyncBatchTo.execute();
                }

            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private final class AsyncSyncNameBy extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            HttpURLConnection con = null;
            Boolean res = false;

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "iftfrequestsyncnameBy"));
                list.add(new BasicNameValuePair("farmerByCode", farmerByCode));

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
                    farmerBy_name=(String) line.get(0);

                    res = true;
                }
                if (res){
                    result = "Sync-Successful";
                }else{
                    result = "No farmer Found";
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
                    farmernamevwBy.setText(farmerBy_name);
                }else {
                    toastMessage("No Farmer Found");
                    farmersCodeByEditText.setEnabled(true);

                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(IFTFRequestActivity.this);
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

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(IFTFRequestActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(IFTFRequestActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void loadBatchSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,batchArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchListSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectBatchSpinner();
    }
    private void loadBatchToSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,batchArrayListTo);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchToListSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void selectBatchSpinner(){
        batchListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedBatchName=selectedOrg;
                    selectedBatchId= hashBatchID.get(selectedBatchName);

                    Cursor cursor = sqliteDbHelper.getBatchIDOrg(selectedBatchName);
                    if(cursor.moveToFirst()){
                        do{
                            OrgID   = cursor.getInt(0);
                            String OrgName = cursor.getString(1);
                            orgname_txt.setText(OrgName);

                        }while(cursor.moveToNext());
                    }


                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            BigDecimal StarterQty=BigDecimal.ZERO;
            BigDecimal GrowerQty=BigDecimal.ZERO;
            BigDecimal CrumbledQty=BigDecimal.ZERO;
            BigDecimal FinisherQty=BigDecimal.ZERO;
            String isStarter="N",isGrower="N",isFinisher="N", isCrumbled = "N";

            if(Starter_checkbox.isChecked()){
                isStarter="Y";
                StarterQty=new BigDecimal(StarterQty_EditText.getText().toString());
            }

            if(Grower_crumble_checkbox.isChecked()){
                isCrumbled="Y";
                CrumbledQty=new BigDecimal(Grower_crumbleqty_Edittext.getText().toString());
            }

            if(Grower_checkbox.isChecked()){
                isGrower="Y";
                GrowerQty=new BigDecimal(GrowerQty_EditText.getText().toString());
            }
            if(Finisher_checkbox.isChecked()){
                isFinisher="Y";
                FinisherQty=new BigDecimal(FinisherQty_EditText.getText().toString());
            }
            String Transportation=Transportation_list_spinner.getSelectedItem().toString();
            String TransferReason = TransferReason_list_spinner.getSelectedItem().toString();

            HttpURLConnection con = null;

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/InsertData")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/InsertData");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "iftfrequest"));
                list.add(new BasicNameValuePair("OrgID", String.valueOf(OrgID)));
                list.add(new BasicNameValuePair("ad_user_id", ad_user_id));
                list.add(new BasicNameValuePair("batch_id", String.valueOf(batch_id)));
                list.add(new BasicNameValuePair("batchTo_id", String.valueOf(batchTo_id)));
                list.add(new BasicNameValuePair("description", description));
                list.add(new BasicNameValuePair("visitDate", visitDate));
                list.add(new BasicNameValuePair("StarterQty", String.valueOf(StarterQty)));
                list.add(new BasicNameValuePair("CrumbledQty", String.valueOf(CrumbledQty)));
                list.add(new BasicNameValuePair("GrowerQty", String.valueOf(GrowerQty)));
                list.add(new BasicNameValuePair("FinisherQty", String.valueOf(FinisherQty)));
                list.add(new BasicNameValuePair("isStarter", isStarter));
                list.add(new BasicNameValuePair("isGrower", isGrower));
                list.add(new BasicNameValuePair("isFinisher", isFinisher));
                list.add(new BasicNameValuePair("isCrumbled", isCrumbled));
                list.add(new BasicNameValuePair("c_bpartnerID", String.valueOf(c_bpartnerID)));
                list.add(new BasicNameValuePair("c_bpartnerIDTo", String.valueOf(c_bpartnerIDTo)));
                list.add(new BasicNameValuePair("farmerByCode", farmerByCode));
                list.add(new BasicNameValuePair("Transportation", Transportation));
                list.add(new BasicNameValuePair("TransferReason", TransferReason));
                list.add(new BasicNameValuePair("distance", distance));
                list.add(new BasicNameValuePair("TransferCost", TransferCost));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);

                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                } else {
                    result=  "Insertion-Successful";
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
                if(s.equals("Insertion-Successful")){
                    toastMessage(s);
                    onRestart();
                }else {
                    showAlert("Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    public void refreshFromUI(View view){
        batchArrayList.clear();
        batchArrayListTo.clear();
        farmersCodeEditText.setEnabled(true);
        farmersCodeEditText.setText("");
        farmernamevw.setText("");
        farmersCodeToEditText.setEnabled(true);
        farmersCodeToEditText.setText("");
        farmernamevwTo.setText("");
        farmersCodeByEditText.setEnabled(true);
        farmersCodeByEditText.setText("");
        farmernamevwBy.setText("");
        visitDateEditText.setText("");
        Starter_checkbox.setChecked(false);
        Grower_checkbox.setChecked(false);
        Finisher_checkbox.setChecked(false);
        Grower_crumble_checkbox.setChecked(false);
        StarterQty_EditText.setText("");
        GrowerQty_EditText.setText("");
        FinisherQty_EditText.setText("");
        Grower_crumbleqty_Edittext.setText("");
        orgname_txt.setText("");
        distance_km.setText("");
        avgWeightEditText.setText("");
        suggestionEditText.setText("");

        Transportation_list_spinner.setSelection(((ArrayAdapter) Transportation_list_spinner.getAdapter()).getPosition(transType));
        TransferReason_list_spinner.setSelection(((ArrayAdapter) Transportation_list_spinner.getAdapter()).getPosition(reasonType));
        loadBatchSpinner();
        loadBatchToSpinner();
    }

    public void onClickDatePicker(View v) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.visit_dateEditText_id).getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                visitDateEditText.setText(sdf.format(c.getTime()));
            }
        }, year, month, day);
        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH, -1); // Go back one day
        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        datePicker.setTitle("Select Date");
        datePicker.show();
    }

    public void savealert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("IFTF Request Saved");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AsyncSendToServer asyncSendToServer = new AsyncSendToServer();
                        asyncSendToServer.execute();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }

}
