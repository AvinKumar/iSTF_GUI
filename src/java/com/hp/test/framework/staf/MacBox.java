
package com.hp.test.framework.staf;

//import com.hp.qa.framework.staf.STAF;
//import com.hp.qa.framework.staf.STAFQAException;
//import com.hp.qa.framework.staf.STAFQAResult;
//import com.hp.qa.utilities.logging.QALogger;

/**
 * Class to handle remote Mac systems.
 * 
 */
public class MacBox extends STAF {

    // logger for the class
    private static QALogger LOGGER = QALogger.getLogger(MacBox.class);

    // the prefix for any rules
    private String ruleFolderPrefix = null;

    // location of the qa common area
    private String qaCommon = "/Users/Shared/qa";

    public MacBox(final String hostname) throws STAFQAException {
        super(hostname);

        // with ls -F, names are tagged with a final character. If the
        // character is a "@" it is a link, which indicates the boot drive.
        final STAFQAResult result = startProcWaitShell(
                "ls",
                "-1F /Volumes",
                getQACommon(),
                "ls",
                60);

        if (result.isOK() && (result.getReturnCode() == 0)) {
            final String[] lines = result.getProcessStdOut().split("\n");
            for (final String line : lines) {
                if (line.trim().endsWith("@")) {
                    ruleFolderPrefix = "/Volumes/" + line.trim().replaceAll("\\@$", "");
                    LOGGER.debug("'"
                                 + hostname
                                 + "' Boot drive detected as '"
                                 + ruleFolderPrefix
                                 + "'");
                    break;
                }
            }
            if (ruleFolderPrefix == null) {
                throw new RuntimeException("Unable to determine the boot drive from '"
                                           + result.getProcessStdOut()
                                           + "'");
            }
        } else {
            throw new RuntimeException("Unable to get boot drive info: " + result.toString());
        }
    }

    @Override
    public String getQACommon() {
        return qaCommon;
    }

    public void setQaCommon(final String path) {
        qaCommon = path;
    }

    public String getRuleFolderPrefix() {
        return ruleFolderPrefix;
    }
}
