/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hp.test.framework.staf;

/**
 *
 * @author sayedmo
 */
public class ExtendSTAF extends STAF{

    public ExtendSTAF(String hostname) throws STAFQAException {
        super(hostname);
    }
    
    

    @Override
    public String getQACommon() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

         
    
}
