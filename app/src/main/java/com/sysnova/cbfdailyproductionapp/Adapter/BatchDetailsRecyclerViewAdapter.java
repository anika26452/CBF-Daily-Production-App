package com.sysnova.cbfdailyproductionapp.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.cbfdailyproductionapp.Model.ViewBatchDetailsModal;
import com.sysnova.cbfdailyproductionapp.R;

import java.util.List;

public class BatchDetailsRecyclerViewAdapter extends RecyclerView.Adapter<BatchDetailsRecyclerViewAdapter.ViewHolder> {


    Context context;
    List<ViewBatchDetailsModal> surveyModals;

    public BatchDetailsRecyclerViewAdapter(Context context, List<ViewBatchDetailsModal> surveyModals) {
        this.context = context;
        this.surveyModals = surveyModals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_details_report,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.pro_datevw.setText(surveyModals.get(position).getProductiondate());
        holder.agetxtvw.setText(surveyModals.get(position).getBirdage());
        holder.birdsnotxtvw.setText(surveyModals.get(position).getBirdnumber());
        holder.mortalitytxtvw.setText(String.valueOf(Double.valueOf(surveyModals.get(position).getMortality())+Double.valueOf(surveyModals.get(position).getTransitmortality())));
        holder.feed1txtvw.setText(surveyModals.get(position).getFeedqty1());
        holder.feed2txtvw.setText(surveyModals.get(position).getFeedqty2());
        holder.feed3txtvw.setText(surveyModals.get(position).getFeedqty3());
    }

    @Override
    public int getItemCount() {
        return surveyModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  agetxtvw, birdsnotxtvw, mortalitytxtvw, feed1txtvw, feed2txtvw, feed3txtvw, pro_datevw;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow = itemView.findViewById(R.id.orderListRow);
            agetxtvw = itemView.findViewById(R.id.bairdage);
            birdsnotxtvw = itemView.findViewById(R.id.birdsno);
            mortalitytxtvw = itemView.findViewById(R.id.mortality);
            feed1txtvw = itemView.findViewById(R.id.feed1);
            feed2txtvw = itemView.findViewById(R.id.feed2);
            feed3txtvw = itemView.findViewById(R.id.feed3);
            pro_datevw = itemView.findViewById(R.id.pro_date);
        }
    }
}
