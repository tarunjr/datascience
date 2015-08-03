package datamining.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datamining.base.EuclidianDistance;
import datamining.base.Matrix;

public class KMeansClustering {
	
	private List<Cluster> clusters; 
	private int k;
	private Matrix dataSet;
	private int[] kIndexes;
	private int numOfIterations;
	
	public  KMeansClustering(Matrix dataSet, int[] kIndexes)
	{
		this.k = kIndexes.length;
		this.dataSet = dataSet;
		this.kIndexes = kIndexes;
		this.numOfIterations = 0;
		
		
		
	}
	public List<Cluster> getClusters()
	{
		this.clusters = new ArrayList<Cluster>();
		// Assign initial cluster centeroids
		for(int i=0; i < this.k; i++)
		{
			int index = this.kIndexes[i];
			double[] centeroid =   Arrays.copyOfRange(this.dataSet.getRow(index), 0, this.dataSet.columnSize()-1);
			Cluster clust = new Cluster(centeroid);
			this.clusters.add(clust);
		}
		for(int i=0; i < this.dataSet.rowSize(); i++)
		{
			double[] testPattern = Arrays.copyOfRange(this.dataSet.getRow(i), 0, this.dataSet.columnSize()-1);
			
			Cluster targetCluster = getTargetCluster(testPattern);
			targetCluster.addPattern(i);
		}
		
		boolean loop = false;
		do
		{
			this.numOfIterations++;
			loop = false;
			
			List<Cluster> currentClusters = new ArrayList<Cluster>();
			for(int i=0; i < this.clusters.size(); i++)
				currentClusters.add(this.clusters.get(i).Clone());
			
			// Calculate the centroid for each clusters
			for(int i=0; i < this.clusters.size(); i++)
			{
				Cluster cluster = this.clusters.get(i);
				double[] centroid = calculateCentroid(cluster.getPatterns());
				cluster.clear();
				cluster.setCentroid(centroid);
			}
			
			for(int i=0; i < this.dataSet.rowSize(); i++)
			{
				double[] testPattern = Arrays.copyOfRange(this.dataSet.getRow(i), 0, this.dataSet.columnSize()-1);
				
				Cluster targetCluster = getTargetCluster(testPattern);
				targetCluster.addPattern(i);
			}
			
			// Test whether the cluster changed during the last iteration
			for(int i=0; i < this.clusters.size(); i++)
			{
				Cluster newCluster = this.clusters.get(i);
				Cluster previousCluster = currentClusters.get(i);
				if(!newCluster.isEqual(previousCluster))
				{
					loop = true;
					break;
				}
			}			
			//printDiganostic();
		} while(loop);
		
		return this.clusters;
	}
	private Cluster getTargetCluster(double[] testPattern)
	{
		double minDistance = Double.MAX_VALUE;
		int targetClusterIndex = 0;
			
		for(int k=0; k<this.k; k++)
		{
			Cluster cluster = this.clusters.get(k);					
			double[] centeroid =   cluster.getCentroid();
			
			double distance = EuclidianDistance.distance(testPattern, centeroid);
			if(distance < minDistance)
			{
				minDistance = distance;
				targetClusterIndex = k;
			}
		}
		return this.clusters.get(targetClusterIndex);
	}
	private double[] calculateCentroid(List<Integer> patternIndexes)
	{
		double []sumPattern = new double[this.dataSet.columnSize()-1];
		for(int i=0; i<sumPattern.length; i++)
		{
			sumPattern[i] = 0;
		}
		
		for(int i=0; i<patternIndexes.size(); i++)
		{
			int index = patternIndexes.get(i);
			double[] testPattern = Arrays.copyOfRange(this.dataSet.getRow(index), 0, this.dataSet.columnSize()-1);
			for(int j=0; j<sumPattern.length; j++)
			{
				sumPattern[j] += testPattern[j];
			}
		}
		
		int n = patternIndexes.size();
		
		for(int j=0; j<sumPattern.length; j++)
		{
			sumPattern[j] = sumPattern[j] /n;
		}
		
		return sumPattern;
	}
	public void printDiganostic()
	{
		System.out.println("---------------------------------------------");
		System.out.println("KMeansClustering: Summary");
		System.out.println("---------------------------------------------");
		double totalSquaredError = 0;
		for(int i=0; i < this.clusters.size(); i++)
		{
			Cluster cluster = this.clusters.get(i);
			List<Integer> patterns = cluster.getPatterns();
			System.out.println("Cluster[" + i + "]");
			System.out.println("Memebers=" + patterns.size());
			double squaredError =  getSquareError(cluster.getCentroid(),patterns);
			System.out.println("Sum-Squared-Error(SSE)=" + squaredError);
			totalSquaredError += squaredError;
		}
		System.out.print("Total Sum-Squared-Error(SSE)=" + totalSquaredError);
		System.out.println(this.numOfIterations);
		System.out.print("Iterations to build clusters=");
		System.out.println(this.numOfIterations);
		
		System.out.println("---------------------------------------------");
	
	}
	private double getSquareError(double[] centroid, List<Integer> patternIndexes)
	{
		double squareError = 0;
		
		for(int i=0; i<patternIndexes.size(); i++)
		{
			int index = patternIndexes.get(i);
			double[] testPattern = Arrays.copyOfRange(this.dataSet.getRow(index), 0, this.dataSet.columnSize()-1);
			double distance = EuclidianDistance.distance(testPattern, centroid);
			
			squareError += Math.pow(distance, 2);
		}
		return squareError;
	
	}
}
