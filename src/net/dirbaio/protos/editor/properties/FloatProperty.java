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

package net.dirbaio.protos.editor.properties;

import java.lang.reflect.Field;

public class FloatProperty extends TextProperty
{

    public FloatProperty(Object obj, Field field, int index, PropertyEditor ed)
    {
        super(obj, field, index, ed);
    }
    
    @Override
    protected boolean parseString(String s)
    {
        try
        {
            setValue(Float.parseFloat(s));
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }
}
