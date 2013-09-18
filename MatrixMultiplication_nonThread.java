package matrix_multiplication;

import java.util.StringTokenizer;
import java.io.*;

public class MatrixMultiplication_nonThread
{

	private static BufferedReader inputfileName; //read from file passed as 1st Argo
	private static FileOutputStream outputFile;
	private static double[][] aMatrix;
	private static double[][] bMatrix;
	private static double[][] c;
	static long ts;
	static long tf;
	static int MATRIXSIZE;
	
	public static double[][] multiply(double[][] A, double[][] B)
	{
		int nA = A.length;
		int nB = B.length;
		int mA = A[0].length;
		int mB = B[0].length;
		if(nA != mB)
			throw new RuntimeException("incorrect matric values");
		
		double[][] outPutMatrix = new double[mA][nB];
		for(int i =0; i < mA; i++)
			for(int j =0; j < nB; j++)
				for(int k =0; k<nA; k++)
					outPutMatrix[i][j] += (A[i][k]*B[k][j]);
		return outPutMatrix;
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
//		boolean row3 = false;
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
		File file = new File("output_file_nonThreaded");
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
        System.out.println("Written output to file: output_file_nonThreaded");
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
		// TODO Auto-generated method stub
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
		}
		c = new double[MATRIXSIZE][MATRIXSIZE];
		ts = System.currentTimeMillis();
		c = multiply(aMatrix, bMatrix);//do major operation
		tf = System.currentTimeMillis();
		double time = (tf-ts)/1000.;
		System.out.println("Time taken for this MatrixMult Computation when computed Seriallly: " + time);
		if(MATRIXSIZE <= 10)//print output and save output to fine
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
