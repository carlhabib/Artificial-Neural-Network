package perceptron;

import java.util.ArrayList;

class Functions {

	Functions(){

	}
	static double calculate(ArrayList<Double> parameters, Matrix matrix){
		switch (parameters.size()){
			case 1:{
				return unitFunction( parameters,  matrix);
			}
			case 2:{
				return sigmoidFunction( parameters,  matrix);
			}
			case 3:{
				return gaussianFunction( parameters,  matrix);
			}
			default:{
				return blfFunction( parameters,  matrix);
			}
		}
	}


	private static double unitFunction(ArrayList<Double> parameters, Matrix matrix){
		double [] matrixData = matrix.getData();
		double functionoutput = 0;

		double a = parameters.get(0);

		for (double aMatrixData : matrixData) {
			if (aMatrixData > 0) {
				functionoutput += a;
			}
		}

		return functionoutput;
	}


	private static double blfFunction(ArrayList<Double> parameters, Matrix matrix){
		double [] matrixData = matrix.getData();
		double functionoutput = 0;

		double vMin = parameters.get(0);
		double vMax = parameters.get(1);
		double a = parameters.get(2);
		double b = parameters.get(3);

		double m = a / (vMax - vMin);

		for (double aMatrixData : matrixData) {
			if (!(aMatrixData < vMin)) {
				if (aMatrixData <= vMax) {
					functionoutput += m * aMatrixData + b;
				} else {
					functionoutput += a;
				}
			}
		}

		return functionoutput;
	}

	private static double sigmoidFunction(ArrayList<Double> parameters, Matrix matrix) {
		double [] matrixData = matrix.getData();
		double functionoutput = 0;

		double a = parameters.get(0);
		double k = parameters.get(1);

		for (double aMatrixData : matrixData) {
			functionoutput += a * ((Math.exp(k * functionoutput) - 1) / (Math.exp(k * functionoutput) + 1));

		}

		return functionoutput;
	}

	private static double gaussianFunction(ArrayList<Double> parameters, Matrix matrix){
		double [] matrixData = matrix.getData();
		double functionoutput = 0;
		double a = parameters.get(0);
		double v0 = parameters.get(1);
		double sigma = parameters.get(2);

		for (double aMatrixData : matrixData) {
			functionoutput += a * Math.exp(Math.pow((functionoutput - v0) / sigma, 2));
		}

		return functionoutput;

	}



}