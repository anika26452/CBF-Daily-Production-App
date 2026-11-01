package com.sysnova.cbfdailyproductionapp.Activity;

import static com.sysnova.cbfdailyproductionapp.Activity.Login.employeecode;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.employeeid;
import static com.sysnova.cbfdailyproductionapp.Activity.Login.employeename;
import static com.sysnova.cbfdailyproductionapp.Database.PostgreSQL.apiUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChangePassword extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    EditText confirm_password_editText,enterNew_password_editText,confirmNew_password_editText;
    Button goButton,confirmButton , cancelbtn;
    LinearLayout linearLayoutPanel, currpasslayout;
    String currentPassword = "",newPassword = "", pass;
    TextView namevw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Kazi Farms Egg Sales");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

        mDatabaseHelper = new DatabaseHelper(this);

        linearLayoutPanel =findViewById(R.id.change_pasword_layout);
        confirm_password_editText = findViewById(R.id.check_password_editText);
        enterNew_password_editText = findViewById(R.id.new_password_editText);
        confirmNew_password_editText = findViewById(R.id.confirm_new_password_editText);
        currpasslayout = findViewById(R.id.currentpasslayout);
        cancelbtn = findViewById(R.id.cancel_button);
        namevw = findViewById(R.id.name_vw);

        namevw.setText(employeename+"\n"+employeecode);

        goButton = findViewById(R.id.go_button);
        confirmButton = findViewById(R.id.confirm_password_button);

        buttonFunction();
    }

    private void buttonFunction(){
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goButton.setEnabled(false);
                currentPassword = confirm_password_editText.getText().toString();
                if(currentPassword.equals("")){
                    goButton.setEnabled(true);
                    confirm_password_editText.setError("Please Enter your current password");
                    return;
                }else {

                    AsyncSender task = new AsyncSender();
                    task.execute();
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass = enterNew_password_editText.getText().toString();
                newPassword = confirmNew_password_editText.getText().toString();

                if(pass.equals("")){
                    enterNew_password_editText.setError("Enter new password");
                    return;
                }

                if(!newPassword.equals(enterNew_password_editText.getText().toString())){
                    showAlert("Error","Passwords do not match!");
                    return;
                }

                if(currentPassword.equals(newPassword)){
                    showAlert("Error","New password cannot be the same as your old password");
                    return;
                }

                AsyncSender1 task = new AsyncSender1();
                task.execute();

            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, Menu.class);
                startActivity(intent);
            }
        });
    }

    private final class AsyncSender extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result="",error="";
            HttpURLConnection con = null;

            try {
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/UpdateOperation")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/UpdateOperation");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "checkPassword"));
                list.add(new BasicNameValuePair("name", employeeid.toLowerCase()));
                list.add(new BasicNameValuePair("pass",currentPassword));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                }else{
                    return "Success";
                }

            }

            catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error=e.getMessage();
                return  error;
            }finally {
                if(con!= null) {
                    con.disconnect();
                    con = null;
                }
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ChangePassword.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Checking");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Success")) {
              //  toastMessage("Password Matched");
                currpasslayout.setVisibility(View.GONE);
                linearLayoutPanel.setVisibility(View.VISIBLE);
            } else if (result.equals("Password Did not Matched!??")) {
                goButton.setEnabled(true);
                showAlert("Error","Incorrect password");
                linearLayoutPanel.setVisibility(View.INVISIBLE);
            } else {
                goButton.setEnabled(true);
                showAlert("Error","Something is Wrong!!");
                linearLayoutPanel.setVisibility(View.INVISIBLE);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private final class AsyncSender1 extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result="",error="";
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection) ( new URL(""+apiUrl+"/UpdateOperation")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/UpdateOperation");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "newPassword"));
                list.add(new BasicNameValuePair("name", employeeid.toLowerCase()));
                list.add(new BasicNameValuePair("pass",currentPassword));
                list.add(new BasicNameValuePair("newpass",newPassword));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.equals("PasswordUpdated")){
                    result=s;
                    return result;
                }else{
                    return "Success";
                }

            }

            catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error=e.getMessage();
                return  error;
            }finally {
                if(con!= null) {
                    con.disconnect();
                    con = null;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ChangePassword.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Updating");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Success")) {
                CreateComfirmation("Password Changed!!!","Please Login Again to Continue.");
            }
            else {
                toastMessage("Something went wrong");
            }
        }
    }

    public void CreateComfirmation(String head, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
        builder.setTitle(head);
        builder.setMessage(msg);
        builder.setCancelable(false);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        Intent intent = new Intent(ChangePassword.this, Login.class);
                        startActivity(intent);
                    }
                }
        );
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ChangePassword.this, Menu.class);
        startActivity(intent);
    }
    public void showAlert(String title,String message){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(ChangePassword.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }
}