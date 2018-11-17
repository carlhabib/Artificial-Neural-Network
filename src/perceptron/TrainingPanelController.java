package perceptron;

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
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static perceptron.Main.*;

public class TrainingPanelController {
    public VBox outputs_VBox;
    public VBox inputs_VBox;
    public ComboBox scenarioComboBox;
    public TextField path_Field;
    public ToggleGroup trainingTG;
    public RadioButton kfoldsRB;
    public RadioButton svRB;
    public Label validationLabel;
    public TextField validationTF;
    public TextField error_TF;
    private int inputNum;
    private int outputNum;
    private int maxIterations = 1000;
    private int val=0;

    @FXML
    private AnchorPane anchorPane = new AnchorPane();


    public TrainingPanelController () {
        inputNum = layersArrayList.get(0).getCells_ArrayList().size();
        outputNum = layersArrayList.get(layersArrayList.size()-1).getCells_ArrayList().size();
        scenarioComboBox = new ComboBox();
        svRB = new RadioButton();
        kfoldsRB = new RadioButton();
        
    }

    public void initialize() {

        kfoldsRB.setUserData(0);
        svRB.setUserData(1);
    	
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
        perceptron.setMaxIter(maxIterations);
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
   

    public void goTesting(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TestingPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
    }

    public void trainWithData(ActionEvent actionEvent) throws IOException {
        String s = (String) scenarioComboBox.getValue();
        ArrayList<double[]> inputsArrayList = new ArrayList<>();
        ArrayList<double[]> outputsArrayList = new ArrayList<>();
        int n = Integer.parseInt(validationTF.getText());

        double err = 0;
        double count = 0;
        double numberOfTests = 0;

        if (s.equalsIgnoreCase("Even Odd Classifier")) {
            if ((int) trainingTG.getSelectedToggle().getUserData() == 0) {
                ArrayList<double[]> inputsType1 = new ArrayList<>();
                ArrayList<double[]> inputsType2 = new ArrayList<>();
                double[] outputType1 = {0};
                double[] outputType2 = {1};

                for (int i = 0; i < n; i++) {// this loop creates the number of points to train desired by the user, the points are equally divided between the four quadrants
                    double[] input = new double[1];

                    Random rand = new Random();// the points are created between the boundaries -10 and 10 randomly
                    if (i % 2 == 0) {
                        input[0] = 2 * rand.nextInt(5);
                        inputsType1.add(input);
                    } else {
                        input[0] = 2 * rand.nextInt(5) + 1;
                        inputsType2.add(input);
                    }
                }

                for (int i = 0; i < (n * 0.7); i++) { // the training will be done on 70% of the created points

                    if (i % 2 == 0) { // the perceptron is trained for one point from each quadrant successively
                        perceptron.trainPerceptron(inputsType1.get(i / 4), outputType1);
                    }
                    if (i % 2 == 1) {
                        perceptron.trainPerceptron(inputsType2.get((i / 4) + 1), outputType2);
                    }

                }
            } else {
                ArrayList<double[]> inputs = new ArrayList<>();
                ArrayList<double[]> outputs = new ArrayList<>();
                int numberOfPoints = 100;
                for (int i = 0; i < numberOfPoints; i++) { // first we will generate the points
                    double[] input = new double[1];
                    double[] output = new double[1];

                    Random rand = new Random();
                    int x = rand.nextInt(21);
                    input[0] = x;

                    if (x % 2 == 0) {
                        output[0] = 0;
                    } else {
                        output[0] = 1;
                    }
                    inputs.add(input);
                    outputs.add(output);
                }
                for (int k = 0; k < n; k++) {// we will divide the number of points to k folds
                    //for each iteration one fold will serve as a training and the others will serve as test

                    int startPosition = k * (numberOfPoints / n);//we will determine the fold starting and ending points in order to train for these points
                    int endPosition = (k + 1) * (numberOfPoints / n);
                    for (int i = startPosition; i < endPosition; i++) {
                        perceptron.trainPerceptron(inputs.get(i), outputs.get(i));
                    }
                    for (int i = 0; i < startPosition; i++) {// the points outside the fold will be used to test
                        double[] temp = perceptron.test(inputs.get(i)).getData();
                        numberOfTests++;
                        double error = ErrorMachine.computeError("MAE", temp, outputs.get(i));
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;
                    }

                    for (int i = startPosition; i < numberOfPoints; i++) {// the points outside the fold will be used to test
                        double[] temp = perceptron.test(inputs.get(i)).getData();
                        numberOfTests++;
                        double error = ErrorMachine.computeError("MAE", temp, outputs.get(i));
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;
                    }


                }

            }
        } else {
            n = 100;
            int k = Integer.parseInt(validationTF.getText());
            ArrayList<double[]> inputsType1 = new ArrayList<>();
            ArrayList<double[]> inputsType2 = new ArrayList<>();
            ArrayList<double[]> inputsType3 = new ArrayList<>();
            ArrayList<double[]> inputsType4 = new ArrayList<>();
            double[] outputType1 = {0, 0};
            double[] outputType2 = {1, 0};
            double[] outputType3 = {1, 1};
            double[] outputType4 = {0, 1};

            for (int i = 0; i < n; i++) {// this loop creates the number of points to train desired by the user, the points are equally divided between the four quadrants
                double[] input = new double[2];

                Random rand = new Random();// the points are created between the boundaries -10 and 10 randomly
                if (i < n / 4) {
                    input[0] = rand.nextInt(11);
                    input[1] = rand.nextInt(11);


                    inputsType1.add(input);
                }
                if (i >= n / 4 & i < n / 2) {

                    input[0] = rand.nextInt(11);
                    input[1] = rand.nextInt(11) - 10;


                    inputsType2.add(input);
                }
                if (i >= n / 2 & i < (3 * n / 4)) {

                    input[0] = rand.nextInt(11) - 10;
                    input[1] = rand.nextInt(11) - 10;


                    inputsType3.add(input);
                }
                if (i >= (3 * n / 4) & i < n) {

                    input[0] = rand.nextInt(11);
                    input[1] = rand.nextInt(11) - 10;


                    inputsType4.add(input);
                }
            }
            if ((int) trainingTG.getSelectedToggle().getUserData() == 0) {

                for (int i = 0; i < (n * k / 100); i++) { // the training will be done on 70% of the created points

                    if (i % 4 == 0) { // the perceptron is trained for one point from each quadrant successively
                        perceptron.trainPerceptron(inputsType1.get(i / 4), outputType1);
                    }
                    if (i % 4 == 1) {
                        perceptron.trainPerceptron(inputsType2.get((i / 4) + 1), outputType2);
                    }
                    if (i % 4 == 2) {
                        perceptron.trainPerceptron(inputsType3.get((i / 4) + 2), outputType3);
                    }
                    if (i % 4 == 3) {
                        perceptron.trainPerceptron(inputsType4.get((i / 4) + 3), outputType4);
                    }

                }

                for (int i = 0; i <= (n * (100 - k) / 100); i++) {//this is the testing phase which is done on 30% of the points created
                    Matrix temp;
                    if (i % 4 == 0) {
                        temp = perceptron.test(inputsType1.get(i / 4));
                        double error = ErrorMachine.computeError("MAE", temp.getData(), outputType1);
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;
                    }
                    if (i % 4 == 1) {
                        temp = perceptron.test(inputsType2.get((i / 4) + 1));
                        double error = ErrorMachine.computeError("MAE", temp.getData(), outputType2);
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;
                    }
                    if (i % 4 == 2) {
                        temp = perceptron.test(inputsType3.get((i / 4) + 2));
                        double error = ErrorMachine.computeError("MAE", temp.getData(), outputType3);
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;
                    }
                    if (i % 4 == 3) {
                        temp = perceptron.test(inputsType4.get((i / 4) + 3));
                        double error = ErrorMachine.computeError("MAE", temp.getData(), outputType4);
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;

                    }
                }
                err = count / (n * k);// the accuracy will give an indication of how many points were successively tested and their output matched with the desired output

                error_TF.setText("" + err);
            } else {
                ArrayList<double[]> inputs = new ArrayList<>();
                ArrayList<double[]> outputs = new ArrayList<>();
                int numberOfPoints = 100;
                for (int i = 0; i < numberOfPoints; i++) { // first we will generate the points
                    double[] input = new double[2];
                    double[] output = new double[2];

                    Random rand = new Random();
                    int x = rand.nextInt(21) - 10;
                    int y = rand.nextInt(21) - 10;
                    input[0] = x;
                    input[1] = y;

                    if (x > 0 & y > 0) {
                        output[0] = 1;
                        output[1] = 1;
                    }
                    if (x >= 0 & y < 0) {
                        output[0] = 1;
                        output[1] = 0;
                    }
                    if (x < 0 & y >= 0) {
                        output[0] = 0;
                        output[1] = 1;
                    }
                    if (x <= 0 & y <= 0) {
                        output[0] = 0;
                        output[1] = 0;
                    }
                    inputs.add(input);
                    outputs.add(output);
                }
                for (int j = 0; j < k; j++) {// we will divide the number of points to k folds
                    //for each iteration one fold will serve as a training and the others will serve as test

                    int startPosition = j * (numberOfPoints / k);//we will determine the fold starting and ending points in order to train for these points
                    int endPosition = (j + 1) * (numberOfPoints / k);
                    for (int i = startPosition; i < endPosition; i++) {
                        perceptron.trainPerceptron(inputs.get(i), outputs.get(i));
                    }
                    for (int i = 0; i < startPosition; i++) {// the points outside the fold will be used to test
                        double[] temp = perceptron.test(inputs.get(i)).getData();
                        numberOfTests++;
                        double error = ErrorMachine.computeError("MAE", temp, outputs.get(i));
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;
                    }

                    for (int i = startPosition; i < numberOfPoints; i++) {// the points outside the fold will be used to test
                        double[] temp = perceptron.test(inputs.get(i)).getData();
                        numberOfTests++;
                        double error = ErrorMachine.computeError("MAE", temp, outputs.get(i));
                        if (ErrorMachine.errorAcceptance(error, 0))
                            count++;
                    }


                }

            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowWeightsView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();

            err = count / numberOfTests;
            error_TF.setText("" + err);

        }
    }


    public void getPathData(ActionEvent actionEvent) throws Exception {
        File f = new File(path_Field.getText());

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(f);
        NodeList allPairsList = doc.getElementsByTagName("Pair");

        double error = 0;
        int k = Integer.parseInt(validationTF.getText());
        int count=0;
        if((int)trainingTG.getSelectedToggle().getUserData()==0) {
            for(int i=0; i<allPairsList.getLength()*k/100; i++){
                count++;
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

                perceptron.trainPerceptron(inputsArr, outputsArr);
            }
            int numberOfTests = 0;
            for (int i = count; i<allPairsList.getLength(); i++){
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

                Matrix temp = perceptron.test(inputsArr);
                double [] tempor = temp.getData();
                numberOfTests++;
                double err = ErrorMachine.computeError("MAE", tempor, outputsArr);
                if (ErrorMachine.errorAcceptance(err, 0))
                    count++;
            }
            error = count/numberOfTests;
            error_TF.setText(""+error);
        }else {
            double numberOfPoints = allPairsList.getLength();
            int currentMin = 0;
            int numberOfTests = 0;
            while (currentMin + k < numberOfPoints) {
                for (int i = 0; i < numberOfPoints; i++) {
                    // we will divide the number of points to k folds
                    //for each iteration one fold will serve as a training and the others will serve as test

                    Element currentPair = (Element) allPairsList.item(i);

                    String str = currentPair.getElementsByTagName("Inputs").item(0).getTextContent();
                    ArrayList<Double> inputs = new ArrayList<>();
                    Scanner scan = new Scanner(str);
                    while (scan.hasNext()) {
                        inputs.add(Double.parseDouble(scan.next()));
                    }

                    str = currentPair.getElementsByTagName("Outputs").item(0).getTextContent();
                    ArrayList<Double> outputs = new ArrayList<>();
                    scan = new Scanner(str);
                    while (scan.hasNext()) {
                        outputs.add(Double.parseDouble(scan.next()));
                    }

                    double[] inputsArr = new double[inputs.size()];
                    double[] outputsArr = new double[outputs.size()];

                    for (int j = 0; j < inputs.size(); j++) {
                        inputsArr[j] = inputs.get(j);
                    }

                    for (int j = 0; j < outputs.size(); j++) {
                        outputsArr[j] = outputs.get(j);
                    }


                    if (i < currentMin || i > (currentMin + k)) {
                        perceptron.trainPerceptron(inputsArr, outputsArr);
                    }
                }
                for (int i = count; i < count + k; i++) {
                    Element currentPair = (Element) allPairsList.item(i);

                    String str = currentPair.getElementsByTagName("Inputs").item(0).getTextContent();
                    ArrayList<Double> inputs = new ArrayList<>();
                    Scanner scan = new Scanner(str);
                    while (scan.hasNext()) {
                        inputs.add(Double.parseDouble(scan.next()));
                    }

                    str = currentPair.getElementsByTagName("Outputs").item(0).getTextContent();
                    ArrayList<Double> outputs = new ArrayList<>();
                    scan = new Scanner(str);
                    while (scan.hasNext()) {
                        outputs.add(Double.parseDouble(scan.next()));
                    }

                    double[] inputsArr = new double[inputs.size()];
                    double[] outputsArr = new double[outputs.size()];

                    for (int j = 0; j < inputs.size(); j++) {
                        inputsArr[j] = inputs.get(j);
                    }

                    for (int j = 0; j < outputs.size(); j++) {
                        outputsArr[j] = outputs.get(j);
                    }
                    for (int j = currentMin; i < currentMin + k; i++) {// the points outside the fold will be used to test
                        double[] temp = perceptron.test(inputsArr).getData();
                        numberOfTests++;
                        double err = ErrorMachine.computeError("MAE", temp, outputsArr);
                        if (ErrorMachine.errorAcceptance(err, 0))
                            count++;
                    }
                }
                currentMin+=k;
            }
            error = count/numberOfTests;
            error_TF.setText(""+error);
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

    public void setValidationParam(ActionEvent actionEvent) {
        if((int)trainingTG.getSelectedToggle().getUserData()==0) {
            validationLabel.setText("Training %: ");
        }else{
            validationLabel.setText("k: ");
        }

    }


}
