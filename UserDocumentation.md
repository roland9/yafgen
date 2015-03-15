# YaFGen - User Manual #
Version 1.0, created on 11. April 2007, 19:34

YaFGen - Yet another Fractal Generator - Generate images based on mathematical formulas
Copyright (C) 2007  Roland Gröpmair

This file is part of YaFGen.

YaFGen is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

YaFGen is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with YaFGen; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

To contact the author, please send an email to the following address: rgropmair "at" gmail.com


# Overview #

## What is YaFGen? ##
YaFGen is an acronym, and stands for Yet Another Fractal Generator. It generates images, based on mathematical formulas. For more information about fractals, refer to http://en.wikipedia.org/wiki/Fractals.
It was developed by me, using NetBeans and the Java programming language.

## Why another fractal generator? ##
If you do some research on the web, you will find _lots_ of applications that generate images of fractals. Some are well maintained and have an active community, others are pretty outdated.

So why did I write _another_ one? Well, to put it in simple words, there are two reasons:
I am fascinated by fractals, and as a typical software engineer, I have to implement my own tool!  ;-)
I always wanted to publish an open source program. Java is the programming language I know best, so there you are!

I hope you find YaFGen useful. But of course, I cannot 'beat' (and never intended to!) more advanced and sophisticated applications (like XaoS, for instance).

## Where can I get the software from? ##
From the YaFGen home page http://rgropmair.googlepages.com/yetanotherfractalgenerator or at the Google Code YaFGen project page http://code.google.com/p/yafgen/

# Quick Start #
## System Requirements, Installing ##
Because YaFGen was written in Java, it should run on any platform that supports a recent Java version. That means, you need to have a Java Runtime Environment (JRE) installed. In most cases, you already have it installed! Just give it a try:
Download YaFGen\_V1.0.zip and extract it to any place on your computer. Double click the YaFGen.jar file. That should do it.

If there is an error, you might not have the required Java environment. Check out Sun's Java Download website for more information or download the JRE for your platform.
I have developed the application using NetBeans 5.5, and J2SE 5.0. But because it does not use any of the 5.0-features of Java, it should also run on J2SE 1.4.

YaFGen was successfully tested on the following platforms:
  * Windows XP, with Java 1.4
  * Ubuntu 7.04 with Sun Java 1.5 and Sun Java 6
  * Mac OS X Version 10.3 with Java 1.4 and 10.4 with Java 1.5

## Your first fractal with YaFGen ##
Let's produce our first fractal!
Start YaFGen. You should see two windows side by side. The left one is the window displaying the generated fractal. The right one is the parameter window where you can select the fractal type and change various input parameters.

Here is an example of the output window with the initial Mandelbrot fractal.


Here is a screenshot of the input window.

One important thing to remember. Whenever you change parameters in the Input Parameters window, just hit the Return key or press the "Repaint" button to - well you guessed it - repaint the fractal.
The application validates the input; so if you enter an invalid float value, it will not repaint, until you correct it.


## Manipulating input parameters ##
Let's do something more exciting. Select another fractal type by pressing the "Iteration Functions" tab. In this version, there are three "Iteration Functions" implemented: "IFS", "NLF" and "Jumper".

Please select "Jumper", just for fun. In order to have some interesting input values, select one of the default settings, e.g. "Raindrops from Right". I just made up the names after one glass of red wine; sorry if they sound a bit strange ;-)

Note that by selecting one of the default settings, the corresponding values in the Input Parameters window change. Then hit Return, or press "Repaint". Voilà, you have another marvelous example of a computer generated image!

In fact, this one should take a while. Basically, it is growing endlessly!
Remark: There is a value "Sleep" (initially set to 10) that defines how much it slows down the generation of the image. The higher the value, the longer it delays - so that you are able to see the image 'growing'. If you have a slow computer, or want quick results (kind of impatient, are you?), then reduce this value, or even set it to 0. Then there is no delay any more, and your computer is calculating at maximum speed. Well, at least at the maximum speed the Java Virtual Machine gives you.
If you want to stop the endless processing of this image, just hit the button “Stop Infinite Loop”. The next time you change parameters, and select the “Repaint”, it starts all over again.
Note the check box “Infinite Loop”. Only if this check box is active, the application will paint endlessly. That means the image is growing and growing... If the check box is inactive, then it will only paint the number of pixels stated in the “Count” input field. Sound maybe confusing. Just give it a try, that might be easier to understand...

## Zooming ##
There is another useful feature that might not be obvious. But because I spent some time on it to program it, I have to tell you. The feature allows you to zoom in and out of the image.
Drag a rectangle in the fractal window with the left mouse button to define the area of the current image you want to zoom to.

Use the right mouse button to zoom out. Think of the other way around: The image will be scaled in a way that it fits into the rectangle you have just defined.

# Credits #
Thanks to Diane for some beta-testing on Windows XP.

I have used various sources for implementing YaFGen:
  * Sun, The Java Tutorials - Creating a GUI with Swing: http://java.sun.com/docs/books/tutorial/uiswing/index.html (especially useful: the chapter “Lesson: Concurrency in Swing”)
  * Netbeans, Java Online Documentation
  * The excellent book "Fraktale Geometrie" (Hofacker Verlag, 1989) by E. D. Schmitter had stimulated my interest many years ago, and has lead finally to this application. Thank you!
  * Mathematische Basteleien, http://www.mathematische-basteleien.de/huepfer.htm – some insights for the Huepfer fractal. A great resource!

# Planned Features for Future Releases #
As always, there are some features I was not able to put into this release. If you find YaFGen useful, let me know, and I might spend some time to implement them.

Some of them are:
  * Saving of fractal images
  * Saving/loading of input parameters
  * Applet version of the application
  * Various color profiles
  * Animations (e. g. of Julia fractals)
  * Antialiasing
...