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
		checkArgument(rowCount > 0 && columnCount > 0, "Invalid matrix size");
		
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
	
	public DoubleStream column(int columnIndex)
	{
		double[] column = new double[rowCount];
		
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
		{
			column[rowIndex] = values[rowIndex][columnIndex];
		}
		
		return Arrays.stream(column);
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
	
	public Matrix column(int columnIndex, double... values)
	{
		Matrix result = new Matrix(this);
		
		for (int rowIndex = 0; rowIndex < values.length; rowIndex++)
		{
			result.values[rowIndex][columnIndex] = values[rowIndex];
		}
		
		return result;
	}
	
	public Matrix subtract(Matrix that)
	{
		checkArgument(rowCount == that.rowCount && columnCount == that.columnCount, "Invalid matrix size");
		
		Matrix result = new Matrix(this);
		
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
		{
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
			{
				result.values[rowIndex][columnIndex] -= that.values[rowIndex][columnIndex];
			}
		}
		
		return result;
	}
	
	public Matrix multiply(Matrix that)
	{
		checkArgument(columnCount == that.rowCount, "Invalid matrix size");
		
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
	
	/**
	 * Element-wise multiplication.
	 */
	public Matrix times(Matrix that)
	{
		checkArgument(rowCount == that.rowCount && columnCount == that.columnCount, "Invalid matrix size");
		
		Matrix result = new Matrix(this);
		
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
		{
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
			{
				result.values[rowIndex][columnIndex] *= that.values[rowIndex][columnIndex];
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
	
	public Matrix scale(double scalar)
	{
		return map(value -> value * scalar);
	}
	
	/**
	 * @see <a href="https://en.wikipedia.org/wiki/Sigmoid_function">Sigmoid function</a>
	 */
	public Matrix scaleSigmoid()
	{
		return map(x -> 1 / (1 + Math.exp(-x)));
	}
	
	/**
	 * Sigmoid differential.
	 */
	public Matrix scaleSigmoidPrime()
	{
		return map(x -> Math.exp(-x) / Math.pow(1 + Math.exp(-x), 2));
	}
	
	public Matrix square()
	{
		return map(x -> x * x);
	}
	
	public Matrix transpose()
	{
		Matrix result = new Matrix(columnCount, rowCount);
		
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
		{
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
			{
				result.values[columnIndex][rowIndex] = values[rowIndex][columnIndex];
			}
		}
		
		return result;
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
	
	private static void checkArgument(boolean condition, String message)
	{
		if (!condition)
		{
			throw new IllegalArgumentException(message);
		}
	}
}
