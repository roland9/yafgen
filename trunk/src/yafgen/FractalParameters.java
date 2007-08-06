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
    private Vector a = new Vector();
    private Vector b = new Vector();
    private Vector c = new Vector();
    private Vector d = new Vector();
    private Vector e = new Vector();
    private Vector f = new Vector();
    
/*    public double[] a;
    public double[] b;
    public double[] c;
    public double[] d;
    public double[] e;
    public double[] f;
 */
    /** Creates a new instance of FractalParameters */
    public FractalParameters() {
        setDefaultParameters(null);
    }
    
    public void setDefaultParameters(FractalImage myFImage) {
        getA().setSize(6);
        getB().setSize(6);
        c.setSize(6);
        d.setSize(6);
        e.setSize(6);
        // todo nštig?
        getF().setSize(6);
        
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
        
        /*
        setA(new double[6]);
        setB(new double[6]);
        setC(new double[6]);
        setD(new double[6]);
        setE(new double[6]);
        setF(new double[6]);
         */
        
        setA(0,0.0);  setB(0, 0.0);  setC(0,-0.0);  setD(0,0.16); setE(0,0.0);  setF(0,0.0);
        setA(1,0.2);   setB(1,-0.26); setC(1,0.23);  setD(1,0.22); setE(1,0.0);  setF(1,1.6);
        setA(2,-0.15); setB(2,0.28);  setC(2,0.26);  setD(2,0.24); setE(2,0.0);  setF(2,0.44);
        setA(3,0.85);  setB(3,0.04);  setC(3,-0.04); setD(3,0.85); setE(3,0.0);  setF(3,1.6);
        setA(4,0.8);   setB(4,0.1);   setC(4,-0.1);  setD(4,0.6);  setE(4,0.1);  setF(4,0.1);
        setA(5,0.8);   setB(5,0.1);   setC(5,-0.1);  setD(5,0.6);  setE(5,0.1);  setF(5,0.1);
        
        // now overwrite some parameters again, depending on FractalImage type
        if (myFImage instanceof FractalMandelbrot) {
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
    
    public void setA(int index, Object x) {
        this.getA().setElementAt(x,index);
    }
    
    public void setB(int index, Object x) {
        this.getB().setElementAt(x,index);
    }
    
    public void setC(int index, Object x) {
        this.c.setElementAt(x,index);
    }
    
    public void setD(int index, Object x) {
        this.d.setElementAt(x,index);
    }
    
    public void setE(int index, Object x) {
        this.e.setElementAt(x,index);
    }
    
    public void setF(int index, Object x) {
        this.f.setElementAt(x,index);
    }

    public double getA(int index) {
        return (double)Double.valueOf(getA().get(index).toString());
    }
    
    public double getB(int index) {
        return (double)Double.valueOf(getB().get(index).toString());
    }
    
    public double getC(int index) {
        return (double)Double.valueOf(c.get(index).toString());
    }
    
    public double getD(int index) {
        return (double)Double.valueOf(d.get(index).toString());
    }
    
    public double getE(int index) {
        return (double)Double.valueOf(e.get(index).toString());
    }
    
    public double getF(int index) {
        return (double)Double.valueOf(getF().get(index).toString());
    }

    public Vector getA() {
        return a;
    }

    public Vector getB() {
        return b;
    }

    public Vector getC() {
        return c;
    }

    public Vector getD() {
        return d;
    }

    public Vector getE() {
        return e;
    }

    public Vector getF() {
        return f;
    }

    public void setA(Vector f) {
        this.a = f;
    }
    
    public void setB(Vector f) {
        this.b = f;
    }
    
    public void setC(Vector f) {
        this.c = f;
    }
    
    public void setD(Vector f) {
        this.d = f;
    }
    
    public void setE(Vector f) {
        this.e = f;
    }
    
    public void setF(Vector f) {
        this.f = f;
    }
    
}
