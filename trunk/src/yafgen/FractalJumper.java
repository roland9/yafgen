/*
 * FractalJumper.java
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
 * The class FractalJumper implements a fractal called "Jumper".
 * The only implemented method is doIteration().
 */
public class FractalJumper extends FractalIterationFunction {
    
    /** Creates a new instance of FractalJumper */
    public FractalJumper(YaFGenMainFrame myFrame, FractalParameters myFPars) {
        super(myFrame, myFPars);
    }
    
    
    protected void doIteration() {
        
        // x[n+1] = y[n] - sign(x[n]) * sqrt( abs( b*x[n] - c ) )
        // y[n+1] = a - x[n]
        xNew = yIter + Math.signum(xIter) *
                Math.sqrt( Math.abs( fPars.bJFix*xIter - fPars.cJFix ) );
        yNew = fPars.aJFix - xIter;
        
    }
}
