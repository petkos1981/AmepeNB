/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

/**
 *
 * @author Administraattori
 */
public class StringTools {
    
    public static String searchFromArrayAndGetNext(String[] stack, String needle) {
        String sNext = null;
        for (int i = 0 ; i < stack.length; i++) {
            if (stack[i].equalsIgnoreCase(needle)) {
                try {
                    i++;
                    sNext = stack[i];
                    break;
                } catch (Exception e) {}
            }
        }
        return sNext;
    }
}
