package bank.fx.bank.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DashbaordDepositController {


        @FXML
        private DatePicker DateBtn;

        @FXML
        private Button DepositBtn;

        @FXML
        private TextField accountBtn;

        @FXML
        private TextField accountNumberBtn;

        @FXML
        private TextField depositIdBtn;

        @FXML
        private TextField userIdBtn;

    @FXML
    private Label errorBtn;
    @FXML
    private Label errorBtn1;


    public void depositBtn(ActionEvent event) {

        if(userIdBtn.getText().isBlank() == false){
            errorBtn.setText("Please enter user ID");
        }

        if(accountNumberBtn.getText().isBlank()== false && depositIdBtn.getText().isBlank()== false
                && accountBtn.getText().isBlank()== false){

            errorBtn1.setText("You try to login !");

        }
        else{
            errorBtn1.setText("Please enter account number deposit ID and amount");
        }
    }
}

