/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Administraattori
 */
public class LogInfo extends Sendable {
    
    private String sUsername;
    private String sPassword;
    
    private String sFirstName;
    private String sLastName;
    
    private int iStatus = LogInfo.TRY;
    
    public static final int TRY = -1;
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int LOG_OUT = 2;
    
    public LogInfo() {}
            
    public String getUsername() { return this.sUsername; }
    public void setUsername( String u ) { this.sUsername = u; }
    
    public String getPassword() { return this.sPassword; }
    public void setPassword( String p ) { this.sPassword = p; }
    
    public String getFirstName() { return this.sFirstName; }
    public void setFirstName( String f) { this.sFirstName = f; }
    
    public String getLastName() { return this.sLastName; }
    public void setLastName( String l) { this.sLastName = l; }
    
    public void setStatus( int iStatus ) { this.iStatus = iStatus; }
    public int getStatus() { return this.iStatus; }
    
    public static String hash(char[] sWord) {
        String sReturn = "";
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Add password bytes to digest
            md.update( String.valueOf(sWord).getBytes() );
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            sReturn = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return sReturn;
    }
    
    public static void main(String[] args) {
        System.out.println( LogInfo.hash("salasana".toCharArray()) );
    }
    
}
