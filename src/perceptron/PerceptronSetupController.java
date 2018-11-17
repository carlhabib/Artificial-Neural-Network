package perceptron;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static perceptron.Main.*;

public class PerceptronSetupController {

    public Button prev_Layer_Btn;
    public Button next_Layer_Btn;
    public Button rmv_Layer_Btn;
    public Button clear_Perceptron_Btn;
    public RadioButton one_Layer_RB;
    public RadioButton multi_Layer_RB;
    public Button create_Perceptron_Btn;
    public Button prev_Cell_Btn;
    public Button next_Cell_Btn;
    public Button add_Layer_Btn;
    public RadioButton mde_RB;
    public RadioButton mse_RB;
    public RadioButton mae_RB;
    private Layer output_Layer;
    private Layer input_Layer;
    static Layer current_Layer;
    private int inputs;
    private int outputs;
    private int fcn;
    private int lyr;




    @FXML
    private TextField num_Of_Inputs_TextField;

    @FXML
    private TextField num_Of_Outputs_TextField;

    @FXML
    private TextField Error_Threshold_TextField;

    @FXML
    private ToggleGroup error_Function_TG;

    @FXML
    private ToggleGroup layered_TG;

    @FXML
    private HBox perceptron_HBox;


    public PerceptronSetupController(){
        layersArrayList = new ArrayList<>();
        perceptron_HBox = new HBox();
        one_Layer_RB = new RadioButton();
        multi_Layer_RB = new RadioButton();
        inputs = 0;
        next_Cell_Btn = new Button();
        next_Layer_Btn = new Button();
        prev_Cell_Btn = new Button();
        next_Cell_Btn = new Button();
        add_Layer_Btn = new Button();
        mse_RB = new RadioButton();
        mae_RB = new RadioButton();
        mde_RB = new RadioButton();
        errorFunction = "MSE";
        errorThreshold = 0.0;

        add_Layer_Btn.setDisable(true);

        if(next_Cell_Btn!=null) {
            next_Cell_Btn.setDisable(true);
        }
        if(prev_Layer_Btn!=null) {
            prev_Layer_Btn.setDisable(true);
        }
        if(next_Layer_Btn!=null){
            next_Layer_Btn.setDisable(true);
        }
        if(prev_Cell_Btn!=null) {
            prev_Cell_Btn.setDisable(true);
        }
    }

    public void initialize() {
        mse_RB.setUserData(0);
        mae_RB.setUserData(1);
        mde_RB.setUserData(2);
        one_Layer_RB.setUserData(0);
        multi_Layer_RB.setUserData(1);
        add_Layer_Btn.setDisable(true);
    }

    @FXML
    void create_Cell(ActionEvent event) {
        current_Layer.add_Cell_toLast();
        prev_Cell_Btn.setDisable(false);
    }

    @FXML
    void create_Layer(ActionEvent event) {
        if(layersArrayList.size()>1) {
            prev_Layer_Btn.setDisable(false);
            next_Layer_Btn.setDisable(false);
            removeCellFocus();
            int k = layersArrayList.size() - 1;
            Layer new_Layer = new Layer();
            new_Layer.setIndex(k);
            perceptron_HBox.getChildren().add(perceptron_HBox.getChildren().size() - 1, new_Layer.getVBox());
            layersArrayList.add(k, new_Layer);
            current_Layer = new_Layer;
            rmv_Layer_Btn.setDisable(false);
            output_Layer.setIndex(layersArrayList.size() - 1);
            setCellFocus(0);
        }
    }

