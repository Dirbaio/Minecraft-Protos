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

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class EditorWindow extends JPanel
{
    Project project;
    ProjectEditor projectEditor;
    FunctionChooser fc;
    
    public EditorWindow(Project p)
    {
        super(new BorderLayout());
        this.project = p;
		projectEditor = new ProjectEditor(p);
		fc = new FunctionChooser(projectEditor);

        add(projectEditor, BorderLayout.CENTER);
        add(new JScrollPane(fc), BorderLayout.WEST);
    }

}

