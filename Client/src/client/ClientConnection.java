package client;

import common.LogInfo;
import common.XMLObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ClientConnection extends Thread {

    private BufferedReader input = null;
    private BufferedWriter output = null;
    private Socket socket = null;
    private boolean bRunning = true;
    private boolean bReady = false;
    private final Main MAIN;
	
    public ClientConnection(Main MAIN, Socket socket) {
        this.socket = socket;
	this.MAIN = MAIN;
        try {
            InputStream is = this.socket.getInputStream();
            OutputStream os = this.socket.getOutputStream();
			
            input = new BufferedReader( new InputStreamReader(is) );			
            output = new BufferedWriter( new OutputStreamWriter(os) );
			
        } catch (IOException e) {		
            e.printStackTrace();
        }
    }
	
    public void run() {
        System.out.println("ClientThread Started!");
	String sInput = "";
	String sLine = null;
        boolean bMultiline;
        boolean bReading = true;
	while (bRunning) {
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
                
            } catch(NullPointerException | IOException e1) {                
                e1.printStackTrace();
                System.out.println( "CC-Error: " + e1.getMessage() );
                if (input == null) {
                    System.out.println(" is input");
                }
                bRunning = false;                
                break;
            }
        }
	close();
        System.out.println("ClientThread Stopped!");
    }
	
    public void terminate() {
        System.out.println("terminate signal");
        this.writeln("bye!");
        bRunning = false;
    }
	
    public void writeln(String sLine) {
        
	if (output != null)
	try {
            // System.out.println("-> " + sLine);
            output.write(sLine + "\n");
            output.write("---------------- END -----------------\n");
            output.flush();
        } catch (IOException e) {	
            e.printStackTrace();
        }
    }
	
    private void close() {
        System.out.print("Closing connection...");
        
        if (input != null) {
            try {
                System.out.print("input...");
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
                output.close();
                output = null;
            } catch (IOException e) {}
            finally {
                output = null;
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
        MAIN.MAINWINDOW.showMessage("Disconnected!");
    }

    private void readLine(String sLine) {
        // System.out.println("<- " + sLine);
        if (!this.bReady) {
            if (sLine.equals("hi!")) {
                bReady = true;
                this.writeln("hi!");
                System.out.println("ready!");
            }
        } else {
            Object oObject = XMLObject.getObject(sLine);
            if (oObject != null) {
                Runnable r = new Runnable() {
                    public void run() {
                        if (oObject instanceof LogInfo) {
                            System.out.println("Received LogInfo!");
                            LogInfo info = (LogInfo) oObject;
                            MAIN.receive(info);
                        }
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        }
    }
    
}