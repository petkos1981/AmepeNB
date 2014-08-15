/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Administraattori
 */
public class ServerActionListener implements ActionListener {
    private final Main MAIN;

    public ServerActionListener(Main MAIN) {
        this.MAIN = MAIN;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            
            case "start":
                this.startAction();
                break;
                
            case "stop":
                this.stopAction();
                break;
                
            case "status":
                this.statusAction();                
                break;

            case "cmanager":
                this.cmanagerAction();
                break;
                
            case "debugw":
                this.debugwAction();
                break;
                
            case "exit":
                this.exitAction();
                break;
        }
    }
    
     public void showMessage(String sMessage) {
        this.MAIN.SST.trayIcon.displayMessage("The Server Message", sMessage, TrayIcon.MessageType.INFO);
    }
    
    private void startAction() {
        if (MAIN.bSystemTray) showMessage( this.MAIN.startServer());
        else JOptionPane.showMessageDialog(null,  this.MAIN.startServer() );
    }

    private void stopAction() {
        if (MAIN.bSystemTray) showMessage( this.MAIN.stopServer());
        else JOptionPane.showMessageDialog(null,  this.MAIN.stopServer() );
    }

    private void statusAction() {        
        JOptionPane.showMessageDialog(null, this.MAIN.getStatusMessage() );
    }

    private void exitAction() {
        JOptionPane.showMessageDialog(null, "Bye Bye!" );
        this.MAIN.exit();
    }

    private void cmanagerAction() {
        this.MAIN.connectionManagerWindow();
    }

    private void debugwAction() {
        this.MAIN.toggleDebugWindowVisibility();
    }
    
}
