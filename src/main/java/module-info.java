open module com.bdd.psymeeting {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.postgresql.jdbc;
    requires c3p0;
    requires java.sql;
    requires java.desktop;
    requires java.naming;
    requires com.jfoenix;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.iconli.core;
    // add icon pack modules
    requires org.kordamp.ikonli.fontawesome5;
    //opens com.bdd.psymeeting to javafx.fxml;
    exports com.bdd.psymeeting.model;
    exports com.bdd.psymeeting.model.tools;
    exports com.bdd.psymeeting.controller;
    exports com.bdd.psymeeting.controller.consultations;
    exports com.bdd.psymeeting.controller.home;
    exports com.bdd.psymeeting.controller.login;
    exports com.bdd.psymeeting.controller.others;
    exports com.bdd.psymeeting.controller.patients;
    exports com.bdd.psymeeting;
}