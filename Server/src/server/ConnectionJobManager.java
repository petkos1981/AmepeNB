/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import common.Sendable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administraattori
 */
public class ConnectionJobManager extends Thread {
    
    private boolean bRunning = true;
    private final ServerConnection connection;
    private Vector<Sendable> jobline = new Vector<Sendable>();
    
    public ConnectionJobManager(ServerConnection conn) {
        this.connection = conn;
    }
    
    public void run() {
        Sendable sendable;
        while ( bRunning ) {
            if (!jobline.isEmpty()) {
                sendable = jobline.firstElement();
                jobline.remove( sendable );
                this.handle( sendable );
            } else {
                
            }
        }
    }
    
    private void send( Sendable sendable ) {
        this.connection.send( sendable );
    }
    
    public void terminate() {
        bRunning = false;
    }

    void add(Sendable sendable) {
        this.jobline.add( sendable );
        try {
            if ( jobline.isEmpty() ) this.notify();
        } catch (Exception e) {}
    }

    private void handle(Sendable sendable) {
        
    }
}
