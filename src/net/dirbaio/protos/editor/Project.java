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

package net.dirbaio.protos.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.dirbaio.protos.functions.Function;
import net.dirbaio.protos.functions.Output;

public class Project
{
    public List<Function> funcs = new ArrayList<>();

    public Project()
    {
        addFunc(new Output());
    }
    
    
    private void addFunc(Function f)
    {
        funcs.add(f);
        Field[] fields = f.getClass().getFields();
        for(Field fi : fields)
        {
            if(Function.class.isAssignableFrom(fi.getType()))
            {
                Function f2 = null;
                try
                {
                    f2 = (Function) fi.get(f);
                } catch (IllegalArgumentException | IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
                
                if(f2 != null)
                    addFunc(f2);
            }
        }
    }
    
    public Output getOutput()
    {
        for(Function f : funcs)
            if(f instanceof Output)
                return (Output) f;

        return null;
    }
}
