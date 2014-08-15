/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.j;

import client.Main;
import client.StaticData;
import client.x.InnerWindow;
import client.x.XMainWindow;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.WindowListener;
import java.util.Hashtable;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Administraattori
 */
public class JMainWindow extends JFrame implements XMainWindow {

    private JMainWindowActionListener MAINACTION;
    private JDesktopPane desktop;
    private JMenuBar MENUBAR;
    private Hashtable<String,InnerWindow> WINDOWS = new Hashtable<String,InnerWindow>();
    private Cursor defaultCursor;
    
    JMenuItem connect;
    JMenuItem disconnect;
    JMenuItem login;
    JMenuItem logout;
    
    public JMainWindow(Main main) {       
        MAINACTION = new JMainWindowActionListener( main );
        this.MAINACTION = MAINACTION;
        desktop = new JDesktopPane();
        this.setTitle( StaticData.getWindowTitle() );
        this.setSize( StaticData.getWindowSize() );        
        this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        this.addWindowListener( MAINACTION );
        this.setLocationRelativeTo( null );
        this.setContentPane( desktop );
        this.setExtendedState( JFrame.MAXIMIZED_BOTH );
        setMenubar();
        this.setVisible( true );        
    }

    public JDesktopPane getDesktopPane() {
        return desktop;
    }

    private JMenu getEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        
        editMenu.addSeparator();
        JMenuItem settings = new JMenuItem("Settings");
        settings.setActionCommand( "editSettings" );
        settings.addActionListener( this.MAINACTION );
        editMenu.add( settings );
        return editMenu;
    }
    
    private JMenu getConnectionMenu() {
        JMenu connectionMenu = new JMenu("Connection");
        
        connect = new JMenuItem("Connect");
        disconnect = new JMenuItem("Disconnect");
        
        login = new JMenuItem("Log In");
        logout = new JMenuItem("Log Out");
        
        JMenuItem close = new JMenuItem("Close");
        
        connect.setActionCommand( "connect" );
        disconnect.setActionCommand( "disconnect" );
        
        login.setActionCommand( "login" );
        logout.setActionCommand( "logout" );
        
        close.setActionCommand( "close" );
        
        connect.addActionListener( MAINACTION );
        disconnect.addActionListener( MAINACTION );
        
        login.addActionListener( MAINACTION );
        logout.addActionListener( MAINACTION );
        
        close.addActionListener( MAINACTION );
        
        connectionMenu.add(connect);
        connectionMenu.add(disconnect);
        
        connectionMenu.addSeparator();
        
        connectionMenu.add( login );
        connectionMenu.add( logout );
        
        connectionMenu.addSeparator();
        
        connectionMenu.add( close );
        return connectionMenu;
    }
    
    private void setMenubar() {
        MENUBAR = new JMenuBar();
        JMenu connectionMenu = this.getConnectionMenu();
        MENUBAR.add( connectionMenu );
        
        JMenu editMenu = this.getEditMenu();
        MENUBAR.add( editMenu );
        
        this.setJMenuBar( MENUBAR );
        
        this.setConnected( false );
        this.setLoggedIn( false );
    }

    public void addWindow(InnerWindow innw) {        
        this.WINDOWS.put( innw.getWindowcode() , innw );
        JInnerWindow jiw = (JInnerWindow) innw;
        jiw.setMainWindow( this );
        this.desktop.add( jiw );
    }
    
    public InnerWindow getWindow(String windowCode) {
        if (windowCode != null) {
        if (this.WINDOWS.containsKey( windowCode )) {
            return this.WINDOWS.get( windowCode );
        } else {
            return null;
        }
        } return null;
    }
    
    public void removeWindow(InnerWindow innerWindow) {
        this.WINDOWS.remove( innerWindow.getWindowcode() );        
        innerWindow.dispose();
        innerWindow = null;
    }
    
    public void setConnected( boolean bool) {
        if (bool) {
            this.connect.setEnabled( false );
            this.disconnect.setEnabled( true );
        } else {
            this.connect.setEnabled( true );
            this.disconnect.setEnabled( false );
        }
    }
    
    public void setLoggedIn( boolean bool ) {
        if (bool) {
            
            this.login.setEnabled( false );
            this.logout.setEnabled( true );
        } else {
            this.login.setEnabled( true );
            this.logout.setEnabled( false );
        }
    }

    @Override
    public void showMessage(String sMessage) {
       JOptionPane.showInternalMessageDialog(this.getDesktopPane(), sMessage);
    }

    @Override
    public void busy(boolean b) {
        if (this.defaultCursor == null) {
            this.defaultCursor = this.getCursor();
        }
        if (b) {
            this.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
        } else {
            this.setCursor( this.defaultCursor );
        }
    }
    
}
