package perceptron;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import static perceptron.Main.layersArrayList;

public class ShowWeightsViewController {

    public HBox viewHBox;

    public ShowWeightsViewController() {
        viewHBox = new HBox();

    }

    public void initialize(){
        for(int i = 1; i<layersArrayList.size(); i++){
            Label layerLabel = new Label("Layer " + (i+1) + " From Layer "+ i);
            layerLabel.setPrefSize(Control.USE_COMPUTED_SIZE,Control.USE_COMPUTED_SIZE);
            VBox currentVBox = new VBox();
            currentVBox.getChildren().add(layerLabel);
            String str = "";
            Layer currentLayer = layersArrayList.get(i);
            for(int j=0; j<currentLayer.getCells_ArrayList().size(); j++) {
                VBox cellVBox = new VBox();
                Label cellLabel = new Label("Cell " + (j+1));
                Cell currentCell = currentLayer.getCells_ArrayList().get(j);
                cellVBox.getChildren().add(cellLabel);
                double [] weights = currentCell.getWeights().toArray();
                for (double weight : weights) {
                    TextField tf = new TextField();
                    str = weight + "";
                    tf.setEditable(false);
                    tf.setText(str);
                    cellVBox.getChildren().add(tf);
                }

                currentVBox.getChildren().add(cellVBox);
            }
            viewHBox.getChildren().add(currentVBox);
        }

    }
}
