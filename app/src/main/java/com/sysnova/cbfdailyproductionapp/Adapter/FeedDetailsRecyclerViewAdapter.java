package com.sysnova.cbfdailyproductionapp.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.cbfdailyproductionapp.Model.ViewFeedDetailsModal;
import com.sysnova.cbfdailyproductionapp.R;

import java.text.DecimalFormat;
import java.util.List;


public class FeedDetailsRecyclerViewAdapter extends RecyclerView.Adapter<FeedDetailsRecyclerViewAdapter.ViewHolder> {


    Context context;
    List<ViewFeedDetailsModal> surveyModals;
    final DecimalFormat decfor = new DecimalFormat("0.00");

    public FeedDetailsRecyclerViewAdapter(Context context, List<ViewFeedDetailsModal> surveyModals) {
        this.context = context;
        this.surveyModals = surveyModals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_details_report,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.fdqty.setText(surveyModals.get(position).getPcode());
        holder.rcvqty.setText(String.valueOf(surveyModals.get(position).getRecieveqty()));
        holder.issueqty.setText(String.valueOf(surveyModals.get(position).getIssueqty()));
        holder.rmnqty.setText(String.valueOf(surveyModals.get(position).getRemainingqty()));

    }

    @Override
    public int getItemCount() {
        return surveyModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView  fdqty, rcvqty, issueqty, rmnqty;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow = itemView.findViewById(R.id.feedListRow);
            fdqty = itemView.findViewById(R.id.fdqty);
            rcvqty = itemView.findViewById(R.id.rcvqty);
            issueqty = itemView.findViewById(R.id.issueqty);
            rmnqty = itemView.findViewById(R.id.rmnqty);
        }
    }
}