    @FXML
    void create_Perceptron(ActionEvent event) throws IOException{
        errorThreshold = Double.parseDouble(Error_Threshold_TextField.getText());
        perceptron = new Perceptron(layersArrayList,errorFunction,errorThreshold);

        Parent root = FXMLLoader.load(getClass().getResource("TrainingPanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 800, 550));
        stage.show();
    }

    @FXML
    void randomActivationWeightsPressed(ActionEvent event) {

        for(int i = 1; i<layersArrayList.size(); i++){
            Layer previousLayer = layersArrayList.get(i-1);
            Layer currentLayer = layersArrayList.get(i);
            int prev = previousLayer.getCells_ArrayList().size();
            for(int j = 0; j<currentLayer.getCells_ArrayList().size();j++){
                double[] weights = new double[prev];
                for(int k=0; k<prev; k++){
                    weights[k] = Math.random()*10.0;
                }
                Matrix m = new Matrix(weights);
                currentLayer.getCells_ArrayList().get(j).setWeights(m);
            }
        }
    }

    @FXML
    void setActivationWeightsPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WeightsSetup.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1)); stage.show();
    }

    @FXML
    void update_Inputs(ActionEvent event) {
        inputs = Integer.parseInt(num_Of_Inputs_TextField.getText());
        if(inputs==0) {
            num_Of_Inputs_TextField.clear();
            one_Layer_RB.requestFocus();
            num_Of_Inputs_TextField.setPromptText("Enter a Positive Integer");
        }else {
            if (layersArrayList.size() > 0) {
                removeCellFocus();
                layersArrayList.remove(0);
                perceptron_HBox.getChildren().remove(0);
            }

            input_Layer = new Layer(inputs);
            perceptron_HBox.getChildren().add(0,input_Layer.getVBox());
            layersArrayList.add(input_Layer);
            input_Layer.setIndex(0);
            current_Layer = input_Layer;
        }
        rmv_Layer_Btn.setDisable(false);
    }

    @FXML
    void update_Outputs(ActionEvent event) {
        if(inputs==0) {
            num_Of_Outputs_TextField.clear();
            num_Of_Inputs_TextField.requestFocus();
            num_Of_Outputs_TextField.setPromptText("Enter the number of Inputs First");
        }else {
            removeCellFocus();
            if (layersArrayList.size()>1) {
                perceptron_HBox.getChildren().remove(layersArrayList.size()-1);
                layersArrayList.remove(layersArrayList.size()-1);
            }
                outputs = Integer.parseInt(num_Of_Outputs_TextField.getText());
                output_Layer = new Layer(outputs);
                perceptron_HBox.getChildren().add(perceptron_HBox.getChildren().size(),output_Layer.getVBox());
                layersArrayList.add(output_Layer);
                output_Layer.setIndex(layersArrayList.size()-1);
                current_Layer = output_Layer;
        }
        if (next_Layer_Btn.isDisabled()){
            next_Layer_Btn.setDisable(false);
        }
    }

    public void check_Int(KeyEvent keyEvent) {

    }

    public void goPreviousLayer(ActionEvent actionEvent) {
            removeCellFocus();
            current_Layer = layersArrayList.get(current_Layer.getIndex() - 1);
            next_Cell_Btn.setDisable(false);
            prev_Cell_Btn.setDisable(true);
            setCellFocus(0);
            if(current_Layer.getIndex()==0){
                prev_Layer_Btn.setDisable(true);
            }
        if (next_Layer_Btn.isDisabled()){
            next_Layer_Btn.setDisable(false);
        }
    }

    public void goNextLayer(ActionEvent actionEvent) {

        removeCellFocus();
        current_Layer = layersArrayList.get(current_Layer.getIndex()+1);
        setCellFocus(0);
        if(current_Layer.getIndex()==layersArrayList.size()-1){
            next_Layer_Btn.setDisable(true);
        }

        if (prev_Layer_Btn.isDisabled()){
            prev_Layer_Btn.setDisable(false);
        }
    }

    public void remove_Layer(ActionEvent actionEvent) {
        if((layersArrayList.size()>=2) && (current_Layer.getIndex() != 0) && (current_Layer.getIndex()!= layersArrayList.size()-1)) {
            int i = current_Layer.getIndex();
            perceptron_HBox.getChildren().remove(i);
            layersArrayList.remove(i);
            current_Layer = layersArrayList.get(i);
            current_Layer.setCurrentCell(current_Layer.getCells_ArrayList().get(current_Layer.getCells_ArrayList().size()-1));
            setCellFocus(current_Layer.getCurrentCell().getIndex());
            output_Layer.setIndex(output_Layer.getIndex()-1);
        }
    }



    public void goNextCell(ActionEvent actionEvent) {
        Cell prev = current_Layer.getCurrentCell();
        current_Layer.setCurrentCell(current_Layer.getCells_ArrayList().get(prev.getIndex()+1));
        prev.getCircle().setStroke(Color.BLACK);
        current_Layer.getCurrentCell().getCircle().setStroke(Color.RED);
        if(current_Layer.getCurrentCell().getIndex()==current_Layer.getCells_ArrayList().size()-1){
            next_Cell_Btn.setDisable(true);
        }else {
            next_Cell_Btn.setDisable(false);
        }
        prev_Cell_Btn.setDisable(false);
    }

    public void goPreviousCell(ActionEvent actionEvent) {
        Cell next = current_Layer.getCurrentCell();
        current_Layer.setCurrentCell(current_Layer.getCells_ArrayList().get(next.getIndex()-1));
        next.getCircle().setStroke(Color.BLACK);
        current_Layer.getCurrentCell().getCircle().setStroke(Color.RED);
        if(current_Layer.getCurrentCell().getIndex()==0) {
            prev_Cell_Btn.setDisable(true);
        }else{
            prev_Cell_Btn.setDisable(false);
        }
        next_Cell_Btn.setDisable(false);
    }

    private void removeCellFocus(){
        current_Layer.getCurrentCell().getCircle().setStroke(Color.BLACK);
    }

    private void setCellFocus(int i){
        Cell c = current_Layer.getCells_ArrayList().get(i);
        c.getCircle().setStroke(Color.RED);
        current_Layer.setCurrentCell(c);
    }

    public void clear_Perceptron(ActionEvent actionEvent) {
        perceptron_HBox.getChildren().clear();
        layersArrayList.clear();
        num_Of_Inputs_TextField.setText("");
        num_Of_Outputs_TextField.setText("");
    }

    public void setLayerStatus(ActionEvent actionEvent) {
        clear_Perceptron(actionEvent);
        lyr = (int) layered_TG.getSelectedToggle().getUserData();
        if(lyr==0){
            add_Layer_Btn.setDisable(true);
        }else{
            add_Layer_Btn.setDisable(false);
        }
    }

    public void setErrorFunction(ActionEvent actionEvent) {
        fcn = (int) error_Function_TG.getSelectedToggle().getUserData();
        if(fcn==0) {
            errorFunction = "MSE";
        }
        if(fcn==1) {
            errorFunction = "MAE";
        }
        if(fcn==2) {
            errorFunction = "MDE";
        }
    }

    public void set_Function_Parameters(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FunctionSetup.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1)); stage.show();
    }

    public void go_Back(ActionEvent actionEvent) throws IOException {
        clear_Perceptron(actionEvent);
        Parent root = FXMLLoader.load(getClass().getResource("HomePanel.fxml"));
        stage.setTitle("Perceptron");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void showActivationWeightsPressed(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowWeightsView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1)); stage.show();
    }
}
