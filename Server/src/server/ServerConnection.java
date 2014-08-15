package server;

import common.LogInfo;
import common.Sendable;
import common.XMLObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnection extends Thread {

    private BufferedReader input = null;
    private BufferedWriter output = null;
    private Socket socket = null;
    private boolean bRunning = true;
    private ServerThread serverThread;
    private Date connectionTime;
    private String client_ip;
    private ConnectionJobManager jobmanager;
    
    private boolean bReady = false;
    private boolean bLoggedIn = false;
    private boolean bForceClose = false;
    
    public ServerConnection(Socket clientSocket, Date stamp) {
		
        socket = clientSocket;
        connectionTime = stamp;
        
        client_ip = socket.getInetAddress().getHostAddress();
        
        try {
			
            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();
			
            input = new BufferedReader( new InputStreamReader(is) );			
            output = new BufferedWriter( new OutputStreamWriter(os) );
			
        } catch (IOException e) {		
            e.printStackTrace();
        }
		
        jobmanager = new ConnectionJobManager( this );
        jobmanager.start();
    }
    
    public String[] toStringArray() {
        String sLoggedIn = "";
        if (this.bLoggedIn) {
            sLoggedIn = "Yes";
        } else sLoggedIn = "No";
        String[] data = {String.valueOf(this.getId()),this.client_ip,this.connectionTime.toString(),sLoggedIn};
        return data;
    }
	
    @Override
    public void run() {
        System.out.println("ClientThread Started!");
        String sLine;
        String sInput;
        this.writeln("hi!");        
        boolean bReading = true;
        while (bRunning) {
            if (this.socket.isClosed()) bRunning = false;
            sInput = "";
            try {                
                sLine = input.readLine();
                if (sLine == null) {
                    
                } else {
                    do {                                                                        
                        if ( !sLine.equals("---------------- END -----------------") ) {                
                            sInput += sLine;
                        } else {
                            break;
                        }
                        sLine = input.readLine();
                        if (sLine == null) bReading = false;
                    } while ( bReading );               
                    
                    this.readLine( sInput );                                        
                }
				
            } catch (IOException e) {
                if (!bForceClose) System.out.println("ClientThread IO Error: " + e.getMessage() );
                bRunning = false;
            } catch (NullPointerException e2) {
                System.out.print("InputStream is null: " + e2.getLocalizedMessage());
                if (this.socket.isConnected()) {
                    System.out.print(", but socket still connected");
                }
                System.out.println("");
                bRunning = false;
            }
        }
        
        close();
        System.out.println("ClientThread Stopped!");
    }
	
    public void terminate() {
        System.out.println("Terminate signal");
        bRunning = false;
    }
    
    private void close() {
        System.out.println("Closing JobManager");
        this.jobmanager.terminate();
        System.out.println("Removing from ServerThread");
        this.serverThread.remove( this );
        System.out.print("Closing thread connection...");
        if (!this.bForceClose){
            if (input != null) {
                try {
                    System.out.print("input...");                
                    input.reset();
                    socket.shutdownInput();                
                    input.close();                
                    input = null;
                } catch (IOException e) {}
                finally {
                    input = null;
                }
            }
        
		
            if (output != null) {
                try {                
                    System.out.print("output...");
                    socket.shutdownOutput();
                    output.close();
                    output = null;
                } catch (IOException e) {}
                finally {
                    output = null;
                }
            }
	}	
        if (socket != null) {
            try {
                System.out.print("socket...");
                socket.close();
                socket = null;
            } catch (IOException e) {}
            finally {
                socket = null;
            }
	}
	System.out.println("Done!");
        
    }

    void setServerThread(ServerThread st) {
        this.serverThread = st;
    }

    private void writeln(String text) {
        try {
            // System.out.println("-> " + text);
            this.output.write(text + "\n");
            this.output.write("---------------- END -----------------\n");
            this.output.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readLine(String sLine) {
        // System.out.println("<- '" + sLine + "'");
        if (!this.bReady) {
            if (sLine.equals("hi!")) {
                bReady = true;
                System.out.println("ready!");
            }
        } else {
            if (sLine.equals("bye!")) {
                bReady = false;
                this.terminate();
            } else {
                Object oObject = XMLObject.getObject(sLine);
                if (oObject != null) {
                    if (oObject instanceof LogInfo) {
                        this.login((LogInfo) oObject);
                    } else {
                        if (this.bLoggedIn) {
                            if (oObject instanceof Sendable) {
                                this.jobmanager.add( (Sendable) oObject );
                            }
                        }
                    }
                }
            }
        }
    }

    private void login(LogInfo logInfo) {
        System.out.println("Got LogInfo!");
        if (logInfo.getStatus() == LogInfo.TRY) {
            boolean bUsername = false;
            boolean bPassword = false;
        
            if (logInfo.getUsername().equals("testi")) {
                bUsername = true;
            }
        
            if (logInfo.getPassword().equals("075a421a01fe4984b4ade4a89afec861f9a435f54b5bced6d0a0e5a8792e521c")) {
                bPassword = true;
            }
        
            logInfo.setPassword("");
        
            if (bUsername && bPassword) {            
                logInfo.setStatus( LogInfo.SUCCESS );
                logInfo.setFirstName("Petri");
                logInfo.setLastName("Koskelainen");
                bLoggedIn = true;
            } else {
                logInfo.setStatus( LogInfo.FAIL );
            }
            String sData = XMLObject.getXML( logInfo );
            this.writeln( sData );
        }
        
        if (logInfo.getStatus() == LogInfo.LOG_OUT) {
            this.bLoggedIn = false;
            send(logInfo);
        }
    }

    void send(Sendable sendable) {
        String sData = XMLObject.getXML( sendable );
        this.writeln( sData );
    }

    void forceClose() {
        bForceClose = true;
        this.close();
    }

}
