package com.sysnova.cbfdailyproductionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.cbfdailyproductionapp.Model.ViewReplyListModel;
import com.sysnova.cbfdailyproductionapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowBidReplys extends RecyclerView.Adapter<ShowBidReplys.ViewHolder> {
    List<ViewReplyListModel> orderListActivities;
    Context mContext;

    DecimalFormat decformatter = new DecimalFormat("#,###.00");
    DecimalFormat decformatter1 = new DecimalFormat("#,###");
    public ShowBidReplys(ArrayList<ViewReplyListModel> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_bid_reply_list_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.reply_time.setText(orderListActivities.get(position).getReptime());
        holder.qty_id.setText(decformatter1.format(Double.parseDouble(orderListActivities.get(position).getReplyqty())));
        holder.rate_id.setText(decformatter.format(Double.parseDouble(orderListActivities.get(position).getReplyrate())));

    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sl_id,qty_id,reply_time, rate_id, remarks;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.replyListRow);
            qty_id = itemView.findViewById(R.id.qty_id);
            reply_time  = itemView.findViewById(R.id.reply_time);
            rate_id = itemView.findViewById(R.id.rate_id);
        }
    }

    public boolean internetIsConnected() {
        try {
            //String command = "ping -w 1 -c 1 172.16.253.22";
            String command = "ping -w 1 -c 1 172.16.253.10";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    public void ShowAlert(String title,String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.image);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
