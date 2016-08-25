package com.hp.test.framework.staf;

import com.ibm.staf.STAFException;

/**
 * 
 * TODO Describe this <code>STAF_QA_Exception</code> type.
 */
public class STAFQAException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2339501901612154825L;

    private static final String[] STAF_ERRORS = {"No error",
                                                 "Invalid API",
                                                 "Unknown service",
                                                 "Invalid handle",
                                                 "Handle already exists",
                                                 "Handle does not exist",
                                                 "Unknown error",
                                                 "Invalid request string",
                                                 "Invalid service result",
                                                 "REXX Error",
                                                 "Base operating system error",
                                                 "Process already complete",
                                                 "Process not complete",
                                                 "Variable does not exist",
                                                 "Unresolvable string",
                                                 "Invalid resolve string",
                                                 "No path to endpoint",
                                                 "File open error",
                                                 "File read error",
                                                 "File write error",
                                                 "File delete error",
                                                 "STAF not running",
                                                 "Communication error",
                                                 "Trusteee does not exist",
                                                 "Invalid trust level",
                                                 "Insufficient trust level",
                                                 "Registration error",
                                                 "Service configuration error",
                                                 "Queue full",
                                                 "No queue element",
                                                 "Notifiee does not exist",
                                                 "Invalid API level",
                                                 "Service not unregisterable",
                                                 "Service not available",
                                                 "Semaphore does not exist",
                                                 "Not sempahore owner",
                                                 "Semaphore has pending requests",
                                                 "Timeout",
                                                 "Java error",
                                                 "Converter error",
                                                 "Not used",
                                                 "Invalid object",
                                                 "Invalid parm",
                                                 "Request number not found",
                                                 "Invalid asynchronous option",
                                                 "Request not complete",
                                                 "Process authentication denied",
                                                 "Invalid value",
                                                 "Does not exist",
                                                 "Already exists",
                                                 "Directory Not Empty",
                                                 "Directory Copy Error",
                                                 "Diagnostics Not Enabled",
                                                 "Handle Authentication Denied",
                                                 "Handle Already Authenticated",
                                                 "Invalid STAF Version",
                                                 "Request Cancelled",};

    /*
     * ****************Zip Service errors************************************ 4001 General zip error
     * A general error occurred, additional error message can be found in result buffer. 4002 Not
     * enough memory There is not enough memory in the system. 4003 Change file size error: <file>
     * Error changing the file size. 4004 Error creating directory: <dir> Error creating directory
     * in the file system. 4005 Invalid zip file: <file> Invalid zip file format. 4006 Bad CRC Bad
     * CRC in the zip archive. 4007 Invalid owner group Invalid owner / group on the system when
     * restore permission. 4008 Invalid file mode Invalid file mode.
     */

    private int returnCode = -1;

    private String returnMessage = "Success";

    public STAFQAException(final String message) {
        super(message);
    }

    public STAFQAException(final String message, final Throwable t) {
        super(message, t);
    }

    public STAFQAException(final String message, final STAFException e) {
        super(message, e);
        setCodeAndMessage(e.rc);
    }

    public STAFQAException(final String message, final int returnCode) {
        super(message);
        setCodeAndMessage(returnCode);
    }

    private void setCodeAndMessage(final int returnCode) {
        this.returnCode = returnCode;
        // TODO Add in the STAF Service specific error codes 4000s.
        if ((returnCode < 0) || (returnCode > 56)) {
            returnMessage = "No such error code: must be custom.";
        } else {
            returnMessage = STAF_ERRORS[returnCode];
        }

    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    @Override
    public String getMessage() {
        return String.format(
                "%s STAF return code: %d STAF message: %s",
                super.getMessage(),
                returnCode,
                returnMessage);
    }
}
