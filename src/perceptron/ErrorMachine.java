package perceptron;

class ErrorMachine {
	static double computeError(String name, double[] Y, double[] C){
		switch (name) {
			case "MAE":
				return computeErrorMAE(Y, C);
			case "MSE":
				return computeErrorMSE(Y, C);
			default:
				return computeErrorMDE(Y, C);
		}
	
	}
	private static double computeErrorMDE(double[] Y, double[] D) {
		double error;

			double sum = 0;
			for (int i = 0; i < Y.length; i++) {
				sum += (D[i] - Y[i]);
			}
			error = sum / Y.length;
			return error;
	}
	
	private static double computeErrorMAE(double[] Y, double[] D) {
		double error;
			double sum = 0;
			for (int i = 0; i < Y.length; i++) {
				sum += (Math.abs(D[i] - Y[i]));
			}
			error = sum / Y.length;
			return error;

	}
	
	private static double computeErrorMSE(double[] Y, double[] D) {
		double error;
			double sum = 0;
			for (int i = 0; i < Y.length; i++) {
				sum += Math.pow((D[i] - Y[i]),2);
			}
			 error = sum / Y.length;
			return error;
	}
	
	//Evaluate the error whether acceptable or no
	static boolean errorAcceptance(double error, double threshold) {
		
		double result = threshold - error;
		return result >= 0.0;

	}
	
	
	
	
}