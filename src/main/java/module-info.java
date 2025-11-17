module web.halma {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;
    requires mysql.connector.j;
    requires java.desktop;

    opens web.halma to javafx.fxml;
    opens web.halma.controllers to javafx.fxml;
    exports web.halma;
    exports web.halma.controllers;
}