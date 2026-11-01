package com.sysnova.cbfdailyproductionapp.Model;

public class ViewAllBidPurchaseModel {
    String m_bid_id ,askingreate , qty, rate , description, approvedqty, approvedrate;

    public ViewAllBidPurchaseModel(String m_bid_id ,String askingreate ,String qty,String rate , String description,
                                   String approvedqty, String approvedrate) {
        this.m_bid_id = m_bid_id;
        this.askingreate = askingreate;
        this.qty = qty;
        this.rate = rate;
        this.description = description;
        this.approvedqty = approvedqty;
        this.approvedrate = approvedrate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApprovedqty() {
        return approvedqty;
    }

    public void setApprovedqty(String approvedqty) {
        this.approvedqty = approvedqty;
    }

    public String getApprovedrate() {
        return approvedrate;
    }

    public void setApprovedrate(String approvedrate) {
        this.approvedrate = approvedrate;
    }
}
