/**

 * This class initializes STAF

 * 
 */
package com.hp.test.framework.staf;

import com.hp.test.framework.staf.STAF;
import com.hp.test.framework.staf.STAFQAException;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;

/**
 * Initialize STAF and get DP folder.
 * 
 * 
 */
public class InitStaf {
	private STAF remotestaf;
	private String OS;
	//private STAFHandle handle1;
	private String destinationHostName;
        
//        public static void main(String[] args) throws STAFQAException, STAFException {
//            InitStaf init=new InitStaf();
//            STAF S=init.initSTAF("localhost");
//           
//            System.out.println("Print the remote staf machine"+S);
//            
//            String OS=init.getOS();
//            System.out.println("OS of remote staf machine"+OS);

//        }

	/**
	 * Method initialize initialize the remote staf.
	 * 
	 * @param destinationHostName
	 *            Host name of remote machine.
	 * 
	 * @throws STAFQAException
	 * 
	 */
	public STAF initSTAF(String destinationHostName) throws STAFQAException {
		this.destinationHostName = destinationHostName;
		remotestaf = STAF.getInstance(destinationHostName);
		OS = remotestaf.getOSType().toString();
		return remotestaf;
	}

	/**
	 * Method getOS get the OS of remote machine.
	 * 
	 */
	public String getOS() {
		return OS;
	}

	
}
