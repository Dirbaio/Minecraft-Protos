/*
 * Copyright (C) 2013 dirbaio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.dirbaio.protos.functions;

import java.util.Random;

public class SimplexNoise3D extends Function3D
{

	public double xScale = 10;
	public double yScale = 10;
	public double zScale = 10;
	public double min = -1;
	public double max = 1;
	private static int perm[] = new int[512];
    private static int grad3[][] = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
        {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
        {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};

    public SimplexNoise3D()
    {
    }

	public SimplexNoise3D(double xscale, double yscale, double zscale, double min, double max)
	{
		this.xScale = xscale;
		this.yScale = yscale;
		this.zScale = zscale;
		this.min = min;
		this.max = max;
	}

    @Override
    public void setRandomSeed(long seed)
    {
		Random r = new Random(seed);
		for (int i = 0; i < 256; i++)
			perm[i] = i;

		for (int i = 0; i < 256; i++)
		{
			int j = r.nextInt(256);
			//Swap perm[i] and perm[j]

			int aux = perm[i];
			perm[i] = perm[j];
			perm[j] = aux;
		}
        System.arraycopy(perm, 0, perm, 256, 256);
	}

// This method is a *lot* faster than using (int)Math.floor(x)
	private static int fastfloor(double x)
	{
		return x > 0 ? (int) x : (int) x - 1;
	}

	private static double dot(int g[], double x, double y)
	{
		return g[0] * x + g[1] * y;
	}

	private static double dot(int g[], double x, double y, double z)
	{
		return g[0] * x + g[1] * y + g[2] * z;
	}

	@Override
	public double[] get3DData(int px, int py, int pz, int sx, int sy, int sz)
	{
		double[] res = new double[sx*sy*sz];
        int ind = 0;
        
        for (int y = 0; y < sy; y++)
            for (int x = 0; x < sx; x++)
				for (int z = 0; z < sz; z++)
				{
					double xin = (x + px) / xScale;
					double yin = (y + py) / yScale;
					double zin = (z + pz) / zScale;
					double n0, n1, n2, n3; // Noise contributions from the four corners
// Skew the input space to determine which simplex cell we're in
					final double F3 = 1.0 / 3.0;
					double s = (xin + yin + zin) * F3; // Very nice and simple skew factor for 3D
					int i = fastfloor(xin + s);
					int j = fastfloor(yin + s);
					int k = fastfloor(zin + s);
					final double G3 = 1.0 / 6.0; // Very nice and simple unskew factor, too
					double t = (i + j + k) * G3;
					double X0 = i - t; // Unskew the cell origin back to (x,y,z) space
					double Y0 = j - t;
					double Z0 = k - t;
					double x0 = xin - X0; // The x,y,z distances from the cell origin
					double y0 = yin - Y0;
					double z0 = zin - Z0;
// For the 3D case, the simplex shape is a slightly irregular tetrahedron.
// Determine which simplex we are in.
					int i1, j1, k1; // Offsets for second corner of simplex in (i,j,k) coords
					int i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords
					if (x0 >= y0)
					{
						if (y0 >= z0)
						{
							i1 = 1;
							j1 = 0;
							k1 = 0;
							i2 = 1;
							j2 = 1;
							k2 = 0;
						} // X Y Z order
						else if (x0 >= z0)
						{
							i1 = 1;
							j1 = 0;
							k1 = 0;
							i2 = 1;
							j2 = 0;
							k2 = 1;
						} // X Z Y order
						else
						{
							i1 = 0;
							j1 = 0;
							k1 = 1;
							i2 = 1;
							j2 = 0;
							k2 = 1;
						} // Z X Y order
					}
					else
					{ // x0<y0
						if (y0 < z0)
						{
							i1 = 0;
							j1 = 0;
							k1 = 1;
							i2 = 0;
							j2 = 1;
							k2 = 1;
						} // Z Y X order
						else if (x0 < z0)
						{
							i1 = 0;
							j1 = 1;
							k1 = 0;
							i2 = 0;
							j2 = 1;
							k2 = 1;
						} // Y Z X order
						else
						{
							i1 = 0;
							j1 = 1;
							k1 = 0;
							i2 = 1;
							j2 = 1;
							k2 = 0;
						} // Y X Z order
					}
// A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
// a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
// a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
// c = 1/6.
					double x1 = x0 - i1 + G3; // Offsets for second corner in (x,y,z) coords
					double y1 = y0 - j1 + G3;
					double z1 = z0 - k1 + G3;
					double x2 = x0 - i2 + 2.0 * G3; // Offsets for third corner in (x,y,z) coords
					double y2 = y0 - j2 + 2.0 * G3;
					double z2 = z0 - k2 + 2.0 * G3;
					double x3 = x0 - 1.0 + 3.0 * G3; // Offsets for last corner in (x,y,z) coords
					double y3 = y0 - 1.0 + 3.0 * G3;
					double z3 = z0 - 1.0 + 3.0 * G3;
// Work out the hashed gradient indices of the four simplex corners
					int ii = i & 255;
					int jj = j & 255;
					int kk = k & 255;
					int gi0 = perm[ii + perm[jj + perm[kk]]] % 12;
					int gi1 = perm[ii + i1 + perm[jj + j1 + perm[kk + k1]]] % 12;
					int gi2 = perm[ii + i2 + perm[jj + j2 + perm[kk + k2]]] % 12;
					int gi3 = perm[ii + 1 + perm[jj + 1 + perm[kk + 1]]] % 12;
// Calculate the contribution from the four corners
					double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
					if (t0 < 0)
					{
						n0 = 0.0;
					}
					else
					{
						t0 *= t0;
						n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0);
					}
					double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
					if (t1 < 0)
					{
						n1 = 0.0;
					}
					else
					{
						t1 *= t1;
						n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1);
					}
					double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
					if (t2 < 0)
					{
						n2 = 0.0;
					}
					else
					{
						t2 *= t2;
						n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2);
					}
					double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
					if (t3 < 0)
					{
						n3 = 0.0;
					}
					else
					{
						t3 *= t3;
						n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3);
					}
// Add contributions from each corner to get the final noise value.
// The result is scaled to stay just inside [-1,1]

					double val = 16.0 * (n0 + n1 + n2 + n3) + 0.5;
					res[ind++] = min + val * (max - min);
				}

		return res;
	}
}
