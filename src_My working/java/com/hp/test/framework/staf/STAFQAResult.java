

package com.hp.test.framework.staf;

import java.util.List;
import java.util.Map;

//import com.hp.qa.utilities.logging.QALogger;
import com.ibm.staf.STAFMarshallingContext;
import com.ibm.staf.STAFResult;

/**
 * 
 * TODO Describe this <code>STAF_QA_Result</code> type.
 * 
 * @author *unknown*
 */
public class STAFQAResult {

    private static final QALogger LOGGER = QALogger.getLogger(STAFQAResult.class);
    private final STAFResult result;
    private String stdout;
    private int processReturnCode;

    public STAFQAResult(final STAFResult result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    private void processResults() throws STAFQAException {
        if (stdout != null) {
            return;
        }

        // process the results.
        // MF TODO create a another class to handle process return code, stdout, stderr.
        if (result.resultObj instanceof Map) {
            final Map<String, Object> resultMap = (Map<String, Object>) result.resultObj;
            convertMapToFieldValues(resultMap);

        } else if (result.resultObj instanceof String) {
            // Sometimes large data gets messed up in STAF's marshalling. This is a hack to get
            // around it. Some of this code was lifted from STAFMarshallingContext.java.
            // First does the String start with the CONTEXT_MARKER
            final String raw = (String) result.resultObj;
            if (raw.startsWith("@SDT/*")) {
                // Get the length of Context.
                final int colonIndex = raw.indexOf(':', "@SDT/*".length());
                int contextIndex = raw.indexOf(':', colonIndex + 1);
                int contextLength = Integer.parseInt(raw.substring(colonIndex + 1, contextIndex));
                contextIndex = contextIndex + 1;

                final StringBuilder fixed = new StringBuilder();
                // if the stated Context length in file doesn't match relative fix it.
                if (contextLength != (raw.length() - contextIndex)) {
                    // Now we are going to fix the length in the file.
                    contextLength = raw.length() - contextIndex;
                    fixed.append("@SDT/*:");
                    fixed.append(contextLength);
                    fixed.append(raw.substring(contextIndex - 1));
                }

                final STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(fixed
                        .toString());
                convertMapToFieldValues((Map<String, Object>) mc.getRootObject());
            } else {
                processReturnCode = Integer.MAX_VALUE;
                stdout = "Another case of STAF not unmarshalling correctly"
                         + (String) result.resultObj;
            }
        }
    }

    private void convertMapToFieldValues(final Map<String, Object> resultMap)
            throws STAFQAException {
        if (resultMap.containsKey("rc")) {
            try {
                processReturnCode = (int) Long.parseLong((String) resultMap.get("rc"));
            } catch (final NumberFormatException e) {
                throw new STAFQAException("Could not convert "
                                          + resultMap.get("rc")
                                          + " to integer", e);
            }
        }
        if (resultMap.containsKey("fileList")) {
            @SuppressWarnings("unchecked")
            final List<Object> returnedFileList = (List<Object>) resultMap.get("fileList");
            @SuppressWarnings("unchecked")
            final Map<String, String> stdoutMap = (Map<String, String>) returnedFileList.get(0);
            stdout = stdoutMap.get("data");
        }
    }

    /**
     * Did STAF's command work.
     * 
     * @return
     */
    public boolean isOK() {
        return result.rc == STAFResult.Ok;
    }

    /**
     * STAF's command return code.
     * 
     * @return
     */
    public int getReturnCode() {
        return result.rc;
    }

    /**
     * STAF's command output.
     * 
     * @return
     */
    public String getResultString() {
        return result.result;
    }

    public Object getResultObject() {
        return result.resultObj;
    }

    /**
     * The return code from process that STAF called.
     * 
     * @return
     * @throws STAFQAException
     */
    public int getProcessReturnCode() throws STAFQAException {
        processResults();
        return processReturnCode;
    }

    /**
     * The stdout from process that STAF called.
     * 
     * @return
     * @throws STAFQAException
     */
    public String getProcessStdOut() throws STAFQAException {
        processResults();
        return stdout;
    }

    // public STAFMarshallingContext getResultConext() {
    // return result.resultContext;
    // }

    @SuppressWarnings("unchecked")
    public Map<String, String> getMapFromString() {
        final STAFMarshallingContext mc = STAFMarshallingContext.unmarshall(result.result);
        return (Map<String, String>) mc.getRootObject();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMapFromObject() {
        return (Map<String, Object>) result.resultObj;
    }

    public void ifNotOkThrow(final String message) throws STAFQAException {
        if (!isOK()) {
            String msg = message;
            if (getResultString() != null) {
                msg = message + "\n" + getResultString();
            }
            LOGGER.error(msg);
            throw new STAFQAException(msg, getReturnCode());
        }
    }

    public void ifNotOkAndProcessNotOkThrow(final String message) throws STAFQAException {
        ifNotOkThrow(message);

        if (getProcessReturnCode() != 0) {
            final String stdout = getProcessStdOut();
            final String msg = String.format(
                    "%s%n Command failed with a return code: '%d'!%nstdout: %s%n",
                    message,
                    getProcessReturnCode(),
                    stdout);
            throw new STAFQAException(msg, getReturnCode());
        }
    }

    @Override
    public String toString() {
        try {
            processResults();
        } catch (final STAFQAException e) {
            return e.getMessage();
        }
        final String stafResult = String.format(
                "STAFResult [returnCode=%d, details=%s]",
                getReturnCode(),
                getResultString());

        return "STAF_QA_Result [processReturnCode="
               + processReturnCode
               + ", result="
               + stafResult
               + ", stdout="
               + stdout
               + "]";
    }

}
