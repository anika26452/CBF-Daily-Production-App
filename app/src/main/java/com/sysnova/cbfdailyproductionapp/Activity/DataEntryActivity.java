package com.sysnova.cbfdailyproductionapp.Activity;

import static com.sysnova.cbfdailyproductionapp.Activity.Login.backdays;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.employeecode;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.employeename;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.user_id;
import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sysnova.cbfdailyproductionapp.Database.DatabaseHelper;

import com.sysnova.cbfdailyproductionapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URL;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class DataEntryActivity extends AppCompatActivity {

    Spinner batchListSpinner,feedCodeListSpinner1,feedCodeListSpinner2,feedCodeListSpinner3, morreasonspinner, diseaseSpinner;
    Calendar nowcal;
    Button saveButton, refreshbtn, getlocbtn;
    EditText farmersCodeEditText,feedIntakeEditText,
            mortalityEditText, suggestionEditText,feedIntakeEditText2,feedIntakeEditText3,
            transitMortalityEditText, otherreasonedittext,  noofbirdsedit, totalweightedit;
    Double feedQty, percentage;
    TextView nametextvw,farmer_namevw,lattxtvw, longtxtvw, qtyTextView1,qtyTextView2,qtyTextView3,
            productionDateTextView, avgWeightTextvw, birdNumberEditText,ageEditText, visitdatetext, disteancevw;
    String placementdate = "",types_diseases, name_diseases, farmercode,farmername,c_bpartner_id, batch,m_batch_id, selectedBatchCode, batchId,movementdate,
            feedID = "",selectedFeedCode1, selectedFeedCode2, selectedFeedCode3,mortalitytxt,
            selectedMortReason,selectedDiseaseReason, productionDatevw="" , currentDate ,birdNumber,
            birdAge ,feedIntake1,feedIntake2,feedIntake3,suggestion,visitdate, productionDate  = "",
            feedName1,feedName2,feedName3,pDate,transitMortality = "",avlFeed1,otherMortReason="",
            avlFeed2,avlFeed3, totalwt="", noofbirds="", exist_lat="", exist_long="", distanceval = "1500", visitdateval ="" ;
    int  m_feed_ID = 0,  total_mortality = 0,mortality,transitmortality, ConvertQty;
    DatabaseHelper mDatabaseHelper;
    HashMap<String,Double> hashFeedQty = new HashMap<>();
    HashMap<String,String> hashBatchcodeID = new HashMap<>();
    HashMap<String,Integer> hashFeedID = new HashMap<>();
    GPSTracker gps;
    LinearLayout  trans_morlayout, reason_layout, birddetaillayoutvw, diseaseid, scan_qrbtn;
    ImageView fcodebtn;
    ArrayList<String> batchArrayList = new ArrayList<>();
    ArrayList<String> feedArrayList = new ArrayList<>();
    ArrayList<String> diseaseList = new ArrayList<>();
    ArrayList<String> mortreasonList = new ArrayList<>();
    ProgressDialog pd;
    private DatePickerDialog.OnDateSetListener dateListener;
    Location gps_loc, network_loc, final_loc,location;
    LocationManager locationManager;
    Double longitude_20 = 0.0, latitude_20 = 0.0;
    private static final DecimalFormat decfor = new DecimalFormat("0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date dateSql=new Date();
    Date finalDateSql = dateSql;
    Date prodDate;
    Double distancevalue= 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataentry);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

        nametextvw = findViewById(R.id.empname_textvw);
        farmersCodeEditText = findViewById(R.id.farmers_code_id);
        fcodebtn = findViewById(R.id.dealercode_btn);
        farmer_namevw = findViewById(R.id.farmer_name);
        lattxtvw = findViewById(R.id.lat_view);
        longtxtvw = findViewById(R.id.long_view);
        batchListSpinner = findViewById(R.id.batch_list_spinner);
        feedCodeListSpinner1   = findViewById(R.id.feed_list_spinner);
        feedCodeListSpinner2   = findViewById(R.id.feed_list_spinner2);
        feedCodeListSpinner3   = findViewById(R.id.feed_list_spinner3);
        qtyTextView1        = findViewById(R.id.qty1_textView);
        qtyTextView2        = findViewById(R.id.qty2_textView);
        qtyTextView3        = findViewById(R.id.qty3_textView);
        productionDateTextView = findViewById(R.id.placementDate_id);  //placementdatevw
        mortalityEditText   = findViewById(R.id.mortality_amount_id);
        morreasonspinner = findViewById(R.id.mortality_list_spinner);
        otherreasonedittext = findViewById(R.id.other_reason);
        getlocbtn = findViewById(R.id.getloc_btn_id);
        visitdatetext = findViewById(R.id.visitdate_id);
        feedIntakeEditText  = findViewById(R.id.intake_amount_id);
        feedIntakeEditText2 = findViewById(R.id.intake_amount_id_2);
        feedIntakeEditText3 = findViewById(R.id.intake_amount_id_3);
        noofbirdsedit = findViewById(R.id.noofbirds);
        totalweightedit = findViewById(R.id.total_weight);
        avgWeightTextvw   = findViewById(R.id.avg_weight);
        suggestionEditText  = findViewById(R.id.suggestion_id);
        trans_morlayout = findViewById(R.id.trans_mor_layout);
        transitMortalityEditText = findViewById(R.id.transit_mortality_amount_id);
        saveButton = findViewById(R.id.save_btn_id);
        birdNumberEditText  = findViewById(R.id.bird_number_id);
        ageEditText = findViewById(R.id.age_id);
        reason_layout = findViewById(R.id.reasonlayout);
        refreshbtn = findViewById(R.id.refresh_btn_id);
        birddetaillayoutvw = findViewById(R.id.birddetaillayout);
        diseaseSpinner = findViewById(R.id.disease_list_spinner);
        diseaseid = findViewById(R.id.reasonlayout_id);
        disteancevw = findViewById(R.id.distance_view);
        scan_qrbtn = findViewById(R.id.scan_qrbtn);
        trans_morlayout.setVisibility(View.GONE);
        nametextvw.setText(employeename+"\n"+employeecode);
        mDatabaseHelper = new DatabaseHelper(this);

        FetchMortalityReson();
        sendToServer();
        refreshbutton();

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String sdate = dayOfMonth+"/"+month+"/"+year;
                Date date1 = new Date(year-1900, month-1,dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                Date date3= date1;

                DataEntryActivity obj1 = new DataEntryActivity();
                Calendar someDate= obj1.dateToCalendar(new Date());

                someDate.add(Calendar.DAY_OF_YEAR, -5);
                Date date31 = someDate.getInstance().getTime();
                Date myDate = finalDateSql;
                Date prvDate3 = new Date(finalDateSql.getTime() - 432000000); // 5 * 24 * 60 * 60 * 1000

                if ((date3.equals(myDate) || date3.before(myDate)) && (date3.after(prvDate3))){
                    visitdate = formatter.format(date1);
                    visitdatetext.setText(visitdate);
                    String newdate = sdf.format(new Date());
                    if(visitdate.equals(sdf.format(new Date()))){
                        birddetaillayoutvw.setVisibility(View.VISIBLE);
                    }else{
                       birddetaillayoutvw.setVisibility(View.GONE);
                    }
                }else {
                    showAlert("Warning", "Visit Date is not valid. You can entry data only for today and previous 5 days.");
                }
            }
        };

        fcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmercode = farmersCodeEditText.getText().toString();
                AsyncSyncBatchCode asyncSyncBatch = new AsyncSyncBatchCode();
                asyncSyncBatch.execute();
            }
        });

        totalweightedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalwt = totalweightedit.getText().toString();
                noofbirds = noofbirdsedit.getText().toString();

                if(!totalwt.equals("") && !noofbirds.equals("")){
                    Double totalwtint  = Double.valueOf(totalwt);
                    int noofbirdsint = Integer.parseInt(noofbirds);

                    Double avdwtint = totalwtint/Double.valueOf(noofbirdsint);

                //    avgWeightTextvw.setText(Double.toString((double)Math.round(avdwtint * 1000.0) / 1000.0));
                    avgWeightTextvw.setText( String.format(Locale.US, "%.2f", Math.round(avdwtint * 100.0) / 100.0));
                }
            }
        });

        noofbirdsedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalwt = totalweightedit.getText().toString();
                noofbirds = noofbirdsedit.getText().toString();

                if(!totalwt.equals("") && !noofbirds.equals("")){
                    Double totalwtint  = Double.valueOf(totalwt);
                    int noofbirdsint = Integer.parseInt(noofbirds);

                    Double avdwtint = totalwtint/Double.valueOf(noofbirdsint);

                    avgWeightTextvw.setText( String.format(Locale.US, "%.2f", Math.round(avdwtint * 100.0) / 100.0));
                }
            }
        });

        getlocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        /**added on 2025-08-04 by anika **/
        scan_qrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(DataEntryActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();
            }
        });
        /**added on 2025-08-04 by anika **/
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(DataEntryActivity.this,Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void refreshbutton(){
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    private void GetMortality(){
        if(!mortreasonList.isEmpty()){
            mortreasonList.clear();
        }

        mortreasonList.add("---Select Disease---");

        Cursor data = mDatabaseHelper.gettypesdiseases();

        while(data.moveToNext()){
            mortreasonList.add(data.getString(0));
        }
        mortreasonList.add("Other");
        loadMortalitySpinner();
    }

    private void sendToServer(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String avlQty1,avlQty2,avlQty3;

                farmercode = farmersCodeEditText.getText().toString();

                if(farmercode.equals("")||farmercode.isEmpty()){
                    farmersCodeEditText.setError("Please Enter farmer code");
                    return;
                }

                if(latitude_20 <= 0.00 || longitude_20 <= 0.00){
                    toastMessage("Please press GEO Location button");
                    return;
                }

//                if(exist_lat.equals("null") || exist_long.equals("null")){
//                    toastMessage("The location of this farmer is not found in the server.");
//                    return;
//                }

                birdNumber   = birdNumberEditText.getText().toString();
                birdAge      = ageEditText.getText().toString();
                feedIntake1  = feedIntakeEditText.getText().toString();
                feedIntake2  = feedIntakeEditText2.getText().toString();
                feedIntake3  = feedIntakeEditText3.getText().toString();
                mortalitytxt = mortalityEditText.getText().toString();
                suggestion   = suggestionEditText.getText().toString();
                feedName1    = feedCodeListSpinner1.getSelectedItem().toString();
                feedName2    = feedCodeListSpinner2.getSelectedItem().toString();
                feedName3    = feedCodeListSpinner3.getSelectedItem().toString();
                transitMortality = transitMortalityEditText.getText().toString();
                visitdateval = visitdatetext.getText().toString();
                pDate        = visitdatetext.getText().toString();
                avlFeed1  = qtyTextView1.getText().toString();
                avlFeed2  = qtyTextView2.getText().toString();
                avlFeed3  = qtyTextView3.getText().toString();
                avlQty1 = qtyTextView1.getText().toString();
                avlQty2 = qtyTextView2.getText().toString();
                avlQty3 = qtyTextView3.getText().toString();
                totalwt = totalweightedit.getText().toString();
                noofbirds = noofbirdsedit.getText().toString();

                Cursor cursor = mDatabaseHelper.checkLastEntry(batchId,visitdateval);

                if(cursor.moveToNext()){
                    showAlert("Error", "An entry is already saved for this farmer's batch for the Production Date");
                    return;
                }

                if(!mortalitytxt.equals("0") && !mortalitytxt.equals("")){
                    if(!selectedMortReason.equals("Other")){
                        if(selectedDiseaseReason.equals("---Select Reason---")) {
                            toastMessage("Please select Mortality Reason");
                            return;
                        }
                    }
                }

                if(selectedMortReason.equals("Other")){
                    selectedDiseaseReason = otherreasonedittext.getText().toString();
                }

                if(!mortalitytxt.equals("0") && !mortalitytxt.equals("")){
                    if(selectedMortReason.equals("---Select Disease---")){
                        toastMessage("Please select type of Diseases");
                        return;
                    }
                }

                if(selectedBatchCode.equals("-- select batch --")){
                    toastMessage("Please select a batch");
                    return;
                }
                try {
                    prodDate = sdf.parse(productionDatevw.substring(0, 10));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if(sdf.format(prodDate).equals(sdf.format(new Date()))){
                    if(noofbirds.isEmpty()){
                        noofbirdsedit.setError("Please Enter Bird's number");
                        return;
                    }
                    if(totalwt.isEmpty()){
                        totalweightedit.setError("Please Enter Bird's total weight");
                        return;
                    }
                }
                if(birdNumber.isEmpty()){
                    birdNumberEditText.setError("Please Enter Bird Number");
                    return;
                }
                if(birdAge.isEmpty()){
                    birdNumberEditText.setError("Please Enter Bird Age");
                    return;
                }
                if(mortalitytxt.isEmpty()){
                    toastMessage("Please Enter mortality");
                    mortalityEditText.setError("Please Enter mortality");
                    return;
                }

                if(birdAge.equals("1")){
                    if(transitMortality.equals("")) {
                        transitMortalityEditText.setError("Please Enter mortality");
                        return;
                    }
                }

                if(visitdateval.equals("")){
                    visitdatetext.setError("Please select the date");
                    return;
                }
                //anika
                total_mortality =  Integer.valueOf(mortalitytxt) +  Integer.valueOf(transitMortality);

                percentage =  (Double.parseDouble(birdNumber) * 30) /100.00 ;


                if(Double.valueOf(total_mortality) > percentage){
                    toastMessage("Mortality can not be greater than Bird Number");
                    return;
                }

          /*
                //eita checking open kore dte hbe
                try {
                    if(sdf.parse(productionDatevw).compareTo(nowcal.getTime()) < 0){
                   //     toastMessage(String.valueOf(nowcal));
                        toastMessage("You are not allowed to enter data for this date");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                */
                //anika

                if(Integer.parseInt(birdNumber) - Integer.parseInt(mortalitytxt) < 0){
                    toastMessage("Check Mortality can not greater than Bird number");
                    return;
                }


                if(feedName1.equals("-- Select Feed --") && feedName2.equals("-- Select Feed --") && feedName3.equals("-- Select Feed --") ){
                    toastMessage("Select at least one feed");
                    return;
                }

                if(!feedName1.equals("-- Select Feed --")) {
                    if (feedName1.equals(feedName2) || feedName1.equals(feedName3)) {
                        if (feedName2.equals("-- Select Feed --") && feedName3.equals("-- Select Feed --")) {

                        } else {
                            toastMessage("Duplicate Feed Code");
                            return;
                        }
                    }
                }

                if(!feedName2.equals("-- Select Feed --")) {
                    if (feedName2.equals(feedName1) || feedName2.equals(feedName3)) {

                        if (feedName1.equals("-- Select Feed --") && feedName3.equals("-- Select Feed --")) {

                        } else {
                            toastMessage("Duplicate Feed Code");
                            return;
                        }
                    }
                }

                if(!feedName3.equals("-- Select Feed --")) {
                    if (feedName3.equals(feedName1) || feedName3.equals(feedName2)) {
                        if (feedName2.equals("-- Select Feed --") && feedName1.equals("-- Select Feed --")) {

                        } else {
                            toastMessage("Duplicate Feed Code");
                            return;
                        }
                    }
                }

                if(!feedName1.equals("-- Select Feed --")) {
                    if (feedIntake1.isEmpty()) {
                        feedIntakeEditText.setError("Please Enter Feed Consumption");
                        return;
                    }
                }
                if(!feedName2.equals("-- Select Feed --")) {
                    if (feedIntake2.isEmpty()) {
                        feedIntakeEditText2.setError("Please Enter Feed Consumption");
                        return;
                    }
                }
                if(!feedName3.equals("-- Select Feed --")) {
                    if (feedIntake3.isEmpty()) {
                        feedIntakeEditText3.setError("Please Enter Feed Consumption");
                        return;
                    }
                }

                if(birdAge.equals("1")){
                    if(transitMortality.equals("")){
                        transitMortalityEditText.setError("Please Enter Transit Mortality");
                        return;
                    }
                }

                if(!feedIntake1.isEmpty()){
                    if(Double.parseDouble(avlQty1) - Double.parseDouble(feedIntake1) < 0){
                        toastMessage("Feed Intake can not greater than Available Qty");
                        return;
                    }
                }

                if(!feedIntake2.isEmpty()){
                    if(Double.parseDouble(avlQty2) - Double.parseDouble(feedIntake2) < 0){
                        toastMessage("Feed Intake can not greater than Available Qty");
                        return;
                    }
                }

                if(!feedIntake3.isEmpty()){
                    if(Double.parseDouble(avlQty3) - Double.parseDouble(feedIntake3) < 0){
                        toastMessage("Feed Intake can not greater than Available Qty");
                        return;
                    }
                }

                try {
                    if(sdf.parse(visitdateval).compareTo(sdf.parse(pDate)) < 0){
                        toastMessage("Production date must greater or equal than Placement Date");
                        return;
                    }

                }catch (Exception ex){
                    String exc = ex.getMessage().toString();
                    System.out.println(exc);
                }
/*
                if(!exist_lat.equals("null") && !exist_long.equals("null")){
                    Double distancetotal = distance(Double.parseDouble(exist_lat), Double.parseDouble(exist_long), latitude_20, longitude_20);

                if(distancetotal > Double.parseDouble(distanceval)){
                        showAlert("Warning","This entry cannot be saved. You are more than "+distanceval+" meters away from the farmer's location.");
                        return;
                    }
                }
*/

                AsyncSendToServer asyncSendToServer = new AsyncSendToServer();
                asyncSendToServer.execute();
            }
        });

    }

    private void selectBatchIDSpinner(){
        batchListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedBatchCode = parent.getSelectedItem().toString();
                    productionDateTextView.setText("");
                    visitdatetext.setText("");
                    birdNumberEditText.setText("");
                    ageEditText.setText("");

                    if(!selectedBatchCode.equals("-- select batch --")){
                        batchId = hashBatchcodeID.get(selectedBatchCode);

                        AsyncLoadBird task1 = new AsyncLoadBird();
                        task1.execute(String.valueOf(batchId));

                        AsyncLoadFeed task = new AsyncLoadFeed();
                        task.execute(String.valueOf(batchId));
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
    private final class AsyncLoadBird extends AsyncTask<String,String,String> {

        int birdage = 0,birdnumber = 0,qtyonhand;
        String productionDate = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
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
                list.add(new BasicNameValuePair("name", "loadbird"));
                list.add(new BasicNameValuePair("batchId", batchId));

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

                    if(line.size() > 2){
                        birdnumber = Integer.parseInt((String) line.get(0));
                        //anika on 2023-04-05
                        productionDatevw = (String) line.get(1);
                        Date ptiondate = sdf.parse(productionDatevw.substring(0, 10));

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(ptiondate);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        productionDate = sdf.format(calendar.getTime());
                        //     productionDate = productionDatevw;
                        //anika on 2023-04-05
                        productionDatevw = productionDate;  //anika on 2025-04-16
                        birdage = Integer.parseInt((String) line.get(2));
                        mortality = Integer.parseInt((String) line.get(3));
                        transitmortality = Integer.parseInt((String)line.get(4));
                        ConvertQty = Integer.parseInt((String)line.get(5));
                        placementdate = (String)line.get(6);
                        qtyonhand = Integer.parseInt((String)line.get(7));
                        movementdate = (String)line.get(8);

                        if(birdnumber == 0){
                            result = "Batch Closed";
                        }else{
                            result = "Bird-Sync-Successful";
                        }
                    }else{
                        qtyonhand = Integer.parseInt((String) line.get(0));
                        movementdate = (String) line.get(1);

                        if (birdnumber == 0) {
                            result = "Batch Closed";
                        } else {
                            result = "Bird-Sync-Successful";
                        }
                    }
                }

                Cursor data = mDatabaseHelper.checkLastProductionEntry(batchId,c_bpartner_id);
                while(data.moveToNext()){
                    birdage = data.getInt(0);
                    birdnumber = data.getInt(1);
                    //anika on 2023-04-05
                    productionDatevw = data.getString(2);
                    Date ptiondatevw = sdf.parse(productionDatevw.substring(0, 10));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(ptiondatevw);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    productionDate = sdf.format(calendar.getTime());

                    productionDatevw = productionDate;  //anika on 2025-04-16
                    //anika on 2023-04-05
                    mortality    = data.getInt(3);
                    transitmortality = data.getInt(4);
                    placementdate = data.getString(5);
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
                if(s.equals("Bird-Sync-Successful")){
                    toastMessage(s);
                 //   birdage = birdage + 1;
                    /** age calculation by anika on 2025-02-19**/
                    Date prodDate = sdf.parse(productionDate.substring(0, 10));
                    Date placeDate = sdf.parse(placementdate);

                    long diffInMillis = prodDate.getTime() - placeDate.getTime();

                    long bAge = TimeUnit.MILLISECONDS.toDays(diffInMillis) + 1;

            //        long bAge = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                    birdage = (int)bAge;
                    /** age calculation by anika on 2025-02-19**/

                    birdnumber = birdnumber - ((mortality + transitmortality)-ConvertQty);

                    productionDate = productionDate.replaceAll("\\d\\d:\\d\\d:\\d\\d","");
                    productionDatevw = productionDate;

                    try {
                        productionDateTextView.setText(placementdate.substring(0,10));
                        visitdatetext.setText(productionDate.substring(0, 10));
                        try {
                            prodDate = sdf.parse(productionDate.substring(0, 10));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if(sdf.format(new Date()).equals(sdf.format(prodDate))){
                            birddetaillayoutvw.setVisibility(View.VISIBLE);
                        }else{
                            birddetaillayoutvw.setVisibility(View.GONE);
                        }
                        nowcal = Calendar.getInstance();
                        currentDate = sdf.format(new Date());
                        nowcal.setTime(sdf.parse(currentDate));
                        nowcal.add(Calendar.DATE, -(backdays+1));

                    }catch (Exception ex){
                        toastMessage("Date Format Exception");
                    }

                    if(birdage == 1){
                        trans_morlayout.setVisibility(View.VISIBLE);
                    }
                    birdNumberEditText.setText(String.valueOf(birdnumber));
                    ageEditText.setText(String.valueOf(birdage));
                    transitMortalityEditText.setEnabled(false);
                    transitMortalityEditText.setText("0");

                }else if(s.equals("First-Bird-Sync-Successful")){
                    birdage = 1;

                    birdNumberEditText.setText(String.valueOf(birdnumber));
                    ageEditText.setText(String.valueOf(birdage));
                    productionDate = productionDate.replaceAll("\\d\\d:\\d\\d:\\d\\d","");
                    productionDatevw = productionDate;
                    placementdate = productionDate;
                    productionDateTextView.setText(productionDate);
                    visitdatetext.setText(productionDate.substring(0, 10));  //added by anika on 2025-06-26
                    transitMortalityEditText.setEnabled(true);
                    if(birdage == 1){
                        trans_morlayout.setVisibility(View.VISIBLE);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     //anika
                    nowcal = Calendar.getInstance();
                    currentDate = sdf.format(new Date());
                    nowcal.setTime(sdf.parse(currentDate));
                    nowcal.add(Calendar.DATE, -(backdays+1));

                }else if(s.equals("Batch Closed")){
                    toastMessage(s);
                    saveButton.setEnabled(false);
                } else {
                    toastMessage("No Bird Found");
                    birdNumberEditText.setText("");
                    ageEditText.setText("");
                    productionDateTextView.setText("");
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private final class AsyncLoadFeed extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            feedArrayList.clear();
            feedArrayList.add("-- Select Feed --");
            String result = "";
            hashFeedQty.clear();
            hashFeedID.clear();
            HttpURLConnection con = null;
            boolean res = false;

            try{
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "loadfeed"));
                list.add(new BasicNameValuePair("batchId", batchId));

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
                    feedID = (String) line.get(0);
                    m_feed_ID = Integer.parseInt((String) line.get(1));
                    feedQty = Double.parseDouble((String) line.get(2));

                    feedArrayList.add(feedID);
                    hashFeedQty.put(feedID, feedQty);
                    hashFeedID.put(feedID, m_feed_ID);
                    res = mDatabaseHelper.insertIntoFeedDetails(feedID, m_feed_ID);
                }

                if(res){
                    result = "Feed-Sync-Successful";
                }

                Cursor data = mDatabaseHelper.checkAvlFeed(batchId,c_bpartner_id);
                    while (data.moveToNext()){
                        String avl1 = data.getString(0);
                        String avl2 = data.getString(1);
                        String avl3 = data.getString(2);
                        String qty1 = data.getString(3);
                        String qty2 = data.getString(4);
                        String qty3 = data.getString(5);
                        String code1 = data.getString(6);
                        String code2 = data.getString(7);
                        String code3 = data.getString(8);
                        if(!avl1.equals("0")){
                            double lastAvl1 = Double.parseDouble(avl1) - Double.parseDouble(qty1);
                            if(!code1.equals("-- Select Feed --")){
                                hashFeedQty.put(code1,lastAvl1);
                            }
                        }
                        if(!avl2.equals("0")){
                            double lastAvl2 = Double.parseDouble(avl2) - Double.parseDouble(qty2);
                            if(!code2.equals("-- Select Feed --")) {
                                hashFeedQty.put(code2, lastAvl2);
                            }
                        }
                        if(!avl3.equals("0")){
                            double lastAvl3 = Double.parseDouble(avl3) - Double.parseDouble(qty3);
                            if(!code3.equals("-- Select Feed --")) {
                                hashFeedQty.put(code3, lastAvl3);
                            }
                        }
                    }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (ClientProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
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
                if(s.equals("Feed-Sync-Successful")){
                    toastMessage(s);
                    loadFeedSpinner(feedArrayList);
                }else {
                    toastMessage("No Feed Found");
                    loadFeedSpinner(feedArrayList);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void SelectFeedSpinnerItem(){

        feedCodeListSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedFeedCode1 = parent.getSelectedItem().toString();
                    if(selectedFeedCode1.equals("-- Select Feed --")){
                        qtyTextView1.setText("");
                    }else{
                        double qty = hashFeedQty.get(selectedFeedCode1);
                        qtyTextView1.setText(String.valueOf(qty));
                    }

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        feedCodeListSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedFeedCode2 = parent.getSelectedItem().toString();
                    if(selectedFeedCode2.equals("-- Select Feed --")){
                        qtyTextView2.setText("");
                    }else{
                        double qty = hashFeedQty.get(selectedFeedCode2);
                        qtyTextView2.setText(String.valueOf(qty));
                    }


                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        feedCodeListSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedFeedCode3 = parent.getSelectedItem().toString();
                    if(selectedFeedCode3.equals("-- Select Feed --")){
                        qtyTextView3.setText("");
                    }else{
                        double qty = hashFeedQty.get(selectedFeedCode3);
                        qtyTextView3.setText(String.valueOf(qty));
                    }

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(DataEntryActivity.this);
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
        Toast toast = Toast.makeText(DataEntryActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(DataEntryActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void loadBatchSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, batchArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        batchListSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectBatchIDSpinner();
    }

    private void loadFeedSpinner(List<String> feedArrayList){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,feedArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedCodeListSpinner1.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,feedArrayList);

        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedCodeListSpinner2.setAdapter(dataAdapter2);
        dataAdapter2.notifyDataSetChanged();

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,feedArrayList);

        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedCodeListSpinner3.setAdapter(dataAdapter3);
        dataAdapter3.notifyDataSetChanged();

        qtyTextView1.setText("");
        qtyTextView2.setText("");
        qtyTextView3.setText("");

        SelectFeedSpinnerItem();
    }
    private void loadDiseaseSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, diseaseList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        diseaseSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectDiseaseSpinner();
    }
    private void loadMortalitySpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mortreasonList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        morreasonspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectMortalitySpinner();
    }
    private void FetchMortalityReson(){
        AsyncMortalityReson asyncSendToServer = new AsyncMortalityReson();
        asyncSendToServer.execute();
    }

    private void selectMortalitySpinner(){
        morreasonspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedMortReason = parent.getSelectedItem().toString();
                    if(selectedMortReason.equals("Other")){
                        reason_layout.setVisibility(View.VISIBLE);
                        diseaseid.setVisibility(View.GONE);
                    }else {
                        diseaseid.setVisibility(View.VISIBLE);
                        reason_layout.setVisibility(View.GONE);
                        if(!diseaseList.isEmpty()){
                            diseaseList.clear();
                        }
                        diseaseList.add("---Select Reason---");
                        Cursor cr = mDatabaseHelper.getnamesdiseases(selectedMortReason);
                        while(cr.moveToNext()){
                            diseaseList.add(cr.getString(0));
                        }
                        loadDiseaseSpinner();
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
    private void selectDiseaseSpinner(){
        diseaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedDiseaseReason = parent.getSelectedItem().toString();

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

            if(feedIntake1.equals("")){
                feedIntake1 = "0";  //changed by anika on 31-10-2023
            }
            if(feedIntake2.equals("")){
                feedIntake2 = "0";  //changed by anika on 31-10-2023
            }
            if(feedIntake3.equals("")){
                feedIntake3 = "0";  //changed by anika on 31-10-2023
            }

            if(feedName1.equals("-- Select Feed --")){
                avlFeed1 = "0";
            }

            if(feedName2.equals("-- Select Feed --")){
                avlFeed2 = "0";
            }

            if(feedName3.equals("-- Select Feed --")){
                avlFeed3 = "0";
            }

            if(transitMortality.equals("")){
                transitMortality = "0";
            }

            if(selectedMortReason.equals("---Select Disease---")){
                selectedMortReason = "";
            }

            if(selectedDiseaseReason.equals("---Select Reason---")){
                selectedDiseaseReason = "";
            }
//            if(selectedMortReason.equals("Other")){
//                mortreason = selectedMortReason;
//            }else{
//                mortreason = otherMortReason;
//            }

            Cursor data = mDatabaseHelper.checkEntry(batchId,c_bpartner_id);
            if(data.moveToNext()){
                String ageOfBird = data.getString(0);
                if(ageOfBird.equals(birdAge)){
                    result = "Can not do multiple entry for same age";
                }else{
                    boolean res = mDatabaseHelper.insertIntoLastProductionTable(user_id,farmercode, farmername,batch,productionDatevw,birdNumber,birdAge,mortalitytxt,"",feedName1,feedName2,feedName3,feedIntake1,feedIntake2,feedIntake3,batchId,placementdate,placementdate,suggestion,transitMortality,c_bpartner_id,avlFeed1,avlFeed2,avlFeed3, String.valueOf(latitude_20), String.valueOf(longitude_20), selectedMortReason, noofbirds,totalwt , selectedDiseaseReason);
                    if(res){
                        result = "Insertion-Successful";
                    }else{
                        result = "Insertion in sqlite is failed";
                    }
                }
            }else{
                boolean res = mDatabaseHelper.insertIntoLastProductionTable(user_id,farmercode, farmername,batch,productionDatevw,birdNumber,birdAge,mortalitytxt,"",feedName1,feedName2,feedName3,feedIntake1,feedIntake2,feedIntake3,batchId,placementdate,placementdate,suggestion,transitMortality,c_bpartner_id,avlFeed1,avlFeed2,avlFeed3, String.valueOf(latitude_20), String.valueOf(longitude_20), selectedMortReason,noofbirds, totalwt , selectedDiseaseReason);
                if(res){
                    result = "Insertion-Successful";
                }else{
                    result = "Insertion in sqlite is failed";
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
                    refresh();
                }else {
                    showAlert("Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void refresh(){
        batchArrayList.clear();
        farmersCodeEditText.setText("");
        trans_morlayout.setVisibility(View.GONE);
        farmersCodeEditText.setEnabled(true);
        farmer_namevw.setText("");
        birdNumberEditText.setText("");
        ageEditText.setText("");
        feedIntakeEditText.setText("");
        feedIntakeEditText2.setText("");
        feedIntakeEditText3.setText("");
        mortalityEditText.setText("");
        avgWeightTextvw.setText("");
        suggestionEditText.setText("");
        transitMortalityEditText.setText("");
        productionDateTextView.setText("");
        lattxtvw.setText("");
        longtxtvw.setText("");
        visitdatetext.setText("");
        feedArrayList.clear();
        noofbirdsedit.setText("");
        totalweightedit.setText("");
        qtyTextView1.setText("");
        qtyTextView2.setText("");
        qtyTextView3.setText("");
        loadBatchSpinner();
        loadMortalitySpinner();
        loadFeedSpinner(feedArrayList);
    }

    private final class AsyncMortalityReson extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(DataEntryActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Mortality Reasons are Loading ... ...");
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
            mDatabaseHelper.deletediseases();

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "Mortality"));

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
                    types_diseases=(String) line.get(0);
                    name_diseases=(String) line.get(1);

                    res = mDatabaseHelper.insertintoMortality(types_diseases,name_diseases);

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
                    GetMortality();
                }else {
                    GetMortality();
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }
    private final class AsyncSyncBatchCode extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(DataEntryActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Batches are Loading ... ...");
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

            if(!batchArrayList.isEmpty()){
                batchArrayList.clear();
            }

            batchArrayList.add("-- select batch --");

            try{
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "farmer"));
                list.add(new BasicNameValuePair("farmercode", farmercode));

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
                    c_bpartner_id=(String) line.get(1);
                    batch = (String) line.get(2);
                    m_batch_id=(String) line.get(3);
                    exist_lat  =(String) line.get(4);
                    exist_long = (String) line.get(5);

                    batchArrayList.add(batch);
                    hashBatchcodeID.put(batch, m_batch_id);

                    res = mDatabaseHelper.insertIntoBatchDetails(batch,m_batch_id);

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
                    farmer_namevw.setText(farmername);
                    loadBatchSpinner();
                }else {
                    farmer_namevw.setText("");
                    batchArrayList.clear();
                    loadBatchSpinner();
                    toastMessage("No Farmer Found");
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

    public void getLocation(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new androidx.appcompat.app.AlertDialog.Builder(DataEntryActivity.this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel,null)
                    .show();
        }else {
            try {
                gps = new GPSTracker(DataEntryActivity.this);
                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (network_loc != null) {
                final_loc = network_loc;
                latitude_20 = final_loc.getLatitude();
                longitude_20 = final_loc.getLongitude();
            } else if (gps != null) {
                latitude_20 = gps.getLatitude();
                longitude_20 = gps.getLongitude();
            } else if (location != null) {
                final_loc = location;
                latitude_20 = final_loc.getLatitude();
                longitude_20 = final_loc.getLongitude();
            } else {
                latitude_20 = 0.0;
                longitude_20 = 0.0;
            }

            lattxtvw.setText(String.valueOf(latitude_20));
            longtxtvw.setText(String.valueOf(longitude_20));

            if(!exist_lat.equals("null") && !exist_long.equals("null")){
                distancevalue = distance(Double.parseDouble(exist_lat), Double.parseDouble(exist_long), latitude_20, longitude_20);

                if(distancevalue > Double.parseDouble(distanceval)){
                    disteancevw.setText(String.valueOf(decfor.format(distancevalue))+" meters");
                }

                disteancevw.setText(String.valueOf(decfor.format(distancevalue))+" meters");
            }
        }
    }

/*    public double distance(Double exist_lat, Double exist_long, Double lat, Double longi) {

        final int R = 6371;

        double latDistance = Math.toRadians(exist_lat - lat);
        double lonDistance = Math.toRadians(exist_long - longi);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(exist_lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
*/
    public double distance(double existLat, double existLon, double lat, double lon) {
        final int R = 6371; // Radius of the Earth in km

        double latDistance = Math.toRadians(lat - existLat);
        double lonDistance = Math.toRadians(lon - existLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(existLat)) * Math.cos(Math.toRadians(lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000; // returns meters
    }

    /**added on 2025-08-04 by anika **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                CreateAlert("Didn't match!", "QR Code didn't match for this batch!!!");
            } else {
                CreateAlert("QR Code is Matched Successfully!", intentResult.getContents());
            }
        } else {

        }
    }

    public void CreateAlert(String replyqtyval,String replyrateval){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DataEntryActivity.this);
        builder1.setTitle(replyqtyval);
        builder1.setMessage(replyrateval);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        boolean res = false;
                    }
                });

        AlertDialog alert = builder1.create();
        alert.show();
    }
    /**added on 2025-08-04 by anika **/
}
