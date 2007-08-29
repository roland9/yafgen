/*
 * FractalIteratedFunction.java
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
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 *
 * FractalIterationFunction is the base class for fractals that represent
 * a graphical image of iterations. Each point x/y is drawn, and then the
 * iteration is executed again.
 * If a pixel has already been painted on the screen, the color changes.
 *
 * It is an abstract class, so that the calculation/iteration itself is implemented
 * in a subclass.
 *
 */
public abstract class FractalIterationFunction extends FractalImage {
    
    double xRange, yRange;
    double xIter, yIter;
    int xPixel = 0, yPixel = 0;
    double p, flaeche, q[];
    double xNew, yNew;
    private YaFGenMainFrame myFrame;
    
    /** Creates a new instance of FractalIterationFunction */
    public FractalIterationFunction(YaFGenMainFrame myFrame, FractalParameters myFPars) {
        super(myFrame, myFPars);
        this.myFrame = myFrame;
    }
    
    public Object doWork() {
        
        System.out.println( this.getClass() + "doWork() started");
        myFrame.setCalculatingLabel( true );
        
        Color c = Color.black;
        finishedDrawing = false;
        
        // for IFS only: determine the area of the function -> find out 'weight'
        q = new double[6]; p = 0.0D;
        
        for( int i=0; i<4; i++ ) {
            flaeche = Math.abs(new Double(fPars.a.get(i).toString()) *
                    new Double(fPars.d.get(i).toString()) -
                    new Double(fPars.b.get(i).toString()) *
                    new Double(fPars.c.get(i).toString()) );
            p += ((flaeche > 0.0D) ? flaeche : 0.01D);
            q[i] = p;
        }
        for( int i=0; i<4; i++) {
            q[i] /= p;
        }
        
        xRange = (double)(fPars.getXMax() - fPars.getXMin());
        yRange = (double)(fPars.getYMax() - fPars.getYMin());
        xIter = fPars.getXStart();
        yIter = fPars.getYStart();
        xPixel = 0;
        yPixel = 0;
        long count = 0L;
        xNew=0;
        yNew=0;
        
        int rgbCol = 0;
        int rgbColNew = 0;
        
        try {
            // as long as we did not reach the number of iterations;
            // or the user did not press 'stop' yet (for infinite loops only)
            while ( (count < fPars.getCount()) || fPars.isInfiniteLoop() ) {
                if( fPars.isInfiniteLoopInterrupted() ) {
                    break;
                }
                
                if (xIter*xIter + yIter*yIter > fPars.getRange() ) {
                    System.out.println("Overflow!");
                    return null;
                }
                
                // perform the iteration (abstract method)
                doIteration();
                
                // now calculate the pixel on the screen
                xPixel = (int)(fPars.getSizeX() * (xNew - fPars.getXMin()) / xRange);
                yPixel = fPars.getSizeY() - (int)(fPars.getSizeY() * (yNew - fPars.getYMin()) /yRange);
                
                // check if the pixel is in the defined range; if not just skip painting
                if( (count>7L) &&
                        (xPixel>=0) && (xPixel<fPars.getSizeX()) && (yPixel>=0) && (yPixel<fPars.getSizeY()) ) {
                    
                    // find out the color of this pixel
                    rgbCol = bufferedImage.getRGB(xPixel, yPixel);
                    
                    // determine the new color, based on the old one
                    rgbColNew = calcNewColorPixel(rgbCol);
                    
                    // finally draw the pixel in the new color
                    bufferedImage.setRGB(xPixel, yPixel, rgbColNew);
                }
                
                // set the interation variables for the next interation
                xIter = xNew; yIter = yNew;
                
                // increase iteration counter
                count ++;
                
                // sometimes check if the thread was interrupted
                if( (count%10000) == 0 ) {
                    if (Thread.interrupted()) {
                        System.out.println( this.getClass() + "FractalJumper.worker was interrupted");
                        throw new InterruptedException();
                    }
                    // sleep that we can see the 'growing' of the image
                    Thread.sleep(fPars.getSleep());
                }
                
            }
            
        } catch (InterruptedException iE) {
            System.out.println( this.getClass() + ": interrupt exception caught");
        }
        
        // we are done with the entire image, so set the flag
        finishedDrawing = true;
        myFrame.setCalculatingLabel( false );                                
        
        // return the drawn image object
        return bufferedImage;
        
    }
    
    protected abstract void doIteration();
    
}