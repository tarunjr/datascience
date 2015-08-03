package datamining.classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datamining.base.Matrix;

public class CondensedNearestNeighbour {
	private final Matrix trainingSet;
	private  Matrix condensedSet;
	private  NearestNeighbourClassifier condensedClassifier;
	
	private int numOfIterationsDuringCondensation;
	
	public CondensedNearestNeighbour(Matrix trainingSet)
	{
		this.trainingSet = trainingSet;
		this.condensedSet = null;
		
		train();
		
		this.condensedClassifier = new NearestNeighbourClassifier(this.condensedSet);
	}
	private void train()
	{
		
		List<Integer> trainingSetIndex = new ArrayList<Integer>();
		List<Integer> condensedSetIndex = new ArrayList<Integer>();
		
		
		this.numOfIterationsDuringCondensation = 0;
		for(int i=0;i<this.trainingSet.rowSize();i++)
			trainingSetIndex.add(i);
		
		
		// Step 1 Add the first pattern to condensation set
		condensedSetIndex.add(0);
		
		boolean loop = false;
		do
		{
			this.numOfIterationsDuringCondensation++;
			loop = false;
			// Step 2: Reduced = Training - Condensed
			List<Integer> reducedSetIndex = new ArrayList<Integer>();
			for(int i=0;i<condensedSetIndex.size();i++)
			{
				int condensedIndex = condensedSetIndex.get(i);
				for(int j=0; j<trainingSetIndex.size(); j++)
				{
					int trainingIndex = trainingSetIndex.get(j);
					if(trainingIndex != condensedIndex)
						reducedSetIndex.add(trainingIndex);
				}
			}
			
			// Step 3: Classify each reduced set pattern by using Condensation set as the training pattern using NN algorithm
			for(int i=0; i<reducedSetIndex.size(); i++)
			{
				Matrix tempCondensedSet = this.trainingSet.getMatrix(condensedSetIndex);
				NearestNeighbourClassifier classifier = new NearestNeighbourClassifier(tempCondensedSet);
				
				int reducedIndex = reducedSetIndex.get(i);
				double[] testPattern = Arrays.copyOfRange(this.trainingSet.getRow(reducedIndex), 0, this.trainingSet.columnSize()-1);
					
				double predictedClassValue = classifier.classify(testPattern);
				double actualClassValue = this.trainingSet.get(reducedIndex,this.trainingSet.columnSize()-1);
				if(predictedClassValue != actualClassValue)
				{
					// Insert this in condensed 
					condensedSetIndex.add(reducedIndex);
					loop = true;
				}
			}
		} while(loop); // Step 3: Repeat until no change in condensed set
		
		// Return the condensed set data
		this.condensedSet = this.trainingSet.getMatrix(condensedSetIndex);
	}
	
	public double classify(double[] testPattern)
	{
		return this.condensedClassifier.classify(testPattern);	
	}
	public void printDiganostic()
	{
		System.out.println("---------------------------------------------");
		System.out.println("CondensedNearestNeighbour: Summary");
		System.out.println("---------------------------------------------");
		System.out.print("Training set size=");
		System.out.println(this.trainingSet.rowSize());
		System.out.print("Condensed Training set size=");
		System.out.println(this.condensedSet.rowSize());
		System.out.print("Iterations to build Condensation Training set=");
		System.out.println(this.numOfIterationsDuringCondensation);
		
		System.out.println("---------------------------------------------");
	
	}

}
