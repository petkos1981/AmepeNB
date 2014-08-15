/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server.cw;

import javax.swing.table.AbstractTableModel;
import server.Main;

/**
 *
 * @author Administraattori
 */
public class ConnectionsTableModel extends AbstractTableModel {

    private String[] sColumns = {"ID","IP-Address","Time","Logged"};
    
    private Main MAIN;
    private String[][] data = new String[0][0];
    
    public ConnectionsTableModel(Main mMain) {
        MAIN = mMain;
    }

    @Override
    public int getRowCount() { return data.length; }

    public String getColumnName(int iColumnIndex) { return sColumns[iColumnIndex]; }
    
    @Override
    public int getColumnCount() { return sColumns.length; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.data[rowIndex][columnIndex];
    }

    public void updateData() {
        String[][] data = MAIN.getConnections();
        this.data = data;
    }

    public long getID(int iRow) {
        long l = -1;        
        if (iRow > -1) l = Long.parseLong( this.data[iRow][0]);
        return l;
    }
    
}
