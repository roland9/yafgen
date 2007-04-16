/*
 * MacOSAboutHandler.java
 *
 * Created on April 14, 2007, 9:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package yafgen;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.Application;
import javax.swing.JFrame;

public class MacOSAboutHandler extends Application {

    public MacOSAboutHandler() {
        addApplicationListener(new AboutBoxHandler());
    }

    class AboutBoxHandler extends ApplicationAdapter {
        public void handleAbout(ApplicationEvent event) {
            new AboutDialog().show();
        }
    }
}