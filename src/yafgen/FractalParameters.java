/*
 * FractalParameters.java
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

/**
 *
 * class FractalParameters stores all the values that are changed by the user interface and are
 * important for the calculations
 *
 */
public class FractalParameters {
    
    // FractalImage: all
    public int sizeX, sizeY;
    
    // RasterIteration: Julia, Mandelbrot, ...
    public double xMin, xMax, yMin, yMax;
    public double xFix, yFix;
    public double maxLength;
    public int maxIterations;
    
    // NLF and Jumper
    public double xStart;
    public double yStart;
    public double aFix, bFix, cFix;
    public double aJFix, bJFix, cJFix;
    public long   range;
    public int    sleep;
    public long   count;
    public boolean infiniteLoop;
    public boolean infiniteLoopInterrupted;
    
    // IFS
    public double[] a, b, c, d, e, f;
    
    /** Creates a new instance of FractalParameters */
    public FractalParameters() {
        setDefaultParameters(null);
    }
    
    public void setDefaultParameters(FractalImage myFImage) {
        
        // set the default parameters, no matter what FractalImage type
        sizeX = 800;
        sizeY = 600;
        
        maxLength = 10.0;
        maxIterations = 100;
        xFix = -0.5;
        yFix = -0.6;
        xMin = -2.5;
        xMax = 1.2;
        yMin = -2.0;
        yMax = 2.0;
        
        xStart = -0.1;
        yStart = 0.0;
        aFix = 1.0;
        bFix = 1.0;
        range = 10000000L;
        sleep = 100;
        count = 1000000L;
        infiniteLoop = true;
        infiniteLoopInterrupted = false;
        
        aJFix = 150;
        bJFix = 0.21;
        cJFix = 100;
        
        a = new double[6];
        b = new double[6];
        c = new double[6];
        d = new double[6];
        e = new double[6];
        f = new double[6];
        
        a[0] = 0.0;  b[0] = 0.0;  c[0] = -0.0;  d[0] = 0.16; e[0] = 0.0;  f[0] = 0.0;
        a[1] = 0.2;  b[1] =-0.26; c[1] =  0.23; d[1] = 0.22; e[1] = 0.0;  f[1] = 1.6;
        a[2] =-0.15; b[2] = 0.28; c[2] = 0.26;  d[2] = 0.24; e[2] = 0.0;  f[2] = 0.44;
        a[3] = 0.85; b[3] = 0.04; c[3] = -0.04; d[3] = 0.85; e[3] = 0.0;  f[3] = 1.6;
        a[4] = 0.8;  b[4] = 0.1;  c[4] = -0.1;  d[4] = 0.6;  e[4] = 0.1;  f[4] = 0.1;
        a[5] = 0.8;  b[5] = 0.1;  c[5] = -0.1;  d[5] = 0.6;  e[5] = 0.1;  f[5] = 0.1;
        
        // now overwrite some parameters again, depending on FractalImage type
        if (myFImage instanceof FractalMandelbrot) {
            xMin = -2.5;
            xMax = 1.2;
            yMin = -2.0;
            yMax = 2.0;
        }
        
        if (myFImage instanceof FractalNLF) {
            xMin = -5;
            xMax = 9;
            yMin = -5;
            yMax = 9;
        }
        
        if (myFImage instanceof FractalIFS) {
            xMin = -4;
            xMax = 4;
            yMin = -2;
            yMax = 12;
        }
        
        if (myFImage instanceof FractalJumper) {
            // Jumper
            xMin = -200;
            xMax = 350;
            yMin = -200;
            yMax = 300;
            aJFix = 150;
            bJFix = 0.21;
            cJFix = 100;
        }
        
    }
    
    /** presets for jumper */
    public void setPresetParameters(int presetIndex) {
        switch(presetIndex){
            
            case 0:
                xMin = -600;
                xMax = 60;
                yMin = -500;
                yMax = 500;
                aJFix = 50;
                bJFix = 0.21;
                cJFix = 100;
                break;
                
            case 1:
                xMin = -200;
                xMax = 350;
                yMin = -200;
                yMax = 300;
                aJFix = 150;
                bJFix = 0.21;
                cJFix = 100;
                break;
                
            case 2:
                xMin = -200;
                xMax = 350;
                yMin = -200;
                yMax = 300;
                aJFix = 250;
                bJFix = 0.21;
                cJFix = 100;
                break;
                
            case 3:
                xMin = -200;
                xMax = 0;
                yMin = -200;
                yMax = 0;
                aJFix = 350;
                bJFix = 0.21;
                cJFix = 100;
                sleep = 10;
                break;
                
            case 4:
                xMin = -4;
                xMax = 4;
                yMin = -4;
                yMax = 4;
                aJFix = 0.01;
                bJFix = -0.03;
                cJFix = 0.003;
                break;
                
            case 5:
                xMin = -3;
                xMax = 3;
                yMin = -3;
                yMax = 3;
                aJFix = 0.4;
                bJFix = 1;
                cJFix = 0;
                break;
                
            case 6:
                xMin = -300;
                xMax = 50;
                yMin = -200;
                yMax = 300;
                aJFix = 500;
                bJFix = 0.833;
                cJFix = 110;
                break;
                
            case 7:
                xMin = -300;
                xMax = 50;
                yMin = -200;
                yMax = 300;
                aJFix = 500;
                bJFix = 0.912341234;
                cJFix = 110;
                break;
                
        }
        
    }
}
