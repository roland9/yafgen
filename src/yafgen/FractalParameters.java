/*
 * FractalParameters.java
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

import java.util.Vector;


/**
 *
 * class FractalParameters stores all the values that are changed by the user interface and are
 * important for the calculations
 *
 */
public class FractalParameters implements java.io.Serializable {
    
    // FractalImage: all
    public int sizeX;
    public int sizeY;
    public double xMin;
    public double xMax;
    public double yMin;
    public double yMax;
    public double xFix;
    public double yFix;
    public double maxLength;
    public int maxIterations;
    
    // NLF and Jumper
    public double xStart;
    public double yStart;
    public double aFix;
    public double bFix;
    public double cFix;
    public double aJFix;
    public double bJFix;
    public double cJFix;
    public long   range;
    public int    sleep;
    public long   count;
    public boolean infiniteLoop;
    public boolean infiniteLoopInterrupted;
    
    // IFS
    public Vector<Double> a;
    public Vector<Double> b;
    public Vector<Double> c;
    public Vector<Double> d;
    public Vector<Double> e;
    public Vector<Double> f;
    
    /* enum does not work with XMLEncoder...
     public enum FractalTypes {
        MANDEL,
        JULIA,
        IFS,
        NLF,
        JUMPER
    };
    private FractalTypes currentFractalType;
     
     */
    
    /** 
        MANDEL  = 1
        JULIA   = 2
        IFS     = 3
        NLF     = 4
        JUMPER  = 5
     */
    public int currentFractalType;
    
    
    /** Creates a new instance of FractalParameters */
    public FractalParameters() {
        //setDefaultParameters(null);
    }
    
    public void setDefaultParameters(FractalImage myFImage) {
        a = new Vector<Double>(6);
        b = new Vector<Double>(6);
        c = new Vector<Double>(6);
        d = new Vector<Double>(6);
        e = new Vector<Double>(6);
        f = new Vector<Double>(6);
        
        // set the default parameters, no matter what FractalImage type
        setSizeX(800);
        setSizeY(600);
        
        setMaxLength(10.0);
        setMaxIterations(100);
        setXFix(-0.5);
        setYFix(-0.6);
        setXMin(-2.5);
        setXMax(1.2);
        setYMin(-2.0);
        setYMax(2.0);
        
        setXStart(-0.1);
        setYStart(0.0);
        setAFix(1.0);
        setBFix(1.0);
        setRange(10000000L);
        setSleep(100);
        setCount(1000000L);
        setInfiniteLoop(true);
        setInfiniteLoopInterrupted(false);
        
        setAJFix(150);
        setBJFix(0.21);
        setCJFix(100);
        
        a.add(0,0.0D);   b.add(0, 0.0D);  c.add(0,-0.0D);  d.add(0,0.16D); e.add(0,0.0D);  f.add(0,0.0D);
        a.add(1,0.2);   b.add(1,-0.26); c.add(1,0.23);  d.add(1,0.22); e.add(1,0.0);  f.add(1,1.6);
        a.add(2,-0.15); b.add(2,0.28);  c.add(2,0.26);  d.add(2,0.24); e.add(2,0.0);  f.add(2,0.44);
        a.add(3,0.85);  b.add(3,0.04);  c.add(3,-0.04); d.add(3,0.85); e.add(3,0.0);  f.add(3,1.6);
        a.add(4,0.8);   b.add(4,0.1);   c.add(4,-0.1);  d.add(4,0.6);  e.add(4,0.1);  f.add(4,0.1);
        a.add(5,0.8);   b.add(5,0.1);   c.add(5,-0.1);  d.add(5,0.6);  e.add(5,0.1);  f.add(5,0.1);
        
        // now overwrite some parameters again, depending on FractalImage type
        if (myFImage instanceof FractalMandelbrot) {
            setXMin(-2.5);
            setXMax(1.2);
            setYMin(-2.0);
            setYMax(2.0);
        }
        
        if (myFImage instanceof FractalManowar) {
            setXMin(-2.5);
            setXMax(1.2);
            setYMin(-2.0);
            setYMax(2.0);
        }
        
        if (myFImage instanceof FractalNLF) {
            setXMin(-5);
            setXMax(9);
            setYMin(-5);
            setYMax(9);
            // todo
            currentFractalType = 4;
        }
        
        if (myFImage instanceof FractalIFS) {
            setXMin(-4);
            setXMax(4);
            setYMin(-2);
            setYMax(12);
        }
        
        if (myFImage instanceof FractalJumper) {
            // Jumper
            setXMin(-200);
            setXMax(350);
            setYMin(-200);
            setYMax(300);
            setAJFix(150);
            setBJFix(0.21);
            setCJFix(100);
        }
        
    }
    
