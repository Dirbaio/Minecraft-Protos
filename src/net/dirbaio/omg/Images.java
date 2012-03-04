
package net.dirbaio.omg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Images
{
    public static ImageIcon neww, open, save, gen, delete, settings, prev, prevstop;

    static
    {
        init();
    }
    
    public static void init()
    {
        neww = createIcon("new.gif");
        open = createIcon("open.png");
        save = createIcon("save.png");
        gen = createIcon("gen.png");
        delete = createIcon("delete.png");
        settings = createIcon("settings.png");
        prev = createIcon("preview.png");
        prevstop = createIcon("stop.png");
    }
    /*
    private static ImageIcon createAdd(ImageIcon icon )
    {
        if(icon == null) return add;
        Image i = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = i.getGraphics();
        g.drawImage(icon.getImage(), 0, 0, null);
        g.drawImage(add.getImage(), 0, 0, null);
        return new ImageIcon(i);
    }*/

    private static ImageIcon createIcon(String path) 
    {
        java.net.URL imgURL = Images.class.getResource("/icons/"+path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


}