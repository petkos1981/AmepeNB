/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.j;

import client.Main;
import client.StaticData;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

/**
 *
 * @author Administraattori
 */
public class JSettingsWindow extends JInnerWindow {

    private Main MAIN;
    private JLoginWindowActionListener actionListener;
    
    JTextField jtfServerAddress;
    JTextField jtfServerPort;
    
    public JSettingsWindow(Main main) {
        super("settings");
        MAIN = main;
        actionListener = new JLoginWindowActionListener( this , MAIN );
        this.setTitle( "Settings" );
        this.setLocation( 20 , 20 );
        this.setSize( 640, 480 );
        this.setClosable( true );
        this.setResizable( true );
        this.setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        
        this.setContentPane();
        this.loadSettings();
    }

    @Override
    public void getObject(Object o) {
        
    }

    private void setContentPane() {
       JPanel content = (JPanel) this.getContentPane();
       JTabbedPane jtp = new JTabbedPane();
       jtp.addTab( "Server settings" , this.getServerSettings() );
       content.add(jtp);
    }
    
    private JPanel getServerSettings() {
        JPanel content = new JPanel();
        SpringLayout layout = new SpringLayout();
        content.setLayout( layout );
        Dimension minimLabelSize = new Dimension(150,27);
        Dimension minimJtfSize = new Dimension(200,30);
        
        JLabel labelServerAddress = new JLabel("Server address");
        labelServerAddress.setMinimumSize(minimLabelSize);
        labelServerAddress.setPreferredSize(minimLabelSize);
        
        jtfServerAddress = new JTextField( "address");                        
        jtfServerAddress.setMinimumSize( minimJtfSize );
        jtfServerAddress.setPreferredSize( minimJtfSize );
        
        JLabel labelServerPort = new JLabel("Server port");
        labelServerPort.setMinimumSize(minimLabelSize);
        labelServerPort.setPreferredSize(minimLabelSize);
        
        jtfServerPort = new JTextField("44");
        jtfServerPort.setMinimumSize(minimJtfSize);
        jtfServerPort.setPreferredSize(minimJtfSize);
   
        content.add( labelServerAddress );
        content.add( jtfServerAddress );

        
        // First label to upper-left corner
        layout.putConstraint( SpringLayout.WEST, labelServerAddress, 5, SpringLayout.WEST, content );
        layout.putConstraint( SpringLayout.NORTH, labelServerAddress, 5, SpringLayout.NORTH, content );
        
        // Textfield next to label
        layout.putConstraint( SpringLayout.WEST, jtfServerAddress, 5, SpringLayout.EAST, labelServerAddress );
        layout.putConstraint( SpringLayout.NORTH, jtfServerAddress, 5, SpringLayout.NORTH, content );
        
        
        content.add( labelServerPort );
        content.add( jtfServerPort );
        
        // Next line, first label
        layout.putConstraint( SpringLayout.WEST, labelServerPort, 5, SpringLayout.WEST, content );
        layout.putConstraint( SpringLayout.NORTH, labelServerPort, 5, SpringLayout.SOUTH, labelServerAddress );
                        
        // Then, again, textfield next to it
        layout.putConstraint( SpringLayout.WEST, jtfServerPort, 5, SpringLayout.EAST, labelServerPort );
        layout.putConstraint( SpringLayout.NORTH, jtfServerPort, 5, SpringLayout.SOUTH, jtfServerAddress );
        
        
        // Save settings button
        Dimension buttonSize = new Dimension( 100,30 );
        JButton saveButton = new JButton( "Save" );
        saveButton.setMinimumSize( buttonSize );
        saveButton.setPreferredSize( buttonSize );
        saveButton.setActionCommand( "saveServerSettings" );
        saveButton.addActionListener( this.actionListener );
        content.add( saveButton );
        layout.putConstraint( SpringLayout.EAST , saveButton , -5 , SpringLayout.EAST , content );
        layout.putConstraint( SpringLayout.SOUTH , saveButton , -5 , SpringLayout.SOUTH , content );
        return content;
    }
    
    private void loadSettings() {
        Properties settings = StaticData.getSettings();
        this.jtfServerAddress.setText( settings.getProperty("ServerAddress" ) );
        this.jtfServerPort.setText( settings.getProperty("ServerPort") );
    }
    
}
