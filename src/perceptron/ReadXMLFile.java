package perceptron;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadXMLFile {

    int index;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;

    public ReadXMLFile () {
        try{
            File fXmlFile = new File("Perceptrons.xml");
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);

        }catch (Exception e){

        }
    }

    Perceptron getPerceptron(int id) {
        NodeList elementsList = doc.getElementsByTagName("Perceptron");
        Element current = (Element) elementsList.item(id);


        NodeList layersList = current.getElementsByTagName("Layer");
        ArrayList<Layer> layersArrayList = new ArrayList<>();

        for(int i=0; i<layersList.getLength(); i++){
            Element currentLayer = (Element) layersList.item(i);
            int ind = Integer.parseInt(currentLayer.getAttributes().getNamedItem("index").getNodeValue());
            NodeList cellsList = currentLayer.getElementsByTagName("Cell");
            ArrayList<Cell> cellsArrayList = new ArrayList<>();

            for(int j=0; j<cellsList.getLength(); j++){
                Element currentCell = (Element) cellsList.item(j);
                NodeList parametersList = currentCell.getElementsByTagName("parameters");
                Element param = (Element) parametersList.item(0);
                String s = param.getTextContent();
                ArrayList<Double> parameters = new ArrayList<>();
                Scanner scan = new Scanner(s);
                while (scan.hasNext()){
                    parameters.add(Double.parseDouble(scan.next()));
                }

                NodeList weightsList = currentCell.getElementsByTagName("weight");
                Element weightsElt = (Element) weightsList.item(0);
                s = weightsElt.getTextContent();
                ArrayList<Double> weightsArrayList = new ArrayList<>();
                scan = new Scanner(s);
                while (scan.hasNext()){
                    weightsArrayList.add(Double.parseDouble(scan.next()));
                }
                double [] arr = new double[weightsArrayList.size()];
                for(int k=0; k<arr.length; k++){
                    arr[k] = weightsArrayList.get(k);
                }
                Matrix weightsMatrix = new Matrix(arr);

                NodeList functionNodeList = currentCell.getElementsByTagName("function");
                Element functionElt = (Element) functionNodeList.item(0);
                String function = functionElt.getTextContent();

                NodeList indexNodeList = currentCell.getElementsByTagName("index");
                Element indexElt = (Element) indexNodeList.item(0);
                int index = Integer.parseInt(indexElt.getTextContent());

                NodeList threshNodeList = currentCell.getElementsByTagName("threshold");
                Element threshElt = (Element) threshNodeList.item(0);
                double threshold = Double.parseDouble(threshElt.getTextContent());

                Cell c = new Cell(index);
                c.setFunction(function);
                c.setParameters(parameters);
                c.setThreshold(threshold);
                c.setWeights(weightsMatrix);
                cellsArrayList.add(c);
            }
            Layer l = new Layer(ind);
            l.setCells_ArrayList(cellsArrayList);
            layersArrayList.add(l);
        }

        NodeList thresholdErrorList = current.getElementsByTagName("errorthreshold");
        Element thresholdErrorElt = (Element) thresholdErrorList.item(0);
        double errorThreshold = Double.parseDouble(thresholdErrorElt.getTextContent());

        NodeList functionErrorList = current.getElementsByTagName("errorfunction");
        Element functionErrorElt = (Element) functionErrorList.item(0);
        String errorFunction = functionErrorElt.getTextContent();

        Perceptron per = new Perceptron(layersArrayList,errorFunction,errorThreshold);
        return per;
    }

}
