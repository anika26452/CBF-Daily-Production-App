package com.sysnova.cbfdailyproductionapp.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresqlConnection {
    public Connection conn;
    public String error;
    public Connection getConn(){
        try{
            Class.forName("org.postgresql.Driver");

            //Live
           conn = DriverManager.getConnection("jdbc:postgresql://172.16.253.10:5432/adempiere", "adempiere", "adempierews");

            //Test
  //          conn = DriverManager.getConnection("jdbc:postgresql://172.16.253.22:5433/ws_15nov2023", "adempiere", "adempiere");

        }catch(ClassNotFoundException e){
            error = e.getMessage();
        }catch (SQLException sqlex){
            error = sqlex.getMessage();
        }catch (Exception ex){
            error = ex.getMessage();
        }
        return conn;
    }
}
