module bank.fx.bank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.security.sasl;
    requires java.security.jgss;

    opens bank.fx.bank to javafx.fxml;

    exports bank.fx.bank;
    exports bank.fx.bank.Controller;

    opens bank.fx.bank.Controller to javafx.fxml;
}