package perceptron;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

import static perceptron.Main.stage;

public class LoadPerceptronController {
    @FXML
    private TextArea outputTextArea;

    @FXML
    private Button backIOBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private TextField accTextField;

    @FXML
    public void initialize() throws IOException {
        //perceptron.display();
        //outputTextArea.setText(perceptron.sendString());
    }

    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    void goHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    void gotoIO(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("IOPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
