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

public class SimplexNoise2D extends Function2D
{

	public double xScale;
	public double zScale;
	public double min;
	public double max;
	private static int perm[] = new int[512];
	
    private static int grad3[][] = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
        {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
        {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};

    public SimplexNoise2D()
    {
    }
	
	public SimplexNoise2D(double xscale, double zscale, double min, double max)
	{
		this.xScale = xscale;
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

	@Override
	public double[][] get2DData(int px, int pz, int sx, int sz)
	{
		double[][] res = new double[sx][sz];

		for (int x = 0; x < sx; x++)
			for (int z = 0; z < sz; z++)
			{
				double xin = (x + px) / xScale;
				double yin = (z + pz) / zScale;
				
				double n0, n1, n2; // Noise contributions from the three corners
				// Skew the input space to determine which simplex cell we're in
				final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
				double s = (xin + yin) * F2; // Hairy factor for 2D
				int i = fastfloor(xin + s);
				int j = fastfloor(yin + s);
				final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
				double t = (i + j) * G2;
				double X0 = i - t; // Unskew the cell origin back to (x,y) space
				double Y0 = j - t;
				double x0 = xin - X0; // The x,y distances from the cell origin
				double y0 = yin - Y0;
				// For the 2D case, the simplex shape is an equilateral triangle.
				// Determine which simplex we are in.
				int i1, j1; // Offsets for second (middle) corner of simplex in (i,j) coords
				if (x0 > y0)
				{
					i1 = 1;
					j1 = 0;
				} // lower triangle, XY order: (0,0)->(1,0)->(1,1)
				else
				{
					i1 = 0;
					j1 = 1;
				} // upper triangle, YX order: (0,0)->(0,1)->(1,1)
				// A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
				// a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
				// c = (3-sqrt(3))/6
				double x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords
				double y1 = y0 - j1 + G2;
				double x2 = x0 - 1.0 + 2.0 * G2; // Offsets for last corner in (x,y) unskewed coords
				double y2 = y0 - 1.0 + 2.0 * G2;
				// Work out the hashed gradient indices of the three simplex corners
				int ii = i & 255;
				int jj = j & 255;
				int gi0 = perm[ii + perm[jj]] % 12;
				int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
				int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;
				// Calculate the contribution from the three corners
				double t0 = 0.5 - x0 * x0 - y0 * y0;
				if (t0 < 0)
				{
					n0 = 0.0;
				}
				else
				{
					t0 *= t0;
					n0 = t0 * t0 * dot(grad3[gi0], x0, y0); // (x,y) of grad3 used for 2D gradient
				}
				double t1 = 0.5 - x1 * x1 - y1 * y1;
				if (t1 < 0)
				{
					n1 = 0.0;
				}
				else
				{
					t1 *= t1;
					n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
				}
				double t2 = 0.5 - x2 * x2 - y2 * y2;
				if (t2 < 0)
				{
					n2 = 0.0;
				}
				else
				{
					t2 *= t2;
					n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
				}
				// Add contributions from each corner to get the final noise value.
				// The result is scaled to return values in the interval [-1,1].

				double val = 35.0 * (n0 + n1 + n2) + 0.5;
				res[x][z] = min + val * (max - min);
			}
		
		return res;
	}
}