    /** presets for jumper */
    public void setPresetParameters(int presetIndex) {
        switch(presetIndex){
            
            case 0:
                setXMin(-600);
                setXMax(60);
                setYMin(-500);
                setYMax(500);
                setAJFix(50);
                setBJFix(0.21);
                setCJFix(100);
                break;
                
            case 1:
                setXMin(-200);
                setXMax(350);
                setYMin(-200);
                setYMax(300);
                setAJFix(150);
                setBJFix(0.21);
                setCJFix(100);
                break;
                
            case 2:
                setXMin(-200);
                setXMax(350);
                setYMin(-200);
                setYMax(300);
                setAJFix(250);
                setBJFix(0.21);
                setCJFix(100);
                break;
                
            case 3:
                setXMin(-200);
                setXMax(0);
                setYMin(-200);
                setYMax(0);
                setAJFix(350);
                setBJFix(0.21);
                setCJFix(100);
                setSleep(10);
                break;
                
            case 4:
                setXMin(-4);
                setXMax(4);
                setYMin(-4);
                setYMax(4);
                setAJFix(0.01);
                setBJFix(-0.03);
                setCJFix(0.003);
                break;
                
            case 5:
                setXMin(-3);
                setXMax(3);
                setYMin(-3);
                setYMax(3);
                setAJFix(0.4);
                setBJFix(1);
                setCJFix(0);
                break;
                
            case 6:
                setXMin(-300);
                setXMax(50);
                setYMin(-200);
                setYMax(300);
                setAJFix(500);
                setBJFix(0.833);
                setCJFix(110);
                break;
                
            case 7:
                setXMin(-300);
                setXMax(50);
                setYMin(-200);
                setYMax(300);
                setAJFix(500);
                setBJFix(0.912341234);
                setCJFix(110);
                break;
                
        }
        
    }
    
    public int getSizeX() {
        return sizeX;
    }
    
    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }
    
    public int getSizeY() {
        return sizeY;
    }
    
    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }
    
    public double getXMin() {
        return xMin;
    }
    
    public void setXMin(double xMin) {
        this.xMin = xMin;
    }
    
    public double getXMax() {
        return xMax;
    }
    
    public void setXMax(double xMax) {
        this.xMax = xMax;
    }
    
    public double getYMin() {
        return yMin;
    }
    
    public void setYMin(double yMin) {
        this.yMin = yMin;
    }
    
    public double getYMax() {
        return yMax;
    }
    
    public void setYMax(double yMax) {
        this.yMax = yMax;
    }
    
    public double getXFix() {
        return xFix;
    }
    
    public void setXFix(double xFix) {
        this.xFix = xFix;
    }
    
    public double getYFix() {
        return yFix;
    }
    
    public void setYFix(double yFix) {
        this.yFix = yFix;
    }
    
    public double getMaxLength() {
        return maxLength;
    }
    
    public void setMaxLength(double maxLength) {
        this.maxLength = maxLength;
    }
    
    public int getMaxIterations() {
        return maxIterations;
    }
    
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }
    
    public double getXStart() {
        return xStart;
    }
    
    public void setXStart(double xStart) {
        this.xStart = xStart;
    }
    
    public double getYStart() {
        return yStart;
    }
    
    public void setYStart(double yStart) {
        this.yStart = yStart;
    }
    
    public double getAFix() {
        return aFix;
    }
    
    public void setAFix(double aFix) {
        this.aFix = aFix;
    }
    
    public double getBFix() {
        return bFix;
    }
    
    public void setBFix(double bFix) {
        this.bFix = bFix;
    }
    
    public double getCFix() {
        return cFix;
    }
    
    public void setCFix(double cFix) {
        this.cFix = cFix;
    }
    
    public double getAJFix() {
        return aJFix;
    }
    
    public void setAJFix(double aJFix) {
        this.aJFix = aJFix;
    }
    
    public double getBJFix() {
        return bJFix;
    }
    
    public void setBJFix(double bJFix) {
        this.bJFix = bJFix;
    }
    
    public double getCJFix() {
        return cJFix;
    }
    
    public void setCJFix(double cJFix) {
        this.cJFix = cJFix;
    }
    
    public long getRange() {
        return range;
    }
    
    public void setRange(long range) {
        this.range = range;
    }
    
    public int getSleep() {
        return sleep;
    }
    
    public void setSleep(int sleep) {
        this.sleep = sleep;
    }
    
    public long getCount() {
        return count;
    }
    
    public void setCount(long count) {
        this.count = count;
    }
    
    public boolean isInfiniteLoop() {
        return infiniteLoop;
    }
    
    public void setInfiniteLoop(boolean infiniteLoop) {
        this.infiniteLoop = infiniteLoop;
    }
    
    public boolean isInfiniteLoopInterrupted() {
        return infiniteLoopInterrupted;
    }
    
    public void setInfiniteLoopInterrupted(boolean infiniteLoopInterrupted) {
        this.infiniteLoopInterrupted = infiniteLoopInterrupted;
    }
    
    public Vector<Double> getA() {
        return a;
    }
    
    public void setA(Vector<Double> a) {
        this.a = a;
    }
    
    public Vector<Double> getB() {
        return b;
    }
    
    public void setB(Vector<Double> b) {
        this.b = b;
    }
    
    public Vector<Double> getC() {
        return c;
    }
    
    public void setC(Vector<Double> c) {
        this.c = c;
    }
    
    public Vector<Double> getD() {
        return d;
    }
    
    public void setD(Vector<Double> d) {
        this.d = d;
    }
    
    public Vector<Double> getE() {
        return e;
    }
    
    public void setE(Vector<Double> e) {
        this.e = e;
    }
    
    public Vector<Double> getF() {
        return f;
    }
    
    public void setF(Vector<Double> f) {
        this.f = f;
    }
    
    /*
    public FractalTypes getCurrentFractalType() {
        return currentFractalType;
    }
    
    public void setCurrentFractalType(FractalTypes currentFractalType) {
        this.currentFractalType = currentFractalType;
    }
     */
    
    public int getCurrentFractalType() {
        return currentFractalType;
    }
    
    public void setCurrentFractalType(int currentFractalType) {
        this.currentFractalType = currentFractalType;
    }
    
}
