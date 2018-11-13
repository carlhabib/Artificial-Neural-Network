package perceptron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

import static perceptron.Main.*;

public class HomePanelController {
    @FXML
    private Button validationModeBtn;

    @FXML
    private Button learningModeBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Button trainWithValuesButton;

    @FXML
    private Button loadPerceptronButton;


    @FXML
    private ComboBox<String> perceptronsList;

    private ObservableList<String> layersList;

    @FXML
    public void initialize() throws Exception{
            layersList = FXCollections.observableArrayList();
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(filepath);
            NodeList elementsList = doc.getElementsByTagName("Perceptron");
            for(int i=0; i<elementsList.getLength(); i++) {
                layersList.add("Perceptron "+elementsList.item(i).getAttributes().getNamedItem("index").getNodeValue());

            }
            perceptronsList.setItems(layersList);


    }

    @FXML
    void loadPerceptron(ActionEvent event) {
        int index = perceptronsList.getSelectionModel().getSelectedIndex();
        System.out.print(index);
        rdx = new ReadXMLFile();
        perceptron = rdx.getPerceptron(index);
        layersArrayList = perceptron.layerArrayList;
        errorFunction = perceptron.errorFunction;
        errorThreshold = perceptron.errorThreshold;
    }

    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    void openLearningMode(ActionEvent event) throws IOException {
        validationModeBtn.setVisible(true);
        trainWithValuesButton.setVisible(true);
        Parent root = FXMLLoader.load(getClass().getResource("PerceptronSetup.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 810, 571));
        stage.show();
    }

    @FXML
    void openValidationMode(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TestingPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 550));
        stage.show();
    }

    @FXML
    void goToIOPanel(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("TrainingPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 550));
        stage.show();
    }
}
