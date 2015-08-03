package datamining.base;

public class EuclidianDistance {
	
	public static double distance(double[] p, double[] q) throws IllegalArgumentException
	{
		if(p.length != q.length)
			throw new IllegalArgumentException("Inequal dimensions");
		
		int dimension = p.length;
		double distance = Double.MAX_VALUE;
		
		double squaredSum = 0;
		for(int i=0; i<dimension; i++ )
			squaredSum += Math.pow(p[i] - q[i], 2);
		
		distance =  Math.sqrt(squaredSum); 	
				
		return distance;
	}
}
