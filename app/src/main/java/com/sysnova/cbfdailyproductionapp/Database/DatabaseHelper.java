package com.sysnova.cbfdailyproductionapp.Database;

/**
 * Created by root on 4/19/17.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "product", COL1 = "productID", COL2 = "productName", COL3 = "catagoryID",
            COL4 = "price", COL5 = "unit", COL6 = "availableQty";
    private static final String DATABASE_NAME = "CBFDailyProduction";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createLastProductionTable = "CREATE TABLE LastProduction(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ad_user_id Integer,bpcode Text,bpname TEXT, batch Text,visitDate Text,birdnumber Text Default '0'," +
                "birdage Text Default '0',mortality Text Default '0',averageweight Text Default '0'," +
                "feedcode1 Text,feedcode2 Text,feedcode3 Text,feedqty1 Text Default '0',feedqty2 Text Default '0'," +
                "feedqty3 Text Default '0',intime Text,outtime Text,suggestion Text,productionDate Text,created TIMESTAMP Default (DATETIME('now'))," +
                "transitMortality Text,c_bpartner_id Text,avlFeed1 Text,avlFeed2 Text,avlFeed3 Text, latitude TEXT, longitude TEXT, selectedMortReason TEXT," +
                "totalwt TEXT, noofbirds TEXT, selectedDiseaseReason TEXT)";
        String createBatchDetailsTable   = "CREATE TABLE batchfeedDetails(totalmortality REAL,feed1 REAL, feed2 REAL, feed3 REAL)";
        String createFeedTable           =  "CREATE TABLE feedDetails(feedCode Text,feedID Integer)";
        String createBatchInfoTable          =  "CREATE TABLE batchInfoDetails(batchCode Text,batchID Text)";
        String createbidtable = "CREATE TABLE AllBid(id INTEGER PRIMARY KEY AUTOINCREMENT,org_name TEXT, bidno TEXT, m_bidline_id TEXT, rate TEXT,m_bid_id TEXT, created TEXT, m_product_type TEXT,decisiondate TEXT, description TEXT,farmname TEXT ,replyrate TEXT)";
        String createmployeetable = "CREATE TABLE employee(id INTEGER PRIMARY KEY AUTOINCREMENT,ad_user_id TEXT, user_level TEXT, name TEXT, currentdate TEXT, feed_org_id TEXT,  employeename TEXT)";
        String createbidreplytable = "CREATE TABLE BidReply(id INTEGER PRIMARY KEY AUTOINCREMENT, c_bpartner_id TEXT, ad_user_id TEXT, m_bid_id TEXT ,replyqtyval TEXT,replyrateval TEXT, replytime TEXT, remarksstr TEXT)";
        String createbidHistorytable = "CREATE TABLE BidHistory(id INTEGER PRIMARY KEY AUTOINCREMENT,m_bid_id TEXT, askingreate TEXT, qty TEXT,  rate TEXT,  m_bidline_id TEXT, org_name TEXT,  bidval TEXT,m_product_type TEXT, decisiondate TEXT,description TEXT," +
                " approvedrate TEXT, approvedqty TEXT, farm_name TEXT)";
        String createOTPtable = "CREATE TABLE OTPTable(id INTEGER PRIMARY KEY AUTOINCREMENT,ad_user_id TEXT, otpval TEXT)";
        String createBatchTable          =  "CREATE TABLE batchDetails(batchCode Text,batchID Integer,OrgID Integer,OrgName Text)";
        String createCustomerTable       =  "CREATE TABLE customer(customer_id INTEGER PRIMARY KEY AUTOINCREMENT,customer_code Text,customer_name Text)";
        String createQuestionTable       =  "CREATE TABLE question(question_id INTEGER PRIMARY KEY AUTOINCREMENT,question_details Text,answers Text,ques_id Integer,answer_id Integer)";
        String createSurveyDataTable     =  "CREATE TABLE surveyData(surveyData_id INTEGER PRIMARY KEY AUTOINCREMENT,ad_user_id Integer,bpcode Text,batch Text,visitDate Text,birdnumber Text Default '0',birdage Text Default '0',mortality Text Default '0',averageweight Text Default '0',feedcode1 Text,feedcode2 Text,feedcode3 Text,feedqty1 Text Default '0',feedqty2 Text Default '0',feedqty3 Text Default '0',intime Text,outtime Text,suggestion Text,productionDate Text,created TIMESTAMP Default (DATETIME('now')),transitMortality Text,c_bpartner_id Text,avlFeed1 Text,avlFeed2 Text,avlFeed3 Text, latitude TEXT, longitude TEXT)";  //lat, long added by anika 21-10-2023
        String createQADataTable         =  "CREATE TABLE QAData(QAData_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id Integer,customer_code Text,survey_code Text)";
        String createQADataLineTable     =  "CREATE TABLE QADataLine(QADataLine_id INTEGER PRIMARY KEY AUTOINCREMENT,survey_code Text,survey_question_id Integer,survey_answer_id Integer,remarks Text,otherfeedmill Text)";

        String createBatchCodeTable      =  "CREATE TABLE batchCodeDetails(batchCode Text,batchID Integer,OrgID Integer,OrgName Text)";
        String createIsAnsweredTable     =  "CREATE TABLE isQuestionAnswered(question Text,question_id Integer,isAnswered Text Default 'N')";
        String createMortalityTable      =  "CREATE TABLE Mortality(types_diseases TEXT, name_diseases TEXT)";
        String createWarehouseTable      = "Create Table warehouse (id INTEGER PRIMARY KEY AUTOINCREMENT,warehouse_id Text,ad_org_id Text,warehouse_name Text)";
        String createBatchToTable          =  "CREATE TABLE batchDetailsto(batchCode Text,batchID Integer,OrgID Integer,OrgName Text)";
        String createBatchCodeToTable      =  "CREATE TABLE batchCodeDetailsto(batchCode Text,batchID Integer,OrgID Integer,OrgName Text)";
        String createCbfIFTFTable      =  "CREATE TABLE CbfIFTF(value Text,CbfIFTF_ID Integer,OrgName Text,description Text,frombatch Text,tobatch Text,frombpcode Text,tobpcode Text,frombp Text,tobp Text,isstarter Text,isgrower Text,isfinisher Text," +
                "transportation Text,transferreason Text,distancekm Text,transferby_code Text,transferby_name Text,starter_qty Text,grower_qty Text,finisher_qty Text,paidamt Text, movementdate Text,  iscrumbled TEXT, crumbled_qty TEXT)";

        db.execSQL(createbidtable);
        db.execSQL(createBatchInfoTable);
        db.execSQL(createBatchDetailsTable);
        db.execSQL(createFeedTable);
        db.execSQL(createBatchTable);
        db.execSQL(createbidreplytable);
        db.execSQL(createbidHistorytable);
        db.execSQL(createOTPtable);
        db.execSQL(createLastProductionTable);
        db.execSQL(createMortalityTable);
        db.execSQL(createBatchCodeTable);
        db.execSQL(createSurveyDataTable);
        db.execSQL(createQuestionTable);
        db.execSQL(createCustomerTable);
        db.execSQL(createQADataTable);
        db.execSQL(createIsAnsweredTable);
        db.execSQL(createQADataLineTable);
        db.execSQL(createWarehouseTable);
        db.execSQL(createBatchToTable);
        db.execSQL(createBatchCodeToTable);
        db.execSQL(createCbfIFTFTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS employee");
        db.execSQL("DROP TABLE IF EXISTS BidReply");
        db.execSQL("DROP TABLE IF EXISTS BidHistory");
        db.execSQL("DROP TABLE IF EXISTS OTPTable");
        db.execSQL("DROP TABLE IF EXISTS LastProduction");
        db.execSQL("DROP TABLE IF EXISTS batchfeedDetails");
        db.execSQL("DROP TABLE IF EXISTS feedDetails");
        db.execSQL("DROP TABLE IF EXISTS batchDetails");
        db.execSQL("DROP TABLE IF EXISTS Mortality");
        db.execSQL("DROP TABLE IF EXISTS customer");
        db.execSQL("DROP TABLE IF EXISTS surveyData");
        db.execSQL("DROP TABLE IF EXISTS batchCodeDetails");
        db.execSQL("DROP TABLE IF EXISTS question");
        db.execSQL("DROP TABLE IF EXISTS QAData");
        db.execSQL("DROP TABLE IF EXISTS isQuestionAnswered");
        db.execSQL("DROP TABLE IF EXISTS QADataLine");
        db.execSQL("DROP TABLE IF EXISTS warehouse");
        db.execSQL("DROP TABLE IF EXISTS batchDetailsto");
        db.execSQL("DROP TABLE IF EXISTS batchCodeDetailsto");
        db.execSQL("DROP TABLE IF EXISTS CbfIFTF");
        db.execSQL("DROP TABLE IF EXISTS batchInfoDetails");
        onCreate(db);
    }

    public Boolean insertintoMortality(String types_diseases ,String name_diseases){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("types_diseases", types_diseases);
        contentValues.put("name_diseases", name_diseases);

        long result = db.insert("Mortality", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor gettypesdiseases(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select distinct types_diseases from Mortality ",null);
        return res;
    }

    public Cursor getnamesdiseases(String selectedMortReason){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name_diseases from Mortality where types_diseases = '"+selectedMortReason+"'",null);
        return res;
    }

    public void deletediseases(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  Mortality";
        db.execSQL(query);
    }

    public boolean insertIntoLastProductionTable(int userID,String bpcode,String bpname,String batch,String visitDate,
                                                 String birdnumber,String birdage,String mortality,
                                                 String averageweight,String feedcode1,String feedcode2,
                                                 String feedcode3,String feedqty1,String feedqty2,String feedqty3,
                                                 String intime,String outtime,String productionDate,
                                                 String suggestion,String transitMortality,String c_bpartner_id,
                                                 String avlFeed1,String avlFeed2,String avlFeed3, String latitude,
                                                 String longitude, String selectedMortReason, String totalwt ,
                                                 String noofbirds, String selectedDiseaseReason){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_user_id", userID);
        contentValues.put("bpcode", bpcode);
        contentValues.put("bpname", bpname);
        contentValues.put("batch", batch);
        contentValues.put("visitDate", visitDate);
        contentValues.put("birdnumber", birdnumber);
        contentValues.put("birdage", birdage);
        contentValues.put("mortality", mortality);
        contentValues.put("averageweight", averageweight);
        contentValues.put("feedcode1", feedcode1);
        contentValues.put("feedcode2", feedcode2);
        contentValues.put("feedcode3", feedcode3);
        contentValues.put("feedqty1", feedqty1);
        contentValues.put("feedqty2", feedqty2);
        contentValues.put("feedqty3", feedqty3);
        contentValues.put("intime", intime);
        contentValues.put("outtime", outtime);
        contentValues.put("productionDate", productionDate);
        contentValues.put("suggestion", suggestion);
        contentValues.put("transitMortality", transitMortality);
        contentValues.put("c_bpartner_id", c_bpartner_id);
        contentValues.put("avlFeed1", avlFeed1);
        contentValues.put("avlFeed2", avlFeed2);
        contentValues.put("avlFeed3", avlFeed3);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("selectedMortReason", selectedMortReason);
        contentValues.put("totalwt",  totalwt);
        contentValues.put("noofbirds",  noofbirds);
        contentValues.put("selectedDiseaseReason", selectedDiseaseReason);

        long res = db.insert("LastProduction", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllReports(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id,bpcode,batch from LastProduction ",null);
        return res;

    }
    public Cursor getFeedIdByCode(String feedCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select feedID from feedDetails where feedCode = '"+feedCode+"'  ",null);
        return res;

    }

    public Cursor getBatchID(String batchCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select batchID from batchDetails where batchCode = '"+batchCode+"' ",null);
        return res;

    }

    public boolean insertIntoBatchDetails( String batchCode,String batchID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("batchCode", batchCode);
        contentValues.put("batchID", batchID);

        long res = db.insert("batchInfoDetails", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }
    public void deleteBatchCodeTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM batchCodeDetails ";
        db.execSQL(query);
    }
    public boolean insertIntoBatchCodeDetails( String batchCode,int batchID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("batchCode", batchCode);
        contentValues.put("batchID", batchID);

        long res = db.insert("batchCodeDetails", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public void deleteBatchDetails(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM batchfeedDetails ";
        db.execSQL(query);
    }

    public boolean insertIntoBatchDetailsTable(Double totalmortality,Double feed1,Double feed2,Double feed3){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("totalmortality", totalmortality);
        contentValues.put("feed1", feed1);
        contentValues.put("feed2", feed2);
        contentValues.put("feed3", feed3);

        long res = db.insert("batchfeedDetails", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getTotalMortality(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select sum(totalmortality) totalmortality, sum(feed1) feed1, sum(feed2) feed1, sum(feed3) feed3 from batchfeedDetails  ",null);
        return res;

    }

    public void deleteBatchToTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM batchDetailsto ";
        db.execSQL(query);
    }

    public void deleteBatchTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM batchDetails ";
        db.execSQL(query);
    }

    public Cursor getAllFeedCode(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select feedCode from feedDetails",null);
        return res;

    }

    public Cursor getReportDetails(int primaryID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LastProduction where id = '"+primaryID+"' ",null);
        return res;

    }

    public boolean updateReport(String primaryId,String farmerCode,String batchCode,String inTime,String birdNumber,String birdAge,
                                String feedCode1,String feedCode2,String feedCode3,String feedAmt1,String feedAmt2,String feedAmt3,
                                String mortality,String weight,String suggestion,String outTime){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("bpcode",farmerCode );
        contentValues.put("batch", batchCode);
        contentValues.put("intime",inTime );
        contentValues.put("birdnumber",birdNumber );
        contentValues.put("birdage", birdAge);
        contentValues.put("mortality", mortality);
        contentValues.put("averageweight", weight);
        contentValues.put("feedcode1", feedCode1);
        contentValues.put("feedcode2", feedCode2);
        contentValues.put("feedcode3", feedCode3);
        contentValues.put("feedqty1", feedAmt1);
        contentValues.put("feedqty2", feedAmt2);
        contentValues.put("feedqty3", feedAmt3);
        contentValues.put("outtime",outTime );
        contentValues.put("suggestion",suggestion );
        long result = db.update("LastProduction",contentValues,"id = ?",new String[]{primaryId});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteReport(int primaryID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM LastProduction where id = '"+ primaryID+"' ";
        db.execSQL(query);
    }

    public Cursor checkLastProductionEntry(String batch_code,String c_bpartner_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select birdage,birdnumber,visitDate,mortality,transitMortality, productionDate from LastProduction where intime = '"+batch_code+"' and c_bpartner_id = '"+c_bpartner_id+"' order by birdage desc limit 1",null);
        return res;
    }

    public Cursor checkLastEntry(String batch_code,String visitdateval){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LastProduction where intime = '"+batch_code+"' and visitDate = '"+visitdateval+"'",null);
        return res;
    }

    public Cursor checkEntry(String batch_code,String c_bpartner_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select birdage from LastProduction where batch = '"+batch_code+"' and c_bpartner_id = '"+c_bpartner_id+"' order by birdage desc limit 1",null);
        return res;
    }

    public boolean insertIntoFeedDetails( String feedCode,int feedID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("feedCode", feedCode);
        contentValues.put("feedID", feedID);

        long res = db.insert("feedDetails", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor checkAvlFeed(String batch_code,String c_bpartner_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select avlFeed1,avlFeed2,avlFeed3,feedqty1,feedqty2,feedqty3,feedcode1,feedcode2,feedcode3 from LastProduction where batch = '"+batch_code+"' and c_bpartner_id = '"+c_bpartner_id+"' order by birdage desc limit 1",null);
        return res;
    }

    public Boolean insertintoBidHistoryTable(String m_bid_id,String askingreate,String qty,String rate ,String m_bidline_id
            ,String org_name ,String  bidval ,String m_product_type ,String decisiondate ,String description, String  approvedrate,
                                             String approvedqty, String farm_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("m_bid_id", m_bid_id);
        contentValues.put("askingreate", askingreate);
        contentValues.put("qty", qty);
        contentValues.put("rate", rate);
        contentValues.put("m_bidline_id", m_bidline_id);
        contentValues.put("org_name", org_name);
        contentValues.put("bidval", bidval);
        contentValues.put("m_product_type", m_product_type);
        contentValues.put("decisiondate", decisiondate);
        contentValues.put("description", description);
        contentValues.put("approvedrate",  approvedrate);
        contentValues.put("approvedqty", approvedqty);
        contentValues.put("farm_name", farm_name);

        long result = db.insert("BidHistory", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getABid(String m_bidline_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM BidHistory where m_bidline_id = '"+m_bidline_id+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getAllBid(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM AllBid";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSingleBid(String m_bidline_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM AllBid where m_bidline_id = '"+m_bidline_id+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteAllBid(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  AllBid";
        db.execSQL(query);
    }

    public boolean insertIntoOTPTable(String ad_user_id,String otpval) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_user_id", ad_user_id);
        contentValues.put("otpval", otpval);

        long result = db.insert("OTPTable", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getOTP(String ad_user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM OTPTable WHERE ad_user_id='"+ad_user_id+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getOTPSize(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM OTPTable";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Boolean insertintobidreply(String c_bpartner_id,String ad_user_id,String m_bid_id,String replyqtyval,String replyrateval, String replytime, String remarksstr){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("c_bpartner_id", c_bpartner_id);
        contentValues.put("ad_user_id", ad_user_id);
        contentValues.put("m_bid_id", m_bid_id);
        contentValues.put("replyqtyval", replyqtyval);
        contentValues.put("replyrateval", replyrateval);
        contentValues.put("replytime", replytime);
        contentValues.put("remarksstr", remarksstr);

        long result = db.insert("BidReply", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getbidreplyList(String c_bpartner_id, String m_bid_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM BidReply WHERE c_bpartner_id= '"+c_bpartner_id+"' and m_bid_id='"+m_bid_id+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean updateSalesOrderTable(String bidnum, String ordernum, String total_cages ,String total_bird, String total_weight ,String avg_weight){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("bidnum", bidnum);
        args.put("ordernum", ordernum);
        args.put("total_cages", total_cages);
        args.put("total_bird", total_bird);
        args.put("total_weight", total_weight);
        args.put("avg_weight", avg_weight);

        int i = db.update("Order_Table", args, "ordernum = "+ordernum+" and bidnum = "+bidnum, null);
        if( i > 0){
            return true;
        }
        return false;

    }





    public Boolean insertintobirdcage(String cagenum, String birdnum, String ordernum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cagenum", cagenum);
        contentValues.put("birdnum", birdnum);
        contentValues.put("ordernum", ordernum);

        long result = db.insert("BirdCage", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getSalesOrderNum(String bidnum, String ordernum){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT bidnum, ordernum FROM Order_Table WHERE ordernum= '"+ordernum+"' and bidnum='"+bidnum+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderList(String orderdate){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM Order_Table WHERE orderdate='"+orderdate+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getTotalcagecount(String ordernum) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(cagenum), count(cagenum) from BirdCage WHERE ordernum='"+ordernum+"'";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public Cursor getTotalweight(String ordernum) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(birdnum) from BirdCage WHERE ordernum='"+ordernum+"'";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public void deletebirdcage(int slno){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  BirdCage WHERE id='"+slno+"'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteViewbirdcage(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  BirdCage";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getbirdcage(String ordernum){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM BirdCage WHERE ordernum='"+ordernum+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //anika
    public Boolean insertIntoViewOrder(String doc_no, String date, String grandtotal, String user_id,String bpname){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("doc_no", doc_no);
        contentValues.put("date", date);
        contentValues.put("grandtotal", grandtotal);
        contentValues.put("user_id", user_id);
        contentValues.put("bpname", bpname);

        long result = db.insert("ViewOrder", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertIntoViewPayment(String doc_no, String dealer, String dealerbank, String kfg_bankacc, String paymentamt, String createddate, String t_app_payment_id, String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("doc_no", doc_no);
        contentValues.put("dealer", dealer);
        contentValues.put("dealerbank", dealerbank); //status
        contentValues.put("kfg_bankacc", kfg_bankacc);
        contentValues.put("paymentamt", paymentamt);
        contentValues.put("createddate", createddate);
        contentValues.put("t_app_payment_id", t_app_payment_id);
        contentValues.put("user_id", user_id);

        long result = db.insert("ViewPayment", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteViewOrder(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ViewOrder";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteViewPayment(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ViewPayment";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getPaymentFromViewPayment(String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ViewPayment where user_id='"+user_id+"'" ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean insertIntoOCustomerNew1(String customer_id,String customer_name,String code,String balance,String creditlimit, String region_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);
        contentValues.put("customer_location", balance);
        contentValues.put("customer_phone", creditlimit);
        contentValues.put("region_id", region_id);  //anika

        long result = db.insert("customer",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getOrderFromViewOrder(String date, String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ViewOrder where date='"+date+"' and user_id='"+user_id+"'" ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDistrictForPayment(String region_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where District_ID='"+region_id+"' and bank_loc is not null ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getDistrictForPaymentNew(String region_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where (District_ID='"+region_id+"' and bank_loc is not null) ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    //anika


    public boolean insertIntoBPGroup(String c_bp_group_id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("c_bp_group_id", c_bp_group_id);
        contentValues.put("name", name);

        long result = db.insert("bpgroup",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoOCustomer(String customer_id,String customer_name,String code,String customer_location,String customer_phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);
        //customer_location,customer_phone added by shanzida 2021-11-16
        contentValues.put("customer_location", customer_location);
        contentValues.put("customer_phone", customer_phone);
        long result = db.insert("customer",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoOCustomerNew0512(String customer_id,String customer_name,String code,String balance,String creditlimit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);
        contentValues.put("customer_location", balance);
        contentValues.put("customer_phone", creditlimit);
        //2022-04-16 for kfg

        long result = db.insert("customer",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoOCustomerNew(String customer_id,String customer_name,String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);

        long result = db.insert("customer",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoOrderRecord(String docNo,String date,String customer_name,String grand_total,String product_name,String uom,String ordered_qty,String pricePerProduct,String status,String orgid,String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("customer_name", customer_name);
        contentValues.put("grand_total", grand_total);
        contentValues.put("product_name", product_name);
        contentValues.put("uom", uom);
        contentValues.put("ordered_qty", ordered_qty);
        contentValues.put("pricePerProduct", pricePerProduct);
        contentValues.put("status", status);
        contentValues.put("orgid", orgid);
        contentValues.put("description",description);


        long result = db.insert("sales_order_record",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoOrderApproval(String order_doc,String erp_orderNo,String grand_total,String prepared_by,String sales_type,String sent_date,String doctype_id,String orgid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_doc", order_doc);
        contentValues.put("erp_orderNo", erp_orderNo);
        contentValues.put("grand_total", grand_total);
        contentValues.put("prepared_by", prepared_by);
        contentValues.put("sales_type", sales_type);
        contentValues.put("sent_date", sent_date);
        contentValues.put("doctype_id",doctype_id);
        contentValues.put("orgid",orgid);

        long result = db.insert("orderApproval",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoSalesTypeNew(String typename,String id,String prodType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salesType_name", typename);
        contentValues.put("salesType_id", id);
        contentValues.put("prodType", prodType);

        long result = db.insert("salesType", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoSalesType(String typename,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salesType_name", typename);
        contentValues.put("salesType_id", id);


        long result = db.insert("salesType", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoOrganization(String ad_org_id,String org_name,String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("org_name", org_name);
        contentValues.put("userid", userid);


        long result = db.insert("organization", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //2022-07-20
    public boolean insertIntoDB_Sync(String Sync,String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Sync", Sync);
        contentValues.put("ad_user_id", userid);

        long result = db.insert("DB_Sync", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //end 2022-07-20

    public boolean insertIntoWarehouse(String warehouse_id,String ad_org_id,String warehouse_name,String Region_ID ,String City_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("warehouse_id", warehouse_id);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("warehouse_name", warehouse_name);
        contentValues.put("Region_ID", Region_ID);
        contentValues.put("City_ID", City_ID);


        long result = db.insert("warehouse", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoWarehouseOld(String warehouse_id,String ad_org_id,String warehouse_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("warehouse_id", warehouse_id);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("warehouse_name", warehouse_name);



        long result = db.insert("warehouse", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoEmployee(String ad_user_id,String user_level,String name,String currentdate,String feed_org_id, String employeename) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_user_id", ad_user_id);
        contentValues.put("user_level", user_level);
        contentValues.put("name", name);
        contentValues.put("currentdate", currentdate);
        contentValues.put("feed_org_id", feed_org_id);
        contentValues.put("employeename", employeename);

        long result = db.insert("employee", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntodrInfo(String dr_code,String name,String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("drName", name);
        contentValues.put("dr_address", address);



        long result = db.insert("dr_information",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoTools(String name,String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", desc);



        long result = db.insert("promo_tools",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoSupervisor(String userid,String docid,String supid,String supname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("docid", docid);
        contentValues.put("supid", supid);
        contentValues.put("supname", supname);


        long result = db.insert("supervisor",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    /*
     * 2022-06-22 */

    public boolean insertIntoSalesOrderLine(String docNo,String ad_org_id,
                                            String product_name,String productID,String qtyordered,String uom,String unit_price,int orderid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("product_name", product_name);
        contentValues.put("productID", productID);
        contentValues.put("qtyordered",qtyordered);
        contentValues.put("uom",uom);
        contentValues.put("unit_price",unit_price);
        contentValues.put("orderid",orderid);
        //contentValues.put("erpdocNo",erpdocNo);

        long result = db.insert("sales_orderline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoTApproval(String docNo,String forwardto_id,String ispercentdiscount,String msg,String discount,String orgid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("discount", discount);
        contentValues.put("forwardto_id", forwardto_id);
        contentValues.put("orgid", orgid);
        contentValues.put("msg", msg);
        contentValues.put("isforward", ispercentdiscount);

        long result = db.insert("TApproval",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoTApprovalLine(String erp_orderNo,String uom,String docNo,String proID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("erp_orderNo",erp_orderNo);
        contentValues.put("docNo", docNo);
        contentValues.put("unit_price",uom);
        contentValues.put("proID",proID);
        long result = db.insert("TApprovalline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoProduct(String productID,String productName,int catagoryID,String price,String unit,String avlQty,String onhandQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productID", productID);
        contentValues.put("productName", productName);
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("price", price);
        contentValues.put("unit", unit);
        contentValues.put("availableQty", avlQty);
        contentValues.put("onhandQty", onhandQty);
        long result = db.insert("product", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }




    public boolean insertIntoProductCatagory(int catagoryID,String catagoryName,String C_Activity_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("catagoryName", catagoryName);
        contentValues.put("C_Activity_ID", C_Activity_ID);

        long result = db.insert("product_catagory", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoProductOrderHead1(String id,String productType,String activityType,String DistrictName,String ThanaName,String org,String wh,String dealer,String truckno,String pricelist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids", id);
        contentValues.put("productType", productType);
        contentValues.put("activityType", activityType);
        contentValues.put("DistrictName", DistrictName);
        contentValues.put("ThanaName", ThanaName);
        contentValues.put("org", org);
        contentValues.put("wh", wh);
        contentValues.put("dealer",dealer);
        contentValues.put("truckno",truckno);
        contentValues.put("pricelist",pricelist);

        long result = db.insert("ProductOrderHead",null,contentValues);


        //if data as inserted incorrectly it will return -1
        if (result == -1) {


            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoProductOrderHeadNew(String id,String productType,String activityType,String DistrictName,
                                                 String ThanaName,String org,String wh,String dealer,String truckno,String pricelist,String orderdate,String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids", id);
        contentValues.put("orderdate",orderdate);
        contentValues.put("productType", productType);
        contentValues.put("activityType", activityType);
        contentValues.put("District", DistrictName);
        contentValues.put("Thana", ThanaName);
        contentValues.put("org", org);
        contentValues.put("wh", wh);
        contentValues.put("dealer",dealer);
        contentValues.put("truck",truckno);
        contentValues.put("pricelist",pricelist);
        contentValues.put("userID",userID);


        long result = db.insert("ProductOrderHeadNew",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoProductOrderLine(int id,String code,String name,double qty,double price,double total,String docno,String mproduct_Id,String userID){ //,String productorderhead_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productId", id);
        contentValues.put("productCode", code);
        contentValues.put("productName", name);
        contentValues.put("productQTY", qty);
        contentValues.put("pricePerUnit", price);
        contentValues.put("totalCost", total);
        contentValues.put("docno",docno);
        contentValues.put("mproduct_Id",mproduct_Id);
        contentValues.put("userID",userID);


        long result = db.insert("ProductOrderLine",null,contentValues);


        //if data as inserted incorrectly it will return -1
        if (result == -1) {


            return false;
        } else {
            return true;
        }
    }
    //2022-04-20 added by shanzida
    //2022-04-17 added by shanzida
    /*public boolean insertIntoProductOrderHeadEdit(String id,String productType,String activityType,String DistrictName,
                                                 String ThanaName,String org,String wh,String dealer,String truckno,String pricelist,String orderdate,String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids", id);
        contentValues.put("orderdate",orderdate);
        contentValues.put("productType", productType);
        contentValues.put("activityType", activityType);
        contentValues.put("District", DistrictName);
        contentValues.put("Thana", ThanaName);
        contentValues.put("org", org);
        contentValues.put("wh", wh);
        contentValues.put("dealer",dealer);
        contentValues.put("truck",truckno);
        contentValues.put("pricelist",pricelist);
        contentValues.put("description",description);

        long result = db.insert("ProductOrderHeadEdit",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoProductOrderLineEdit(int id,String code,String name,int qty,double price,double total,String docno){ //,String productorderhead_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productId", id);
        contentValues.put("productCode", code);
        contentValues.put("productName", name);
        contentValues.put("productQTY", qty);
        contentValues.put("pricePerUnit", price);
        contentValues.put("totalCost", total);
        contentValues.put("docno",docno);
        //contentValues.put("productorderhead_Id",productorderhead_Id);


        long result = db.insert("ProductOrderLineEdit",null,contentValues);


        //if data as inserted incorrectly it will return -1
        if (result == -1) {


            return false;
        } else {
            return true;
        }
    }*/
    //end 2022-04-17
    public boolean insertIntoProductType(String ProductType_ID,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProductType_ID", ProductType_ID);
        contentValues.put("name", name);

        long result = db.insert("ProductType",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoDistrict(String District_ID,String name,String wh_loc,String bank_loc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("District_ID", District_ID);
        contentValues.put("name", name);
        contentValues.put("wh_loc", wh_loc);
        contentValues.put("bank_loc", bank_loc);

        long result = db.insert("District",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //2022-09-13
    public boolean insertIntoBranchName(String Thana_ID,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Thana_ID", Thana_ID);
        contentValues.put("name", name);

        long result = db.insert("BranchName",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntokfgbankname(String bank_id,String bank_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bank_id", bank_id);
        contentValues.put("bank_name", bank_name);

        long result = db.insert("kfgbankname",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntobkashbankname(String bank_id,String bank_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bank_id", bank_id);
        contentValues.put("bank_name", bank_name);

        long result = db.insert("bkashbankname",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntodealerbankname(String bank_id,String bank_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bank_id", bank_id);
        contentValues.put("bank_name", bank_name);

        long result = db.insert("Dealerbankname",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getDealerbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT bank_id,bank_name FROM Dealerbankname order by bank_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteDealerbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  Dealerbankname";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletekfgbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  kfgbankname";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletebkashbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  bkashbankname";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getkfgbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT bank_id,bank_name FROM kfgbankname order by bank_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public boolean insertIntoBankName(String Thana_ID,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Thana_ID", Thana_ID);
        contentValues.put("name", name);

        long result = db.insert("BankName",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getBankName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name FROM BankName where Thana_ID not in ('1000202','1000212') order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getBkashBankName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name FROM BankName where Thana_ID  in ('1','1000202') order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getagentBankName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name FROM BankName where Thana_ID  in ('1','1000212') order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getRocketBankName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name FROM BankName where Thana_ID  in ('1','1000202') order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getBranchName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name FROM BranchName order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //2022-09-13
    public boolean insertIntoThana(String Thana_ID,String name,String Region_ID,String wh_loc,String bank_loc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Thana_ID", Thana_ID);
        contentValues.put("name", name);
        contentValues.put("Region_ID", Region_ID);
        contentValues.put("wh_loc", wh_loc);
        contentValues.put("bank_loc", bank_loc);


        long result = db.insert("Thana",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public void deleteProductType()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductType";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    //2022-09-13
    public void deletekfgBankName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  BankName";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteBranchName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  BranchName";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    //2022-09-13
    public Cursor getProductType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ProductType";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteDistrict()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  District";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    /*public Cursor getDistrict(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District";
        Cursor data = db.rawQuery(query, null);
        return data;
    }*/
    public Cursor getDistrict_Bank(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where bank_loc != '0' order by name ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getDistrict_Wh(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where wh_loc != '0' order by name ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getDistrictSelected(String Region){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where name like '"+Region+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteThana()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  Thana";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getThana_banknew(String Region_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where ((Region_ID='"+Region_ID+"' and bank_loc != '0') or Thana_ID ='1') order by Thana_ID";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getThana_Bank(String Region_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where (Region_ID='"+Region_ID+"') and bank_loc is not null order by Thana_ID";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getThana_Wh(String Region_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where Region_ID='"+Region_ID+"' and wh_loc != '0' order by Thana_ID";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getThanaSelected(String thanaName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where name like '"+thanaName+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    //2022-04-20 added by shanzida
    //2022-07-20 added by shanzida
    public void deleteDB_Sync()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  DB_Sync";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getDB_Sync(String userid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM DB_Sync where ad_user_id like '"+userid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //end 2022-07-20
    public void deleteProductOrderLine()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getProductOrderLine(String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM productorderline where userID='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteAllProductOrderLine(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderLine: query: " +query);
        db.execSQL(query);
    }

    public void deleteOneProductOrderLine(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline WHERE productId='"+id+"'";
        //  Log.d(TAG, "deleteOneProductOrderLine: query: " + query);
        System.out.println("deleteOneProductOrderLine: query: " +query);
        db.execSQL(query);
    }


    public boolean updateProductOrderLine(int id,double newQTY,double newTotal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("productQTY", newQTY);
        args.put("totalCost", newTotal);

        int i = db.update("ProductOrderLine", args, "productId = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }
    public void deleteProductOrderHeadNew()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadNew";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getProductOrderheadNew(String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ProductOrderHeadNew where userID='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteAllProductOrderHeadNew(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadNew";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderHead: query: " +query);
        db.execSQL(query);
    }

    public void deleteOneProductOrderHead(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadNew WHERE prodId='"+id+"'";
        //  Log.d(TAG, "deleteOneProductOrderLine: query: " + query);
        System.out.println("deleteOneProductOrderhead: query: " +query);
        db.execSQL(query);
    }


    public boolean updateProductOrderHeadNew(String id,String orderdate,String productType,String activityType,String DistrictName,String ThanaName,String org,String wh,String dealer,String truckno,String pricelist,String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("ids", id);
        args.put("orderdate",orderdate);
        args.put("productType", productType);
        args.put("activityType", activityType);
        args.put("District", DistrictName);
        args.put("Thana", ThanaName);
        args.put("org", org);
        args.put("wh", wh);
        args.put("dealer",dealer);
        args.put("truck",truckno);
        args.put("pricelist",pricelist);
        args.put("userID",userID);

        int i = db.update("ProductOrderHeadNew", args, "ids = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }
    //2022-05-17
    /*public void deleteProductOrderHeadEdit()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadEdit";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getProductOrderheadEdit(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ProductOrderHeadEdit";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public boolean updateProductOrderHeadEdit(String id,String orderdate,String productType,String activityType,String DistrictName,String ThanaName,String org,String wh,String dealer,String truckno,String pricelist,String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("ids", id);
        args.put("orderdate",orderdate);
        args.put("productType", productType);
        args.put("activityType", activityType);
        args.put("District", DistrictName);
        args.put("Thana", ThanaName);
        args.put("org", org);
        args.put("wh", wh);
        args.put("dealer",dealer);
        args.put("truck",truckno);
        args.put("pricelist",pricelist);
        args.put("description",description);


        int i = db.update("ProductOrderHeadEdit", args, "ids = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }
    public void deleteProductOrderLineEdit()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderlineEdit";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getProductOrderLineEdit(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM productorderlineEdit";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteAllProductOrderLineEdit(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderlineEdit";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderLine: query: " +query);
        db.execSQL(query);
    }

    public void deleteOneProductOrderLineEdit(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderlineEdit WHERE productId='"+id+"'";
        //  Log.d(TAG, "deleteOneProductOrderLine: query: " + query);
        System.out.println("deleteOneProductOrderLine: query: " +query);
        db.execSQL(query);
    }


    public boolean updateProductOrderLineEdit(int id,int newQTY,double newTotal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("productQTY", newQTY);
        args.put("totalCost", newTotal);

        int i = db.update("ProductOrderLineEdit", args, "productId = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }*/
    //2022-05-17
    public List<String> getProductCategory(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT catagoryID,catagoryName FROM product_catagory order by catagoryName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1)+" "+cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public boolean insertIntoDCR(String code,String promotools,String workstation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("promo_tools", promotools);
        contentValues.put("work_station", workstation);



        long result = db.insert("dcr", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoPrescription(String dr_code,String product,String image_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("product_name", product);
        contentValues.put("image_name", image_name);



        long result = db.insert("prescription", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoChemist(String chemist_code,String chemist_name,String address,String balance,String phoneNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("chemist_code", chemist_code);
        contentValues.put("chemist_name", chemist_name);
        contentValues.put("chemist_address", address);
        contentValues.put("customer_balance", balance);
        contentValues.put("phone", phoneNo);

        long result = db.insert("chemist", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromo(String bp_code,String bp_name,String pr_code,String pr_name,String m_locator_id,String m_warehouse_id,String ad_userid,String movementqty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bpcode", bp_code);
        contentValues.put("bpname", bp_name);
        contentValues.put("product_code", pr_code);
        contentValues.put("product_name", pr_name);
        contentValues.put("m_locatorto_id", m_locator_id);
        contentValues.put("m_warehouse_id", m_warehouse_id);
        contentValues.put("ad_user_id", ad_userid);
        contentValues.put("movementqty", movementqty);



        long result = db.insert("promo", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }





    public List<String> searchProduct(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and  product.productName LIKE '%"+name+"%' Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> searchProductByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product  inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and product.productID LIKE '%"+code+"%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public List<String> searchCustomerByName(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        //customer_location,customer_phone newly added by shanzida 2021-11-16
        String selectQuery = "SELECT customer_name,code,customer_location,customer_phone FROM customer where customer_name LIKE '"+name+"%' Order by customer_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    //2022-04-16 shanzida
    public Cursor getCustomerdetails(String dealerCode) {
        String selectQuery = "SELECT * FROM customer where code||'-'||customer_name LIKE '" + dealerCode + "' limit 1 ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        return data;

        // closing connection
    }

    public Cursor getCustomerdetailsNew() {
        String selectQuery = "SELECT customer_id,code,(case when customer_id='1' then customer_name else code||'-'||customer_name end) CustomerName,customer_location balance,customer_phone creditlimit FROM customer  order by CustomerName";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        return data;

        // closing connection
    }

    //2021-11-18
    public Cursor getBPGroup() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM bpgroup  order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getProductCategoryCursor(String ConditionAct) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT catagoryID,catagoryName FROM product_catagory where "+ConditionAct+" order by catagoryName ";

        Cursor data = db.rawQuery(selectQuery, null);
        return data;
    }
    public Cursor getProductCursor(String catID) {

        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "SELECT * FROM product order by productName";
        String query = "SELECT * FROM product where catagoryID='"+catID+"'order by productName";

        Log.d("DEBUG","query"+query);
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getCustomer() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT (case when customer_id='1' then customer_name else code||'-'||customer_name end) bp,customer_location balance,* FROM customer  order by bp  "; //limit 1
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor searchCustomerByNameNew(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist  where chemist_name LIKE '%"+name+"%' Order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getWarehouseByOrg(String ad_org_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM warehouse where ad_org_id='"+ad_org_id+"' Order by warehouse_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getWarehouseByRegion(String Region_ID,String City_ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM warehouse where Region_ID='"+Region_ID+"' and City_ID='"+City_ID+"' Order by warehouse_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getCustomerAddress(String name) {

        // Select All Query
        //customer_location,customer_phone newly added by shanzida 2021-11-16
        String selectQuery = "SELECT customer_location,customer_phone FROM customer where customer_id LIKE '" + name + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        return data;

        // closing connection
    }


    public String getCustomerAddressByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String address="";
        String selectQuery = "SELECT chemist_address FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                address=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return address;
    }

    public String getCustomerBalanceByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String balance="";
        String selectQuery = "SELECT customer_balance FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                balance=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return balance;
    }
    public Cursor searchCustomerByCodeNew(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT customer_name FROM " +
                "  where code = '"+code+"' Order by customer_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public List<String> searchCustomerByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        //customer_location,customer_phone newly added by shanzida 2021-11-16
        String selectQuery = "SELECT customer_name,code,customer_location,customer_phone FROM customer where code  LIKE '%"+code+"%' Order by customer_name";



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public boolean insertIntoBatchDetailsNew( String batchCode,int batchID, int OrgID,String OrgName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("batchCode", batchCode);
        contentValues.put("batchID", batchID);
        contentValues.put("OrgID", OrgID);
        contentValues.put("OrgName", OrgName);

        long res = db.insert("batchDetails", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertIntoBatchDetailstoNew( String batchCode,int batchID, int OrgID,String OrgName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("batchCode", batchCode);
        contentValues.put("batchID", batchID);
        contentValues.put("OrgID", OrgID);
        contentValues.put("OrgName", OrgName);

        long res = db.insert("batchDetailsto", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getBatchIDOrg(String batchCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select OrgID,OrgName from batchDetails where batchCode = '"+batchCode+"' ",null);
        return res;

    }

    public Cursor getSalesOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();

        // String query = "SELECT product_name,uom,ordered_qty,pricePerProduct,customer_name,orgid FROM sales_order_record where docNo='"+docNo+"'";
        String query = "SELECT * FROM sales_order_record where docNo='"+docNo+"'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderLineDataTemp(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderLineDataNew(int orderid,String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where orderid ="+orderid+" and docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getSalesOrderListForApproval(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM orderApproval";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getOrderListForApproval(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM tApproval where isforward='N' ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPromoOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT productID,quantity FROM promo_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSupervisorData(String userid,String docid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT supid,supnmae FROM supervisor where userid='"+userid+"' and docid='"+docid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesType(String prodType){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_id,salesType_name from salesType where prodType like '%"+prodType+"%' order by salesType_id";  //lower(salesType_name) like '%"+prodType.toLowerCase()+"%'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSalesTypeCheck(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_id,salesType_name from salesType order by salesType_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //anika

    public void deleteCbfIFTFTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM CbfIFTF ";
        db.execSQL(query);
    }

    public Cursor getActivityType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_id,salesType_name from salesType";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //anika

    public String getUserIDFromEmployee(){


        String ad_user_id = null;

        // Select All Query
        String selectQuery = "select ad_user_id from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ad_user_id=(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return ad_user_id;
    }

    public List<String> getUserDetailsFromEmployee(){


        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "select ad_user_id,user_level,name from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
                labels.add(cursor.getString(2));
                labels.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public boolean insertIntoCbfIFTF( String value,int CbfIFTF_ID,String OrgName,String description,String frombatch,String tobatch,
                                      String frombpcode,String tobpcode ,String frombp ,String tobp,String isstarter ,String isgrower ,String isfinisher ,String transportation ,
                                      String transferreason ,String distancekm ,String transferby_code ,String transferby_name ,String starter_qty
            ,String grower_qty ,String finisher_qty ,String paidamt, String movementdate, String  iscrumbled,String  crumbled_qty) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", value);
        contentValues.put("CbfIFTF_ID", CbfIFTF_ID);
        contentValues.put("OrgName", OrgName);
        contentValues.put("description", description);
        contentValues.put("frombatch", frombatch);
        contentValues.put("tobatch", tobatch);
        contentValues.put("frombpcode", frombpcode);
        contentValues.put("tobpcode", tobpcode);
        contentValues.put("frombp", frombp);
        contentValues.put("tobp", tobp);
        contentValues.put("isstarter", isstarter);
        contentValues.put("isgrower", isgrower);
        contentValues.put("isfinisher", isfinisher);
        contentValues.put("transportation", transportation);
        contentValues.put("transferreason", transferreason);
        contentValues.put("distancekm", distancekm);
        contentValues.put("transferby_code", transferby_code);
        contentValues.put("transferby_name", transferby_name);
        contentValues.put("starter_qty", starter_qty);
        contentValues.put("grower_qty", grower_qty);
        contentValues.put("finisher_qty", finisher_qty);
        contentValues.put("paidamt", paidamt);
        contentValues.put("movementdate", movementdate);
        contentValues.put("iscrumbled", iscrumbled);
        contentValues.put("crumbled_qty", crumbled_qty);

        long res = db.insert("CbfIFTF", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getCbfIFTF(int CbfIFTF_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CbfIFTF where CbfIFTF_ID = "+CbfIFTF_ID+" ",null);
        return res;

    }

    public String getProductName(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT productName FROM product where productID='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }
    public String getProductNameFromPromo(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT product_name FROM promo where product_code='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }


    public String getChemistName(String chemId){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist where chemist_code='"+chemId+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getDrNameByCode(String drID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT drName FROM dr_information where dr_code='"+drID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getdrCodeByDocNo(String docNo){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM promo_order where docNo='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }



    public List<String> getAllDrNames(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT drName FROM dr_information";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public String getTotalPrice(String docNo){


        String total = null;
        //total_price convert to new BigDecimal 2021-11-17 by shanzida
        BigDecimal total_price=new BigDecimal(0);
        // Select All Query
        String selectQuery = "select qtyordered  from sales_orderline where docNo='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String qty = cursor.getString(0);

                total_price = total_price.add(new BigDecimal(qty)); //total_price convert to new BigDecimal 2021-11-17 by shanzida
            } while (cursor.moveToNext());
            total= String.valueOf(total_price);
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return total;
    }

    public String getOrderedProduct(String docNo){


        String name1 = "";

        // Select All Query
        String selectQuery = "select product_name from sales_orderline where docno='"+docNo+"' order by product_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=(cursor.getString(0));
                name1 += name + "\n";
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name1;
    }

    public String getOrderedProductQty(String docNo){


        String qty = "";

        // Select All Query
        String selectQuery = "select qtyordered from sales_orderline where docno='"+docNo+"' order by product_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String qty1=(cursor.getString(0));
                qty += qty1 + "\n";
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return qty;
    }


    public int getCatagoryID(String catagoryName){


        int id = 0;

        // Select All Query
        String selectQuery = "SELECT catagoryID FROM product_catagory where catagoryName='"+catagoryName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getProductIDByProductName(String productName){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT productID FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }


    public String getProductIDByProductNameFromPromo(String productName){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT product_code FROM promo where product_name='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getDrCodeByNameAddress(String name,String Address){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM dr_information where drName='"+name+"' and dr_address='"+Address+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }
    public String getChemistCodeByName(String name,String Address){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM chemist where chemist_name='"+name+"' and chemist_address='"+Address+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getChemistIDByName(String name){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM chemist where chemist_name='"+name+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }


    public String getProductQty(String productName){


        String avlQty=null;

        // Select All Query
        String selectQuery = "SELECT availableQty FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                avlQty=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return avlQty;
    }
    public String getonHandProductQty(String productName){


        String avlQty=null;

        // Select All Query
        String selectQuery = "SELECT onhandQty FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                avlQty=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return avlQty;
    }



    public String getProductPrice(String productName){


        String Price=null;

        // Select All Query
        String selectQuery = "SELECT price FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Price=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return Price;
    }


    public String getProductUnit(String productName){
        String unit=null;
        String selectQuery = "SELECT unit FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                unit=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return unit;
    }

    public int getAvailableQty(String productCode){


        int qty=0;

        // Select All Query
        String selectQuery = "SELECT movementqty FROM promo where product_code='"+productCode+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                qty=(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return qty;
    }




    public List<String> getAllProductByCatagory(int catagoryID){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product where catagoryID="+catagoryID+" Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }






    public List<String> getAllProductCatagory(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT catagoryName FROM product_catagory order by catagoryName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public boolean getSalesOrderLineByDocAndProduct(String docNo,String productID){

        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where docNo='"+docNo+"' and productID='"+productID+"'";
        Cursor data = db.rawQuery(query, null);
        if (data.moveToNext()){

            res=true;
        }
        else{
            res=false;
        }
        return res;

    }

    public boolean getPromoOrderLineByDocAndProduct(String docNo,String productID){

        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM promo_orderline where docNo='"+docNo+"' and productID='"+productID+"'";
        Cursor data = db.rawQuery(query, null);
        if (data.moveToNext()){

            res=true;
        }
        else{
            res=false;
        }
        return res;

    }

    public Cursor getSalesOrderByDate(String date1,String date2){
        SQLiteDatabase db = this.getWritableDatabase();



        // String query = "SELECT DISTINCT docNo,date,customer_name FROM sales_order_record where date between '"+date1+"' and '"+date+"'";
        String query = "SELECT DISTINCT docNo,date,customer_name,status,orgid FROM sales_order_record where date between '"+date1+"' and '"+date2+"' ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSalesOrderByDateAndStatus(String date1,String date2,String status){
        SQLiteDatabase db = this.getWritableDatabase();

        // String query = "SELECT DISTINCT docNo,date,customer_name FROM sales_order_record where date between '"+date1+"' and '"+date+"'";
        String query = "SELECT DISTINCT docNo,date,customer_name,status,orgid FROM sales_order_record where date between '"+date1+"' and '"+date2+"' and status='"+status+"'";
        Cursor data = db.rawQuery(query, null);
        return data;

    }


    public List<String> getAllChemists(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }






    //end
    public void updateName(String newProduct,String newItem,String newQuantity, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + newProduct + "',"+COL3+ "= '"+newItem+"', "+COL4+" = '"+newQuantity+ "' WHERE " + COL1 + " = " + id+"" ;

        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newItem);
        db.execSQL(query);


    }

    public boolean updatePromoQty(String product_code,int qty){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("movementqty", qty);

        int i = db.update("promo", args, "product_code = ?", new String[]{product_code});
        if( i > 0){
            return true;
        }
        return false;

    }
    public void deleteBPGroup(){
        SQLiteDatabase db = this.getWritableDatabase();

        String q="delete from bpgroup";

        db.execSQL(q);
    }

    public void deleteSupervisorData(){
        SQLiteDatabase db = this.getWritableDatabase();

        String q="delete from supervisor";

        Log.d(TAG, "deleteName: query: " + q);
        // Log.d(TAG, "deleteName: Deleting " + item + " from database.");
        db.execSQL(q);
    }

    public void deleteCustomer1(){
        SQLiteDatabase db = this.getWritableDatabase();

        String q="delete from customer";

        db.execSQL(q);
    }
    public void deleteCustomer(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  customer";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }



    public void deleteName(int id, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = " + id + "";


        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + item + " from database.");
        db.execSQL(query);
    }

    public void deleteSalesOrderLine(String id, String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_orderline WHERE docNo='"+docNo+"' AND productID='"+id+"'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoOrderLine(String id, String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo_orderline WHERE docNo='"+docNo+"' AND productID='"+id+"'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteSalesType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  salesType";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteProductCatagory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product_catagory";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }



    public Cursor getPromoOrder(String docNo) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT dr_code,date FROM promo_order where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    public Cursor getDrDetails() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM dr_information order by drName ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPromoProduct(String ad_user_id,String ad_warehouse_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT product_code,product_name,movementqty FROM promo order by product_name ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getChemistDetails() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getOrganizationUserID(String UserID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT userid FROM organization where userid like '" + UserID + "' limit 1";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getOrganizationSelected(String OName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT ad_org_id,org_name FROM organization where org_name like '"+OName+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getBranch() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT branch_name,branch_id FROM organization where branch_name != 'TRANSIT WH' and branch_name != 'RAW MATERIALS WH'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getSkuWiseStock(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select productName,availableQty,unit from product \n" +
                "inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deletechemist() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  chemist";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletedr() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  dr_information";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoTools() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo_tools";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrganization() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  organization";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteWarehouse() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  warehouse";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  employee";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrderApprovalTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  orderApproval";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrderApprovalLine(String erp_doc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  orderApproval where erp_orderNo = '"+erp_doc+"'  ";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteOrdertApprovalTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  tApproval";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrdertApprovalLine(String erp_doc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  tApproval where erp_orderNo = '"+erp_doc+"'  ";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }


    public Cursor Currentdate(String ad_user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select currentdate from employee where ad_user_id='"+ad_user_id+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteSalesOrderRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_order_record";
        db.execSQL(query);
    }
    //2022-09-20
    public Cursor getProductOrderLineProduct(String userID,String selectedProdId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Distinct mproduct_Id FROM productorderline where mproduct_Id='"+selectedProdId+"' and userID='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}