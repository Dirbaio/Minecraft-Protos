package net.dirbaio.protos.generator;

public class RandomSeed
{
	private static int s = 34175;
	public static int get()
	{
		return s++;
	}
}
