/*
 * FractalImage.java
 *
 * Version 1.1, updated 6. August 2007
 *
 *
 *   YaFGen - Yet another Fractal Generator - Generate images based on mathematical formulas
 *   Copyright (C) 2007  Roland Gršpmair
 *
 *   This file is part of YaFGen.
 *
 *   YaFGen is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   YaFGen is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with YaFGen; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *   To contact the author, please send an email to the following address: rgropmair "at" gmail.com
 *
 *   Version history:
 *      1.0, created on 11. April 2007, 19:34
 *           - first release
 *
 *      1.1, updated 6. August 2007
 *           - Various enhancements, see README
 *
 */

package yafgen;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

/**
 *
 * FractalImage represents the graphicsl representation of a fractal on the screen.
 * It provides the management of the worker thread, mouse interaction (zooming),
 * painting/repainting and calculating a new color
 *
 */
public abstract class FractalImage extends JPanel {
    
    protected FractalParameters fPars;
    
    protected int sizeX_old, sizeY_old;
    
    /** reference to the main application */
    protected YaFGenMainFrame mainFrame;
    
    /** we paint into this buffered image, and display that one periodically */
    protected BufferedImage bufferedImage;
    protected Graphics2D graphics;
    
    protected boolean finishedDrawing = false;
    protected boolean repaintFlag = true;
    protected SwingWorker worker;
    
    /**
     * Creates a new instance of FractalImage
     */
    public FractalImage(YaFGenMainFrame myFrame, FractalParameters myFPars) {
        mainFrame = myFrame;
        
        fPars = myFPars;
        
        bufferedImage = new BufferedImage(fPars.sizeX, fPars.sizeY, BufferedImage.TYPE_USHORT_555_RGB);
        graphics = bufferedImage.createGraphics();
    }
    
    /** abstract method that does the calculation of the fractal */
    public abstract Object doWork();
    
    public void setMouseDraggedRect( Point pointMouseDraggedStart, Point pointMouseDraggedEnd, Point oldPointMouse ) {
        graphics.setXORMode(Color.BLUE);
        
        // clear old rectangle
        if( oldPointMouse != null ) {
            int x1 = (int)pointMouseDraggedStart.getX();
            int y1 = (int)pointMouseDraggedStart.getY();
            int x2 = (int)oldPointMouse.getX();
            int y2 = (int)oldPointMouse.getY();
            
            graphics.drawLine( x1, y1, x2, y1 );
            graphics.drawLine( x2, y1, x2, y2 );
            graphics.drawLine( x2, y2, x1, y2 );
            graphics.drawLine( x1, y2, x1, y1 );
            
        }
        int x1 = (int)pointMouseDraggedStart.getX();
        int y1 = (int)pointMouseDraggedStart.getY();
        int x2 = (int)pointMouseDraggedEnd.getX();
        int y2 = (int)pointMouseDraggedEnd.getY();
        
        graphics.drawLine( x1, y1, x2, y1 );
        graphics.drawLine( x2, y1, x2, y2 );
        graphics.drawLine( x2, y2, x1, y2 );
        graphics.drawLine( x1, y2, x1, y1 );
        
        graphics.setPaintMode();
    }
    
    public void repaint( boolean paint ) {
        repaintFlag = true;
        finishedDrawing = false;
        repaint();
    }
    
    
    public void paintComponent(Graphics comp) {
        super.paintComponent(comp);
        
        // System.nanoTime only in Java 1.5
        //  long deltaSeconds, initSeconds = System.nanoTime();
        sizeX_old = fPars.sizeX; sizeY_old = fPars.sizeY;
        
        if( repaintFlag == true ) {
            if( worker != null ) {
                System.out.println("repaintFlag == true, let's interrupt worker");
                worker.interrupt();
            }
            
            // create new buffer for new worker
            bufferedImage = new BufferedImage(fPars.sizeX, fPars.sizeY, BufferedImage.TYPE_USHORT_555_RGB);
            graphics = bufferedImage.createGraphics();
            
            // create new worker ...
            worker = new SwingWorker() {
                public Object construct() {
                    return doWork();
                }
                
                public void finished() {
                    System.out.println("worker finished");
                }
            };
            
            // ... and start thread
            worker.start();
            
            // remember not to start a new worker again; only if button "Repaint" pressed!
            repaintFlag = false;
        }
        
        //deltaSeconds = System.nanoTime() - initSeconds;
        //System.out.println("doWork() done, seconds" + deltaSeconds/1000000);
        
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(Color.BLACK);
        
        comp2D.drawImage(bufferedImage, null, 0, 0);
    }
    
