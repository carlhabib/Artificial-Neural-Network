package perceptron;

import static perceptron.Main.layersArrayList;
import static perceptron.Main.perceptron;
import static perceptron.Main.stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TrainingPanelController {
    
    public Label iterationsLabel;
    public Slider iterationsSlider;
    public VBox outputs_VBox;
    public VBox inputs_VBox;
    public ComboBox scenarioComboBox;
    public TextField path_Field;
    private int inputNum;
    private int outputNum;
    private int maxIterations;

    @FXML
    private AnchorPane anchorPane = new AnchorPane();


    public TrainingPanelController () {
        inputNum = layersArrayList.get(0).getCells_ArrayList().size();
        outputNum = layersArrayList.get(layersArrayList.size()-1).getCells_ArrayList().size();
        scenarioComboBox = new ComboBox();
        iterationsLabel = new Label();
        iterationsSlider = new Slider();
        
    }

    public void initialize() {
    	iterationsLabel.setText(iterationsSlider.getValue()+"");
        maxIterations = (int) iterationsSlider.getValue();
        perceptron.setMaxIter((int) iterationsSlider.getValue());
    	iterationsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            maxIterations = (int) newValue.intValue();
            iterationsLabel.setText(maxIterations+"");
        });
    	
        for(int i=0; i<inputNum; i++) {
            inputs_VBox.getChildren().add(new TextField());
        }
        for(int i=0; i<outputNum; i++) {
            outputs_VBox.getChildren().add(new TextField());
        }
        ObservableList<String> scenarios = FXCollections.observableArrayList("Quadrant Classifier", "Even Odd Classifier");

        scenarioComboBox.setItems(scenarios);
    }

    public void train_Perceptron(ActionEvent actionEvent) throws IOException {
        double [] inputs = new double [inputNum];
        double [] outputs = new double[outputNum];
        perceptron.setMaxIter((int) iterationsSlider.getValue());
        for(int i=0; i<inputs_VBox.getChildren().size(); i++) {
            TextField tf = (TextField) inputs_VBox.getChildren().get(i);
            inputs[i] = Double.parseDouble(tf.getText());
        }

        for(int i=0; i<outputs_VBox.getChildren().size(); i++) {
            TextField tf = (TextField) outputs_VBox.getChildren().get(i);
            outputs[i] = Double.parseDouble(tf.getText());
        }
        perceptron.trainPerceptron(inputs,outputs);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowWeightsView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1)); stage.show();
    }

    public void updateLabel(MouseEvent mouseEvent) {
        iterationsLabel.setText(iterationsSlider.getValue()+"");
        
    }
   

    public void goTesting(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TestingPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }

    public void trainWithData(ActionEvent actionEvent) throws IOException {
        String s = (String) scenarioComboBox.getValue();
        if(s.equalsIgnoreCase("Even Odd Classifier")){
            double[] inputsTest = new double[layersArrayList.get(0).getCells_ArrayList().size()];
            double[] expectedOutput = new double[1];
            Random rnd = new Random();
            for(int i = 0; i<100; i++){
                for(int j=0; j<inputsTest.length; j++){
                    inputsTest[j]=rnd.nextInt(9);
                }
                double d = 0;
                for (double anInputsTest : inputsTest) {
                    d += anInputsTest;
                }
                int val = (int) d;
                if(val%2==0) {
                    expectedOutput[0]=0;
                }else{
                    expectedOutput[0]=1;
                }
                perceptron.trainPerceptron(inputsTest, expectedOutput);
            }
        }else {
            double[] inputsTest = new double[2];
            double[] expectedOutput = new double[2];
            Random rnd = new Random();
            for(int i = 0; i<100; i++) {
                for (int j = 0; j < inputsTest.length; j++) {
                    inputsTest[j] = rnd.nextDouble()*10 - 5;
                }
                if(inputsTest[0]>=0 && inputsTest[1]>=0)
                {
                    expectedOutput[0]=0;
                    expectedOutput[1]=0;
                }
                if(inputsTest[0]<0 && inputsTest[1]>=0)
                {
                    expectedOutput[0]=1;
                    expectedOutput[1]=0;
                }
                if(inputsTest[0]>=0 && inputsTest[1]<0)
                {
                    expectedOutput[0]=0;
                    expectedOutput[1]=1;
                }
                if(inputsTest[0]<0 && inputsTest[1]<0)
                {
                    expectedOutput[0]=1;
                    expectedOutput[1]=1;
                }
                System.out.println("Input:"+inputsTest[0]+" "+inputsTest[1]);
                System.out.println("Output:"+expectedOutput[0]+" "+expectedOutput[1]);
                perceptron.trainPerceptron(inputsTest,expectedOutput);
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowWeightsView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1)); stage.show();


    }

    public void getPathData(ActionEvent actionEvent) throws Exception {
        File f = new File(path_Field.getText());

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(f);
        NodeList allPairsList = doc.getElementsByTagName("Pair");

        for(int i=0; i<allPairsList.getLength(); i++){
            Element currentPair = (Element) allPairsList.item(i);

            String str = currentPair.getElementsByTagName("Inputs").item(0).getTextContent();
            ArrayList<Double> inputs = new ArrayList<>();
            Scanner scan = new Scanner(str);
            while (scan.hasNext()){
                inputs.add(Double.parseDouble(scan.next()));
            }

            str = currentPair.getElementsByTagName("Outputs").item(0).getTextContent();
            ArrayList<Double> outputs = new ArrayList<>();
            scan = new Scanner(str);
            while (scan.hasNext()){
                outputs.add(Double.parseDouble(scan.next()));
            }

            double[] inputsArr = new double[inputs.size()];
            double[] outputsArr = new double[outputs.size()];

            for(int j=0; j<inputs.size(); j++) {
                inputsArr[j] = inputs.get(j);
            }

            for(int j=0; j<outputs.size(); j++) {
                outputsArr[j] = outputs.get(j);
            }
            perceptron.trainPerceptron(inputsArr,outputsArr);
        }
    }

    public void setPath(ActionEvent actionEvent) throws Exception{
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Training Set");
        Stage stage = new Stage();

        File f = fc.showOpenDialog(stage);

        if(f != null) {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(f);
        }
        path_Field.setText(f.getAbsolutePath());
    }
}
