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

public class Max3D extends Function3D
{

    public Function3D a, b;

    public Max3D()
    {
    }

    public Max3D(Function3D a, Function3D b)
    {
        this.a = a;
        this.b = b;
    }

	@Override
	public double[] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
        double[] da = a.get3DData(px, py, pz, sx, sy, sz);
        double[] db;

        if (a == b)
            db = da;
        else
            db = b.get3DData(px, py, pz, sx, sy, sz);

        int s = sx*sy*sz;
		for(int i = 0; i < s; i++)
            da[i] = da[i] > db[i] ? da[i] : db[i];
        
        return da;
	}

    @Override
    public void prepare(int x, int z, int sx, int sz)
    {
        a.prepare(x, z, sx, sz);
        b.prepare(x, z, sx, sz);
    }
}
