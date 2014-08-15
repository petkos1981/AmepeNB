package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class ServerThread extends Thread {
    
    int iServerPort = 44440;
    boolean bRunning = true;
        
    private Hashtable<Long,ServerConnection> htConnections = new Hashtable<Long,ServerConnection>();
    private ServerSocket serverSocket;
    private boolean bShuttingDown = false;
    public ServerThread() {
	init();
    }
    
    
    void init() {
        		
	serverSocket = null;
        
        try {
            System.out.print( "Starting server socket on port " + this.iServerPort );
            System.out.println( " @ " + InetAddress.getLocalHost().getHostAddress() );
            serverSocket = new ServerSocket( this.iServerPort , 10 , InetAddress.getLocalHost() );            
        } catch (IOException e) {
            System.out.println( e.getLocalizedMessage() );
            return;
        }
    }
	
    public void add(ServerConnection newConnection) {
        htConnections.put( new Long(newConnection.getId()), newConnection);
    }	        
        
    public void run() {
        Socket clientSocket;
        Calendar calendar = Calendar.getInstance();
        Date now;
        ServerConnection newConnection = null;
        System.out.println("Waiting connections...");
        while (this.bRunning) {
            if (serverSocket != null) {
                try {
                    clientSocket = serverSocket.accept();
                    System.out.println("Connection made!");
                    now = calendar.getTime();
                    newConnection = new ServerConnection(clientSocket, now);
                    System.out.println("- Thread made for connection!");
                    this.add(newConnection);
                    System.out.println("- Thread added to the pool");
                    System.out.println("- Starting the thread");
                    newConnection.setServerThread( this );
                    newConnection.start();
                    System.out.println("Waiting for next connection...");
                } catch (IOException e) {
                    if (this.bShuttingDown) {
                        
                    } else {
                        System.out.println("ServerThread IO-Error!");
                    }
                    bRunning = false;
                }
            } else {
                System.out.println("No serverSocket!");
                bRunning = false; 
            }
        }
        this.stopServer1();
    }

    public void stopServer() {
        bShuttingDown = true;        
        try {
            if (serverSocket != null) serverSocket.close();        
        } catch (IOException e) {}
        this.bRunning = false;        
    }
    
    synchronized boolean isServerSocketNull() {
        if (this.serverSocket == null) return true;
        else return false;
    }
    
    synchronized private void stopServer1() {
        System.out.println("Stopping");
        if (htConnections.size() > 0) this.closeAllConnections();
        
        if (serverSocket != null) {
            try {
                System.out.println("Closing server socket");
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {}
        }
        System.out.println("socket is now null");
    }

    public void closeAllConnections() {
        System.out.print("Closing all connections...");
        Enumeration<Long> keys = htConnections.keys();
        ServerConnection conn;
        Long key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            conn = htConnections.get(key);
            htConnections.remove(key);
            conn.terminate();            
        }
        System.out.println("Done!");
    }

    synchronized boolean isRunning() {
       return this.bRunning;
    }

    synchronized int getConnectionCount() {
        return htConnections.size();
    }

    synchronized void remove(ServerConnection aThis) {
        System.out.println("Removing Thread id: " + aThis.getId() );
        this.htConnections.remove( new Long(aThis.getId()) );        
    }

    String socketState() {
        if (this.serverSocket == null) return "null";
        else {
            String sMessage = "";            
            if (serverSocket.isBound()) sMessage = " | bounded | ";
            if (serverSocket.isClosed()) sMessage += " | closed | ";
            try {
                if (serverSocket.getReuseAddress()) sMessage += " | reused | ";
            } catch (IOException e) {}
            return sMessage;
        }        
    }

    void closeSocketAndSetNull() {
        if (!this.serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {}
        }
        serverSocket = null;
    }
    
    public synchronized String[][] tableList() {
        int iRowCount = this.htConnections.size();
        int iColumnCount = 3;
        String[][] data = new String[iRowCount][iColumnCount];
        Enumeration<Long> keys = htConnections.keys();
        ServerConnection conn;
        Long key;
        int i = 0;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            conn = htConnections.get(key);
            data[i] = conn.toStringArray();
            i++;
        }
        return data;
    }

    void closeConnection(long lID) {
        if (this.htConnections.containsKey( new Long( lID ))) {
            ServerConnection sc = this.htConnections.get( lID );
            sc.terminate();
            sc.forceClose();
        } else {
            System.out.println(" Thread ID " + lID + " not found");
        }
    }
	
}