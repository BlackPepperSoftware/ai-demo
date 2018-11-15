package uk.co.blackpepper.neuroevolution;

final class Maths
{
	private static final double DOUBLE_EQUALS_DELTA = 0.000001;
	
	private Maths()
	{
		throw new AssertionError();
	}
	
	public static boolean equals(double x, double y)
	{
		return Math.abs(x - y) < DOUBLE_EQUALS_DELTA;
	}
}
