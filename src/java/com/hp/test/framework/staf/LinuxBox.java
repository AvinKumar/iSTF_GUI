/**
 * Class to handle remote Linux systems.
 * 
 */
package com.hp.test.framework.staf;

//import com.hp.qa.framework.staf.STAF;
//import com.hp.qa.framework.staf.STAFQAException;
//import com.hp.qa.framework.staf.STAFQAResult;
//import com.hp.qa.utilities.logging.QALogger;

public class LinuxBox extends STAF{
	
	// logger for the class
    private static QALogger LOGGER = QALogger.getLogger(LinuxBox.class);

    // location of the qa common area
    private String qaCommon = "/usr/share/qa";
   
    public LinuxBox(final String hostname) throws STAFQAException {
        super(hostname);
        createDirIfNotExists(qaCommon);
    }

    /*
     * get the qa common path
     */
    public String getQACommon() {
        return qaCommon;
    }

    /* set a different qa path if the default is not used
     * @param path - new qa path.
     */
    public void setQaCommon(final String path) throws STAFQAException {
        qaCommon = path;
        createDirIfNotExists(qaCommon);
    }
}
