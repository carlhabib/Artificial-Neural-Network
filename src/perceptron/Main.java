package perceptron;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

public class Main extends Application {


    static ArrayList<Layer> layersArrayList;
    static Perceptron perceptron;
    static String filepath = "Perceptrons.xml";
    static Document doc;
    static DocumentBuilder docBuilder;
    static DocumentBuilderFactory docFactory;
    static Stage stage;
    static WriteXMLFile wxf;
    static ReadXMLFile rdx;

    static String errorFunction;
    static double errorThreshold;


    @Override
    public void start(Stage primaryStage) throws Exception{
        wxf = new WriteXMLFile();
        wxf.numOfPerceptrons();

        Parent root = FXMLLoader.load(getClass().getResource("HomePanel.fxml"));
        primaryStage.setTitle("Perceptron");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        stage = primaryStage;

    }

    public static void main(String[] args) {
        launch(args);
    }
}