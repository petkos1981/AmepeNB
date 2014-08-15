/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.x;

/**
 *
 * @author Administraattori
 */
public interface XMainWindow {

    public void showMessage(String disconnected);

    public void setLoggedIn(boolean b);

    public void setConnected(boolean b);

    public InnerWindow getWindow(String sWindowCode);

    public void addWindow(InnerWindow iw);

    public void removeWindow(InnerWindow innerWindow);

    public void busy(boolean b);
        
}
