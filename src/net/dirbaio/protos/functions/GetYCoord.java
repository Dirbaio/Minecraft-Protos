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

public class GetYCoord extends Function3D
{

    public GetYCoord()
    {
    }

    @Override
    public double[] get3DData(int px, int py, int pz, int sx, int sy, int sz)
    {
        double[] res = new double[sx * sy * sz];

        int s = sx * sz;
        int i = 0;
        for (int y = 0; y < sy; y++)
        {
            double v = y + py;
            for(int j = 0; j < s; j++)
                res[i++] = v;
        }
        
        return res;
    }
}
