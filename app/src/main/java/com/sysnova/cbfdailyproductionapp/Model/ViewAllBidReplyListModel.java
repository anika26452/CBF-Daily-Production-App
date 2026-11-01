package com.sysnova.cbfdailyproductionapp.Model;

public class ViewAllBidReplyListModel {
    String m_bid_id ,askingreate , qty, rate, m_bidline_id, bidval;

    public ViewAllBidReplyListModel(String m_bid_id ,String askingreate ,String qty,String rate, String m_bidline_id, String bidval) {
        this.m_bid_id = m_bid_id;
        this.askingreate = askingreate;
        this.qty = qty;
        this.rate = rate;
        this.m_bidline_id = m_bidline_id;
        this.bidval = bidval;
    }

    public String getBidval() {
        return bidval;
    }

    public void setBidval(String bidval) {
        this.bidval = bidval;
    }

    public String getM_bidline_id() {
        return m_bidline_id;
    }

    public void setM_bidline_id(String m_bidline_id) {
        this.m_bidline_id = m_bidline_id;
    }

    public String getM_bid_id() {
        return m_bid_id;
    }

    public void setM_bid_id(String m_bid_id) {
        this.m_bid_id = m_bid_id;
    }

    public String getAskingreate() {
        return askingreate;
    }

    public void setAskingreate(String askingreate) {
        this.askingreate = askingreate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
