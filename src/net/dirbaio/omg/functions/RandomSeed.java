package net.dirbaio.omg.functions;

public class RandomSeed
{
	private static int s = 0;
	public static int get()
	{
		return s++;
	}
}
