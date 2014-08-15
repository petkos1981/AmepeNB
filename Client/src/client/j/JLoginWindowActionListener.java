/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.j;

import client.Main;
import client.StaticData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 *
 * @author Administraattori
 */
public class JLoginWindowActionListener implements ActionListener {

    Main MAIN;
    JSettingsWindow window;
    
    JLoginWindowActionListener(JSettingsWindow aThis, Main MAIN) {
        MAIN = MAIN;
        window = aThis;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "saveServerSettings":
                this.saveServerSettingsAction();
                break;
        }
    }

    private void saveServerSettingsAction() {
        String serverAddress = window.jtfServerAddress.getText();
        String serverPort = window.jtfServerPort.getText();
        Properties settings = StaticData.getSettings();
        settings.setProperty( "ServerAddress" , serverAddress );
        settings.setProperty( "ServerPort" , serverPort );
        StaticData.saveSettings();       
        window.getMainWindow().showMessage( "Settings saved!" );
    }
    
}
