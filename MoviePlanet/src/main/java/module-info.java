module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    requires org.postgresql.jdbc;

    opens main to javafx.fxml;
    exports main;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports Facades;
    opens Facades to javafx.fxml;
    exports Services;
    opens Services to javafx.fxml;
    opens BuisnessClasses to javafx.fxml;
    exports BuisnessClasses;
    exports Persistence;
    opens Persistence to javafx.fxml;
}