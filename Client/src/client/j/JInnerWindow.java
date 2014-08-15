/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.j;

import client.x.InnerWindow;
import javax.swing.JInternalFrame;

/**
 *
 * @author Administraattori
 */
abstract public class JInnerWindow extends JInternalFrame implements InnerWindow {
    
    private String sWindowCode;
    private JMainWindow MAINWINDOW = null;
    
    public JInnerWindow( String sWindowCode ) {
        this.setWindowcode( sWindowCode );
    }
    
    abstract public void getObject(Object o);

    @Override
    public void setWindowcode(String sCode) { this.sWindowCode = sCode; }

    @Override
    public String getWindowcode() { return sWindowCode; }
    
    public void setMainWindow( JMainWindow mw ) { this.MAINWINDOW = mw; } 
    public JMainWindow getMainWindow() { return this.MAINWINDOW; }
    
}
