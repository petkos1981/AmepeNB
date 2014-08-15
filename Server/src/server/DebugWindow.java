/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Administraattori
 */
public class DebugWindow extends JFrame {
    
    JTextArea TEXT;
    private BufferedReader reader;
    private boolean bRunning = true;
    
    public DebugWindow() {
        this.setTitle("Debug");
        this.setSize( 640 , 480 );
        this.setLocation( 10 , 10 );                
        TEXT = new JTextArea();
        this.getContentPane().add( new JScrollPane(TEXT) );
    }
    
    public void init() throws IOException {
        PipedOutputStream pOut = new PipedOutputStream();
        System.setOut( new PrintStream(pOut) );
        PipedInputStream pIn = new PipedInputStream( pOut );
        reader = new BufferedReader( new InputStreamReader(pIn) );
        Thread t = new Thread() {
            public void run() {
                while( bRunning ) {
                    try {
                        String sLine = reader.readLine();
                        TEXT.append( sLine + "\n" );
                    } catch (IOException e) {}
                }
            }
        };
        t.start();
    }
    
    
    
    public void stop() {
        this.bRunning = false;
    }
    
}
