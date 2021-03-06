module com.bafcloud.cloud {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;


    requires io.netty.common;
    requires io.netty.transport;
    requires java.logging;
    requires io.netty.codec;
    requires lombok;
    requires org.slf4j;
    requires io.netty.buffer;
    requires java.sql;
    requires sqlite.jdbc;


    opens com.bafcloud.cloud to javafx.fxml;
    exports com.bafcloud.cloud.Client;
    opens com.bafcloud.cloud.Client to javafx.fxml;

}