package bank.fx.bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Name Bank");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("images/dog.jpg")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        ResultSet rs = Database.get("select * from users");
        while (rs.next())
            System.out.println(rs.getString(2));
        launch();
    }
}