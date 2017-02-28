package neural_network;

import java.util.Arrays;
import java.util.Random;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Matrix
{
	private final int rows;
	
	private final int columns;
	
	private final double[][] values;
	
	public Matrix(int rows, int columns)
	{
		if (rows < 1 || columns < 1)
		{
			throw new IllegalArgumentException("Invalid size");
		}
		
		this.rows = rows;
		this.columns = columns;
		values = new double[rows][columns];
	}
	
	public Matrix(Matrix that)
	{
		rows = that.rows;
		columns = that.columns;
		values = new double[rows][columns];
		
		for (int row = 0; row < rows; row++)
		{
			System.arraycopy(that.values[row], 0, values[row], 0, columns);
		}
	}
	
	public Matrix fill(DoubleSupplier supplier)
	{
		Matrix result = new Matrix(rows, columns);
		
		for (double[] row : result.values)
		{
			double[] newRow = DoubleStream.generate(supplier)
				.limit(columns)
				.toArray();
			
			System.arraycopy(newRow, 0, row, 0, columns);
		}
		
		return result;
	}
	
	public Matrix fillGaussian(Random random)
	{
		return fill(random::nextGaussian);
	}
	
	public Matrix row(int row, double... values)
	{
		Matrix result = new Matrix(this);
		
		System.arraycopy(values, 0, result.values[row], 0, values.length);
		
		return result;
	}
	
	public Matrix multiply(Matrix that)
	{
		if (columns != that.rows)
		{
			throw new IllegalArgumentException("Cannot multiply matrices");
		}
		
		Matrix result = new Matrix(rows, that.columns);
		
		for (int row = 0; row < rows; row++)
		{
			for (int column = 0; column < that.columns; column++)
			{
				double value = 0;
				
				for (int index = 0; index < columns; index++)
				{
					value += values[row][index] * that.values[index][column];
				}
				
				result.values[row][column] = value;
			}
		}
		
		return result;
	}
	
	public Matrix scale(DoubleUnaryOperator function)
	{
		Matrix result = new Matrix(this);
		
		for (double[] row : result.values)
		{
			for (int column = 0; column < columns; column++)
			{
				row[column] = function.applyAsDouble(row[column]);
			}
		}
		
		return result;
	}
	
	/**
	 * @see <a href="https://en.wikipedia.org/wiki/Sigmoid_function">Sigmoid function</a>
	 */
	public Matrix scaleSigmoid()
	{
		return scale(t -> 1 / (1 + Math.exp(-t)));
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
