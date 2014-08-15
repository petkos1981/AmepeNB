/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Administraattori
 */
public class ServerSystemTray {

    final PopupMenu popup = new PopupMenu();
    final TrayIcon trayIcon = new TrayIcon(createImage("systemtrayicon.png", "tray icon"));
    final SystemTray tray = SystemTray.getSystemTray();
    final Main MAIN;
    
    ServerSystemTray(Main MAIN) {
        this.MAIN = MAIN;
        trayIcon.setToolTip("The Server Menu");
        trayIcon.addActionListener( MAIN.actionListener );
        trayIcon.setActionCommand( "cmanager" );
        trayIcon.setPopupMenu( popup );
        MenuItem start = new MenuItem("Start");
        MenuItem stop = new MenuItem("Stop");
        MenuItem status = new MenuItem("Status");
        MenuItem cmanager = new MenuItem("Connections Manager");
        MenuItem debugw = new MenuItem("Debug Window");
        MenuItem exit = new MenuItem("Exit");
        
        start.setActionCommand("start");
        stop.setActionCommand("stop");
        status.setActionCommand("status");
        cmanager.setActionCommand("cmanager");
        debugw.setActionCommand("debugw");
        exit.setActionCommand("exit");
        
        start.addActionListener( MAIN.actionListener );
        stop.addActionListener( MAIN.actionListener );
        status.addActionListener( MAIN.actionListener );
        cmanager.addActionListener( MAIN.actionListener );
        debugw.addActionListener( MAIN.actionListener );
        exit.addActionListener( MAIN.actionListener );
        
        popup.add(start);
        popup.add(stop);
        popup.add(status);
        popup.add(cmanager);
        popup.add(debugw);
        popup.add(exit);
        
        try {
            tray.add( trayIcon );
        } catch (AWTException e) {
            System.out.println( "Error: TrayIcon could not be added." );
        }
    }
    
    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        String sPath = ServerSystemTray.class.getResource(".").getPath();
        URL imageURL = ServerSystemTray.class.getResource(path); 
        if (imageURL == null) {
            System.err.println("Resource not found: " + sPath + path );
            System.out.println("Cannot continue.");
            System.exit(0);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
   
}
