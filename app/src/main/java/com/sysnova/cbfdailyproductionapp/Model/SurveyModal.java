package com.sysnova.cbfdailyproductionapp.Model;

public class SurveyModal {

    int    Id;
    String farmerCode;
    String batchCode;
    String userID;


    public SurveyModal(int Id, String farmerCode, String batchCode, String userID) {
        this.Id = Id;
        this.farmerCode = farmerCode;
        this.batchCode = batchCode;
        this.userID = userID;

    }

    public int getId() {
        return Id;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public String getBatchCode() {
        return batchCode;
    }
    public String getUserID() {
        return userID;
    }
}
