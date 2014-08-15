/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Dimension;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administraattori
 */
public class StaticData {

    private static Properties Settings = null;
    private static String SettingsFilename = "settings.xml";
    
    public static Properties getSettings() {
        if (Settings == null) {
            Settings = new Properties();
            try {
                Settings.load( new FileReader( SettingsFilename ) );
            } catch (IOException ex) {
                System.out.println( "Could not save '" + SettingsFilename + "' file" );
            }
        }
        return Settings;
    }
    
    public static void saveSettings() {
        if (Settings != null) {
            try {
                Settings.store( new FileWriter(SettingsFilename), "Client configuration file");
            } catch (IOException ex) {
                System.out.println( "Could not save '" + SettingsFilename + "' file,\n" + ex.getLocalizedMessage() );
            }               
        }
    }
    
    public static String getWindowTitle() {
        return "The Client";
    }

    public static Dimension getWindowSize() {
        return new Dimension(640,480);
    }
    
}
