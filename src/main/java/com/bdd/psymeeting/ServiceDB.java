/*
 * Copyright (c) 2020. Thomas GUILLAUME & Gabriel DUGNY
 */

package com.bdd.psymeeting;

import com.bdd.psymeeting.controller.consultations.AddConsultationController;
import com.bdd.psymeeting.controller.consultations.ConsultationHistoric;
import com.bdd.psymeeting.controller.home.HomeController;
import com.bdd.psymeeting.controller.patients.PatientDetailsController;
import com.bdd.psymeeting.controller.patients.PatientFormController;
import com.bdd.psymeeting.controller.patients.PatientsController;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import javafx.concurrent.Worker;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Database class
 */
public class ServiceDB {


    //private static final OracleDB instance = new OracleDB();
    public ComboPooledDataSource comboPooledDataSource;

    public ServiceDB() {

    }

    public Connection getConnection() throws SQLException {
        return this.comboPooledDataSource.getConnection();
    }

    public void resetPoolManager() {
        this.comboPooledDataSource.resetPoolManager();
        // Cancel services
        if (PatientsController.loadPatients.getState() != Worker.State.READY) {
            PatientsController.loadPatients.cancel();
            PatientsController.loadPatients.reset();
        }
        if (PatientFormController.addingPatientStatic != null && PatientFormController.addingPatientStatic.getState() != Worker.State.READY) {
            PatientFormController.addingPatientStatic.cancel();
            PatientFormController.addingPatientStatic.reset();
        }
        if (PatientDetailsController.updateUserDetails.getState() != Worker.State.READY) {
            PatientDetailsController.updateUserDetails.cancel();
            PatientDetailsController.updateUserDetails.reset();
        }
        if (HomeController.loadConsultationsWeek.getState() != Worker.State.READY) {
            HomeController.loadConsultationsWeek.cancel();
            HomeController.loadConsultationsWeek.reset();
        }
        if (AddConsultationController.addPatientStatic != null && AddConsultationController.addPatientStatic.getState() != Worker.State.READY) {
            AddConsultationController.addPatientStatic.cancel();
            AddConsultationController.addPatientStatic.reset();
        }
        if (AddConsultationController.updateNewConsultationStatic != null && AddConsultationController.updateNewConsultationStatic.getState() != Worker.State.READY) {
            AddConsultationController.updateNewConsultationStatic.cancel();
            AddConsultationController.updateNewConsultationStatic.reset();
        }
        if (ConsultationHistoric.loadConsultationsStatic != null && ConsultationHistoric.loadConsultationsStatic.getState() != Worker.State.READY) {
            ConsultationHistoric.loadConsultationsStatic.cancel();
            ConsultationHistoric.loadConsultationsStatic.reset();
        }
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

            this.comboPooledDataSource.setMinPoolSize(2);
            this.comboPooledDataSource.setAcquireIncrement(2);
            this.comboPooledDataSource.setMaxPoolSize(5);

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
