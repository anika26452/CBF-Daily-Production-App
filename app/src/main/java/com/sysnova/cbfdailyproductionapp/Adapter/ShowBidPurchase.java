package com.sysnova.cbfdailyproductionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.cbfdailyproductionapp.Model.ViewAllBidPurchaseModel;
import com.sysnova.cbfdailyproductionapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowBidPurchase extends RecyclerView.Adapter<ShowBidPurchase.ViewHolder> {
    List<ViewAllBidPurchaseModel> orderListActivities;
    Context mContext;

    DecimalFormat decformatter = new DecimalFormat("#,###.00");
    DecimalFormat decformatter1 = new DecimalFormat("#,###");
    public ShowBidPurchase(ArrayList<ViewAllBidPurchaseModel> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_bid_purchase_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        double qtyval = Double.parseDouble(orderListActivities.get(position).getQty());
        double rateval = Double.parseDouble(orderListActivities.get(position).getRate());

        holder.bidid.setText(orderListActivities.get(position).getM_bid_id());
        holder.qty_id.setText(decformatter1.format(qtyval));
        holder.rate_id.setText(decformatter.format(rateval));
        holder.asking_rate.setText(orderListActivities.get(position).getAskingreate());
        if(orderListActivities.get(position).getApprovedqty().equals("0")){
            holder.approvedqty.setText("");
        }else{
            double appqty = Double.parseDouble(orderListActivities.get(position).getApprovedqty());

            holder.approvedqty.setText(decformatter1.format(appqty));
        }
        if(orderListActivities.get(position).getApprovedrate().equals("0")){
            holder.approvedrate.setText("");
        }else {
            double apprate = Double.parseDouble(orderListActivities.get(position).getApprovedrate());

            holder.approvedrate.setText(decformatter.format(apprate));
        }
    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bidid,asking_rate, qty_id, rate_id, approvedqty, approvedrate;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.orderListRow);
            bidid = itemView.findViewById(R.id.bid_id);
            asking_rate = itemView.findViewById(R.id.asking_rate);
            qty_id = itemView.findViewById(R.id.qty);
            approvedqty = itemView.findViewById(R.id.app_qty);
            approvedrate = itemView.findViewById(R.id.app_rate);
            rate_id = itemView.findViewById(R.id.bid_rate);
        }
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -w 1 -c 1 172.16.253.10";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
