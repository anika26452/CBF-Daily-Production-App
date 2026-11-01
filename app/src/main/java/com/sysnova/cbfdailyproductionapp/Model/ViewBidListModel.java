package com.sysnova.cbfdailyproductionapp.Model;

public class ViewBidListModel {
    String bidno, m_bidline_id,  rate, m_bid_id, created, m_product_type, decisiondate, description,replyrateval;

    public ViewBidListModel(String bidno, String m_bidline_id, String rate,String m_bid_id, String created,
                            String m_product_type,String decisiondate,String description, String replyrateval) {
        this.bidno = bidno;
        this.m_bidline_id = m_bidline_id;
        this.rate = rate;
        this.m_bid_id = m_bid_id;
        this.created = created;
        this.m_product_type = m_product_type;
        this.decisiondate = decisiondate;
        this.description = description;
        this.replyrateval = replyrateval;
    }

    public String getBidno() {
        return bidno;
    }

    public void setBidno(String bidno) {
        this.bidno = bidno;
    }

    public String getReplyrateval() {
        return replyrateval;
    }

    public void setReplyrateval(String replyrateval) {
        this.replyrateval = replyrateval;
    }

    public String getM_bidline_id() {
        return m_bidline_id;
    }

    public void setM_bidline_id(String m_bidline_id) {
        this.m_bidline_id = m_bidline_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getM_bid_id() {
        return m_bid_id;
    }

    public void setM_bid_id(String m_bid_id) {
        this.m_bid_id = m_bid_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getM_product_type() {
        return m_product_type;
    }

    public void setM_product_type(String m_product_type) {
        this.m_product_type = m_product_type;
    }
}
