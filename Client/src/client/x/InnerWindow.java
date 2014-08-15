/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.x;

import javax.swing.JInternalFrame;

/**
 *
 * @author Administraattori
 */
public interface InnerWindow {             
    
    public abstract void getObject(Object o);
    public void setWindowcode( String sCode);
    public String getWindowcode();
    public void dispose();
    
}
