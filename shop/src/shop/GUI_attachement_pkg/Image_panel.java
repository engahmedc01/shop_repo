/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.GUI_attachement_pkg;

/**
 *
 * @author ahmed
 */
//import GUI_pkg.GATE;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Image_panel extends JPanel {
static JFrame window;
static JPanel mainPanel;

BufferedImage originalImage;
BufferedImage scaledImage;
public InputStream image;
 BufferedImage readImage(InputStream path)
    {BufferedImage myimage=null;
      
    try {
        myimage = ImageIO.read(path);
    } catch (IOException ex) {
        Logger.getLogger(Image_panel.class.getName()).log(Level.SEVERE, null, ex);
    }
       
    return myimage;
    }
public Image_panel(InputStream image) {
    //setPreferredSize(new Dimension(700, 200));
    this.image=image;
    try {
        originalImage = ImageIO.read(image);
    } catch(Exception e){}

    addComponentListener(new ResizerListener());
}

public void resize() {
    double widthScaleFactor = getWidth() / (double)originalImage.getWidth();
    double heightScaleFactor =getHeight() / (double)originalImage.getHeight();
    double scaleFactor =(widthScaleFactor < heightScaleFactor)? heightScaleFactor : widthScaleFactor;

    AffineTransform at = new AffineTransform();
    at.scale(scaleFactor, scaleFactor);

    AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    scaledImage = scaleOp.filter(originalImage, null);

    repaint();
}

@Override
public void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.drawImage(scaledImage, 0, 0, null);
}

//public static void main(String[] args) {
//    javax.swing.SwingUtilities.invokeLater(new Runnable() {
//        @Override
//        public void run(){
//            window = new JFrame("Scale Test");
//           // window.setLayout(new BorderLayout());
//            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//            mainPanel = new ScaleTest();
//            mainPanel.setBorder( BorderFactory.createTitledBorder("A"));
//          //  window.getContentPane().add(mainPanel,BorderLayout.CENTER);
//            window.getContentPane().add(mainPanel);
//
//            window.pack();
//            window.setLocationRelativeTo(null);  // positions window in center of screen
//            window.setVisible(true);
//        }
//    });
//}

class ResizerListener implements ComponentListener {
    @Override
    public void componentResized(ComponentEvent e) {
        resize();
    }

    @Override public void componentHidden(ComponentEvent e) {}
    @Override public void componentMoved(ComponentEvent e) {}
    @Override public void componentShown(ComponentEvent e) {}
}
}