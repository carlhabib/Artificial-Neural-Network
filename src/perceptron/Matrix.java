package perceptron;

public class Matrix {
    public final int M;             // number of rows
    public final int N;             // number of columns
    public final double[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public double[] getData(){
        double[] temp=new double[M];
        for(int i=0;i<M;i++){
            temp[i]=data[i][0];
        }
        return temp;
    }

    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[M][N];
    }

    // create matrix based on 2d array
    public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }
    public Matrix(double[] data) {
        M = data.length;
        N = 1;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
                    this.data[i][0] = data[i];
    }

    // copy constructor
    public double[] toArray(){
    	double [] returned = new double[data.length];
    	for(int i=0;i<returned.length;i++){
    		returned[i]=data[i][0];
    	}
    	
    	return returned;
    }

    // create and return a random M-by-N matrix with values between 0 and 1
    public static Matrix random(int M, int N) {
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] = Math.random();
        return A;
    }

    // create and return the N-by-N identity matrix
    public static Matrix identity(int N) {
        Matrix I = new Matrix(N, N);
        for (int i = 0; i < N; i++)
            I.data[i][i] = 1;
        return I;
    }


    // create and return the transpose of the invoking matrix
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public Matrix plus(Matrix B) {

    	Matrix A = this;
        
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }


    // return C = A - B
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    // does A = B exactly?
    public boolean eq(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }


    public String showStr() {
        String str = "";
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++)
                str+=( data[i][j]+" ");
            str+="\n";
        }
        str+="\n";
        return str;
    }

    // return x = A^-1 b, assuming A is square and has full rank
   
    public void show() {
    	
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                System.out.print( data[i][j]+" ");
           System.out.println();
        }
        System.out.println();
        
    }



    
        
    }