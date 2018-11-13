package perceptron;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

import static perceptron.Main.layersArrayList;

public class FunctionSetupController {

    @FXML
    public ToggleGroup function_Cell_NumTG;
    public TextField threshold_TF;

    @FXML
    private ToggleGroup function_TG;

    @FXML
    public RadioButton single_Cell_RB;

    
    @FXML
    public RadioButton all_Cells_RB;

    @FXML
    private RadioButton unit_RB;

    @FXML
    private RadioButton blf_RB;

    @FXML
    private RadioButton gaussian_RB;

    @FXML
    private RadioButton sigmoid_RB;

    @FXML
    private VBox settings_VBox;

    private String function;

    private ArrayList<TextField> parameters_TextFields_ArrayList;

    public void initialize(){
        parameters_TextFields_ArrayList = new ArrayList<>();
        setUnity();


        unit_RB.setUserData(0);
        blf_RB.setUserData(1);
        gaussian_RB.setUserData(2);
        sigmoid_RB.setUserData(3);
        single_Cell_RB.setUserData(0);
        all_Cells_RB.setUserData(1);
    }

    private void setUnity(){
        settings_VBox.getChildren().clear();
        parameters_TextFields_ArrayList.clear();

        HBox aHBox = new HBox();
        aHBox.setAlignment(Pos.CENTER_RIGHT);
        aHBox.getChildren().add(new Label("a "));
        TextField aTextField = new TextField();
        aHBox.getChildren().add(aTextField);

        settings_VBox.getChildren().add(aHBox);
        parameters_TextFields_ArrayList.add(aTextField);

        function = "Unity";
    }

    private void setBlf(){
        settings_VBox.getChildren().clear();
        parameters_TextFields_ArrayList.clear();

        HBox vMinHBox = new HBox();
        vMinHBox.setAlignment(Pos.CENTER_RIGHT);
        vMinHBox.getChildren().add(new Label("Vmin "));
        TextField vMinTextField = new TextField();
        vMinHBox.getChildren().add(vMinTextField);
        settings_VBox.getChildren().add(vMinHBox);

        HBox vMaxHBox = new HBox();
        vMaxHBox.setAlignment(Pos.CENTER_RIGHT);
        vMaxHBox.getChildren().add(new Label("Vmax "));
        TextField vMaxTextField = new TextField();
        vMaxHBox.getChildren().add(vMaxTextField);
        settings_VBox.getChildren().add(vMaxHBox);

        HBox aHBox = new HBox();
        aHBox.setAlignment(Pos.CENTER_RIGHT);
        aHBox.getChildren().add(new Label("a "));
        TextField aTextField = new TextField();
        aHBox.getChildren().add(aTextField);
        settings_VBox.getChildren().add(aHBox);

        HBox bHBox = new HBox();
        bHBox.setAlignment(Pos.CENTER_RIGHT);
        bHBox.getChildren().add(new Label("b "));
        TextField bTextField = new TextField();
        bHBox.getChildren().add(bTextField);
        settings_VBox.getChildren().add(bHBox);

        parameters_TextFields_ArrayList.add(vMinTextField);
        parameters_TextFields_ArrayList.add(vMaxTextField);
        parameters_TextFields_ArrayList.add(aTextField);
        parameters_TextFields_ArrayList.add(bTextField);

        function = "BLF";
    }

    private void setGaussian(){
        settings_VBox.getChildren().clear();
        parameters_TextFields_ArrayList.clear();

        HBox aHBox = new HBox();
        aHBox.setAlignment(Pos.CENTER_RIGHT);
        aHBox.getChildren().add(new Label("a "));
        TextField aTextField = new TextField();
        aHBox.getChildren().add(aTextField);
        settings_VBox.getChildren().add(aHBox);

        HBox v0HBox = new HBox();
        v0HBox.setAlignment(Pos.CENTER_RIGHT);
        v0HBox.getChildren().add(new Label("v0 "));
        TextField v0TextField = new TextField();
        v0HBox.getChildren().add(v0TextField);
        settings_VBox.getChildren().add(v0HBox);

        HBox sigmaHBox = new HBox();
        sigmaHBox.setAlignment(Pos.CENTER_RIGHT);
        sigmaHBox.getChildren().add(new Label("sigma "));
        TextField sigmaTextField = new TextField();
        sigmaHBox.getChildren().add(sigmaTextField);
        settings_VBox.getChildren().add(sigmaHBox);

        parameters_TextFields_ArrayList.add(aTextField);
        parameters_TextFields_ArrayList.add(v0TextField);
        parameters_TextFields_ArrayList.add(sigmaTextField);

        function = "Gaussian";
    }

    private void setSigmoid(){
        settings_VBox.getChildren().clear();
        parameters_TextFields_ArrayList.clear();

        HBox aHBox = new HBox();
        aHBox.setAlignment(Pos.CENTER_RIGHT);
        aHBox.getChildren().add(new Label("a "));
        TextField aTextField = new TextField();
        aHBox.getChildren().add(aTextField);
        settings_VBox.getChildren().add(aHBox);

        HBox kHBox = new HBox();
        kHBox.setAlignment(Pos.CENTER_RIGHT);
        kHBox.getChildren().add(new Label("k "));
        TextField kTextField = new TextField();
        kHBox.getChildren().add(kTextField);
        settings_VBox.getChildren().add(kHBox);

        parameters_TextFields_ArrayList.add(aTextField);
        parameters_TextFields_ArrayList.add(kTextField);

        function = "Sigmoid";
    }

    public void set_SettingsVBox(ActionEvent actionEvent) {
        int i = (int) function_TG.getSelectedToggle().getUserData();
        switch (i) {
            case 0:{
                setUnity();
                break;
            }
            case 1:{
                setBlf();
                break;
            }
            case 2:{
                setGaussian();
                break;
            }
            case 3:{
                setSigmoid();
                break;
            }
        }
    }

    public void set_Cell_Function(ActionEvent actionEvent) {
        int i = (int) function_Cell_NumTG.getSelectedToggle().getUserData();
        switch (i){
            case 0: {
                Cell c = PerceptronSetupController.current_Layer.getCurrentCell();
                c.setFunction(function);
                c.getParameters().clear();
                for (TextField aParameters_TextFields_ArrayList : parameters_TextFields_ArrayList) {
                    double d = Double.parseDouble(aParameters_TextFields_ArrayList.getText());
                    c.getParameters().add(d);
                }
                c.setThreshold(Double.parseDouble(threshold_TF.getText()));
                break;
            }
            case 1: {
                for (Layer l : layersArrayList){
                    for (Cell c : l.getCells_ArrayList()){
                        c.setFunction(function);
                        c.getParameters().clear();
                        for (TextField aParameters_TextFields_ArrayList : parameters_TextFields_ArrayList) {
                            double d = Double.parseDouble(aParameters_TextFields_ArrayList.getText());
                            c.getParameters().add(d);
                        }
                        c.setThreshold(Double.parseDouble(threshold_TF.getText()));
                    }
                }
                break;
            }
        }
        Stage s = (Stage) sigmoid_RB.getScene().getWindow();
        s.close();
    }
}
