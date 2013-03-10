package net.dirbaio.protos.functions;

public class RandomSeed
{
	private static int s = 34175;
	public static int get()
	{
		return s++;
	}
}
