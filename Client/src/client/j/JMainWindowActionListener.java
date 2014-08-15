/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.j;

import client.Main;
import client.x.XMainWindow;
import common.LogInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Administraattori
 */
public class JMainWindowActionListener implements ActionListener, WindowListener {

    Main MAIN;
    
    public JMainWindowActionListener(Main aMain) {
        MAIN = aMain;
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        switch (e.getActionCommand()) {
            
            case "editSettings":
                this.editSettingsAction();                
                break;
                
            case "login":
                this.loginAction();
                break;
            
            case "logout":
                this.logoutAction();
                break;
            
            case "connect":
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        MAIN.MAINWINDOW.busy( true );
                        MAIN.makeConnection();
                        MAIN.MAINWINDOW.busy( false );
                    }
                };
                Thread t = new Thread(r);
                t.start();
                break;
                
            case "disconnect":
                MAIN.closeConnection();
                break;
                
            case "close":
                this.windowClosing();
                break;
        }        
    }

    @Override
    public void windowOpened(WindowEvent e) {            
    }

    
    
    @Override
    public void windowClosing(WindowEvent e) {
        this.windowClosing();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    
    }

    @Override
    public void windowIconified(WindowEvent e) {
    
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    
    }

    @Override
    public void windowActivated(WindowEvent e) {
    
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    
    }

    private void loginAction() {
        if (!this.MAIN.isLoggedIn()) {
            JLoginWindow LW = new JLoginWindow( MAIN );        
            this.MAIN.addWindow( LW );
            LW.setVisible( true );
        }
    }

    private void logoutAction() {
        System.out.println("logoutAction");
        if (this.MAIN.isLoggedIn()) {
            LogInfo li = new LogInfo();
            li.setStatus( LogInfo.LOG_OUT );
            System.out.println("Sending LOG_OUT");
            this.MAIN.send( li );
        } else {
            System.out.println("Already logged out!");
        }
    }

    private void windowClosing() {
        JMainWindow window = (JMainWindow) MAIN.MAINWINDOW;        
        int confirm = JOptionPane.showInternalOptionDialog(window.getDesktopPane(),
                        "Are You Sure to Close this Application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == JOptionPane.YES_OPTION) {
            window.dispose();
            MAIN.closeProgram();
        }
    }

    private void editSettingsAction() {
        JSettingsWindow SW = new JSettingsWindow( MAIN );
        this.MAIN.addWindow( SW );
        SW.setVisible( true );
    }
    
}
