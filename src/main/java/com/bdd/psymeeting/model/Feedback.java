/*
 * Copyright (c) 2020. Thomas GUILLAUME & Gabriel DUGNY
 */

package com.bdd.psymeeting.model;


import com.bdd.psymeeting.App;

import java.sql.*;
import java.util.ArrayList;

public class Feedback {

    // Feedback attributes from table FEEDBACK
    private int feedbackID;
    private ArrayList<String> commentaries;
    private ArrayList<String> keywords;
    private ArrayList<String> postures;
    private int indicator;

    private int consultation_id;

    // --------------------
    //   Constructors
    // --------------------

    // Feedback from DB
    public Feedback(int consultationID) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try (Connection connection = App.database.getConnection()) {

            this.consultation_id = consultationID;
            this.commentaries = new ArrayList<>();
            this.keywords = new ArrayList<>();
            this.postures = new ArrayList<>();

            // get feedback attribute
            String query = "select feedback_id, indicator from feedback where feedback_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, consultationID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.feedbackID = resultSet.getInt(1);
                this.indicator = resultSet.getInt(2);
            }

            // get commentary
            query = "select commentary from feedback join commentary c on feedback.feedback_id = c.feedback_id where consultation_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, consultationID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) this.getCommentary().add(resultSet.getString(1));

            // get posture
            query = "select posture from feedback join posture p on feedback.feedback_id = p.feedback_id where consultation_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, consultationID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) this.getPosture().add(resultSet.getString(1));

            // get keyword
            query = "select keyword from feedback join keyword k on feedback.feedback_id = k.feedback_id where consultation_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, consultationID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) this.getKeyword().add(resultSet.getString(1));


        } catch (SQLException ex) {
            System.out.println("Error add name or last name to the user (3)");
            ex.printStackTrace();
            System.out.println(ex.getErrorCode() + " : " + ex.getMessage());
        } finally {
            if( preparedStatement != null ) preparedStatement.close();
            if( resultSet != null ) resultSet.close();
        }
    }


    // ----------------------
    //  Getters and Setters
    // ----------------------

    public static int getLastFeedbackID() {
        try (Connection connection = App.database.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery("select max(FEEDBACK_ID) from FEEDBACK");
            result.next();
            return result.getInt(1);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return -1;
        }

    }

    public static boolean insertFeedback(int consultation_id) {
        try (Connection connection = App.database.getConnection()) {
            String query = "insert into FEEDBACK (FEEDBACK_ID, CONSULTATION_ID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (getLastFeedbackID() != -1) {
                preparedStatement.setInt(1, getLastFeedbackID() + 1);
                preparedStatement.setInt(2, consultation_id + 1);
                return true;
            } else return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public ArrayList<String> getCommentary() {
        return commentaries;
    }

    public ArrayList<String> getKeyword() {
        return keywords;
    }

    // ----------------------
    //  Setters
    // ----------------------

    public ArrayList<String> getPosture() {
        return postures;
    }

    public int getIndicator() {
        return indicator;
    }

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }


    // --------------------
    //   Statement methods
    // --------------------

    public int getConsultation_id() {
        return consultation_id;
    }

    public void setConsultation_id(int consultation_id) {
        this.consultation_id = consultation_id;
    }

}
