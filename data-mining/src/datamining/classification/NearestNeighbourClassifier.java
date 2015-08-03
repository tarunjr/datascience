package datamining.classification;

import java.util.Arrays;

import datamining.base.EuclidianDistance;
import datamining.base.Matrix;

public class NearestNeighbourClassifier {
	private final Matrix trainingSet;
	
	public NearestNeighbourClassifier(Matrix trainingSet)
	{
		this.trainingSet = trainingSet;
	}
	public double classify(double[] testPattern)
	{
		double minDistance = Double.MAX_VALUE;
		int minDistanceIndex = -1;
		for(int i = 0; i < this.trainingSet.rowSize(); i++)
		{
			double []trainPattern = Arrays.copyOfRange(this.trainingSet.getRow(i),0, this.trainingSet.columnSize()-1);
			
			double distance = EuclidianDistance.distance(trainPattern, testPattern);
								
			if(distance <= minDistance)
			{
				minDistance = distance;
				minDistanceIndex = i;
			}
								
		}	
	    return this.trainingSet.get(minDistanceIndex, this.trainingSet.columnSize()-1);
	}
}
