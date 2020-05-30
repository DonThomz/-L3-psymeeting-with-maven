/*
 * Copyright (c) 2020. Thomas GUILLAUME & Gabriel DUGNY
 */

package com.bdd.psymeeting;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Database class
 */
public class ServiceDB {


    //private static final OracleDB instance = new OracleDB();
    private ComboPooledDataSource comboPooledDataSource;

    public ServiceDB() {

    }

//    public static final OracleDB getInstance() {
//        return instance;
//    }

    public Connection getConnection() throws SQLException {
        return this.comboPooledDataSource.getConnection();
    }

    public void close() {
        this.comboPooledDataSource.close();
    }

    /**
     * Connect to the DB.
     */
    public boolean connectionDatabase(String username, String password) {
        System.out.println("Connecting to DB...");
        String urlDb = "jdbc:postgresql://rogue.db.elephantsql.com:5432/lepigeut";
        String usernameTest = "lepigeut";
        String passwordTest = "73BxWKUvQ7MmtaR-eMOxQpotOtAJ3hoI";
        try {
            this.comboPooledDataSource = new ComboPooledDataSource();
            this.comboPooledDataSource.setDriverClass("org.postgresql.Driver");
            this.comboPooledDataSource.setJdbcUrl(urlDb);
            this.comboPooledDataSource.setUser(usernameTest);
            this.comboPooledDataSource.setPassword(passwordTest);

            this.comboPooledDataSource.setMinPoolSize(5);
            this.comboPooledDataSource.setAcquireIncrement(5);
            this.comboPooledDataSource.setMaxPoolSize(20);

            this.comboPooledDataSource.setAcquireRetryAttempts(0);

            this.comboPooledDataSource.setUnreturnedConnectionTimeout(10);
            this.comboPooledDataSource.setDebugUnreturnedConnectionStackTraces(true);

            this.comboPooledDataSource.setTestConnectionOnCheckout(true);

            System.out.println("Pooled data source set up: done!");

            return username.equals("admin") && password.equals("adminpwd");

        } catch (PropertyVetoException e) {
            System.out.println("Unable to set up connection pool!");
            e.printStackTrace();
            // System.exit(1);
            return false;
        }
    }

    /**
     * Close the connection to the DB properly.
     */
    public void closeDatabase() {
        this.comboPooledDataSource.close();
    }


}
