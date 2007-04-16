/*
 * FractalIFS.java
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
import java.awt.*;

/**
 * The class FractalIFS implements an Iterated Function System, based on the defined matrix.
 * The only implemented method is doIteration().
 */
public class FractalIFS extends FractalIterationFunction {
    
    /** Creates a new instance of FractalIFS */
    public FractalIFS(YaFGenMainFrame myFrame, FractalParameters myFPars) {
        super(myFrame, myFPars);
    }        
    
    protected void doIteration() {
        
        // pick the function, according to its weight
        int k = 0;
        p = (double)Math.random();
        
        while( p > q[k])
            k++;
        
        // x[n+1] = MAk * x[n] + Vk;   n>1
        xNew = fPars.a[k]*xIter + fPars.b[k]*yIter + fPars.e[k];
        yNew = fPars.c[k]*xIter + fPars.d[k]*yIter + fPars.f[k];
        
    }
}
