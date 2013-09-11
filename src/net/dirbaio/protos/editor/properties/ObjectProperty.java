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
import javax.swing.JComponent;
import javax.swing.JLabel;


public class ObjectProperty extends Property
{

    public ObjectProperty(Object obj, Field field, int index, PropertyEditor ed)
    {
        super(obj, field, index, ed);
    }

    @Override
    public JComponent createComponent()
    {
        Object o = getValue();
        String str = "null";
        if(o != null) str = o.toString();
        return new JLabel(str);
    }
    
}
