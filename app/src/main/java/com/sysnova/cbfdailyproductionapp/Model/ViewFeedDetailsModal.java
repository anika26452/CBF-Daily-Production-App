package com.sysnova.cbfdailyproductionapp.Model;

public class ViewFeedDetailsModal {

    String pcode;
    Double issueqty,remainingqty,recieveqty;


    public ViewFeedDetailsModal(String pcode, Double remainingqty, Double recieveqty, Double issueqty) {
        this.pcode = pcode;
        this.remainingqty = remainingqty;
        this.recieveqty = recieveqty;
        this.issueqty = issueqty;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public Double getRemainingqty() {
        return remainingqty;
    }

    public void setRemainingqty(Double remainingqty) {
        this.remainingqty = remainingqty;
    }

    public Double getRecieveqty() {
        return recieveqty;
    }

    public void setRecieveqty(Double recieveqty) {
        this.recieveqty = recieveqty;
    }

    public Double getIssueqty() {
        return issueqty;
    }

    public void setIssueqty(Double issueqty) {
        this.issueqty = issueqty;
    }
}
