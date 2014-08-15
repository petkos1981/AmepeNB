package server;

import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Main {
    
    ServerThread thread = null;
    ServerSystemTray SST = null;
    ConnectionsWindow CW = null;
    boolean bSystemTray = false;
    DebugWindow DEBUG;
    boolean bDebugWindowDefaultVisibility = false;
    
    ServerActionListener actionListener;
    
    public Main(String[] args) {
        System.out.print("Does graphics environment exist? ");
        if (!GraphicsEnvironment.isHeadless()) {
           System.out.println("Yes!");
           System.out.println("Initializing DebugWindow...");
           actionListener = new ServerActionListener( this );
           DEBUG = new DebugWindow();
           DEBUG.setVisible( bDebugWindowDefaultVisibility );
           try {
               DEBUG.init();               
           } catch(IOException e) {
               System.out.println("Error!");
           }
           System.out.println("DebugWindow Initialized!");
           System.out.print("Is SystemTray supported? ");
           if (SystemTray.isSupported()) {
               System.out.println("Yes!");
               System.out.print("Initializing SystemTray...");
               SST = new ServerSystemTray(this);
               bSystemTray = true;
               System.out.println("Done!");
           } else {
               System.out.println("No!");
               System.out.print("Initializing Window Interface...");
               System.out.println("not exist!");
               System.exit(0);
           }
        } else {
            System.out.println("No!");
            System.out.println("Bye bye!");
            System.exit(0);
        }
        
    }

    private void closeAllConnection() {
        thread.closeAllConnections();
    }
        
    private void shutdownServer() {
    
    }

    public static void main(String[] args) {
        new Main(args);
    }

    String getStatusMessage() {
        String sMessage = "";
        if (thread == null) {
            sMessage = "ServerThread is null\n";
        } else {
            if ( thread.isRunning() ) sMessage = "- ServerThread is running";
            else sMessage = "- ServerThread is stopped";
            sMessage = sMessage + "\n- Server has " + thread.getConnectionCount() + " connections alive";
            sMessage = sMessage + "\n- Server´s socket is " + thread.socketState();
        }
        return sMessage;
    }

    String startServer() {
        String sMessage = "Starting server!";
        if (thread != null) {
            if (!thread.isServerSocketNull()) {
                thread.closeSocketAndSetNull();
            }
            thread = null;
        }
        thread = new ServerThread();        
        thread.start();
        return sMessage;
    }

    String stopServer() {
        thread.stopServer();       
        return "Server stopped";
    }

    void exit() {
        if (thread != null) {
            if (thread.isAlive()) {
                System.out.println("Stopping server");
                thread.stopServer();                
            }
            if (!thread.isServerSocketNull()) {
                thread.closeSocketAndSetNull();
            }
        }
        if (thread != null) {
            if (thread.isServerSocketNull()) System.out.println("ServerSocket IS null!");
        }
        thread = null;
        
        System.exit(0);
    }

    void connectionManagerWindow() {
        if (CW == null) {
            CW = new ConnectionsWindow(this);            
        }
        
        if (!CW.isShowing()) {
            CW.setVisible( true );
        }
    }

    public String[][] getConnections() {
        if (thread != null) return thread.tableList();
        else return new String[0][0];
    }

    void closeConnection(long lID) {
        this.thread.closeConnection( lID );
    }

    void toggleDebugWindowVisibility() {
        if (this.DEBUG.isVisible()) {
            this.DEBUG.setVisible( false );
        } else this.DEBUG.setVisible( true );
    }

}