    public void interruptWorker() {
        if(worker != null)
            worker.interrupt();
    }
    
    public Dimension getPreferredSize() {
        return(new Dimension(fPars.sizeX, fPars.sizeY));
    }
    
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
    
    boolean getFinishedDrawing() {
        return finishedDrawing;
    }
    
    protected Color calcNewColorIterations(final int iterations) {
        Color c;
        
        // find out color value by some weird algo
        switch( fPars.getSelectedColorSet() ) {
            case 1:
                if( iterations != fPars.maxIterations ) {
                    float floatIterations = (float)iterations/(float)fPars.maxIterations;
                    c = Color.getHSBColor( floatIterations, 1.0F, 1.0F );
                } else
                    c = Color.BLACK;
                break;
                
            case 2:
                if( iterations != fPars.maxIterations ) {
                    float floatIterations = (float)iterations/(float)fPars.maxIterations + 0.8F;
                    c = Color.getHSBColor( floatIterations, 1.0F, 1.0F );
                } else
                    c = Color.BLACK;
                break;
                
            case 3:
                if( iterations != fPars.maxIterations ) {
                    float floatIterations = (float)iterations/(float)fPars.maxIterations *5.2F;
                    c = Color.getHSBColor( floatIterations, 0.4F, 1.0F );
                } else
                    c = Color.BLACK;
                break;
                
            case 4:
                if( iterations != fPars.maxIterations ) {
                    float floatIterations = (float)iterations/(float)fPars.maxIterations *10.0F;
                    c = Color.getHSBColor( floatIterations, 0.8F, 1.0F );
                } else
                    c = Color.BLACK;
                break;
                
            default:
                if( iterations != fPars.maxIterations ) {
                    float floatIterations = (float)iterations/(float)fPars.maxIterations;
                    c = Color.getHSBColor( floatIterations, 1.0F, 1.0F );
                } else
                    c = Color.BLACK;
                break;
        }
        
        return c;
    }
    
    protected int calcNewColorPixel( final int oldCol ) {
        int redCol, greenCol, blueCol;
        
        // find out the red/green/blue components
        redCol   = new Color(oldCol).getRed();
        greenCol = new Color(oldCol).getGreen();
        blueCol  = new Color(oldCol).getBlue();
        
        // now manipulate the rgb components/colors for the new pixel
        //redCol = (redCol+10)%256;
        //greenCol = (greenCol+20)%256;
        
        switch( fPars.getSelectedColorSet() ) {
            case 1:
                redCol = (redCol+1)%256;
                greenCol = (greenCol+15)%256;
                blueCol = (blueCol+30)%256;
                break;
                
            case 2:
                redCol = (redCol+20)%256;
                greenCol = (greenCol+15)%256;
                blueCol = (blueCol+10)%256;
                break;
                
            case 3:
                redCol = (redCol+20)%256;
                greenCol = (greenCol+1)%256;
                blueCol = (blueCol+30)%256;
                break;
                
            case 4:
                redCol = (redCol+1)%256;
                greenCol = (greenCol+1)%256;
                blueCol = (blueCol+15)%256;
                break;
                
            default:
                redCol = (redCol+1)%256;
                greenCol = (greenCol+15)%256;
                blueCol = (blueCol+30)%256;
                
        }
        
        return( new Color(redCol, greenCol, blueCol).getRGB() );
        
    }
}