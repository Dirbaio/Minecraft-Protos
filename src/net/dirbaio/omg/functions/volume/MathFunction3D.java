package net.dirbaio.omg.functions.volume;

import net.dirbaio.omg.functions.*;

public class MathFunction3D extends Function3D
{
	
	public static final int FUNC_ADD = 0;
	public static final int FUNC_SUB = 1;
	public static final int FUNC_MUL = 2;
	public static final int FUNC_DIV = 3;
	public static final int FUNC_MIN = 4;
	public static final int FUNC_MAX = 5;
	/*
	public static final int FUNC_ADD_CT = 10;
	public static final int FUNC_SUB_CT = 10;
	public static final int FUNC_RSB_CT = 10;
	public static final int FUNC_MUL_CT = 10;
	public static final int FUNC_DIV_CT = 10;
	public static final int FUNC_RDV_CT = 10;
	public static final int FUNC_MIN_CT = 10;
	public static final int FUNC_MAX_CT = 10;
	*/
	
	public Function3D a, b;
	public int func;

	public MathFunction3D()
	{
	}

	public MathFunction3D(Function3D a, Function3D b, int func)
	{
		this.a = a;
		this.b = b;
		this.func = func;
	}

	@Override
	public double[][][] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[][][] da = a.get3DData(px, py, pz, sx, sy, sz);
		double[][][] db = null;
		
		if(a == b)
			db = da;
		else
			db = b.get3DData(px, py, pz, sx, sy, sz);
		
		for(int i = 0; i < sx; i++)
			for(int j = 0; j < sy; j++)
				for(int k = 0; k < sz; k++)
				{
					double res = 0;
					double na = da[i][j][k];
					double nb = db[i][j][k];
					switch(func)
					{
						case FUNC_ADD: res = na+nb; break;
						case FUNC_SUB: res = na-nb; break;
						case FUNC_MUL: res = na*nb; break;
						case FUNC_DIV: res = na/nb; break;
						case FUNC_MIN: res = na<nb?na:nb; break;
						case FUNC_MAX: res = na>nb?na:nb; break;
					}
					da[i][j][k] = res;
				}
		return da;
	}
	
	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		a.prepare(x, z, sx, sz);
		b.prepare(x, z, sx, sz);
	}
}
