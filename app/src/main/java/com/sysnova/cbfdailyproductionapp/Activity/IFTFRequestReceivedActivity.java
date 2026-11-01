package com.sysnova.cbfdailyproductionapp.Activity;

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
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


public class IFTFRequestReceivedActivity extends AppCompatActivity {

    Spinner iftf_spinner;
    Button saveButton;
    ProgressDialog pd;
    EditText farmersCodeEditText, visitDateEditText, BatchFrom_EditText, BatchTo_EditText, TransferReason_EditText,
            Transportation_EditText, mortalityEditText, avgWeightEditText, suggestionEditText, farmersCodeToEditText,
            farmersCodeByEditText, StarterQty_EditText, GrowerQty_EditText, FinisherQty_EditText, Grower_crumbleqty_Edittext;
    TextView farmernamevw,farmernamevwTo,farmernamevwBy, orgname_txt;
    CheckBox Starter_checkbox, Grower_checkbox, Finisher_checkbox;
    int batch_id = 0,c_bpartnerID = 0,c_bpartnerIDTo=0,batchTo_id = 0,OrgID=0,selectedBatchId=0, selectedWHId=0;
    DatabaseHelper sqliteDbHelper;
    HashMap<String,Integer> hashIFTFID = new HashMap<>();
    List<String> IFTFNameArrayList = new ArrayList<String>();
    public static String selectedWHName="";
    String orgname = "",description  = "",frombatch  = "", tobatch = "", frombpcode  = "", tobpcode = "",
            frombp  = "",tobp  = "",isstarter  = "", isgrower  = "",isfinisher  = "", transportation  = "",
            transferreason  = "",distancekm  = "",transferby_code  = "",transferby_name  = ""
            ,starter_qty  = "",grower_qty = "",finisher_qty  = "",paidamt  = "",movementdate= "", iscrumbled = "", crumbled_qty="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iftf_received);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

        orgname_txt= findViewById(R.id.org_name_txt);
        BatchFrom_EditText = findViewById(R.id.BatchFrom_EditText);
        BatchTo_EditText = findViewById(R.id.BatchTo_EditText);

