package perceptron;

import java.util.ArrayList;


//This class contains all the methods used by the perceptron
public class Perceptron {

    ArrayList<Layer> layerArrayList;
    String errorFunction;
    double errorThreshold;
    private int maxIter;


    public Perceptron(ArrayList<Layer> layerArrayList, String errorFunction, double errorThreshold) {

        this.layerArrayList = layerArrayList;
        this.errorFunction = errorFunction;
        this.errorThreshold = errorThreshold;
        maxIter = 100;
    }

    void setMaxIter(int val){
        maxIter = val;
    }


    //run with weights correction at each iteration
    void trainPerceptron(double[] inputs, double[] outputs) {
        Matrix desiredOutput = new Matrix(outputs);//transform desired output arrray to matrix
        boolean done = false; // boolean value to check if training has completed
        int count = 0; //number of iterations before we quit training for this pair

        while (!done && count < maxIter) {  //as long as the training was not reached we repeat updating the weigthts
            ArrayList<Matrix> intermediateOutput = run(inputs); //ArrayList containing the output of each layer
            count++;
            intermediateOutput.get((intermediateOutput.size() - 1)).show();
            double error = ErrorMachine.computeError(errorFunction, intermediateOutput.get((intermediateOutput.size() - 1)).toArray(), desiredOutput.toArray());//Calcualte error between desired output and actual output


            done = ErrorMachine.errorAcceptance(error, errorThreshold);// Compare error to thershold
            if (!done) {// if the error is more than the threshold we should update weights
                Matrix dm;
                dm = desiredOutput;
                Matrix ym = intermediateOutput.get((intermediateOutput.size() - 1));//actual output


                Matrix err2 = dm.minus(ym);//error at layer between desired and actual output


                ArrayList<Cell> cellsArrayList = layerArrayList.get(layerArrayList.size() - 1).getCells_ArrayList(); //weights at last layer
                Matrix Zm = intermediateOutput.get((intermediateOutput.size() - 2)); //input to last layer
                Matrix vt;
                Matrix deltaV = (err2.times(Zm.transpose()));
                double [][] deltaVArray = deltaV.data;
                for (int i = 0; i < cellsArrayList.size(); i++) {
                    //calculate the needed value to adjust the weights by multiplying the error at layer by input of the layer transposed twice
                    Matrix temp = new Matrix(deltaVArray[i]);
                    vt = cellsArrayList.get(i).getWeights(); //get cell weights
                    
                    vt = vt.plus(temp); //corrects the weights
                    cellsArrayList.get(i).setWeights(vt);
                }
            }
        }
    }


    //gets the output at last element of the array
    Matrix test(double[] input) {

            ArrayList<Matrix>  intermediateOutput = run(input);
            return intermediateOutput.get((intermediateOutput.size()-1));

    }


    //run the inputs through the perceptron and gets an output
    private ArrayList<Matrix> run(double[] inputs) {
        ArrayList<Matrix> intermediateOutput = new ArrayList<>();
        Matrix inputM = new Matrix(inputs);
        intermediateOutput.add(inputM);
        for (int i = 1; i < layerArrayList.size(); i++) {
            Layer current = layerArrayList.get(i);
            double[] intermediateOutputArray = new double[current.getCells_ArrayList().size()];
            for (int j = 0; j < current.getCells_ArrayList().size(); j++) {
                Cell c = current.getCells_ArrayList().get(j);
                double[] transition = c.getWeights().transpose().times(intermediateOutput.get(i - 1)).toArray();
                for (int k = 0; k < transition.length; k++) {
                    transition[k] -= c.getThreshold();
                }
                Matrix next = new Matrix(transition);
                Functions f = new Functions();
                intermediateOutputArray[j] = f.calculate(c.getParameters(), next);
                c.setOutput(intermediateOutputArray[j]);
            }
            intermediateOutput.add(new Matrix(intermediateOutputArray));
        }
        return intermediateOutput;
    }

    public String sendString(){
        String str = "";
        str+=("Number of layers:\n");
        str+=(layerArrayList.size())+"\n";
        for(int i=1;i<=layerArrayList.size();i++){
            str+=("Layer: " + i+"\n");
            str+=("Number of cells per layer "+ i +": "+ layerArrayList.get(i-1).getCells_ArrayList().size()+"\n");
            str+=("Weigths at this layer: \n");
            for(int j=0; j<layerArrayList.get(i-1).getCells_ArrayList().size(); j++){
                //str+= layerArrayList.get(i-1).getCells_ArrayList().get(j).getWeights().showStr();
            }

        }
        str+= "The error Function is "+errorFunction +"\n";
        str+= "The error Threshold is "+errorThreshold +"\n";
        str+= "The number of Inputs is "+ layerArrayList.get(0).getCells_ArrayList().size() +"\n";
        return str;
    }
}

