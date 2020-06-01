/*
 * Copyright (c) 2020. Thomas GUILLAUME & Gabriel DUGNY
 */

package com.bdd.psymeeting.model;

import com.bdd.psymeeting.App;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Patient {

    // --------------------
    //   Attributes
    // --------------------

    protected static final int KID_AGE_LIMIT = 12;
    protected static final int TEEN_AGE_LIMIT = 18;
    private int patientId;
    private String name;
    private String lastName;
    private boolean newPatient;
    private Date birthday;
    private String gender;
    private String relationship;
    private String discoveryWay;
    private User user;
    private ArrayList<Job> jobs;
    private ArrayList<Consultation> consultationHistoric;

    // --------------------
    //   Constructors
    // --------------------

    public Patient(int patientId) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query;

        try (Connection connection = App.database.getConnection()) {

            this.patientId = patientId;

            // PATIENT INFO
            query = "select NAME, LAST_NAME, BIRTHDAY, GENDER, RELATIONSHIP, DISCOVERY_WAY from PATIENT " +
                    "where PATIENT_ID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, this.patientId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            this.name = resultSet.getString(1);
            this.lastName = resultSet.getString(2);
            this.birthday = resultSet.getDate(3);
            this.gender = resultSet.getString(4);
            this.relationship = resultSet.getString(5);
            this.discoveryWay = resultSet.getString(6);

            // CONSULTATIONS HISTORIC
            this.consultationHistoric = new ArrayList<>();
            query = "select CC.CONSULTATION_ID from CONSULTATION\n" +
                    "join CONSULTATION_CARRYOUT CC on CONSULTATION.CONSULTATION_ID = CC.CONSULTATION_ID\n" +
                    "where PATIENT_ID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.consultationHistoric.add(new Consultation(resultSet.getInt(1)));
            }

            // JOBS
            this.jobs = new ArrayList<>();
            query = "select JOB_NAME, JOB_DATE\n" +
                    "from JOBS J\n" +
                    "join PATIENTJOB P on J.JOBS_ID = P.JOBS_ID\n" +
                    "where PATIENT_ID = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, this.patientId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.jobs.add(new Job(resultSet.getString(1), resultSet.getDate(2)));
            }

            // USER INFO
            this.user = User.getUserByPatientID(this.getPatientId());

        } catch (SQLException ex) {
            System.err.println("Error loading information about patient with ID " + patientId);
            ex.printStackTrace();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (resultSet != null) resultSet.close();
        }

    }

    // patient without jobs
    public Patient(int patientId, String name, String lastName, boolean newPatient) {
        this.patientId = patientId;
        this.name = name;
        this.lastName = lastName;
        this.newPatient = newPatient;
    }

    // patient (name and last name)
    public Patient(int patientId, String name, String lastName) {
        this.patientId = patientId;
        this.name = name;
        this.lastName = lastName;
    }

    // patient with email
    public Patient(int patientId, String name, String lastName, String email) {
        this.patientId = patientId;
        this.name = name;
        this.lastName = lastName;
        this.user = User.getUserByEmail(email);
    }

    public Patient(int patientId, String name, String lastName, Date birthday, String gender, String relationship, String discoveryWay) {
        this.patientId = patientId;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.relationship = relationship;
        this.discoveryWay = discoveryWay;

        this.jobs = new ArrayList<>();
        this.consultationHistoric = new ArrayList<>();
    }

    // --------------------
    //   Get methods
    // --------------------


    public static boolean insertIntoPatientTable(ArrayList<Patient> patients, int lastPatientID) throws SQLException {

        PreparedStatement preparedStatement = null;

        try (Connection connection = App.database.getConnection()) {
            // the insert statement
            String query = " insert into patient (patient_id, name, last_name)"
                    + " values (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            // create the insert preparedStatement
            if (lastPatientID != -1) {
                for (Patient p : patients // for each patients saved in tmp_patients
                ) {
                    if (p.isNewPatient()) { // if patient does not exist in database

                        // config parameters//
                        // patient already exist
                        /*if (p.getPatient_id() <= lastPatientID) preparedStmt.setInt(1, p.getPatient_id());
                        else {
                            tmpLastPatientID++;
                            preparedStmt.setInt(1, tmpLastPatientID);
                        }*/
                        preparedStatement.setInt(1, p.getPatientId());
                        preparedStatement.setString(2, p.getName());
                        preparedStatement.setString(3, p.getLastName());
                        // execute the preparedStatement
                        preparedStatement.executeUpdate();
                    }
                }
            }
            return true;
        } catch (SQLException ex) {
            System.err.println("Error insert patients in table");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) preparedStatement.close();
        }
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    // --------------------
    //   Set methods
    // --------------------

    public void setBirthday(LocalDate birthday) {
        this.birthday = Date.valueOf(birthday);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getDiscoveryWay() {
        return discoveryWay;
    }

    public void setDiscoveryWay(String discoveryWay) {
        this.discoveryWay = discoveryWay;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    // --------------------
    //  Methods
    // --------------------


    // --------------------
    //   Statement methods
    // --------------------

    public static int getLastPrimaryKeyId() {

        try (Connection connection = App.database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("select max(patient_id) from PATIENT")) {

            resultSet.next();

            return resultSet.getInt(1);

        } catch (SQLException throwable) {
            System.err.println("Get last patient ID failed, return 0");
            throwable.printStackTrace();
            return 0;
        }
    }

    public static Patient getPatientByEmail(String email) {
        Patient patient = null;
        try (Connection connection = App.database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("select\n" +
                                                                                                                                             "       u.PATIENT_ID,\n" +
                                                                                                                                             "       p.NAME,\n" +
                                                                                                                                             "       p.LAST_NAME\n" +
                                                                                                                                             "from USER_APP u\n" +
                                                                                                                                             "join PATIENT P on u.PATIENT_ID = P.PATIENT_ID\n" +
                                                                                                                                             "where u.EMAIL = '" + email + "'"); ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                patient = new Patient(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), false);
                return patient;
            }
        } catch (SQLException throwable) {
            System.err.println("Error get patient with email " + email);
            throwable.printStackTrace();
        }
        return patient;
    }

    public static ArrayList<Patient> getPatientsProfiles() {

        ArrayList<Patient> list_patients = new ArrayList<>();

        // adding patients to list_patients
        try (Connection connection = App.database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("select patient_id from PATIENT")) {
            while (resultSet.next()) {
                list_patients.add(new Patient(resultSet.getInt(1)));
            }
        } catch (SQLException ex) {
            System.err.println("Error get all patients profiles");
            ex.printStackTrace();
        }
        return list_patients;
    }

    public static ArrayList<Patient> getSimplyPatientsProfiles() {

        ArrayList<Patient> list_patients = new ArrayList<>();

        // adding patients to list_patients
        try (Connection connection = App.database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("select PATIENT.PATIENT_ID, NAME, LAST_NAME, EMAIL from PATIENT join USER_APP UA on PATIENT.PATIENT_ID = UA.PATIENT_ID")) {
            while (resultSet.next()) {
                list_patients.add(new Patient(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)));
            }

        } catch (SQLException ex) {
            System.err.println("Error get all patients profiles");
            ex.printStackTrace();
        }
        return list_patients;
    }

    public ArrayList<Consultation> getConsultationHistoric() {
        return consultationHistoric;
    }

    public void setConsultationHistoric(ArrayList<Consultation> consultationHistoric) {
        this.consultationHistoric = consultationHistoric;
    }

    public boolean isNewPatient() {
        return newPatient;
    }

    public void setNewPatient(boolean newPatient) {
        this.newPatient = newPatient;
    }

    /**
     * Update the Patient in DB with local state.
     *
     * @return true if succeeded !
     */
    public boolean updatePatient() throws SQLException {

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = App.database.getConnection();
            connection.setAutoCommit(false);

            String request = "UPDATE PATIENT SET NAME = ?, LAST_NAME = ?, BIRTHDAY = ?, GENDER = ?, RELATIONSHIP = ?, DISCOVERY_WAY= ? WHERE PATIENT_ID = ?";
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, this.getName().toUpperCase());
            preparedStatement.setString(2, this.getLastName().toUpperCase());
            preparedStatement.setDate(3, this.getBirthday());
            preparedStatement.setString(4, this.getGender() != null ? this.getGender() : "");
            preparedStatement.setString(5, this.getRelationship() != null ? this.getRelationship() : "");
            preparedStatement.setString(6, this.getDiscoveryWay() != null ? this.getDiscoveryWay() : "");
            preparedStatement.setInt(7, this.getPatientId());

            preparedStatement.executeUpdate();

            connection.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Error updating patient profil, transaction is being roll back");
            if (connection != null) connection.rollback();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return false;
    }

    /**
     * Insert new patient to Patient Table
     *
     * @return true if succeeded
     */
    public boolean insertPatient() throws SQLException {

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = App.database.getConnection();
            connection.setAutoCommit(false);
            String request = "INSERT INTO PATIENT (PATIENT_ID, NAME, LAST_NAME, BIRTHDAY, GENDER, RELATIONSHIP, DISCOVERY_WAY) VALUES (?,?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setInt(1, this.getPatientId());
            preparedStatement.setString(2, this.getName());
            preparedStatement.setString(3, this.getLastName());
            preparedStatement.setDate(4, new Date(this.getBirthday().getTime()));
            preparedStatement.setString(5, this.getGender());
            preparedStatement.setString(6, this.getRelationship());
            preparedStatement.setString(7, this.getDiscoveryWay());

            preparedStatement.executeUpdate();

            connection.commit();

            return true;

        } catch (SQLException ex) {
            System.err.println("Error insert patient" + this.getPatientId() + " in database");
            System.err.println("Transaction is being rolled back");
            if (connection != null) connection.rollback();
            ex.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    public static void submitNewPatient(Patient patient, User user, ArrayList<Job> jobs) throws SQLException {

        Connection connection = null;
        try {
            connection = App.database.getConnection();
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint("savePoint");

            boolean resultPatient = patient.insertPatient();
            boolean resultUser = user.insertNewUser();
            boolean resultJob = Job.insertJobs(jobs);

            System.out.println(resultPatient + " " + resultUser + resultJob);

            if (resultPatient && resultJob && resultUser) {
                System.out.println("Commit patient");
                connection.commit(); // commit if no SQL error
            } else {
                System.out.println("Rollback patient");
                connection.rollback(savepoint);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            if (connection != null) connection.rollback();
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    // --------------------
    //   Override methods
    // --------------------

    @Override
    public String toString() {
        return this.getName() + " " + this.getLastName() + (this.user != null ? " : " + this.user.getEmail() : "");
    }
}
