/*
 * YAFGMainFrame.java
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
import java.awt.geom.*;
import java.awt.*;

import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * .Y.et .A.other .F.ractal .Gen.erator YAFG
 *
 * This Java application calculates and displays several fractals
 *
 */
public class YaFGenMainFrame extends javax.swing.JFrame
        implements ActionListener, MouseInputListener {
    
    /** frame for the fractal image */
    private JFrame fractalFrame;
    
    /** the fractal image */
    private FractalImage fractalImage;
    
    /** we need a timer so that the user can see the "growing" of the fractal image
     *  periodically the display is refreshed with the newest image
     */
    private Timer timer;
    
    /** we support dragging and clicking in the fractal image
     * dragging with left mouse button: zoom in
     * dragging with right mouse button: zoom out
     * clicking: set fix point, for Julia only */
    private Point pointMouseDraggedStart, pointMouseDraggedEnd, oldPointMouse;
    
    /** here we store all the values that are changed by the user interface and are
     *  important for the calculations */
    private FractalParameters fPars;
    
    /** verifcation the input fields: double */
    private InputVerifier myDoubleVerifier;
    /** verifcation the input fields: integer */
    private InputVerifier myIntegerVerifier;
    
    
    
    /** Creates new form YAFGMainFrame */
    public YaFGenMainFrame() {
        initComponents();
        
        myDoubleVerifier = new MyDoubleVerifier();
        myIntegerVerifier = new MyIntegerVerifier();
        
        fractalXMin.setInputVerifier( myDoubleVerifier );
        fractalXMax.setInputVerifier( myDoubleVerifier );
        fractalYMin.setInputVerifier( myDoubleVerifier );
        fractalYMax.setInputVerifier( myDoubleVerifier );
        
        fractalXFix.setInputVerifier( myDoubleVerifier );
        fractalYFix.setInputVerifier( myDoubleVerifier );
        
        fractalMaxIter.setInputVerifier( myIntegerVerifier );
        fractalMaxLen.setInputVerifier( myDoubleVerifier );
        
        fractalSizeX.setInputVerifier( myIntegerVerifier );
        fractalSizeY.setInputVerifier( myIntegerVerifier );
        
        fractalCount.setInputVerifier( myIntegerVerifier );
        fractalSleep.setInputVerifier( myIntegerVerifier );
        
        
        fractalXStart.setInputVerifier( myDoubleVerifier );
        fractalYStart.setInputVerifier( myDoubleVerifier );
        
        fractalA.setInputVerifier( myDoubleVerifier );
        fractalB.setInputVerifier( myDoubleVerifier );
        
        fractalAJFix.setInputVerifier( myDoubleVerifier );
        fractalBJFix.setInputVerifier( myDoubleVerifier );
        fractalCJFix.setInputVerifier( myDoubleVerifier );
        
        
        fractalTypeMandelbrot.setSelected(true);
        
        JTable tableIFS = new JTable( new MyTableModel() );
        jScrollPane1.setViewportView(tableIFS);
        jumperPresets.setModel( new MyComboBoxModel() );
        
        getRootPane().setDefaultButton(repaintButton);
        
        fPars = new FractalParameters();
        
        fractalFrame = new JFrame("YaFGen - Yet Another Fractal Generator, Version 1.0, April 2007");
        fractalFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
//        create Mac about box        
//        if (System.getProperty("mrj.version") != null) {
//            new MacOSAboutHandler();
//        }
        
        fractalImage = new FractalMandelbrot(this, fPars);
        
        // Java 1.5: fractalFrame.add(fractalImage);
        fractalFrame.getContentPane().add(fractalImage);
        
        fractalFrame.pack();
        fractalFrame.setVisible(true);
        
        // move the window/frame of the parameter panel to the right of the fractal frame
        this.setLocation(fractalFrame.getWidth()+fractalFrame.getX(),0);
        
        fPars.sizeX = fractalImage.getSize().width;
        fPars.sizeY = fractalImage.getSize().height;
        
        refreshInputFields();
        
        // Set up a timer that calls repaint for the fractal image
        timer = new Timer(100, this);
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        
        timer.start();
        
        fractalImage.addMouseListener(this);
        fractalImage.addMouseMotionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        // did the timer remind us to redisplay the image?
        if( e.getSource() == timer ) {
            fractalImage.repaint();
            
            // if the drawing is finished, also stop the timer, because we don't need it anymore
            if( fractalImage.finishedDrawing() )
                timer.stop();
        }
        
    }
    
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }
    
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }
    
    public void mouseExited(MouseEvent e) {
        // do nothing
    }
    
    public void mousePressed(MouseEvent e) {
        // do nothing
    }
    
    public void mouseReleased(MouseEvent e) {
        
        if (pointMouseDraggedStart == null) {
            // user did only click, but not drag => define fix point, do not zoom
            Point pointClicked = e.getPoint();
            
            fPars.xFix = fPars.xMin +
                    pointClicked.getX() * ((fPars.xMax - fPars.xMin) / fPars.sizeX );
            fPars.yFix = fPars.yMax -
                    pointClicked.getY() * ((fPars.yMax - fPars.yMin) / fPars.sizeY );
            
            fractalXFix.setText(Double.toString(fPars.xFix));
            fractalXFix.moveCaretPosition(0);
            fractalYFix.setText(Double.toString(fPars.yFix));
            fractalYFix.moveCaretPosition(0);
            
            pointMouseDraggedStart = null;
            return;
        }
        
        // user did dragging, so meant zooming in/out
        pointMouseDraggedEnd = e.getPoint();
        
        if( pointMouseDraggedStart.getX() > pointMouseDraggedEnd.getX()) {
            int temp = pointMouseDraggedStart.x;
            pointMouseDraggedStart.x = pointMouseDraggedEnd.x;
            pointMouseDraggedEnd.x = temp;
        }
        
        if( pointMouseDraggedStart.getY() > pointMouseDraggedEnd.getY()) {
            int temp = pointMouseDraggedStart.y;
            pointMouseDraggedStart.y = pointMouseDraggedEnd.y;
            pointMouseDraggedEnd.y = temp;
        }
        
        // zoom completed: calculate new parameters after zoom
        Point start = pointMouseDraggedStart;
        Point end = pointMouseDraggedEnd;
        
        if( e.getButton() == MouseEvent.BUTTON1 ) {
            System.out.println( this.getClass() + "zoom in" );
            
            double xMinNew = fPars.xMin + pointMouseDraggedStart.getX() * ( (fPars.xMax - fPars.xMin) / fPars.sizeX );
            fPars.xMax = fPars.xMax - (fPars.sizeX - pointMouseDraggedEnd.getX()) * ( (fPars.xMax - fPars.xMin) / fPars.sizeX );
            double yMinNew = fPars.yMax - pointMouseDraggedEnd.getY() * ( (fPars.yMax - fPars.yMin) / fPars.sizeY );
            fPars.yMax = fPars.yMax - pointMouseDraggedStart.getY() * ( (fPars.yMax - fPars.yMin) / fPars.sizeY );
            fPars.xMin = xMinNew;
            fPars.yMin = yMinNew;
        } else if ( e.getButton() == MouseEvent.BUTTON3 ) {
            System.out.println( this.getClass() + "zoom out" );
            
            double xMinNew = fPars.xMin - pointMouseDraggedStart.getX() *
                    ( (fPars.xMax - fPars.xMin) / (pointMouseDraggedEnd.getX() - pointMouseDraggedStart.getX() ) );
            fPars.xMax = fPars.xMax + (fPars.sizeX - pointMouseDraggedEnd.getX()) *
                    ( (fPars.xMax - fPars.xMin) / (pointMouseDraggedEnd.getX() - pointMouseDraggedStart.getX() ) );
            double yMinNew = fPars.yMin - ( fPars.sizeY - pointMouseDraggedEnd.getY()) *
                    ( (fPars.yMax - fPars.yMin) / (pointMouseDraggedEnd.getY() - pointMouseDraggedStart.getY() ) );
            fPars.yMax = fPars.yMax + pointMouseDraggedStart.getY() *
                    ( (fPars.yMax - fPars.yMin) / (pointMouseDraggedEnd.getY() - pointMouseDraggedStart.getY() ) );
            fPars.xMin = xMinNew;
            fPars.yMin = yMinNew;
        }
        
        // update input fields
        fractalXMin.setText(Double.toString(fPars.xMin));
        fractalXMin.moveCaretPosition(0);
        fractalXMax.setText(Double.toString(fPars.xMax));
        fractalXMax.moveCaretPosition(0);
        fractalYMin.setText(Double.toString(fPars.yMin));
        fractalYMin.moveCaretPosition(0);
        fractalYMax.setText(Double.toString(fPars.yMax));
        fractalYMax.moveCaretPosition(0);
        
        pointMouseDraggedStart = null;
    }
    
    public void mouseMoved(MouseEvent e) {
        // do nothing
    }
    
    public void mouseDragged( MouseEvent e) {
        //System.out.println( "mouseDragged " + e.getPoint() );
        
        if( pointMouseDraggedStart == null )
            pointMouseDraggedStart = e.getPoint();
        
        // ask fractalImage to draw a new rectangle
        fractalImage.setMouseDraggedRect( pointMouseDraggedStart, e.getPoint(), oldPointMouse );
        fractalImage.repaint();
        
        oldPointMouse = e.getPoint();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroupFractalType = new javax.swing.ButtonGroup();
        sizeLabel = new javax.swing.JLabel();
        fractalSizeXLabel = new javax.swing.JLabel();
        fractalSizeX = new javax.swing.JTextField();
        fractalSizeYLabel = new javax.swing.JLabel();
        fractalSizeY = new javax.swing.JTextField();
        setSizeButton1 = new javax.swing.JButton();
        setSizeButton2 = new javax.swing.JButton();
        repaintButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        fractalType = new javax.swing.JTabbedPane();
        jPanelMandelJulia = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        fractalXFix = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        fractalYFix = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        fractalMaxIter = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        fractalMaxLen = new javax.swing.JTextField();
        fractalTypeMandelbrot = new javax.swing.JRadioButton();
        fractalTypeJulia = new javax.swing.JRadioButton();
        setDefaultValuesMandelbrot = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanelIterationFunctions = new javax.swing.JPanel();
        iterationFunctions = new javax.swing.JTabbedPane();
        jPanelIFS = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        setDefaultValuesIFS = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanelNLF = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        fractalXStart = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fractalYStart = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        fractalB = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        fractalA = new javax.swing.JTextField();
        setDefaultValuesNLF = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanelJumper = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        fractalCJFix = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        fractalBJFix = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        fractalAJFix = new javax.swing.JTextField();
        jumperPresets = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        fractalSleep = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        fractalCount = new javax.swing.JTextField();
        checkBoxInfiniteLoop = new javax.swing.JCheckBox();
        buttonStopInfiniteLoop = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        fractalXMin = new javax.swing.JTextField();
        fractalXMax = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fractalYMax = new javax.swing.JTextField();
        fractalYMin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        setSizeManually = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Input Parameters");
        sizeLabel.setText("Fractal Size");

        fractalSizeXLabel.setText("Size X");
        fractalSizeXLabel.setName("InpPar123Label");

        fractalSizeX.setText("200");
        fractalSizeX.setName("InpPar123SizeX");
        fractalSizeX.setPreferredSize(new java.awt.Dimension(40, 22));

        fractalSizeYLabel.setText("Size Y");
        fractalSizeYLabel.setName("InpPar123Label");

        fractalSizeY.setText("100");
        fractalSizeY.setName("InpPar123SizeY");
        fractalSizeY.setPreferredSize(new java.awt.Dimension(40, 22));

        setSizeButton1.setText("800x600");
        setSizeButton1.setDefaultCapable(false);
        setSizeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSizeButton86ActionPerformed(evt);
            }
        });

        setSizeButton2.setText("1024x768");
        setSizeButton2.setDefaultCapable(false);
        setSizeButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSizeButton17ActionPerformed(evt);
            }
        });

        repaintButton.setText("Repaint");
        repaintButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repaintButtonActionPerformed(evt);
            }
        });

        jLabel6.setText("xfix");
        jLabel6.setName("InpPar1Label");

        fractalXFix.setText("jTextField1");
        fractalXFix.setName("InpPar1XFix");
        fractalXFix.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel7.setText("yfix");
        jLabel7.setName("InpPar1Label");

        fractalYFix.setText("jTextField1");
        fractalYFix.setName("InpPar1YFix");
        fractalYFix.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel8.setText("maxIter");
        jLabel8.setName("InpPar1Label");

        fractalMaxIter.setText("jTextField1");
        fractalMaxIter.setName("InpPar1MaxIter");
        fractalMaxIter.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel9.setText("maxLen");
        jLabel9.setName("InpPar1Label");

        fractalMaxLen.setText("jTextField1");
        fractalMaxLen.setName("InpPar1MaxLen");
        fractalMaxLen.setPreferredSize(new java.awt.Dimension(90, 22));

        buttonGroupFractalType.add(fractalTypeMandelbrot);
        fractalTypeMandelbrot.setText("Mandelbrot");
        fractalTypeMandelbrot.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        fractalTypeMandelbrot.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroupFractalType.add(fractalTypeJulia);
        fractalTypeJulia.setText("Julia");
        fractalTypeJulia.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        fractalTypeJulia.setMargin(new java.awt.Insets(0, 0, 0, 0));

        setDefaultValuesMandelbrot.setText("Set Def. Values");
        setDefaultValuesMandelbrot.setDefaultCapable(false);
        setDefaultValuesMandelbrot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultValuesMandelbrotActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel10.setText("z[n+1] := z[n]\u00b2 + c; z[0] := 0 ");

        jLabel21.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel21.setText("z[n+1] := z[n]\u00b2 + c; z[0] := fix");

        jLabel26.setText("fix for Julia:");

        org.jdesktop.layout.GroupLayout jPanelMandelJuliaLayout = new org.jdesktop.layout.GroupLayout(jPanelMandelJulia);
        jPanelMandelJulia.setLayout(jPanelMandelJuliaLayout);
        jPanelMandelJuliaLayout.setHorizontalGroup(
            jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelMandelJuliaLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanelMandelJuliaLayout.createSequentialGroup()
                            .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(fractalTypeJulia)
                                .add(fractalTypeMandelbrot))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 20, Short.MAX_VALUE)
                            .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel10)
                                .add(jLabel21))
                            .add(55, 55, 55))
                        .add(jPanelMandelJuliaLayout.createSequentialGroup()
                            .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jPanelMandelJuliaLayout.createSequentialGroup()
                                    .add(66, 66, 66)
                                    .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, fractalMaxIter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, fractalMaxLen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .add(jLabel8)
                                .add(jLabel9))
                            .addContainerGap(156, Short.MAX_VALUE))
                        .add(jPanelMandelJuliaLayout.createSequentialGroup()
                            .add(jLabel6)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(fractalXFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 22, Short.MAX_VALUE)
                            .add(jLabel7)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(fractalYFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(41, 41, 41))
                        .add(jPanelMandelJuliaLayout.createSequentialGroup()
                            .add(jLabel26)
                            .addContainerGap(238, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelMandelJuliaLayout.createSequentialGroup()
                        .add(setDefaultValuesMandelbrot)
                        .addContainerGap())))
        );
        jPanelMandelJuliaLayout.setVerticalGroup(
            jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelMandelJuliaLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(fractalTypeMandelbrot)
                    .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(fractalTypeJulia)
                    .add(jLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jLabel26)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(fractalXFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7)
                    .add(fractalYFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 44, Short.MAX_VALUE)
                .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(fractalMaxIter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelMandelJuliaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(fractalMaxLen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(23, 23, 23)
                .add(setDefaultValuesMandelbrot)
                .addContainerGap())
        );
        fractalType.addTab("Mandelbrot/Julia", jPanelMandelJulia);

        iterationFunctions.setToolTipText("");
        jPanelIFS.setToolTipText("Iterated Function System");
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultValuesIFS.setText("Set Def. Values");
        setDefaultValuesIFS.setDefaultCapable(false);
        setDefaultValuesIFS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultValuesIFSActionPerformed(evt);
            }
        });

        jLabel22.setText("Iterated Function System");
        jLabel22.setToolTipText("Iterated Function System");

        jLabel27.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel27.setText("x[n+1] = MAk * x[n] + Vk;   n>1");

        org.jdesktop.layout.GroupLayout jPanelIFSLayout = new org.jdesktop.layout.GroupLayout(jPanelIFS);
        jPanelIFS.setLayout(jPanelIFSLayout);
        jPanelIFSLayout.setHorizontalGroup(
            jPanelIFSLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelIFSLayout.createSequentialGroup()
                .add(jLabel22)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(setDefaultValuesIFS)
                .addContainerGap(8, Short.MAX_VALUE))
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
            .add(jPanelIFSLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel27)
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanelIFSLayout.setVerticalGroup(
            jPanelIFSLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelIFSLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel27)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 114, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelIFSLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel22)
                    .add(setDefaultValuesIFS)))
        );
        iterationFunctions.addTab("IFS", null, jPanelIFS, "");

        jPanelNLF.setToolTipText("Non-Linear Function");
        jLabel11.setText("xStart");
        jLabel11.setName("InpPar1Label");

        fractalXStart.setText("jTextField1");
        fractalXStart.setName("InpPar1MaxLen");
        fractalXStart.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel12.setText("yStart");
        jLabel12.setName("InpPar1Label");

        fractalYStart.setText("jTextField1");
        fractalYStart.setName("InpPar1MaxLen");
        fractalYStart.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel13.setText("a");
        jLabel13.setName("InpPar1Label");

        fractalB.setText("jTextField1");
        fractalB.setName("InpPar1MaxLen");
        fractalB.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel14.setText("b");
        jLabel14.setName("InpPar1Label");

        fractalA.setText("jTextField1");
        fractalA.setName("InpPar1MaxLen");
        fractalA.setPreferredSize(new java.awt.Dimension(90, 22));

        setDefaultValuesNLF.setText("Set Def. Values");
        setDefaultValuesNLF.setDefaultCapable(false);
        setDefaultValuesNLF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultValuesNLFActionPerformed(evt);
            }
        });

        jLabel23.setText("Non-Linear Function");
        jLabel23.getAccessibleContext().setAccessibleName("");

        jLabel30.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel30.setText("x[0] = xStart;  y[0] = yStart");

        jLabel28.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel28.setText("x[n] = abs(x[n-1]) -b*y[n-1] + a;  n>0");

        jLabel29.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel29.setText("y[n] = x[n-1]");

        org.jdesktop.layout.GroupLayout jPanelNLFLayout = new org.jdesktop.layout.GroupLayout(jPanelNLF);
        jPanelNLF.setLayout(jPanelNLFLayout);
        jPanelNLFLayout.setHorizontalGroup(
            jPanelNLFLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelNLFLayout.createSequentialGroup()
                .add(jPanelNLFLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelNLFLayout.createSequentialGroup()
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel12)
                        .add(11, 11, 11)
                        .add(fractalYStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(26, 26, 26)
                        .add(jLabel14)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fractalB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelNLFLayout.createSequentialGroup()
                        .add(jLabel23)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 38, Short.MAX_VALUE)
                        .add(setDefaultValuesNLF))
                    .add(jPanelNLFLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanelNLFLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelNLFLayout.createSequentialGroup()
                                .add(jLabel11)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fractalXStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 26, Short.MAX_VALUE)
                                .add(jLabel13)
                                .add(11, 11, 11)
                                .add(fractalA, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel30)
                            .add(jLabel28)
                            .add(jLabel29))))
                .addContainerGap())
        );
        jPanelNLFLayout.setVerticalGroup(
            jPanelNLFLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelNLFLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel30)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel28)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel29)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelNLFLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(fractalA, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel11)
                    .add(fractalXStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelNLFLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(fractalYStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel14)
                    .add(fractalB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(19, 19, 19)
                .add(jPanelNLFLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel23)
                    .add(setDefaultValuesNLF))
                .add(13, 13, 13))
        );
        iterationFunctions.addTab("NLF", null, jPanelNLF, "");

        jLabel15.setText("cFix");
        jLabel15.setName("InpPar1Label");

        fractalCJFix.setText("jTextField1");
        fractalCJFix.setName("InpPar1MaxLen");
        fractalCJFix.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel18.setText("bFix");
        jLabel18.setName("InpPar1Label");

        fractalBJFix.setText("jTextField1");
        fractalBJFix.setName("InpPar1MaxLen");
        fractalBJFix.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel19.setText("aFix");
        jLabel19.setName("InpPar1Label");

        fractalAJFix.setText("jTextField1");
        fractalAJFix.setName("InpPar1MaxLen");
        fractalAJFix.setPreferredSize(new java.awt.Dimension(90, 22));

        jumperPresets.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel20.setText("Set Def. Values");
        jLabel20.setName("InpPar1Label");

        jLabel24.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel24.setText("x[n+1] = y[n] - sign(x[n]) * sqrt( abs( b*x[n] - c ) )");

        jLabel25.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jLabel25.setText("y[n+1] = a - x[n]");

        org.jdesktop.layout.GroupLayout jPanelJumperLayout = new org.jdesktop.layout.GroupLayout(jPanelJumper);
        jPanelJumper.setLayout(jPanelJumperLayout);
        jPanelJumperLayout.setHorizontalGroup(
            jPanelJumperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelJumperLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelJumperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel24)
                    .add(jLabel25)
                    .add(jPanelJumperLayout.createSequentialGroup()
                        .add(jLabel20)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jumperPresets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelJumperLayout.createSequentialGroup()
                        .add(jLabel18)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fractalBJFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelJumperLayout.createSequentialGroup()
                        .add(jLabel15)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fractalCJFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelJumperLayout.createSequentialGroup()
                        .add(jLabel19)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fractalAJFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanelJumperLayout.setVerticalGroup(
            jPanelJumperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelJumperLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel24)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel25)
                .add(18, 18, 18)
                .add(jPanelJumperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel19)
                    .add(fractalAJFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelJumperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel18)
                    .add(fractalBJFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelJumperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel15)
                    .add(fractalCJFix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanelJumperLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel20)
                    .add(jumperPresets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        iterationFunctions.addTab("Jumper", jPanelJumper);

        jLabel16.setText("Sleep");
        jLabel16.setName("InpPar1Label");

        fractalSleep.setText("jTextField1");
        fractalSleep.setName("InpPar1MaxLen");
        fractalSleep.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel17.setText("Count");
        jLabel17.setName("InpPar1Label");

        fractalCount.setText("jTextField1");
        fractalCount.setName("InpPar1MaxLen");
        fractalCount.setPreferredSize(new java.awt.Dimension(90, 22));

        checkBoxInfiniteLoop.setText("Infinte Loop");
        checkBoxInfiniteLoop.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBoxInfiniteLoop.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonStopInfiniteLoop.setText("Stop Infinite Loop");
        buttonStopInfiniteLoop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopInfiniteLoopActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelIterationFunctionsLayout = new org.jdesktop.layout.GroupLayout(jPanelIterationFunctions);
        jPanelIterationFunctions.setLayout(jPanelIterationFunctionsLayout);
        jPanelIterationFunctionsLayout.setHorizontalGroup(
            jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelIterationFunctionsLayout.createSequentialGroup()
                .add(jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelIterationFunctionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel17)
                            .add(jLabel16))
                        .add(2, 2, 2)
                        .add(jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(fractalCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(fractalSleep, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(20, 20, 20)
                        .add(jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(buttonStopInfiniteLoop)
                            .add(checkBoxInfiniteLoop)))
                    .add(iterationFunctions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 316, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanelIterationFunctionsLayout.setVerticalGroup(
            jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelIterationFunctionsLayout.createSequentialGroup()
                .add(jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel17)
                    .add(fractalCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(checkBoxInfiniteLoop))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelIterationFunctionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel16)
                    .add(fractalSleep, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(buttonStopInfiniteLoop))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(iterationFunctions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(50, 50, 50))
        );
        fractalType.addTab("Iteration Functions", jPanelIterationFunctions);

        jLabel2.setText("xmin");
        jLabel2.setName("InpPar123Label");

        fractalXMin.setText("jTextField1");
        fractalXMin.setName("InpPar123XMin");
        fractalXMin.setPreferredSize(new java.awt.Dimension(90, 22));

        fractalXMax.setText("jTextField1");
        fractalXMax.setName("InpPar123XMax");
        fractalXMax.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel3.setText("xmax");
        jLabel3.setName("InpPar123Label");

        jLabel5.setText("ymax");
        jLabel5.setName("InpPar123Label");

        fractalYMax.setText("jTextField1");
        fractalYMax.setName("InpPar123YMax");
        fractalYMax.setPreferredSize(new java.awt.Dimension(90, 22));

        fractalYMin.setText("jTextField1");
        fractalYMin.setName("InpPar123YMin");
        fractalYMin.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel4.setText("ymin");
        jLabel4.setName("InpPar123Label");

        jLabel1.setText("Area");

        setSizeManually.setText("Resize");
        setSizeManually.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSizeManuallyActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel2)
                                    .add(jLabel4))
                                .add(12, 12, 12)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(fractalXMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(fractalYMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(34, 34, 34)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(jLabel3)
                                    .add(jLabel5))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(fractalYMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(fractalXMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .add(50, 50, 50))
                    .add(layout.createSequentialGroup()
                        .add(repaintButton)
                        .addContainerGap(286, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 14, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(setSizeButton1)
                            .add(layout.createSequentialGroup()
                                .add(fractalSizeXLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fractalSizeX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(23, 23, 23)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fractalSizeYLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(fractalSizeY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(17, 17, 17)
                                .add(setSizeManually))
                            .add(setSizeButton2))
                        .add(58, 58, 58))))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(fractalType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 353, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(sizeLabel)
                .addContainerGap(290, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(fractalXMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(fractalYMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel5)
                            .add(fractalYMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(33, 33, 33)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(fractalXMax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(fractalType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 337, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 10, Short.MAX_VALUE)
                .add(jSeparator6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(sizeLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(fractalSizeXLabel)
                    .add(fractalSizeX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(fractalSizeYLabel)
                    .add(fractalSizeY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(setSizeManually))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(setSizeButton1)
                    .add(setSizeButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(repaintButton)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void setSizeManuallyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSizeManuallyActionPerformed
        // set window size to the enteres values
        try {
            fPars.sizeX = Integer.parseInt(fractalSizeX.getText());
            fPars.sizeY = Integer.parseInt(fractalSizeY.getText());
            
            setNewSize(fPars.sizeX,fPars.sizeY);
        } catch (NumberFormatException nfe) { }
    }//GEN-LAST:event_setSizeManuallyActionPerformed
    
    private void buttonStopInfiniteLoopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopInfiniteLoopActionPerformed
        // user pressed the stop button, so interrupt the infinite loop
        // we do this by just setting the flag; in the loop the flag is checked
        System.out.println("stop button pressed");
        fPars.infiniteLoopInterrupted = true;
    }//GEN-LAST:event_buttonStopInfiniteLoopActionPerformed
    
    private void setDefaultValuesIFSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultValuesIFSActionPerformed
        fPars.setDefaultParameters(new FractalIFS(this, fPars) );
        refreshInputFields();
    }//GEN-LAST:event_setDefaultValuesIFSActionPerformed
    
    private void setDefaultValuesMandelbrotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultValuesMandelbrotActionPerformed
        fPars.setDefaultParameters(new FractalMandelbrot(this, fPars) );
        refreshInputFields();
    }//GEN-LAST:event_setDefaultValuesMandelbrotActionPerformed
    
    private void setDefaultValuesNLFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultValuesNLFActionPerformed
        fPars.setDefaultParameters(new FractalNLF(this, fPars) );
        refreshInputFields();
    }//GEN-LAST:event_setDefaultValuesNLFActionPerformed
    
    private void setNewSize( int x, int y) {
        fPars.sizeX = x;
        fPars.sizeY = y;
        fractalSizeX.setText(Integer.toString(fPars.sizeX));
        fractalSizeY.setText(Integer.toString(fPars.sizeY));
        
        // because we set a new size of the image, we need to interrupt the worker
        fractalImage.interruptWorker();
        // wait?
        
        fractalImage.setSize(x,y);
    }
    
    private void setSizeButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSizeButton17ActionPerformed
        // set window size to 1024x768
        setNewSize(1024,768);
    }//GEN-LAST:event_setSizeButton17ActionPerformed
    
    private void setSizeButton86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSizeButton86ActionPerformed
        // set window size to 800x600
        setNewSize(800,600);
    }//GEN-LAST:event_setSizeButton86ActionPerformed
    
    /** the user pressed the repaint button, or he hit the enter key (default action)
     * so we need to repaint the entire image, based on the (maybe) new parameters
     */
    private void repaintButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repaintButtonActionPerformed
        if( evt.getActionCommand() == "Repaint" ) {
            
            oldPointMouse = null;
            
            // if the user resized the image window manually, get the new size now.
            // check if the fractalImage was really added to the container
            // note: it was not added, if there was an input error (see below)
            if( fractalImage.getSize().width != 0 ) {
                fPars.sizeX = fractalImage.getSize().width;
                fPars.sizeY = fractalImage.getSize().height;
                fractalSizeX.setText(Integer.toString(fPars.sizeX));
                fractalSizeY.setText(Integer.toString(fPars.sizeY));
            }
            
            FractalImage oldFractalImage = fractalImage;
            
            // interrupt the worker; we will start a new one!
            fractalImage.interruptWorker();
            
            fractalImage = null;
            
            fPars.infiniteLoopInterrupted = false;
            
            // find out what fractal type is selected
            switch( fractalType.getSelectedIndex()) {
                case 0:
                    // Mandelbrot or Julia
                    if(fractalTypeMandelbrot.isSelected())
                        fractalImage = new FractalMandelbrot(this, fPars);
                    else
                        fractalImage = new FractalJulia(this, fPars);
                    break;
                    
                case 1:
                    // Iteration Function
                    switch( iterationFunctions.getSelectedIndex()) {
                        case 0:
                            // IFS
                            fractalImage = new FractalIFS(this, fPars);
                            break;
                            
                        case 1:
                            // NLF
                            fractalImage = new FractalNLF(this, fPars);
                            break;
                            
                        case 2:
                            // Jumper
                            fractalImage = new FractalJumper(this, fPars);
                            break;
                            
                    }
                    break;
            }
            
            boolean inputOK = true;
            
            try {
                refreshFractalParameters();
            } catch (NumberFormatException nfe) {
                Toolkit.getDefaultToolkit().beep();
                inputOK = false;
            }
            
            
            if( inputOK == true ) {
                // only remove/add new fractalImage, if there is no input error; otherwise it will repaint anyway!
                // remove the "old" fractal from the container - because a new one will be created
                fractalFrame.getContentPane().remove(oldFractalImage);
                fractalFrame.getContentPane().add(fractalImage);
                fractalImage.addMouseListener(this);
                fractalImage.addMouseMotionListener(this);
                
                fractalFrame.pack();
                
                timer.start();
                
                // not needed - because of .add?
                //fractalImage.repaint(true);
            }
            
        }
    }//GEN-LAST:event_repaintButtonActionPerformed
    
    /** read all the parameters from the input fields in the GUI */
    private void refreshFractalParameters() {
        fPars.sizeX = Integer.parseInt(fractalSizeX.getText());
        fPars.sizeY = Integer.parseInt(fractalSizeY.getText());
        
        fPars.maxLength = Double.parseDouble(fractalMaxLen.getText());
        fPars.maxIterations = Integer.parseInt(fractalMaxIter.getText());
        fPars.xFix = Double.parseDouble(fractalXFix.getText());
        fPars.yFix = Double.parseDouble(fractalYFix.getText());
        fPars.xMin = Double.parseDouble(fractalXMin.getText());
        fPars.xMax = Double.parseDouble(fractalXMax.getText());
        fPars.yMin = Double.parseDouble(fractalYMin.getText());
        fPars.yMax = Double.parseDouble(fractalYMax.getText());
        
        fPars.xStart = Double.parseDouble(fractalXStart.getText());
        fPars.yStart = Double.parseDouble(fractalYStart.getText());
        fPars.aFix = Double.parseDouble(fractalA.getText());
        fPars.bFix = Double.parseDouble(fractalB.getText());
        fPars.sleep = Integer.parseInt(fractalSleep.getText());
        fPars.count = Long.parseLong(fractalCount.getText());
        fPars.infiniteLoop = checkBoxInfiniteLoop.isSelected();
        
        fPars.aJFix = Double.parseDouble(fractalAJFix.getText());
        fPars.bJFix = Double.parseDouble(fractalBJFix.getText());
        fPars.cJFix = Double.parseDouble(fractalCJFix.getText());
    }
    
    /** write all the parameters to the GUI */
    private void refreshInputFields() {
        fractalSizeX.setText(Integer.toString(fPars.sizeX));
        fractalSizeY.setText(Integer.toString(fPars.sizeY));
        
        fractalXMin.setText(Double.toString(fPars.xMin));
        fractalXMin.moveCaretPosition(0);
        fractalXMax.setText(Double.toString(fPars.xMax));
        fractalXMax.moveCaretPosition(0);
        fractalYMin.setText(Double.toString(fPars.yMin));
        fractalYMin.moveCaretPosition(0);
        fractalYMax.setText(Double.toString(fPars.yMax));
        fractalYMax.moveCaretPosition(0);
        fractalXFix.setText(Double.toString(fPars.xFix));
        fractalXFix.moveCaretPosition(0);
        fractalYFix.setText(Double.toString(fPars.yFix));
        fractalYFix.moveCaretPosition(0);
        fractalMaxLen.setText(Double.toString(fPars.maxLength));
        fractalMaxLen.moveCaretPosition(0);
        fractalMaxIter.setText(Integer.toString(fPars.maxIterations));
        fractalMaxIter.moveCaretPosition(0);
        
        fractalXStart.setText(Double.toString(fPars.xStart));
        fractalXStart.moveCaretPosition(0);
        fractalYStart.setText(Double.toString(fPars.yStart));
        fractalYStart.moveCaretPosition(0);
        fractalA.setText(Double.toString(fPars.aFix));
        fractalA.moveCaretPosition(0);
        fractalB.setText(Double.toString(fPars.bFix));
        fractalB.moveCaretPosition(0);
        fractalSleep.setText(Integer.toString(fPars.sleep));
        fractalCount.setText(Long.toString(fPars.count));
        checkBoxInfiniteLoop.setSelected(fPars.infiniteLoop);
        
        fractalAJFix.setText(Double.toString(fPars.aJFix));
        fractalAJFix.moveCaretPosition(0);
        fractalBJFix.setText(Double.toString(fPars.bJFix));
        fractalBJFix.moveCaretPosition(0);
        fractalCJFix.setText(Double.toString(fPars.cJFix));
        fractalCJFix.moveCaretPosition(0);
        
    }
    
    /**
     * main method
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new YaFGenMainFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupFractalType;
    private javax.swing.JButton buttonStopInfiniteLoop;
    private javax.swing.JCheckBox checkBoxInfiniteLoop;
    private javax.swing.JTextField fractalA;
    private javax.swing.JTextField fractalAJFix;
    private javax.swing.JTextField fractalB;
    private javax.swing.JTextField fractalBJFix;
    private javax.swing.JTextField fractalCJFix;
    private javax.swing.JTextField fractalCount;
    private javax.swing.JTextField fractalMaxIter;
    private javax.swing.JTextField fractalMaxLen;
    private javax.swing.JTextField fractalSizeX;
    private javax.swing.JLabel fractalSizeXLabel;
    private javax.swing.JTextField fractalSizeY;
    private javax.swing.JLabel fractalSizeYLabel;
    private javax.swing.JTextField fractalSleep;
    private javax.swing.JTabbedPane fractalType;
    private javax.swing.JRadioButton fractalTypeJulia;
    private javax.swing.JRadioButton fractalTypeMandelbrot;
    private javax.swing.JTextField fractalXFix;
    private javax.swing.JTextField fractalXMax;
    private javax.swing.JTextField fractalXMin;
    private javax.swing.JTextField fractalXStart;
    private javax.swing.JTextField fractalYFix;
    private javax.swing.JTextField fractalYMax;
    private javax.swing.JTextField fractalYMin;
    private javax.swing.JTextField fractalYStart;
    private javax.swing.JTabbedPane iterationFunctions;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanelIFS;
    private javax.swing.JPanel jPanelIterationFunctions;
    private javax.swing.JPanel jPanelJumper;
    private javax.swing.JPanel jPanelMandelJulia;
    private javax.swing.JPanel jPanelNLF;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox jumperPresets;
    private javax.swing.JButton repaintButton;
    private javax.swing.JButton setDefaultValuesIFS;
    private javax.swing.JButton setDefaultValuesMandelbrot;
    private javax.swing.JButton setDefaultValuesNLF;
    private javax.swing.JButton setSizeButton1;
    private javax.swing.JButton setSizeButton2;
    private javax.swing.JButton setSizeManually;
    private javax.swing.JLabel sizeLabel;
    // End of variables declaration//GEN-END:variables
    
    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = { "a", "b", "c", "d", "e", "f" };
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public int getRowCount() {
            return 6;
        }
        
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return new Double(fPars.a[row]);
                    
                case 1:
                    return new Double(fPars.b[row]);
                    
                case 2:
                    return new Double(fPars.c[row]);
                    
                case 3:
                    return new Double(fPars.d[row]);
                    
                case 4:
                    return new Double(fPars.e[row]);
                    
                case 5:
                    return new Double(fPars.f[row]);
            }
            return (String)"Dummy";
        }
        
        public Class getColumnClass(int c) {
            return new Double(1.0).getClass();
        }
        
        public boolean isCellEditable(int row, int col) {
            return true;
        }
        
        public void setValueAt(Object value, int row, int col) {
            double val = ((Double)value).doubleValue();
            
            switch (col) {
                case 0:
                    fPars.a[row] = val;
                    break;
                    
                case 1:
                    fPars.b[row] = val;
                    break;
                    
                case 2:
                    fPars.c[row] = val;
                    break;
                    
                case 3:
                    fPars.d[row] = val;
                    break;
                    
                case 4:
                    fPars.e[row] = val;
                    break;
                    
                case 5:
                    fPars.f[row] = val;
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }
    
    class MyComboBoxModel implements ComboBoxModel {
        private String[] presetNames = { "Tubes Symmetric", "Carpet Tiled",
        "Carpet Endless", "Raindrops from Right", "Flower 1", "Insect",
        "Stem 1", "Stem 2" };
        
        Object selectedItem = presetNames[0];
        
        public int getSize() {
            return presetNames.length;
        }
        
        public Object getElementAt(int index) {
            return presetNames[index];
        }
        
        public Object getSelectedItem() {
            return selectedItem;
        }
        
        public void setSelectedItem(Object anItem) {
            // another item in the combo box was selected
            selectedItem = anItem;
            
            // set the default parameters for this new type
            fPars.setDefaultParameters(new FractalJumper(YaFGenMainFrame.this, fPars) );
            // and now set the specific presets
            fPars.setPresetParameters( jumperPresets.getSelectedIndex() );
            // and refresh the fields on the screen
            refreshInputFields();
        }
        
        public void removeListDataListener(ListDataListener l) { }
        public void addListDataListener(ListDataListener l) { }
        
    };
    
    
    class MyDoubleVerifier extends InputVerifier implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            JTextField source = (JTextField)e.getSource();
            shouldYieldFocus(source); //ignore return value
            source.selectAll();
        }
        
        public boolean shouldYieldFocus(JComponent input) {
            boolean inputOK = verify(input);
            
            if (inputOK) {
                return true;
            } else {
                Toolkit.getDefaultToolkit().beep();
                return false;
            }
        }
        
        //This method checks input, but should cause no side effects (e.g. no beeping)
        public boolean verify(JComponent input) {
            boolean wasValid = true;
            
            try {
                double amount = Double.parseDouble( ((JTextField)input).getText() );
            } catch (NumberFormatException nfe) {
                wasValid = false;
            }
            return wasValid;
        }
        
    }
    class MyIntegerVerifier extends InputVerifier implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            //JTextField source = (JTextField)e.getSource();
            //shouldYieldFocus(source); //ignore return value
            //source.selectAll();
        }
        
        public boolean shouldYieldFocus(JComponent input) {
            boolean inputOK = verify(input);
            
            if (inputOK) {
                return true;
            } else {
                Toolkit.getDefaultToolkit().beep();
                return false;
            }
        }
        
        //This method checks input
        public boolean verify(JComponent input) {
            boolean wasValid = true;
            
            try {
                double amount = Integer.parseInt( ((JTextField)input).getText() );
            } catch (NumberFormatException nfe) {
                wasValid = false;
            }
            return wasValid;
        }
        
    }
    
    
}
