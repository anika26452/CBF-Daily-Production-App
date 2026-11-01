package com.sysnova.cbfdailyproductionapp.Model;

public class ViewReplyListModel {
    String replyqty, replyrate, reptime;

    public ViewReplyListModel(String replyqty,String replyrate,String reptime) {
        this.replyqty = replyqty;
        this.replyrate = replyrate;
        this.reptime = reptime;
    }

    public String getReplyqty() {
        return replyqty;
    }

    public void setReplyqty(String replyqty) {
        this.replyqty = replyqty;
    }

    public String getReplyrate() {
        return replyrate;
    }

    public void setReplyrate(String replyrate) {
        this.replyrate = replyrate;
    }

    public String getReptime() {
        return reptime;
    }

    public void setReptime(String reptime) {
        this.reptime = reptime;
    }
}
