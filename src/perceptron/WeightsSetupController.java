package perceptron;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static perceptron.Main.layersArrayList;

public class WeightsSetupController {
    public Button saveWeightsBtn;
    public HBox layerHBox;

    public WeightsSetupController () {
        layerHBox = new HBox();

    }

    public void initialize(){


        for(int i = 1; i<layersArrayList.size(); i++){
            Label layerLabel = new Label("Layer " + (i+1) + " From Layer "+ i);
            layerLabel.setMinSize(Control.USE_COMPUTED_SIZE,Control.USE_COMPUTED_SIZE);
            VBox currentVBox = new VBox();
            currentVBox.setMinSize(Control.USE_COMPUTED_SIZE,Control.USE_COMPUTED_SIZE);
            currentVBox.getChildren().add(layerLabel);
            Layer currentLayer = layersArrayList.get(i);
            for(int j=0; j<currentLayer.getCells_ArrayList().size(); j++) {
                VBox cellVBox = new VBox();
                Label cellLabel = new Label("Cell " + (j+1));
                cellVBox.getChildren().add(cellLabel);

                for (int k=0; k<layersArrayList.get(i-1).getCells_ArrayList().size(); k++){
                    TextField tf = new TextField();
                    cellVBox.getChildren().add(tf);
                }

                currentVBox.getChildren().add(cellVBox);
            }
            layerHBox.getChildren().add(currentVBox);
        }
    }

    public void saveWeights(ActionEvent actionEvent) {

        for (int i=0; i<layerHBox.getChildren().size(); i++){
            VBox layerVBox = (VBox) layerHBox.getChildren().get(i);
            Layer l = layersArrayList.get(i+1);
            for(int j=1; j<layerVBox.getChildren().size(); j++){
                VBox cellVBox = (VBox) layerVBox.getChildren().get(j);
                Cell c = l.getCells_ArrayList().get(j-1);
                double [] cellWeights = new double[cellVBox.getChildren().size()-1];
                for(int k=1; k<cellVBox.getChildren().size(); k++){
                    TextField tf = (TextField) cellVBox.getChildren().get(k);
                    cellWeights[k-1] = Double.parseDouble(tf.getText());
                }
                Matrix m = new Matrix(cellWeights);
                c.setWeights(m);
            }
        }

        Stage s = (Stage) saveWeightsBtn.getScene().getWindow();
        s.close();
    }
}
