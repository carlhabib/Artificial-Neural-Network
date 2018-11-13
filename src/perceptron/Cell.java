package perceptron;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Cell {
    private Circle c;
    private int index;
    private String function;
    private ArrayList<Double> parameters;
    private double threshold;
    private Matrix weights;
    private double output;

    public Cell(int i){
        weights = Matrix.random(1,1);
        index = i;
        create_Circle();
        function = "Unity";
        parameters = new ArrayList<>();
    }

    private void create_Circle () {
        c = new Circle(20);
        c.setFill(Color.WHITE);
        c.setStrokeWidth(3.0);
        c.setStroke(Color.BLACK);
    }

    Circle getCircle(){
        return c;
    }

    int getIndex() {
        return index;
    }

    void removeFocus() {
        c.setStroke(Color.BLACK);
    }

    void setFocus() {
        c.setStroke(Color.RED);
    }

    String getFunction() {
        return function;
    }

    void setFunction(String function) {
        this.function = function;
    }

    ArrayList<Double> getParameters() {
        return parameters;
    }

    void setParameters(ArrayList<Double> parameters) {
        this.parameters = parameters;
    }

    double getThreshold() {
        return threshold;
    }

    void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    Matrix getWeights() {
        return weights;
    }

    void setWeights(Matrix weights) {
        this.weights = weights;
    }

    void setOutput(double output) {
        this.output = output;
    }
}
