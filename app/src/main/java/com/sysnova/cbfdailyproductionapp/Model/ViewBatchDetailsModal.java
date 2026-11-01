package com.sysnova.cbfdailyproductionapp.Model;

public class ViewBatchDetailsModal {

    String batch,productiondate,birdage,birdnumber,mortality, transitmortality, feedqty1, feedqty2, feedqty3;


    public ViewBatchDetailsModal(String batch, String productiondate, String birdage, String birdnumber, String mortality, String transitmortality, String feedqty1,
                                 String feedqty2, String feedqty3) {
        this.batch = batch;
        this.productiondate = productiondate;
        this.birdage = birdage;
        this.birdnumber = birdnumber;
        this.mortality = mortality;
        this.transitmortality = transitmortality;
        this.feedqty1 = feedqty1;
        this.feedqty2 = feedqty2;
        this.feedqty3 = feedqty3;

    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getProductiondate() {
        return productiondate;
    }

    public void setProductiondate(String productiondate) {
        this.productiondate = productiondate;
    }

    public String getBirdage() {
        return birdage;
    }

    public void setBirdage(String birdage) {
        this.birdage = birdage;
    }

    public String getBirdnumber() {
        return birdnumber;
    }

    public void setBirdnumber(String birdnumber) {
        this.birdnumber = birdnumber;
    }

    public String getMortality() {
        return mortality;
    }

    public void setMortality(String mortality) {
        this.mortality = mortality;
    }

    public String getTransitmortality() {
        return transitmortality;
    }

    public void setTransitmortality(String transitmortality) {
        this.transitmortality = transitmortality;
    }

    public String getFeedqty1() {
        return feedqty1;
    }

    public void setFeedqty1(String feedqty1) {
        this.feedqty1 = feedqty1;
    }

    public String getFeedqty2() {
        return feedqty2;
    }

    public void setFeedqty2(String feedqty2) {
        this.feedqty2 = feedqty2;
    }

    public String getFeedqty3() {
        return feedqty3;
    }

    public void setFeedqty3(String feedqty3) {
        this.feedqty3 = feedqty3;
    }
}
