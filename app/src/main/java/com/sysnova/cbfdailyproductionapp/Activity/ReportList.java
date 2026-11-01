package com.sysnova.cbfdailyproductionapp.Activity;


import static com.sysnova.cbfdailyproductionapp.Activity.Login.uName;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.cbfdailyproductionapp.Adapter.SurveyModalRecyclerViewAdapter;
import com.sysnova.cbfdailyproductionapp.Database.DatabaseHelper;
import com.sysnova.cbfdailyproductionapp.Model.SurveyModal;
import com.sysnova.cbfdailyproductionapp.R;

import java.util.ArrayList;

public class ReportList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper sqliteDbHelper;
    ArrayList<SurveyModal> surveyModalArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CBF Daily Production App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.kfg_red)));

        recyclerView = findViewById(R.id.report_list_recyclerview);

        sqliteDbHelper = new DatabaseHelper(this);

        loadReport();
    }

    private void loadReport(){

        Cursor data = sqliteDbHelper.getAllReports();

        while(data.moveToNext()){
            int ID  = data.getInt(0);
            String farmerCode = data.getString(1);
            String batchCode = data.getString(2);

            SurveyModal surveyModal = new SurveyModal(ID,farmerCode,batchCode,uName);
            surveyModalArrayList.add(surveyModal);
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        SurveyModalRecyclerViewAdapter orderSummaryRecyclerViewAdapter = new SurveyModalRecyclerViewAdapter(ReportList.this,surveyModalArrayList);
        recyclerView.setAdapter(orderSummaryRecyclerViewAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        Intent intent = new Intent(ReportList.this,Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
