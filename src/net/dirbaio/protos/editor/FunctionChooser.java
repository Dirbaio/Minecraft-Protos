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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.dirbaio.protos.functions.FunctionList;

public class FunctionChooser extends JPanel
{
    ProjectEditor ed;
    ArrayList<FunctionButton> buttons = new ArrayList<>();
    
    public FunctionChooser(ProjectEditor ed)
    {
        this.ed = ed;
        setLayout(new GridLayout(FunctionList.functions.size(), 1, 5, 5));
        
        for(Class c : FunctionList.functions)
        {
            FunctionButton b = new FunctionButton(c);
            buttons.add(b);
            add(b);
        }
        setPreferredSize(new Dimension(200, getPreferredSize().height));
    }
       
    public class FunctionButton extends JButton implements ActionListener
    {
        Class c;

        public FunctionButton(Class c)
        {
            this.c = c;
            setIcon(FunctionEditor.getIconForClass(c));
            setText(FunctionEditor.unCamelCase(c.getSimpleName()));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ed.addFunction(c);
        }
        
    }
}
