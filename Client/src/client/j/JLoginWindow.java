/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.j;

import client.Main;
import client.x.InnerWindow;
import common.LogInfo;
import common.XMLObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Administraattori
 */
public class JLoginWindow extends JInnerWindow implements ActionListener {
    
    private JTextField USERNAME;
    private JPasswordField PASSWORD;
    private final Main MAIN;
    
    public JLoginWindow( Main MAIN ) {
        super("LOGINWINDOW");
        this.MAIN = MAIN;
        this.setTitle( "Login Window" );
        this.setSize( 380, 180 );
        this.setDefaultCloseOperation( JInternalFrame.DISPOSE_ON_CLOSE );
        this.setLocation( 10, 10 );
        this.setClosable( true );
        this.setContent();
    }

    private void setContent() {
        this.getContentPane().setLayout( null );
        JLabel label;
        JButton loginButton;
        JButton cancelButton;
        USERNAME = new JTextField();
        PASSWORD = new JPasswordField();
                        
        int iLabelX = 5;
        int iFieldX = 150;
        
        int iLabelHeight = 27;
        int iLabelWidth = 100;
        
        int iFieldHeigth = 27;
        int iFieldWidth = 200;
        
        int iButtonHeight = 27;
        int iButtonWidth = 110;
        
        
        int iCancelButtonX = 40;
        int iCancelButtonY = 70;
        
        int iLoginButtonX = 220;
        int iLoginButtonY = 70;
        
        USERNAME.setSize( iFieldWidth , iFieldHeigth );
        PASSWORD.setSize( iFieldWidth , iFieldHeigth );
        
        USERNAME.setLocation( iFieldX, 5  );
        PASSWORD.setLocation( iFieldX, 30 );
        
        PASSWORD.setActionCommand( "login" );
        PASSWORD.addActionListener( this );
        
        label = new JLabel( "Username : " );
        label.setSize( iLabelWidth , iLabelHeight );
        label.setLocation( iLabelX, 5);
        this.getContentPane().add( label );
        
        label = new JLabel( "Password : " );
        label.setSize( iLabelWidth , iLabelHeight );
        label.setLocation( iLabelX, 30);
        this.getContentPane().add( label );
        
        this.getContentPane().add( USERNAME );
        this.getContentPane().add( PASSWORD );
        
        loginButton = new JButton("Log IN");
        loginButton.setSize( iButtonWidth , iButtonHeight );
        loginButton.setLocation( iLoginButtonX , iLoginButtonY );
        loginButton.setActionCommand( "login" );
        loginButton.addActionListener( this );
        this.getContentPane().add( loginButton );
        
        cancelButton = new JButton("Cancel");
        cancelButton.setSize( iButtonWidth , iButtonHeight );
        cancelButton.setLocation( iCancelButtonX , iCancelButtonY );
        cancelButton.setActionCommand( "cancel" );
        cancelButton.addActionListener( this );
        this.getContentPane().add( cancelButton );
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch ( e.getActionCommand() ) {
            case "login":
                this.logIn( this.USERNAME.getText() , this.PASSWORD.getPassword() );
                break;
        
            case "cancel":
                this.setVisible( false );
                this.dispose();
                break;
        }
    }
    
    void logIn(String username, char[] password) {
        LogInfo logTry = new LogInfo();
        logTry.setReceivingWindowcode( this.getWindowcode() );
        logTry.setUsername( username );
        logTry.setPassword( LogInfo.hash(password) );
        
        this.MAIN.send(logTry);
        
    }

    @Override
    public void getObject(Object o) {
        if (o instanceof LogInfo) {
            LogInfo info = (LogInfo) o;
            switch( info.getStatus() ) {
            
                case LogInfo.FAIL:
                    this.MAIN.MAINWINDOW.setLoggedIn( false );
                    JOptionPane.showInternalMessageDialog( this , "Login failed, wrong usename and/or password!");
                break;
                
                case LogInfo.SUCCESS:
                    this.MAIN.setLoggedIn( true );
                    this.MAIN.MAINWINDOW.setLoggedIn( true );
                    JOptionPane.showInternalMessageDialog( this , "You have logged in successfully!");
                    this.MAIN.MAINWINDOW.removeWindow( (InnerWindow) this );                    
                break;
                
                case LogInfo.TRY:
                    JOptionPane.showInternalMessageDialog( this , "Weird happening has occurred about login!");
                break;
            }
        }
    }
        
}