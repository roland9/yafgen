/*
 * FractalManowar.java
 *
 * Version 1.0, created on 11. April 2007, 19:34
 *
 *
 *   YaFGen - Yet another Fractal Generator - Generate images based on mathematical formulas 
 *   Copyright (C) 2007  Roland Gr�pmair
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
 * The class FractalManowar implements a fractal called "Manowar".
 * The only implemented method is doIteration().
 */
public class FractalManowar extends FractalRasterIteration {
    
    /** Creates a new instance of FractalManowar */
    public FractalManowar(YaFGenMainFrame myFrame, FractalParameters myFPars) {
        super(myFrame, myFPars);
    }
    
    protected int doIteration(double x, double y) {
        
        double tmpX = x, tmpY = y, tmpX_old = x;
        int i = 0;
        
        // z[n+1] := z[n]? + c; z[0] := fix
        do {
            i++;
            tmpX_old = tmpX;
            tmpX = tmpX*tmpX/tmpY/10.0 - tmpY*tmpX*tmpX/5.0 + fPars.getXFix();
            tmpY = 2.1*tmpX_old*tmpY - tmpY/tmpX_old*0.8 - 0.98*tmpX_old + fPars.getYFix();
            // tmpX = tmpX*(tmpX+tmpY/3.0) - tmpY*tmpY + fPars.xFix;
            // tmpY = 2*tmpX_old*tmpY + tmpX_old/20.0 + fPars.yFix;
        } while (((tmpX*tmpX + tmpY*tmpY) <= fPars.getMaxLength()) && (i < fPars.getMaxIterations()));
        
        return  i;
    }
    
}
