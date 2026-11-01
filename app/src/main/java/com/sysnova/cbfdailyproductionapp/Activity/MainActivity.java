package com.sysnova.cbfdailyproductionapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.sysnova.cbfdailyproductionapp.R;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doprogress();
                startapp();
            }
        });
        thread.start();
    }

    private void startapp() {
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }

    private void doprogress() {

        for(progress = 33; progress<=99; progress = progress+33){
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}