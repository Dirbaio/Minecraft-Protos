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

class ArrayCache
{
    //TODO Improve this shit so it actually caches.
    
    static int[] newInt(int size)
    {
        return new int[size];
    }

    static double[] newDouble(int size)
    {
        return new double[size];
    }

    static short[] newShort(int size)
    {
        return new short[size];
    }
}