        iftf_spinner   = findViewById(R.id.iftf_spinner);
        saveButton = findViewById(R.id.save_btn_id);
        iftf_spinner.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);

        farmersCodeEditText = findViewById(R.id.farmers_code_id);
        farmersCodeToEditText = findViewById(R.id.farmersTo_code_id);
        farmersCodeByEditText = findViewById(R.id.TransferBy_code_id);

        visitDateEditText   = findViewById(R.id.visit_dateEditText_id);
        mortalityEditText   = findViewById(R.id.mortality_amount_id); //distance
        avgWeightEditText   = findViewById(R.id.TransferCost_id); // TransferCost
        suggestionEditText  = findViewById(R.id.suggestion_id); //description
        Starter_checkbox        = findViewById(R.id.Starter_checkbox);
        Grower_checkbox        = findViewById(R.id.Grower_checkbox);
        Finisher_checkbox        = findViewById(R.id.Finisher_checkbox);
        TransferReason_EditText = findViewById(R.id.TransferReason_EditText);
        Transportation_EditText = findViewById(R.id.Transportation_EditText);

        StarterQty_EditText        = findViewById(R.id.Starterqty_id);
        GrowerQty_EditText        = findViewById(R.id.Growerqty_id);
        FinisherQty_EditText        = findViewById(R.id.Finisherqty_id);
        Grower_crumbleqty_Edittext = findViewById(R.id.Grower_crumbleqty_id);

        farmernamevw = findViewById(R.id.farmer_name_txt);
        farmernamevwTo = findViewById(R.id.farmerTo_name_txt);
        farmernamevwBy = findViewById(R.id.TransferBy_name_txt);

        orgname_txt.setEnabled(false);
        StarterQty_EditText.setEnabled(false);
        GrowerQty_EditText.setEnabled(false);
        FinisherQty_EditText.setEnabled(false);
        Grower_crumbleqty_Edittext.setEnabled(false);
        BatchFrom_EditText.setEnabled(false);
        BatchTo_EditText.setEnabled(false);

        farmersCodeEditText.setEnabled(false);
        farmersCodeToEditText.setEnabled(false);
        farmersCodeByEditText.setEnabled(false);
        visitDateEditText.setEnabled(false);
        mortalityEditText.setEnabled(false);
        avgWeightEditText.setEnabled(false);
        suggestionEditText.setEnabled(false);
        TransferReason_EditText.setEnabled(false);
        Transportation_EditText.setEnabled(false);
        StarterQty_EditText.setEnabled(false);
        GrowerQty_EditText.setEnabled(false);
        FinisherQty_EditText.setEnabled(false);
        farmernamevw.setEnabled(false);
        farmernamevwTo.setEnabled(false);
        farmernamevwBy.setEnabled(false);

        sqliteDbHelper = new DatabaseHelper(this);
        AsyncSyncIFTF AsyncSyncIFTF = new AsyncSyncIFTF();
        AsyncSyncIFTF.execute();
        sendToServer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(IFTFRequestReceivedActivity.this,Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void sendToServer(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savealert();
            }
        });

    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(IFTFRequestReceivedActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(IFTFRequestReceivedActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

/*
    private final class AsyncSendToServer extends AsyncTas<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            int resultSet=0;

            if(connection != null){
                try{
                    Statement statement = connection.createStatement();
                    String getQuestion_query = "UPDATE t_cbf_ift set isReceived='Y' where t_Cbf_IFT_ID="+selectedWHId;

                    resultSet = statement.executeUpdate(getQuestion_query);
                    if (resultSet > 0) {
                        result = "Update-Successful";
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
            }
            else{
                result = "Check Port,hostname is correct";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("Update-Successful")){
                    toastMessage(s);
                    refresh();
                }else {
                    showAlert("Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    */

    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
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
                list.add(new BasicNameValuePair("name", "iftfreqrectoserver"));
                list.add(new BasicNameValuePair("selectedWHId", String.valueOf(selectedWHId)));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);

                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                } else if(s.equals("Success")){
                    result=  "Update-Successful";
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
                if(s.equals("Update-Successful")){
                    toastMessage(s);
                    refresh();
                }else {
                    showAlert("Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(IFTFRequestReceivedActivity.this);
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

    private void refresh(){

        farmersCodeEditText.setText("");
        farmersCodeEditText.setEnabled(true);
        farmernamevw.setText("");
        farmersCodeToEditText.setText("");
        farmersCodeToEditText.setEnabled(true);
        farmernamevwTo.setText("");
        farmersCodeByEditText.setText("");
        farmersCodeByEditText.setEnabled(true);
        farmernamevwBy.setText("");
        visitDateEditText.setText("");
        Starter_checkbox.setChecked(false);
        Grower_checkbox.setChecked(false);
        Finisher_checkbox.setChecked(false);
   //     Grower_checkbox.setChecked(false);
        StarterQty_EditText.setText("");
        GrowerQty_EditText.setText("");
        FinisherQty_EditText.setText("");
        Grower_crumbleqty_Edittext.setText("");
        orgname_txt.setText("");
        mortalityEditText.setText("");
        avgWeightEditText.setText("");
        suggestionEditText.setText("");
        BatchFrom_EditText.setText("");
        BatchTo_EditText.setText("");
        TransferReason_EditText.setText("");
        Transportation_EditText.setText("");

        AsyncSyncIFTF AsyncSyncIFTF = new AsyncSyncIFTF();
        AsyncSyncIFTF.execute();
    }

    public void refreshFromUI(View view){

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
        StarterQty_EditText.setText("");
        GrowerQty_EditText.setText("");
        FinisherQty_EditText.setText("");
        orgname_txt.setText("");
        mortalityEditText.setText("");
        avgWeightEditText.setText("");
        suggestionEditText.setText("");
        BatchFrom_EditText.setText("");
        BatchTo_EditText.setText("");
        TransferReason_EditText.setText("");
        Transportation_EditText.setText("");
        AsyncSyncIFTF AsyncSyncIFTF = new AsyncSyncIFTF();
        AsyncSyncIFTF.execute();

    }

    public void onClickDatePicker(View v){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.visit_dateEditText_id).getContext(), new DatePickerDialog.OnDateSetListener() {
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
    //    datePicker.getDatePicker().setMinDate(c.getTimeInMillis()-(86400000*24));//  3 * 24 * 60 * 60 * 1000 //to get previous days


        datePicker.updateDate(year,month,day);
        datePicker.setTitle("Select Date");
        datePicker.show();
    }

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


    public void ShowAlert(String title,String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void savealert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("IFTF Request Receive");
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
        //alert11.show();
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }

/*
    private final class AsyncSyncIFTF extends AsyncTas<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            hashIFTFID.clear();
            if(!IFTFNameArrayList.isEmpty())
            {
                IFTFNameArrayList.clear();
            }
            sqliteDbHelper.deleteCbfIFTFTable();
            //hashIFTFID.put("",1);

            if(connection != null){
                try{
                    Statement statement = connection.createStatement();
                    String getQuestion_query = "Select distinct t_Cbf_IFT_ID,cast(t_Cbf_IFT_ID as text) valuename,\n" +
                            "(select name from ad_org where ad_org.ad_org_id= t_Cbf_IFT.ad_org_id order by ad_org.name FETCH FIRST 1 ROWS ONLY) orgname\n" +
                            " ,t_Cbf_IFT.description,fba.value frombatch,tba.value tobatch,\n" +
                            " fba.bpcode frombpcode,tba.bpcode tobpcode,\n" +
                            " (select t_customer_kfg.bpname from t_customer_kfg where t_customer_kfg.bpcode =fba.bpcode FETCH FIRST 1 ROWS ONLY) frombp,\n" +
                            " (select t_customer_kfg.bpname from t_customer_kfg where t_customer_kfg.bpcode =tba.bpcode FETCH FIRST 1 ROWS ONLY) tobp\n" +
                            " ,isstarter,isgrower,isfinisher\n" +
                            " ,transportation,transferreason,cast(distancekm as text) distancekm,transferby_code," +
                            " (select t_customer_kfg.bpname from t_customer_kfg where t_customer_kfg.bpcode =transferby_code FETCH FIRST 1 ROWS ONLY) transferby_name,\n" +
                            " cast(coalesce(starter_qty,0) as text) starter_qty,cast(coalesce(grower_qty,0) as text) grower_qty,\n" +
                            " cast(coalesce(finisher_qty,0) as text) finisher_qty,cast(coalesce(paidamt,0) as text) paidamt\n" +
                            " ,cast(cast(movementdate as date) as text) movementdate\n" +
                            " from t_Cbf_IFT\n" +
                            " left join m_batch fba on (fba.m_batch_id=t_Cbf_IFT.from_m_batch_id and fba.c_bpartner_id=t_Cbf_IFT.c_bpartner_id)\n" +
                            " left join m_batch tba on (tba.m_batch_id=t_Cbf_IFT.to_m_batch_id and tba.c_bpartner_id=t_Cbf_IFT.c_bpartnerto_id)\n" +
                            "\n" +
                            " where bm_approved ='Y' and issent ='Y' \n" +
                            " and isreceived ='N' \n" +
                            " Order by t_Cbf_IFT_ID";
                    ResultSet resultSet = statement.executeQuery(getQuestion_query);
                    while (resultSet.next()){
                        String IFTF    = resultSet.getString("valuename");
                        int IFTFID     = resultSet.getInt("t_Cbf_IFT_ID");
                        hashIFTFID.put(IFTF,IFTFID);
                        IFTFNameArrayList.add(IFTF);
                        String orgname = resultSet.getString("orgname");
                        String description  = resultSet.getString("description");
                        String frombatch  = resultSet.getString("frombatch");
                        String tobatch = resultSet.getString("tobatch");
                        String frombpcode  = resultSet.getString("frombpcode");
                        String tobpcode = resultSet.getString("tobpcode");
                        String frombp  = resultSet.getString("frombp");
                        String tobp  = resultSet.getString("tobp");
                        String isstarter  = resultSet.getString("isstarter");
                        String isgrower  = resultSet.getString("isgrower");
                        String isfinisher  = resultSet.getString("isfinisher");
                        String transportation  = resultSet.getString("transportation");
                        String transferreason  = resultSet.getString("transferreason");
                        String distancekm  = resultSet.getString("distancekm");
                        String transferby_code  = resultSet.getString("transferby_code");
                        String transferby_name  = resultSet.getString("transferby_name");
                        String starter_qty  = resultSet.getString("starter_qty");
                        String grower_qty = resultSet.getString("grower_qty");
                        String finisher_qty  = resultSet.getString("finisher_qty");
                        String paidamt  = resultSet.getString("paidamt");
                        String movementdate  = resultSet.getString("movementdate");

                        sqliteDbHelper.insertIntoCbfIFTF(IFTF,IFTFID,orgname,description,frombatch,tobatch,
                                frombpcode,tobpcode ,frombp ,tobp,isstarter ,isgrower ,isfinisher ,transportation ,
                                transferreason ,distancekm ,transferby_code ,transferby_name ,starter_qty
                                ,grower_qty ,finisher_qty ,paidamt, movementdate);

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
                    loadIFTFSpinner();

                    //getSelectedSpinnerItem();
                    //farmersCodeEditText.setEnabled(true);
                }else {
                    loadIFTFSpinner();
                    toastMessage("No Batch Found");
                    //farmersCodeEditText.setEnabled(true);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
*/

    private final class AsyncSyncIFTF extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            hashIFTFID.clear();
            if(!IFTFNameArrayList.isEmpty())
            {
                IFTFNameArrayList.clear();
            }
            sqliteDbHelper.deleteCbfIFTFTable();
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
                list.add(new BasicNameValuePair("name", "iftfrequestrecieved"));

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

                    String IFTF    = (String) line.get(0);
                    int IFTFID     = Integer.parseInt((String) line.get(1));
                    String orgname = (String) line.get(2);
                    String description  = (String) line.get(3);
                    String frombatch  = (String) line.get(4);
                    String tobatch = (String) line.get(5);
                    String frombpcode  = (String) line.get(6);
                    String tobpcode = (String) line.get(7);
                    String frombp  = (String) line.get(8);
                    String tobp  = (String) line.get(9);
                    String isstarter  = (String) line.get(10);
                    String isgrower  = (String) line.get(11);
                    String isfinisher  = (String) line.get(12);
                    String iscrumbled  = (String) line.get(13);
                    String transportation  = (String) line.get(14);
                    String transferreason  = (String) line.get(15);
                    String distancekm  = (String) line.get(16);
                    String transferby_code  = (String) line.get(17);
                    String transferby_name  = (String) line.get(18);
                    String starter_qty  = (String) line.get(19);
                    String grower_qty = (String) line.get(20);
                    String finisher_qty  = (String) line.get(21);
                    String crumbled_qty  = (String) line.get(22);
                    String paidamt  = (String) line.get(23);
                    String movementdate  = (String) line.get(24);

                    hashIFTFID.put(IFTF,IFTFID);
                    IFTFNameArrayList.add(IFTF);

                    res = sqliteDbHelper.insertIntoCbfIFTF(IFTF,IFTFID,orgname,description,frombatch,tobatch,
                            frombpcode,tobpcode ,frombp ,tobp,isstarter ,isgrower ,isfinisher ,transportation ,
                            transferreason ,distancekm ,transferby_code ,transferby_name ,starter_qty
                            ,grower_qty ,finisher_qty ,paidamt, movementdate, iscrumbled, crumbled_qty);
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
                    toastMessage(s);
                    loadIFTFSpinner();
                }else {
                    loadIFTFSpinner();
                    toastMessage("No Batch Found");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void loadIFTFSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, IFTFNameArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iftf_spinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectIFTFSpinner();

    }
    private void selectIFTFSpinner(){
        iftf_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedWHName=selectedOrg;
                    selectedWHId= hashIFTFID.get(selectedWHName);
                    getIFTF(selectedWHId);
                    setTextField();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getIFTF(int IFTFID)
    {
        Cursor cursor ;
        cursor = sqliteDbHelper.getCbfIFTF(IFTFID);

        if(cursor.moveToFirst()){
            do{
                String IFTFName  = cursor.getString(0);
                //String IFTFNameID = cursor.getString(1);
                 orgname = cursor.getString(2);
                 description  = cursor.getString(3);
                 frombatch  = cursor.getString(4);
                 tobatch = cursor.getString(5);
                 frombpcode  = cursor.getString(6);
                 tobpcode = cursor.getString(7);
                 frombp  = cursor.getString(8);
                 tobp  = cursor.getString(9);
                 isstarter  = cursor.getString(10);
                 isgrower  = cursor.getString(11);
                 isfinisher  = cursor.getString(12);
                 transportation  = cursor.getString(13);
                 transferreason  = cursor.getString(14);
                 distancekm  = cursor.getString(15);
                 transferby_code  = cursor.getString(16);
                 transferby_name  = cursor.getString(17);
                 starter_qty  = cursor.getString(18);
                 grower_qty = cursor.getString(19);
                 finisher_qty  = cursor.getString(20);
                 paidamt  = cursor.getString(21);
                 movementdate = cursor.getString(22);
                 iscrumbled = cursor.getString(23);
                 crumbled_qty = cursor.getString(24);

            }while(cursor.moveToNext());
        }
        //loadIFTFSpinner();
    }


    private void setTextField(){
        String orgname1="",description1="";
        /*if(!orgname.equals("null") && orgname!= null && !orgname.isEmpty()) {
            orgname1=orgname;
        }
        if(!description.equals("null") && description!= null && !description.isEmpty()) {
            description1 = description;
        }*/

        orgname_txt.setText(orgname);
        BatchFrom_EditText.setText(frombatch);
        BatchTo_EditText.setText(tobatch);


        farmersCodeEditText.setText(frombpcode);
        farmersCodeToEditText.setText(tobpcode);
        farmersCodeByEditText.setText(transferby_code);

        visitDateEditText.setText(movementdate);
        mortalityEditText.setText(distancekm); //distance
        avgWeightEditText.setText(paidamt); // TransferCost
        suggestionEditText.setText(description); //description

        TransferReason_EditText.setText(transferreason);
        Transportation_EditText.setText(transportation);

        StarterQty_EditText.setText(starter_qty);
        GrowerQty_EditText.setText(grower_qty);
        FinisherQty_EditText.setText(finisher_qty);
        Grower_crumbleqty_Edittext.setText(crumbled_qty);

        farmernamevw.setText(frombp);
        farmernamevwTo.setText(tobp);
        farmernamevwBy.setText(transferby_name);

        if(isstarter=="Y"){
            Starter_checkbox.setChecked(true);
        }else{
            Starter_checkbox.setChecked(false);
        }
        if(isgrower=="Y"){
            Grower_checkbox.setChecked(true);
        }
        else{
            Grower_checkbox.setChecked(false);
        }
        if(isfinisher=="Y"){
            Finisher_checkbox.setChecked(true);
        }
        else{
            Finisher_checkbox.setChecked(false);
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
