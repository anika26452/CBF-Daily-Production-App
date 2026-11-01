package com.sysnova.cbfdailyproductionapp.Activity;

import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Login extends AppCompatActivity {

    EditText userNameEditText,passwordEditText;
    CheckBox rememberCkBox;
    Button loginButton;
    String password;
    private SharedPreferences rememberLoginPreference;
    private SharedPreferences.Editor rememberLoginPreferenceEditor;
    boolean rememberLogin = false;
    String logged_user_id="";
    public static String uName = "";

    DatabaseHelper mDatabaseHelper;
    ProgressDialog pd;
    public static String C_Bpartner_ID="", employeeid="",currentdate="",ad_user_id = "",userName="",feed_org_id="",userbp_id="", employeename ="",employeecode = "";
    public static int backdays = 0, user_id=0;
    String[] permissionsRequired = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        rememberLoginPreference = getSharedPreferences("loginPrefsCustomerVisit",MODE_PRIVATE);
        rememberLoginPreferenceEditor = rememberLoginPreference.edit();

        mDatabaseHelper = new DatabaseHelper(this);

        initializeUI();
        checkPermissionMethod();
        clickLoginButton();
        loadFromSharedPreferences();

//        Cursor isotp = mDatabaseHelper.getOTPSize();
//
//        if(isotp.moveToFirst()) {
//            userNameEditText.setEnabled(false);
//            userNameEditText.setText(isotp.getString(1));
//        }else{
//
//        }
    }

    private void initializeUI(){
        userNameEditText = findViewById(R.id.employeeID_editText);
        passwordEditText = findViewById(R.id.password_editText);
        rememberCkBox = findViewById(R.id.remember_checkbox);
        loginButton   = findViewById(R.id.login_button);
    }

    private void checkPermissionMethod(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Login.this, permissionsRequired, 1);
                return;
            }
        }
    }

    private void loadFromSharedPreferences(){
        rememberLogin = rememberLoginPreference.getBoolean("rememberLogin",false);
        if(rememberLogin){
            userNameEditText.setText(rememberLoginPreference.getString("userName",""));
            passwordEditText.setText(rememberLoginPreference.getString("password",""));
            rememberCkBox.setChecked(true);
        }
    }

    private void clickLoginButton(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                userName = userNameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                employeeid = userNameEditText.getText().toString().trim();

                if(userName.isEmpty()){
                    userNameEditText.setError("Please Enter user Name");
                    return;
                }

                if(password.isEmpty()){
                    passwordEditText.setError("Please Enter Password");
                    return;
                }

                if(rememberCkBox.isChecked()){
                    rememberLoginPreferenceEditor.putBoolean("rememberLogin",true);
                    rememberLoginPreferenceEditor.putString("userName",userName);
                    rememberLoginPreferenceEditor.putString("password",password);
                    rememberLoginPreferenceEditor.commit();
                }else {
                    rememberLoginPreferenceEditor.clear();
                    rememberLoginPreferenceEditor.commit();
                }

                boolean net=internetIsConnected();
                if (!net) {
                    ShowAlert("Warning","Please Check Internet Connection...");
                    loginButton.setEnabled(true);
                }else{
                    AysncLogin task = new AysncLogin();
                    task.execute();
                }
            }
        });
    }

    private final class AysncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String result="",error="";
            boolean r=false;
            HttpURLConnection con = null;
            String app_v = getResources().getString(R.string.app_v);

            try {
                mDatabaseHelper.deleteEmployee();
            } catch (Exception e) {

            }

            try {
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/Login")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/Login");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", employeeid.toLowerCase().trim()));
                list.add(new BasicNameValuePair("pass",password));
                list.add(new BasicNameValuePair("app_version",app_v));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.startsWith("[")){
                    result=s;
                    Log.d("DEBUG","Result "+result);
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
                    ad_user_id=(String) line.get(0);
                    backdays = Integer.parseInt((String) line.get(1));
                    C_Bpartner_ID=(String) line.get(2);
                    currentdate=(String) line.get(3);
                    feed_org_id=(String)line.get(4);
                    employeename=(String)line.get(5);
                    employeecode= (String)line.get(6);

                    userbp_id=C_Bpartner_ID;

                    logged_user_id=ad_user_id;
                    user_id = Integer.parseInt(ad_user_id);
                    uName = userName;

                    r= true;
                }

                if (r) {
                    result = "login-successful";
                }else{
                    result = "Unauthorized UserName or Wrong-Password";
                    return error;
                }
            }
            catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error="Failed To Connect";
                result = "Failed To Connect";
                return  error;
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
                loginButton.setEnabled(true);
                if(s.equals("login-successful")) {
                    finish();
                    Intent activityintent = new Intent(Login.this, Menu.class);
                    activityintent.putExtra("LOGGED_USER_ID",logged_user_id);
                    activityintent.putExtra("userName",userName);
                    activityintent.putExtra("employeename", employeename);
                    startActivity(activityintent);

                }else if(s.equals("Username or Password did not match!")){
                    showAlert("Login Failed","This user name or password does not match.");
                }else if(s.startsWith("You are using wrong version")){
                    showAlert("Login Failed", s+".");
                }else if(s.equals("Phone number does not match registered number.")){
                    showAlert("Login Failed", "Phone number does not match registered number.");
                }else if(s.equals("There is no registered phone number")){
                    showAlert("Login Failed", "There is no registered phone number");
                }else if(s.equals("This user has no partner code.")){
                    showAlert("Login Failed", "This user has no partner code.");
                }else if(s.equals("Failed To Connect")){
                    showAlert("Warning","Please Check Internet Connection...");
                }
                else {
                    showAlert("Login Failed",s+"!");
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
            while ((line=bufferedReader.readLine())!=null)
            {
                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e)
        {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;
    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(Login.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(Login.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Logging in.....");
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
        finishAffinity();
    }

    public void ShowAlert(String title,String msg){

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.image);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public boolean internetIsConnected() {
        try {
     //       String command = "ping -w 1 -c 1 172.16.9.67";
            //  String command = "ping -w 1 -c 1 119.40.85.53";
            String command = "ping -w 1 -c 1 webapps.kflapps.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
