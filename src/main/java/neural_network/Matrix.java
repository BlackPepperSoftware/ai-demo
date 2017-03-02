package neural_network;

import java.util.Arrays;
import java.util.Random;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Matrix
{
	private final int rowCount;
	
	private final int columnCount;
	
	private final double[][] values;
	
	public Matrix(int rowCount, int columnCount)
	{
		if (rowCount < 1 || columnCount < 1)
		{
			throw new IllegalArgumentException("Invalid size");
		}
		
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		values = new double[rowCount][columnCount];
	}
	
	public Matrix(Matrix that)
	{
		rowCount = that.rowCount;
		columnCount = that.columnCount;
		values = new double[rowCount][columnCount];
		
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
		{
			System.arraycopy(that.values[rowIndex], 0, values[rowIndex], 0, columnCount);
		}
	}
	
	public Matrix fill(DoubleSupplier supplier)
	{
		Matrix result = new Matrix(rowCount, columnCount);
		
		for (double[] row : result.values)
		{
			double[] newRow = DoubleStream.generate(supplier)
				.limit(columnCount)
				.toArray();
			
			System.arraycopy(newRow, 0, row, 0, columnCount);
		}
		
		return result;
	}
	
	public Matrix fillGaussian(Random random)
	{
		return fill(random::nextGaussian);
	}
	
	public Matrix row(int rowIndex, double... values)
	{
		Matrix result = new Matrix(this);
		
		System.arraycopy(values, 0, result.values[rowIndex], 0, values.length);
		
		return result;
	}
	
	public Matrix multiply(Matrix that)
	{
		if (columnCount != that.rowCount)
		{
			throw new IllegalArgumentException("Cannot multiply matrices");
		}
		
		Matrix result = new Matrix(rowCount, that.columnCount);
		
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
		{
			for (int columnIndex = 0; columnIndex < that.columnCount; columnIndex++)
			{
				double value = 0;
				
				for (int index = 0; index < columnCount; index++)
				{
					value += values[rowIndex][index] * that.values[index][columnIndex];
				}
				
				result.values[rowIndex][columnIndex] = value;
			}
		}
		
		return result;
	}
	
	public Matrix map(DoubleUnaryOperator function)
	{
		Matrix result = new Matrix(this);
		
		for (double[] row : result.values)
		{
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
			{
				row[columnIndex] = function.applyAsDouble(row[columnIndex]);
			}
		}
		
		return result;
	}
	
	/**
	 * @see <a href="https://en.wikipedia.org/wiki/Sigmoid_function">Sigmoid function</a>
	 */
	public Matrix scaleSigmoid()
	{
		return map(t -> 1 / (1 + Math.exp(-t)));
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for (double[] row : values)
		{
			builder.append(Arrays.stream(row)
				.mapToObj(value -> String.format("%f", value))
				.collect(Collectors.joining(", ", "[", "]\n"))
			);
		}
		
		return builder.toString();
	}
}
