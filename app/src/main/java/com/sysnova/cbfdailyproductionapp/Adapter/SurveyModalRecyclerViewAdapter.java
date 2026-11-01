package com.sysnova.cbfdailyproductionapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.cbfdailyproductionapp.Activity.ReportDetailsActivity;
import com.sysnova.cbfdailyproductionapp.Model.SurveyModal;
import com.sysnova.cbfdailyproductionapp.R;

import java.util.List;


public class SurveyModalRecyclerViewAdapter extends RecyclerView.Adapter<SurveyModalRecyclerViewAdapter.ViewHolder> {


    Context context;
    List<SurveyModal> surveyModals;

    public SurveyModalRecyclerViewAdapter(Context context, List<SurveyModal> surveyModals) {
        this.context = context;
        this.surveyModals = surveyModals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_report,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.farmerCodeTextView.setText(surveyModals.get(position).getFarmerCode());
        holder.batchCodeTextView.setText(surveyModals.get(position).getBatchCode());

        holder.detailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReportDetailsActivity.class);
                intent.putExtra("ID",surveyModals.get(position).getId());
                intent.putExtra("userID",surveyModals.get(position).getUserID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return surveyModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView farmerCodeTextView,batchCodeTextView,detailsTextView;
        TableRow tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            farmerCodeTextView = itemView.findViewById(R.id.farmerCode);
            batchCodeTextView = itemView.findViewById(R.id.batchCode);
            detailsTextView = itemView.findViewById(R.id.summary_details);
        }
    }
}
