package perceptron;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static perceptron.Main.*;

public class TestingPanelController {
    public VBox inputs_VBox;
    public VBox outputs_VBox;
    private int inputNum;
    private int outputNum;

    public TestingPanelController () {
        inputNum = layersArrayList.get(0).getCells_ArrayList().size();
        outputNum = layersArrayList.get(layersArrayList.size() - 1).getCells_ArrayList().size();
    }
    public void initialize() {
        for (int i = 0; i < inputNum; i++) {
            inputs_VBox.getChildren().add(new TextField());
        }
    }

    public void goTesting(ActionEvent actionEvent) {
        outputs_VBox.getChildren().clear();
        double [] inputs = new double [inputNum];
        for(int i=0; i<inputs_VBox.getChildren().size(); i++) {
            TextField tf = (TextField) inputs_VBox.getChildren().get(i);
            inputs[i] = Double.parseDouble(tf.getText());
        }
        double [] outputs = perceptron.test(inputs).toArray();

        for (int i = 0; i < outputNum; i++) {
            TextField tf = new TextField();
            tf.setText(outputs[i]+"");
            tf.setEditable(false);
            outputs_VBox.getChildren().add(tf);
        }
    }

    public void goTraining(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TrainingPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 800, 550));
        stage.show();
    }

    public void goHome(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("HomePanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void savePerceptron(ActionEvent actionEvent) throws Exception {
        wxf = new WriteXMLFile();
        wxf.writeXML(perceptron);

    }
}
