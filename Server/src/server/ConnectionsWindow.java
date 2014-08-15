/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import server.cw.ConnectionsTableModel;

/**
 *
 * @author Administraattori
 */
public class ConnectionsWindow extends JFrame implements ActionListener {

    private ConnectionsTableModel ctm;
    private JTable table;
    private JMenuBar jmb;
    private JScrollPane jsp;
    private final Main MAIN;
    
    ConnectionsWindow(Main mMain) {
        this.MAIN = mMain;
        this.setTitle( "Connections Manager" );
        this.setSize(640,480);
        this.setLocationRelativeTo( null );    
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
       
       
        jmb = new JMenuBar();
        JMenu jmMenu = new JMenu("Menu");
        JMenuItem jmiRefresh = new JMenuItem("Refresh");
        jmiRefresh.setActionCommand("refresh");
        jmiRefresh.addActionListener( this );
        jmMenu.add( jmiRefresh );
        jmb.add( jmMenu );
        this.setJMenuBar( jmb );
       
        JPopupMenu popupmenu = new JPopupMenu();
       
        JMenuItem closeConnection = new JMenuItem("Close connection");
        closeConnection.setActionCommand("closeconnection");
        closeConnection.addActionListener( this );
               
        popupmenu.add( closeConnection );
       
        ctm = new ConnectionsTableModel( mMain );
        table = new JTable( ctm );
        table.setComponentPopupMenu( popupmenu );
        jsp = new JScrollPane( table );
        this.getContentPane().add( jsp );
        
    }      

    @Override
    public void actionPerformed(ActionEvent e) {
        switch( e.getActionCommand() ) {
            case "refresh":
                this.refreshAction();
                break;        
                
            case "closeconnection":
                this.closeSelectedConnection();
                break;
        }
        
    }

    private void refreshAction() {
        ctm.updateData();
        ctm.fireTableDataChanged();
    }

    private long getConnectionID(int iRow) {
        long lID = this.ctm.getID( iRow );
        return lID;
    }

    private void closeSelectedConnection() {
        int iSelectedRow = this.table.getSelectedRow();
        if (iSelectedRow > -1) {
            long lID = this.getConnectionID( iSelectedRow );
            this.MAIN.closeConnection( lID );
        }
    }
        
}