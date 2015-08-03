package datamining.clustering;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private double[] centeroid;
	private List<Integer> patternIndexes;
	
	public Cluster(double[] centeroid)
	{
		this.centeroid = centeroid;
		this.patternIndexes = new ArrayList<Integer>();
	}
	public double[] getCentroid()
	{
		return this.centeroid;
	}
	public void setCentroid(double[] centroid)
	{
		this.centeroid = centroid;
	}
	
	public void addPattern(int patternIndex)
	{
		this.patternIndexes.add(patternIndex);
	}
	public List<Integer> getPatterns()
	{
		return this.patternIndexes;
	}
	public Cluster Clone()
	{
		Cluster clone = new Cluster(this.centeroid.clone());
		
		for(int i=0; i < this.patternIndexes.size(); i++)
		{
			int index = this.patternIndexes.get(i);
			clone.addPattern(index);
		}
		
		return clone;
	}
	public void clear()
	{
		this.centeroid = null;
		this.patternIndexes.clear();
	}
	public boolean isEqual(Cluster b)
	{
		if(this.patternIndexes.size() != b.getPatterns().size())
			return false;
		
		List<Integer> otherPatternIndexes = b.getPatterns();
		
		for(int i=0; i < this.patternIndexes.size(); i++)
		{
			int index = this.patternIndexes.get(i);
			
			boolean found = false;
			for(int j=0; j < otherPatternIndexes.size(); j++)
			{
				int otherIndex = otherPatternIndexes.get(j);
				if(otherIndex == index)
				{
					found  = true;
					break;
				}	
			}
			if(!found)
				return false;
		}
		return true;
	}
	
}
