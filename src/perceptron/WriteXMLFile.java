package perceptron;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WriteXMLFile {
    int perceptronIndex;
    Perceptron p;
    Document doc;

    public WriteXMLFile() {

    }

    //gets the number of perceptron in the file
    int numOfPerceptrons(){
        try {
            String filepath = "Perceptrons.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(filepath);
            Node root = doc.getFirstChild();
            perceptronIndex = root.getChildNodes().getLength();
            return perceptronIndex;
        }catch (Exception e){
            return 0;
        }
    }


    //Writes the Percepteptron to XML
    void writeXML(Perceptron p) throws Exception {
        this.p = p;
        if(!(new File("Perceptrons.xml")).exists() && numOfPerceptrons()<1) {
            perceptronIndex=0;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // Perceptron elements
            Element Perceptron = doc.createElement("Perceptron");
            rootElement.appendChild(Perceptron);
            Perceptron.setAttribute("index", "" + perceptronIndex + "");

            for(int i=0; i<p.layerArrayList.size(); i++) {
                // Layers elements
                Element Layers = doc.createElement("Layer");
                Perceptron.appendChild(Layers);
                Layer l = p.layerArrayList.get(i);
                Layers.setAttribute("index", "" + (l.getIndex()));
                for(int j=0; j<l.getCells_ArrayList().size(); j++) {
                    Cell c = l.getCells_ArrayList().get(j);
                    Layers.appendChild(getCell(doc, c.getParameters(), c.getFunction(), c.getWeights().toArray(), c.getIndex(), c.getThreshold()));
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Perceptrons.xml"));
            transformer.transform(source, result);
        }
        else {
            appendXML();
        }
    }


    //append to the XML document
    private void appendXML() throws Exception{
        String filepath = "Perceptrons.xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(filepath);
        Node root = doc.getFirstChild();

        perceptronIndex = root.getChildNodes().getLength()-1;

        // Perceptron elements
        Element perceptron = doc.createElement("Perceptron");
        root.appendChild(perceptron);
        perceptron.setAttribute("index", "" + perceptronIndex + "");


        perceptron.appendChild(getCellElements(doc,perceptron,"errorfunction",p.errorFunction+""));
        perceptron.appendChild(getCellElements(doc,perceptron,"errorthreshold",p.errorThreshold+""));

        for(int i=0; i<p.layerArrayList.size(); i++) {
            // Layers elements
            Element Layers = doc.createElement("Layer");
            perceptron.appendChild(Layers);
            Layer l = p.layerArrayList.get(i);
            Layers.setAttribute("index", "" + (l.getIndex()));
            for(int j=0; j<l.getCells_ArrayList().size(); j++) {
                Cell c = l.getCells_ArrayList().get(j);
                Layers.appendChild(getCell(doc, c.getParameters(), c.getFunction(), c.getWeights().toArray(), c.getIndex(), c.getThreshold()));
            }
        }

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);

    }


    //get the cell and inserts it in the XML File
    private static Node getCell(Document doc,ArrayList<Double> parameters, String funct, double[] weight, int index,
                                double thresh) {
        // Cells elements
        Element cell = doc.createElement("Cell");

        //create parameters element
        String p = "";
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<parameters.size(); i++){
            if(i<parameters.size()-1) {
                sb.append(parameters.get(i) + " ");
            }else{
                sb.append(parameters.get(i));
            }
        }
        p = sb.toString();
        cell.appendChild(getCellElements(doc, cell, "parameters", p+""));

        //create function element
        cell.appendChild(getCellElements(doc, cell, "function", funct+""));

        //create weight element
        String w = "";
        sb= new StringBuilder();
        for(int i=0; i<weight.length; i++){

            if(i<weight.length-1) {
                sb.append(weight[i] + " ");
            }else {
                sb.append(weight[i]+"");
            }
        }
        w = sb.toString();
        cell.appendChild(getCellElements(doc, cell, "weight", w+""));

        //create index element
        cell.appendChild(getCellElements(doc, cell, "index", index+""));

        //create threshold element
        cell.appendChild(getCellElements(doc, cell, "threshold", thresh+""));

        return cell;
    }


    private static Node getCellElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}