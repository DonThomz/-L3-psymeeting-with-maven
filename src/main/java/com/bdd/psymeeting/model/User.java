/*
 * Copyright (c) 2020. Thomas GUILLAUME & Gabriel DUGNY
 */

package com.bdd.psymeeting.model;

import com.bdd.psymeeting.App;

import java.sql.*;
import java.util.ArrayList;

public class User {

    // --------------------
    //   Attributes
    // --------------------

    private int user_id;
    private int patient_id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String lastName;

    private boolean new_user;


    // --------------------
    //   Constructors
    // --------------------
    public User(String username) {
        this.username = username;
        /*if (this.username.equals("admin")) {
            try (Connection connection = App.database.getConnection()) {
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery("select NAME, LAST_NAME from ADMINISTRATOR");
                resultSet.next();
                this.name = resultSet.getString(1);
                this.last_name = resultSet.getString(2);
                System.out.println("User: " + this.name + " " + this.last_name);

            } catch (SQLException ex) {
                System.out.println("Error add name or last name to the user (1)");
                System.out.println(ex.getErrorCode() + " : " + ex.getMessage());
            }
        }*/
    }


    // guest user without password => generate a random password
    public User(int user_id, String email, int patient_id, boolean new_user) {
        this.user_id = user_id;
        this.email = email;
        if (new_user) this.password = "tmp_password";
        this.patient_id = patient_id;
        this.new_user = new_user;
    }

    // --------------------
    //   Statement methods
    // --------------------
    public static int getLastUserId() {

        try (Connection connection = App.database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("select max(USER_ID) from USER_APP")) {
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException throwable) {
            throwable.printStackTrace();
            System.err.println("Get last user ID failed, return 0");
            return 0;
        }
    }

    public static int getPatientIDByEmail(String email) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try (Connection connection = App.database.getConnection()) {
            preparedStatement = connection.prepareStatement("select\n" +
                    "       u.PATIENT_ID\n" +
                    "from USER_APP u\n" +
                    "where u.EMAIL = ? ");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(1);
            else return 0;
        } catch (SQLException throwable) {
            System.err.println("Get patient ID failed, return 0");
            throwable.printStackTrace();
            return 0;
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (resultSet != null) resultSet.close();
        }
    }

    public static String getUserFullName(String username) {
        // get name
        if (username.equals("admin")) {
            return "Olivia Pope";
        } else {
            return "Invité";
        }
    }

    public static User getUserByPatientID(int patientID) {

        try (Connection connection = App.database.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("select USER_ID, EMAIL from USER_APP where PATIENT_ID = ?");
            preparedStatement.setInt(1, patientID);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Read results
            if (resultSet.next()) {
                User user = new User(resultSet.getInt(1), resultSet.getString(2), patientID, false);
                preparedStatement.close();
                resultSet.close();
                return user;
            } else return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Error get user by patient ID = " + patientID);
            return null;
        }

    }

    /**
     * Check if a user exists with its email.
     *
     * @param email: the mail for which you want to check if a user exist
     * @return true if exist or false if not
     */
    public static boolean userExist(String email) throws SQLException {
        try (Connection connection = App.database.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("select email from user_app")) {

            // Read results
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(email)) {
                    return true;
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            System.err.println("Error checking if user with email " + email + " exists in DB");
            return false;
        }
        return false;
    }

    public static User getUserByEmail(String email) {
        try (Connection connection = App.database.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("select USER_ID, EMAIL, PATIENT_ID from USER_APP where EMAIL = ?");
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Read results
            if (resultSet.next()) {
                // return user who already exist in database
                User user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), false);
                preparedStatement.close();
                resultSet.close();
                return user;
            } else return null;

        } catch (SQLException throwable) {
            throwable.printStackTrace();
            System.err.println("Error get user with email " + email);
            return null;
        }
    }

    /**
     * Insert into user table new users
     *
     * @param users list of users to add
     * @return true if succeeded
     */
    public static boolean insertIntoUserTable(ArrayList<User> users) {
        try (Connection connection = App.database.getConnection()) {

            // the insert statement
            String query = " insert into USER_APP (USER_ID, EMAIL, PASSWORD, PATIENT_ID)"
                    + " values (?, ?, ?, ?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query);

            int lastUserId = User.getLastUserId();

            // create the insert preparedStatement
            if (lastUserId != -1) {
                for (User u : users // for each patients saved in tmp_patients
                ) {
                    if (u.isNew_user()) { // if patient does not exist in database

                        // config parameters
                        //lastUserId++;
                        preparedStmt.setInt(1, u.getUser_id());
                        preparedStmt.setString(2, u.getEmail());
                        preparedStmt.setString(3, u.getPassword());
                        preparedStmt.setInt(4, u.getPatient_id());
                        // execute the preparedStatement
                        preparedStmt.executeUpdate();
                    }
                }
                preparedStmt.close();
                return true;
            } else return false;
        } catch (SQLException ex) {
            System.err.println("Error updating table user in database");
            ex.printStackTrace();
            return false;
        }
    }

    // --------------------
    //   Get methods
    // --------------------
    public String getUsername() {
        return username;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public boolean isNew_user() {
        return new_user;
    }

    /**
     * Insert 1 new user to user table
     *
     * @return true if succeeded
     */
    public boolean insertNewUser() throws SQLException {

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = App.database.getConnection();
            connection.setAutoCommit(false);
            // the insert statement
            String query = " insert into USER_APP (USER_ID, EMAIL, PASSWORD, PATIENT_ID)"
                    + " values (?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(query);
            // create the insert preparedStatement
            if (this.isNew_user()) { // if patient does not exist in database
                // config parameters
                preparedStatement.setInt(1, this.getUser_id());
                preparedStatement.setString(2, this.getEmail());
                preparedStatement.setString(3, this.getPassword());
                preparedStatement.setInt(4, this.getPatient_id());
                // execute the preparedStatement
                preparedStatement.executeUpdate();
                connection.commit();
                return true;
            }

        } catch (SQLException ex) {
            System.err.println("Error insert user in database !");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            if (connection != null) {
                try {
                    System.err.println("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            return false;
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return false;
    }


}
