package datamining.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import datamining.base.Data;
import datamining.base.Matrix;
import datamining.classification.CondensedNearestNeighbour;
import datamining.classification.NearestNeighbourClassifier;
import datamining.clustering.Cluster;
import datamining.clustering.KMeansClustering;



public class Driver {
	public static void main(String[] args)  
	{
		
		Driver d = new Driver();
		// Default file to use if none specified on command line
		String inputDataSetFile = "C:/iris.data";
		if(args.length > 0)
			inputDataSetFile = args[0];
		
		
		Matrix allDataSet = d.getAllData(inputDataSetFile);
		Data data = d.getData(inputDataSetFile, 0.6);
		Matrix trainingDataSet = data.train;
		Matrix testDataSet = data.test;
		
		
		d.Assignment1_NN(trainingDataSet, testDataSet);
		d.Assignment2_CNN(trainingDataSet, testDataSet);
		d.Assignment3_KMeans(allDataSet);
		
	}
	public void Assignment1_NN(Matrix trainingDataSet, Matrix testDataSet)
	{
		int total = testDataSet.rowSize();
		int correct = 0;
		
		NearestNeighbourClassifier classifier = new NearestNeighbourClassifier(trainingDataSet);
		for(int i=0; i < total;i++)
		{
			double[] testPattern = Arrays.copyOfRange(testDataSet.getRow(i),0,testDataSet.columnSize()-1); 
			double predictedClassValue = classifier.classify(testPattern);
			double actualClassValue = testDataSet.get(i, testDataSet.columnSize()-1);
			
			if(predictedClassValue == actualClassValue)
				correct++;
		}
		
		report("NearestNeighbour Classifier (NN)", total, correct);
	}
	public void Assignment2_CNN(Matrix trainingDataSet, Matrix testDataSet)
	{
		int total = testDataSet.rowSize();
		int correct = 0;
		
		CondensedNearestNeighbour classifier = new CondensedNearestNeighbour(trainingDataSet);
		for(int i=0; i < total;i++)
		{
			double[] testPattern = Arrays.copyOfRange(testDataSet.getRow(i),0,testDataSet.columnSize()-1); 
			double predictedClassValue = classifier.classify(testPattern);
			double actualClassValue = testDataSet.get(i, testDataSet.columnSize()-1);
			
			if(predictedClassValue == actualClassValue)
				correct++;
		}
		
		report("CondensedNearestNeighbour Classifier (CNN)", total, correct);
		classifier.printDiganostic();
	}
	public void Assignment3_KMeans(Matrix dataSet)
	{
		System.out.println("K= First 3 patterns");
		int [] kIndexes = {0,1,2};
		KMeansClustering clustering = new KMeansClustering(dataSet, kIndexes);
		
		List<Cluster> clusters = clustering.getClusters();
		clustering.printDiganostic();
		
		System.out.println("K=First pattern from each class");
		int [] kIndexes2 = {0,50,100};
		KMeansClustering clustering2 = new KMeansClustering(dataSet, kIndexes2);
		
		List<Cluster> clusters2 = clustering2.getClusters();
		clustering2.printDiganostic();
		
		System.out.println("K=3 random pattern from entire dataset");
		int [] kIndexes3 = {95,145,73};
		KMeansClustering clustering3 = new KMeansClustering(dataSet, kIndexes3);
		
		List<Cluster> clusters3 = clustering3.getClusters();
		clustering3.printDiganostic();
	}
	public void report(String header, int total, int classified)
	{
		System.out.println("---------------------------------------------");
		System.out.println(header);
		System.out.println("---------------------------------------------");
		System.out.print("Total Patterns=");
		System.out.println(total);
		System.out.print("Classified Patterns=");
		System.out.println(classified);
		System.out.print("Mis-Classified Patterns=");
		System.out.println(total-classified);
		System.out.print("Classification Accuracy=");
		double accuracy = ((double)classified / (double)total) * 100;
		System.out.println(accuracy);
		System.out.println("---------------------------------------------");
	}
	public Data getData(String file, double train_percentage)
	{
		InputStream    fis = null;
		BufferedReader br = null;
		String         line;
		 
		
		List<double[]> setosa = new ArrayList<double[]>();
		List<double[]>  versicolor= new ArrayList<double[]>();
		List<double[]> virginica = new ArrayList<double[]>();
		
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			
			while ((line = br.readLine()) != null) {
				
			    StringTokenizer token = new StringTokenizer(line, ",");
			    if(token.countTokens() < 5)
			    	continue;
			    
			    double[] row = new double[5];
			    row[0] = new Double(token.nextToken());
			    row[1] = new Double(token.nextToken());
			    row[2] = new Double(token.nextToken());
			    row[3] = new Double(token.nextToken());
			    row[4]  = discretize(token.nextToken());
			    
			    if(row[4] == 1)
			    	setosa.add(row);
			    else if(row[4] == 2)
			    	versicolor.add(row);
			    else if(row[4] == 3)
			    	virginica.add(row);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
		
		int part = (int)(setosa.size() * train_percentage);
		List<double[]> setosaTrain = setosa.subList(0, part);
		List<double[]> setosaTest = setosa.subList(part, setosa.size());
		
		part = (int)(versicolor.size() * train_percentage);
		List<double[]> versicolorTrain = versicolor.subList(0, part);
		List<double[]> versicolorTest = versicolor.subList(part, versicolor.size());
		
		part = (int)(virginica.size() * train_percentage);
		List<double[]> virginicaTrain = virginica.subList(0, part);
		List<double[]> virginicaTest = virginica.subList(part, virginica.size());
		
		Matrix train = new Matrix(virginicaTrain.size() + setosaTrain.size() + versicolorTrain.size(), 5);
		int rowIndex = 0;
		for(int i=0; i < setosaTrain.size();i++)
		{
			double[] row = setosaTrain.get(i);
			train.add(rowIndex, 0, row[0]);
			train.add(rowIndex, 1, row[1]);
			train.add(rowIndex, 2, row[2]);
			train.add(rowIndex, 3, row[3]);
			train.add(rowIndex, 4, row[4]);
			rowIndex++;
		}
		
		for(int i=0; i < versicolorTrain.size();i++)
		{
			double[] row = versicolorTrain.get(i);
			train.add(rowIndex, 0, row[0]);
			train.add(rowIndex, 1, row[1]);
			train.add(rowIndex, 2, row[2]);
			train.add(rowIndex, 3, row[3]);
			train.add(rowIndex, 4, row[4]);
			rowIndex++;
		}
		
		for(int i=0; i < virginicaTrain.size();i++)
		{
			double[] row = virginicaTrain.get(i);
			train.add(rowIndex, 0, row[0]);
			train.add(rowIndex, 1, row[1]);
			train.add(rowIndex, 2, row[2]);
			train.add(rowIndex, 3, row[3]);
			train.add(rowIndex, 4, row[4]);
			rowIndex++;
		}
		
		Matrix test = new Matrix(setosaTest.size() + versicolorTest.size() + virginicaTest.size(), 5);
		rowIndex = 0;
		
		for(int i=0; i < setosaTest.size();i++)
		{
			double[] row = setosaTest.get(i);
			test.add(rowIndex, 0, row[0]);
			test.add(rowIndex, 1, row[1]);
			test.add(rowIndex, 2, row[2]);
			test.add(rowIndex, 3, row[3]);
			test.add(rowIndex, 4, row[4]);
			rowIndex++;
		}
		for(int i=0; i < versicolorTest.size();i++)
		{
			double[] row = versicolorTest.get(i);
			test.add(rowIndex, 0, row[0]);
			test.add(rowIndex, 1, row[1]);
			test.add(rowIndex, 2, row[2]);
			test.add(rowIndex, 3, row[3]);
			test.add(rowIndex, 4, row[4]);
			rowIndex++;
		}
		for(int i=0; i < virginicaTest.size();i++)
		{
			double[] row = virginicaTest.get(i);
			test.add(rowIndex, 0, row[0]);
			test.add(rowIndex, 1, row[1]);
			test.add(rowIndex, 2, row[2]);
			test.add(rowIndex, 3, row[3]);
			test.add(rowIndex, 4, row[4]);
			rowIndex++;
		}
		Data d = new Data();
		d.train = train;
		d.test = test;
		
		return d;
	}
	public Matrix getAllData(String file)
	{
		InputStream    fis = null;
		BufferedReader br = null;
		String         line;
		 
		
		List<double[]> patterns = new ArrayList<double[]>();
	
		
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			
			while ((line = br.readLine()) != null) {
				
			    StringTokenizer token = new StringTokenizer(line, ",");
			    if(token.countTokens() < 5)
			    	continue;
			    
			    double[] pattern = new double[5];
			    pattern[0] = new Double(token.nextToken());
			    pattern[1] = new Double(token.nextToken());
			    pattern[2] = new Double(token.nextToken());
			    pattern[3] = new Double(token.nextToken());
			    pattern[4]  = discretize(token.nextToken());
			    
			    patterns.add(pattern);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		Matrix dataSet = new Matrix(patterns.size(), 5);
		for(int i=0; i < patterns.size();i++)
		{
			double[] row = patterns.get(i);
			dataSet.addRow(i, row);
		}
		
		return dataSet;
	}
	private double discretize(String variable)
	{
		double result = 1;		
		if(variable.equals("Iris-setosa"))
			result = 1;
		else if(variable.equals("Iris-versicolor"))
			result = 2;
		else if(variable.equals("Iris-virginica"))
			result = 3;

		return(result);
	}
}



