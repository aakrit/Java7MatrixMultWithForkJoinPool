package matrix_multiplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.RecursiveAction;

public class MatrixMultiplicationForkJoinAction extends RecursiveAction{

		private static BufferedReader inputfileName; //read from file passed as 1st Argo
		private static FileOutputStream outputFile;
		private static double[][] aMatrix;
		private static double[][] bMatrix;
		private static double[][] c;
		static long ts;
		static long tf;
		static int MATRIXSIZE, colSize;
		private int rowSize;
		private static final int task_threshold = 10;
		public MatrixMultiplicationForkJoinAction(double[][] aM, double[][] bM, double[][] cM)
		{
			this(aM, bM, cM, -1);
		}
		private MatrixMultiplicationForkJoinAction(double[][] aM, double[][] bM, double[][] cM, int size)
		{
			if(aM[0].length != bM.length)
				throw new IllegalArgumentException("not a square matrix");
			this.aMatrix = aM; this.bMatrix = bM; this.c = cM; this.rowSize = size;
		}
		@Override
		protected void compute()
		{
			if(rowSize == -1)
			{
				List<MatrixMultiplicationForkJoinAction> actions = new ArrayList<MatrixMultiplicationForkJoinAction>();
				for(int row = 0; row < aMatrix[0].length; row++)
				{
					actions.add(new MatrixMultiplicationForkJoinAction(aMatrix, bMatrix, c, row));
				}
				invokeAll(actions);
			}
			else
				multiply(aMatrix, bMatrix, c, rowSize);	
		}
		public static void multiply(double[][] A, double[][] B, double[][] C, int row)
		{
			int nA = A.length;
			int nB = B.length;
			int mA = A[0].length;
			int mB = B[0].length;
			if(nA != mB)
				throw new RuntimeException("incorrect matric values");
			double[][] outPutMatrix = new double[mA][nB];
				for(int j =0; j < nB; j++)
					for(int k =0; k<nA; k++)
						C[row][j] += (A[row][k]*B[k][j]);
			
		}
		private static void readInputFromFile() throws IOException
		{
			
			String read;
			int lineIncrement = 0;
			boolean done = false;
			boolean matrixB = false;
			while ((read = inputfileName.readLine()) != null)
			{
				StringTokenizer s = new StringTokenizer(read);//read entire line into s
				MATRIXSIZE = s.countTokens();//count number of elements in the line saved in s
				colSize = MATRIXSIZE;
				int n = MATRIXSIZE;
				if(!done)//create new matrix 1st time around
				{
					aMatrix = new double[n][n];
					bMatrix = new double[n][n];
					done = true;
				}
				if(n==0)//if no elements read, time to 
				{
					matrixB = true;
					lineIncrement = 0;
					continue;
				}
				int i = 0;
				while(s.hasMoreTokens() && (matrixB == false))
				{
					int a = Integer.parseInt(s.nextToken());
					aMatrix[lineIncrement][i] = a;
					i++;
				}
				int j = 0;
				while(s.hasMoreTokens() && (matrixB == true))
				{
					int a = Integer.parseInt(s.nextToken());
					bMatrix[lineIncrement][j] = a;
					j++;
				}
				lineIncrement++;
			}
		}
		private static void printInitialMatrix()
		{
			System.out.println("Matrix A provided is:");
			printMatrix(aMatrix); 
			System.out.println("Matrix B provided is:");
			printMatrix(bMatrix);
		}
		private static void printMatrix(double[][] c)
		{
			for(int i = 1; i <= c[0].length; i++)
			{
				System.out.print("\tCol "+i);

			}
			System.out.println();
	        for ( int i = 0 ; i < c.length ; i++ )
	        {
	        	System.out.print(" Row "+(i+1)+" \t");
	           for ( int j = 0 ; j < c[0].length ; j++ )
	           {
	              System.out.print(c[i][j]+"\t");
	           }
	           System.out.println();
	        }
		}
		private static void writeOutputToFile() throws IOException
		{
			File file = new File("output_file_ForkJoinPoolRecursiveAction");
			outputFile = new FileOutputStream(file);
			if(!file.exists())
			{
				file.createNewFile();
			}
			writeFile("MATRIX MULT RESULT:\n");
			for(int i = 1; i <= c[0].length; i++)
			{
				writeFile("\tCol "+i);
			}
			writeFile("\n");
	        for ( int i = 0 ; i < c.length ; i++ )
	        {
	        	writeFile(" Row "+(i+1)+" \t");
	           for ( int j = 0 ; j < c[0].length ; j++ )
	           {
	        	   	double value = c[i][j];
	        	   	String inwa = String.valueOf(value)+"\t";
	        	   	writeFile(inwa);
	           }
	           writeFile("\n");
	   		}
	        System.out.println("Written output to file: output_file_ForkJoinPoolRecursiveAction");
	        outputFile.close();
		}
		private static void writeFile(String s) throws IOException
		{
			byte[] input = s.getBytes();
			outputFile.write(input);
			outputFile.flush();
		}
		public static void main(String[] args) throws IOException 
		{
			//read input from file and then parse the data into two 2-dimentional array for computing
			if(args.length == 0)
			{
				try//get the filename to read from and the number of threads to launch
				{
					inputfileName = new BufferedReader(new FileReader("input_file"));
					readInputFromFile();	
				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(args.length == 1)
			{
				try//get the filename to read from and the number of threads to launch
				{
					inputfileName = new BufferedReader(new FileReader(args[0]));
					readInputFromFile();
				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				aMatrix = new double[][]
						{
							{1, 2, 3, 5},
							{4, 5, 6, 6},
							{7, 8, 7, 5},
							{7, 3, 9, 3}
						};
				bMatrix = new double[][]
						{
							{1, 2, 3, 4},
							{4, 5, 3, 3},
							{7, 3, 9, 3},
							{7, 3, 9, 3}
						};
				MATRIXSIZE = 4;
//				rowSize = 4;
				colSize = 4;
			}
			c = new double[MATRIXSIZE][MATRIXSIZE];//intialize empty matrix to store mult data
			ForkJoinPool fjp = new ForkJoinPool();
			ts = System.currentTimeMillis();
			fjp.invoke(new MatrixMultiplicationForkJoinAction(aMatrix, bMatrix, c));
			tf = System.currentTimeMillis();
			double time = (tf-ts)/1000.;
			System.out.println("Time taken for this MatrixMult Computation when computed Parallel with ForkJoinPool_ResuriveAction Lib: " + time);
			if(MATRIXSIZE <= 10)//do not print output, save output to fine
			{
				printInitialMatrix();//output matrixA and B
				System.out.println("Matrix Multiplication Results!\n");
				printMatrix(c);
				try{
					writeOutputToFile();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else//large matrix, save output to a file
			{
				try{
					writeOutputToFile();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
