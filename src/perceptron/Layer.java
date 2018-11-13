package perceptron;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Layer {
    private ArrayList<Cell> cells_ArrayList;
    private VBox vBox;
    private int index;
    private Cell current;

    public Layer(){
        cells_ArrayList = new ArrayList<>();
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        add_Cell_toLast();
        index = 0;
    }

    public Layer(int i){
        cells_ArrayList = new ArrayList<>();
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        for(int j = 0; j<i; j++){
            add_Cell_toLast();
        }
        index = 0;
    }

    void add_Cell_toLast() {
        Cell c = new Cell(cells_ArrayList.size());
        cells_ArrayList.add(c);
        vBox.getChildren().add(c.getCircle());

        if(current!=null){
            current.removeFocus();
        }
        current = c;
        current.setFocus();
    }

    public void delete_Last_Cell() {
        if(!cells_ArrayList.isEmpty()) {
            cells_ArrayList.remove(cells_ArrayList.size()-1);
        }
    }

    void setCells_ArrayList(ArrayList<Cell> cells_ArrayList) {
        this.cells_ArrayList = cells_ArrayList;
    }

    ArrayList<Cell> getCells_ArrayList(){
        return cells_ArrayList;
    }

    public VBox getVBox () {
        return vBox;
    }

    int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    Cell getCurrentCell() {
        return current;
    }

    void setCurrentCell(Cell cell) {
        current = cell;
    }
}
