/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administraattori
 */
public class XMLObject {
    
    
    
    public static String getXML( Object o) {
        String sReturn = "";
        try {
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            java.beans.XMLEncoder xe1 = new java.beans.XMLEncoder(fos);            
            xe1.writeObject( o );
            xe1.flush();
            xe1.close();
            sReturn = fos.toString();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(XMLObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sReturn;
    }
    
    public static Object getObject( String sData ) {
        Object oReturn = null;
        try {
            ByteArrayInputStream fis = new ByteArrayInputStream( sData.getBytes() );
            java.beans.XMLDecoder xe1 = new java.beans.XMLDecoder(fis);            
            oReturn = xe1.readObject();            
            xe1.close();
            fis.close();
            
        } catch (IOException ex) { 
            System.out.println("Error on converting XML to Object, but let´s go on...");
        }
        return oReturn;
    }
}
