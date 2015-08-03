package datamining.base;

import java.util.List;

public class Matrix {
	private int M;
	private int N;
	private double[][] data;
	
	public Matrix(int row, int column)
	{
		M = row;
		N = column;
		data = new double[M][N];
	}
	public Matrix(double data[][])
	{
		if(data == null)
			return;
		this.M = data.length;
		this.N = data[0].length;
		this.data = new double[M][N];
		for(int i=0; i < this.M; i++)
			for(int j=0; j < this.N;j++)
				this.data[i][j] = data[i][j];
	}
	
	public void add(int row, int column, double data) throws IllegalArgumentException
	{
		if( row > this.rowSize() || column > this.columnSize())
			throw new IllegalArgumentException("row or column");
		
		this.data[row][column] = data;
	}
	public void addRow(int row,  double[] columnData) throws IllegalArgumentException
	{
		if( row > this.rowSize() || columnData.length > this.columnSize())
			throw new IllegalArgumentException("row or column");
		
		for(int j=0; j < this.columnSize(); j++)
			data[row][j] = columnData[j];

	}
	
	public double get(int row, int column) throws IllegalArgumentException
	{
		if( row > this.rowSize() || column > this.columnSize())
			throw new IllegalArgumentException("row or column");
		
		return this.data[row][column];
	}
	public double[] getRow(int rowIndex) throws IllegalArgumentException
	{
		if( rowIndex > this.rowSize())
			throw new IllegalArgumentException("row or column");
		
		double [] rowData = new double[this.columnSize()];
		for(int i=0; i < this.columnSize(); i++)
			rowData[i] = this.data[rowIndex][i];
		
	
		return rowData;
		
	}
	public Matrix getMatrix(List<Integer> rowIndexes)
	{
		int rows = rowIndexes.size();
		int columns = this.columnSize();
		
		Matrix output = new Matrix(rows, columns);
		
		for(int i = 0; i < rows; i++)
		{
			int row = rowIndexes.get(i);
			output.addRow(i, getRow(row));		
		}	
		return output;
	}
	public int rowSize()
	{
		return M;
	}
	public int columnSize()
	{
		return N;
	}
}
