/*
 * FractalRasterIteration.java
 *
 * Version 1.0, created on 11. April 2007, 19:34
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
 */

package yafgen;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 *
 * FractalRasterIteration is the base class for fractals that can be represented by images
 * on the complex plane. The algorithm therefore is based on complex numbers, i.e.
 * real numbers combined with imaginary units.
 * It calculates the image by determining the color value of every pixel in the raster image.
 * For performance reasons, it starts with square tiles that consist of several pixels on the screen,
 * and subsequently shrinks the tiles. By that way, the user is able to see a less detailed image
 * at the first run, which is refined more and more.
 *
 * It is an abstract class, so that the calculation/iteration itself is implemented
 * in a subclass.
 *
 */
public abstract class FractalRasterIteration extends FractalImage {
    
    private double xStep, yStep;
    private int loopX, loopY;
    
    /** Creates a new instance of FractalRasterIteration */
    public FractalRasterIteration(YaFGenMainFrame myFrame, FractalParameters myFPars) {
        super(myFrame, myFPars);
    }
        
    public Object doWork() {
        
        System.out.println( this.getClass() + ": doWork() started");
        
        Color c = Color.black;
        finishedDrawing = false;
        
        int oldLoopTiles = 100000;
        
        try {
            
            // start with tiles of the size 16x16
            for( int loopTiles = 16; loopTiles > 0; loopTiles/=2 ){
                double x = fPars.xMin;
                double y = fPars.yMax;
                // calculate the value by which we need to increment the x/y numbers in every loop
                xStep = (fPars.xMax - fPars.xMin)/(double)fPars.sizeX * loopTiles;
                yStep = (fPars.yMax - fPars.yMin)/(double)fPars.sizeY * loopTiles;
                
                // loop through the x axis
                for (loopX = 0; loopX < fPars.sizeX; loopX+=loopTiles) {
                    
                    y = fPars.yMax;
                    
                    // loop through the y axis
                    for (loopY = 0; loopY < fPars.sizeY; loopY+=loopTiles) {
                        
                        // if we have already calculated this pixel, then skip
                        if( ((loopY % oldLoopTiles) == 0) && ((loopX % oldLoopTiles)==0)) {
                            y-=yStep;
                            continue;
                        }
                        
                        // perform the iteration; note that this is an abstract method
                        int iterations = doIteration(x,y);
                        c = calcNewColorIterations(iterations);
                        
                        // draw tile on the screen in the specified color
                        graphics.setColor(c);
                        graphics.fillRect(loopX,loopY,loopTiles,loopTiles);
                        
                        y-=yStep;
                        
                    }
                    
                    // check if this thread was interrupted
                    if (Thread.interrupted()) {
                        System.out.println( this.getClass() + ": thread was interrupted");
                        throw new InterruptedException();
                    }
                    
                    x+=xStep;
                    
                }
                oldLoopTiles = loopTiles;
            }
        } catch (InterruptedException iE) {
            System.out.println( this.getClass() + ": interrupt exception caught");
            
        }
        
        // we are done with the entire image, so set the flag
        finishedDrawing = true;
        // return the drawn image object
        return bufferedImage;
        
    }

    
    
    // this is the abstract method that does the iteration
    protected abstract int doIteration(double x, double y);
    
}
