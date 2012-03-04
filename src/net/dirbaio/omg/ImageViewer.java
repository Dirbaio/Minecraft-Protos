package net.dirbaio.omg;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class ImageViewer extends JFrame
{
    Image ii;

    public ImageViewer(Image i)
    {
        super("MCMap");
        ii = i;
        setSize(800, 500);
        add(new JScrollPane(new ImagePreviewerControl()), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class ImagePreviewerControl extends JComponent
    {
        public ImagePreviewerControl()
        {
            setSize(ii.getWidth(null), ii.getHeight(null));
            setPreferredSize(new Dimension(ii.getWidth(null), ii.getHeight(null)));
        }

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            g.drawImage(ii, 0, 0, null);
        }


    }

}