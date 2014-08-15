package client;

import client.j.JMainWindow;
import client.j.JMainWindowActionListener;
import client.x.InnerWindow;
import client.x.XMainWindow;
import common.LogInfo;
import common.Sendable;
import common.StringTools;
import common.XMLObject;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main {
    private static String sLookAndFeelName = null;

    private boolean bLoggedIn = false;
    private String sAddress = null;
    private int iServerPort = -1;
    private ClientConnection connection = null;
    public XMainWindow MAINWINDOW = null;        
    
    
    public Main(String[] args) {
        Properties settings = StaticData.getSettings();
        this.sAddress = settings.getProperty( "ServerAddress" );                
        this.iServerPort = Integer.parseInt( settings.getProperty("ServerPort") );
        windowInit();        
    }
        
    private void windowInit() {        
        if (!GraphicsEnvironment.isHeadless()) {            
            SwingUtilities.invokeLater(() -> {
                MAINWINDOW = new JMainWindow( this );                
            });                        
        }
    }
    
    public synchronized boolean isLoggedIn() { return this.bLoggedIn; }
        
    public static void main(String[] args) {
        boolean bListOnly = false;
        if (args.length == 1) {
            if (args[0].equals("-listlnf")) {
                bListOnly = true;
            }
        }
        if (args.length > 1) {
            sLookAndFeelName = StringTools.searchFromArrayAndGetNext(args, "-lnf");
        }
        
        if (sLookAndFeelName == null) sLookAndFeelName = "Nimbus";
                
        System.out.println("System lnf:" + UIManager.getSystemLookAndFeelClassName());
        System.out.println("Lookin' for installed Look´n´Feels:");
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.print("- '"+info.getName() + "'");
                if (info.getClassName().equals(UIManager.getSystemLookAndFeelClassName())) { 
                    System.out.print(" (system)");
                }
                if (sLookAndFeelName.equals(info.getName())) {
                    if (!bListOnly) System.out.println(", yes!");
                    if (!bListOnly) UIManager.setLookAndFeel(info.getClassName());                    
                } else {
                    if (!bListOnly) System.out.println(", no");
                }
                if (bListOnly) System.out.println("");
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        System.out.println("");
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        if (!bListOnly) new Main(args);
    }
	
    public void makeConnection() {
        if (this.sAddress != null && this.iServerPort > -1) {
            if (connection == null) {
                System.out.println("Connecting...");
                try {
                
                    Socket socket = new Socket();
                    socket.connect( new InetSocketAddress( sAddress, iServerPort), 500);
                    connection = new ClientConnection( this , socket );
                    connection.start();
                    this.MAINWINDOW.setLoggedIn( false );
                    this.MAINWINDOW.setConnected( true );
                    MAINWINDOW.showMessage("Connected!");
                } catch (IOException e) {                
                    MAINWINDOW.showMessage( "FAIL! Can´t connect to server " + sAddress + ":" + iServerPort + "\nError message: " + e.getLocalizedMessage() );
                }
            
            } else {
                MAINWINDOW.showMessage("Already connected!");
            }
        }
    }
	
    public void closeConnection() {
        if (connection != null) {
            connection.terminate();
            connection = null;
        }        
        this.MAINWINDOW.setLoggedIn( false );
        this.MAINWINDOW.setConnected( false );
    }

    public void closeProgram() {
        this.closeConnection();
        System.exit( 0 );
    }


    public void receive(Sendable sendable) {                        
        String sWindowCode = sendable.getReceivingWindowcode();
        InnerWindow iw = this.MAINWINDOW.getWindow(sWindowCode);
        if (iw != null) {
            iw.getObject( sendable );
        } else {
            if (sendable instanceof LogInfo) {
                LogInfo li = (LogInfo) sendable;
                Runnable r = new Runnable() {
                    public void run() {
                        if (li.getStatus() == LogInfo.LOG_OUT) {
                            System.out.println("Log Out");
                            MAINWINDOW.setLoggedIn( false );
                            setLoggedIn( false );
                            MAINWINDOW.showMessage( "Logged out!");
                        }
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        }
    }

    public void addWindow(InnerWindow iw) {
        this.MAINWINDOW.addWindow( iw );
    }

    public void send(Sendable sendable) {
        String sendThis = XMLObject.getXML( sendable );
        if (connection != null) {
            this.connection.writeln( sendThis );
        }
    }

    public void removeWindow(InnerWindow innerWindow) {
       this.MAINWINDOW.removeWindow( innerWindow );
    }

    public void setXWindow(XMainWindow mainWindow) {
        this.MAINWINDOW = mainWindow;
    }

    public void setLoggedIn(boolean b) {
        this.bLoggedIn = b;
    }

}
