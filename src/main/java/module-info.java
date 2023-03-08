module bank.fx.bank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens bank.fx.bank to javafx.fxml;

    exports bank.fx.bank;
}