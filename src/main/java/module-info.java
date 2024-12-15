module org.floulou.flouloushop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.commons.io;

    opens org.floulou.flouloushop to javafx.fxml;
    exports org.floulou.flouloushop;
    exports org.floulou.flouloushop.model;
    opens org.floulou.flouloushop.model to javafx.fxml;
}