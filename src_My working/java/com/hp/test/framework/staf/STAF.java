
package com.hp.test.framework.staf;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.apache.commons.io.IOUtils;
//
//import com.hp.qa.frameworkstaf.box.LinuxBox;
//import com.hp.qa.framework.staf.box.MacBox;
//import com.hp.qa.framework.staf.box.OSType;
//import com.hp.qa.framework.staf.box.WindowsBox;
//import com.hp.qa.framework.staf.model.FileDetails;
//import com.hp.qa.utilities.logging.QALogger;
//import com.hp.qa.utilities.zip.SimpleZipEntry;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFResult;
import com.ibm.staf.STAFUtil;



/**
 * 
 * TODO Describe this <code>STAF</code> type.
 * 
 * @author *unknown*
 */
public abstract class STAF implements Closeable {

	private static final QALogger LOGGER = QALogger.getLogger(STAF.class);

	// Abstract Methods
	public abstract String getQACommon();

	// Default Process Variables.
	private static final int TIMEOUT_NOWAIT = -1;
	private static final int TIMEOUT_WAITFOREVER = 0;
	// private static final String DEFAULT_PARAMETERS = "";
	// private static final String DEFAULT_WORKDIR = ".";
	private static final String[] DEFAULT_ENV = {};
	private static final String DEFAULT_USERNAME = null;
	private static final String DEFAULT_PASSWORD = null;
	private static final String[] PROCESS_EXTRAS = { "LOGHOST", "LOGNAME",
			"TESTGROUP", "WORKGROUP", "STAF_USERNAME", "STAF_PASSWORD",
			"TESTSCENARIO", "ADMINPWD", "ADMINUSER", "PATH", };

	// The name of the box where are operating on.
	private final String hostname;

	// Tells us if this STAF instance has been closed.
	private boolean closed;

	// current STAF operation
	private static volatile STAFHandle handle = null;
	private static volatile int count = 0;
	private static final Map<String, String> ENV_VARIABLES = new HashMap<String, String>();

	// type of contacted box
	private OSType osType = null;

	/**
	 * Cached value for path separator.
	 */
	private String separatorChar;
        

        public static void main(String[] args) throws STAFQAException {
            
            STAF s=STAF.getInstance("IWFVM01155.ind.hp.com");
            
            System.out.println("Print Staf Hostname"+s);
            
            
        }
        
       
	/**
	 * Set the handle for STAF.
	 * 
	 * @throws STAFQAException
	 */
	private static void setHandle() throws STAFQAException {
		if ((handle == null) || (count == 0)) {
			try {
				handle = new STAFHandle("OneHandle");
			} catch (final STAFException e) {
				throw new STAFQAException("Could not create STAF Handle.", e);
			}
		}
	}

	/**
	 * Get an instance of a STAF object if and converts it to the correct box
	 * type.
	 * 
	 * @param hostname
	 *            The desired hostname.
	 * @return An Box Object.
	 * @throws STAFQAException
	 */
	public static STAF getInstance(String hostname) throws STAFQAException {
		setHandle();

		if ("localhost".equalsIgnoreCase(hostname)) {
			hostname = "local";
		}

		final STAFQAResult result = new STAFQAResult(handle.submit2(hostname,
				"MISC", "LIST PROPERTIES"));
		result.ifNotOkThrow("Cannot get property list on machine " + hostname);

		switch (OSType.getValue(result.getMapFromString().get("osname"))) {
		case WINDOWS:
			return new WindowsBox(hostname);
		case MAC:
			return new MacBox(hostname);
		case LINUX:
			return new LinuxBox(hostname);
		default:
			throw new STAFQAException("Unknown OS Type for " + hostname);
		}
	}

	protected STAF(final String hostname) throws STAFQAException {
		try {
			setHandle();

			this.hostname = hostname;
			closed = false;
			count++;

			if (!ping()) {
				throw new IllegalStateException(hostname
						+ " is not allowing STAF connections. (Installed??)");
			}
		} catch (final STAFQAException e) {
			throw new STAFQAException("Could not create STAF connection to "
					+ hostname, e);
		}
	}

	// ---------------------------PROCESS SERVICES------------------------

	/**
	 * Start a process asynchronously on the local or remote machine. Executes
	 * as the service process SYSTEM. Uses the parent's (current) environment.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @return STAFResult. The STAFResult.result is the handle to use when
	 *         querying whether the process is done.
	 * @throws STAFException
	 */
	public STAFQAResult startProcNoWait(final String programName,
			final String parameters, final String workDir, final String title)
			throws STAFQAException {
		return startProcNoWait(programName, parameters, workDir, title, true);
	}

	public STAFQAResult startProcNoWait(final String programName,
			final String parameters, final String workDir,
			final boolean returnOutput) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, programName,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD,
				TIMEOUT_NOWAIT, null, false, returnOutput);
	}

	/**
	 * Start a process asynchronously on the local or remote machine. Executes
	 * as the service process SYSTEM. Uses the parent's (current) environment.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param returnOutput
	 *            flag indicating whether the contents of the file to which
	 *            standard output and standard error were redirected should be
	 *            returned when the process completes
	 * @return STAFResult. The STAFResult.result is the handle to use when
	 *         querying whether the process is done.
	 * @throws STAFException
	 */
	public STAFQAResult startProcNoWait(final String programName,
			final String parameters, final String workDir, final String title,
			final boolean returnOutput) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD,
				TIMEOUT_NOWAIT, null, false, returnOutput);
	}

	/**
	 * Start a process synchronously on the local or remote machine. Waits the
	 * maximum default of 10 minutes for the process to complete. Executes as
	 * the system service user "SYSTEM", using the parent's (current)
	 * environment settings.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @return STAF_QA_Result STAFResult.rc is the process return code of the
	 *         process executed. STAFResult.result is a String containing the
	 *         combined stderr and stdout from the program.
	 * @throws STAFQAException
	 */
	public STAFQAResult startProcWait(final String programName,
			final String parameters) throws STAFQAException {
		// final String workDir)
		return startProcWait(programName, parameters, getQACommon(),
				programName, DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD,
				TIMEOUT_WAITFOREVER, null, false, true);
	}

	/**
	 * Start a process synchronously on the local or remote machine. Waits the
	 * maximum default of 10 minutes for the process to complete. Executes as
	 * the system service user "SYSTEM", using the parent's (current)
	 * environment settings.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @return STAF_QA_Result STAFResult.rc is the process return code of the
	 *         process executed. STAFResult.result is a String containing the
	 *         combined stderr and stdout from the program.
	 * @throws STAFQAException
	 */
	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final String title)
			throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD,
				TIMEOUT_WAITFOREVER, null, false, true);
	}

	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final int timeout)
			throws STAFQAException {
		return startProcWait(programName, parameters, workDir, programName,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD, timeout, null,
				false, true);
	}

	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final String title,
			final int timeout) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD, timeout, null,
				false, true);
	}

	public STAFQAResult startProcWaitShell(final String programName,
			final String parameters, final String workDir, final String title,
			final int timeout) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD, timeout, null,
				true, true);
	}

	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir,
			final boolean shellCommand) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, programName,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD,
				TIMEOUT_WAITFOREVER, null, shellCommand, true);
	}

	/**
	 * Start a process synchronously on the local or remote machine. Waits the
	 * maximum default of 10 minutes for the process to complete. Executes as
	 * the system service user "SYSTEM", using the parent's (current)
	 * environment settings.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param shellCommand
	 *            flag indicating if the process should be started via a
	 *            separate shell
	 * @return STAFResult STAFResult.rc is the process return code of the
	 *         process executed. STAFResult.result is a String containing the
	 *         combined stderr and stdout from the program.
	 * @throws STAFException
	 */
	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final String title,
			final boolean shellCommand) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title,
				DEFAULT_ENV, DEFAULT_USERNAME, DEFAULT_PASSWORD,
				TIMEOUT_WAITFOREVER, null, shellCommand, true);
	}

	/**
	 * Start a process asynchronously on the local or remote machine. Executes
	 * as the service process SYSTEM.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param env
	 *            an array of strings- key=value, which will become the entire
	 *            environment for the program to be executed. If null or empty,
	 *            defaults to the parent's (current) environment.
	 * @return STAFResult. The STAFResult.result is the handle to use when
	 *         querying whether the process is done.
	 * @throws STAFException
	 */
	public STAFQAResult startProcNoWait(final String programName,
			final String parameters, final String workDir, final String title,
			final String[] env) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title, env,
				DEFAULT_USERNAME, DEFAULT_PASSWORD, TIMEOUT_NOWAIT, null,
				false, true);
	}

	/**
	 * Start a process synchronously on the local or remote machine. Waits the
	 * maximum default of 10 minutes for the process to complete. Executes as
	 * the system service user "SYSTEM"
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param env
	 *            an array of strings- key=value, which will become the entire
	 *            environment for the program to be executed. If null or empty,
	 *            defaults to the parent's (current) environment.
	 * @return STAFResult STAFResult.rc is the process return code of the
	 *         process executed. STAFResult.result is a String containing the
	 *         combined stderr and stdout from the program.
	 * @throws STAFException
	 */
	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final String title,
			final String[] env) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title, env,
				DEFAULT_USERNAME, DEFAULT_PASSWORD, TIMEOUT_WAITFOREVER, null,
				false, true);
	}

	/**
	 * Start a process asynchronously on the local or remote machine.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param env
	 *            an array of strings- key=value, which will become the entire
	 *            environment for the program to be executed. If null or empty,
	 *            defaults to the parent's (current) environment.
	 * @param userName
	 *            optional. If supplied, the process will be executed as this
	 *            user
	 * @param passWord
	 *            optional. If supplied, used as the password for the userName
	 *            parameter
	 * @return STAFResult. The STAFResult.result is the handle to use when
	 *         querying whether the process is done.
	 * @throws STAFException
	 */
	public STAFQAResult startProcNoWait(final String programName,
			final String parameters, final String workDir, final String title,
			final String[] env, final String userName, final String passWord)
			throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title, env,
				userName, passWord, TIMEOUT_NOWAIT, null, false, true);
	}

	/**
	 * Start a process synchronously on the local or remote machine. Waits the
	 * maximum default of 10 minutes for the process to complete.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param env
	 *            an array of strings- key=value, which will become the entire
	 *            environment for the program to be executed. If null or empty,
	 *            defaults to the parent's (current) environment.
	 * @param userName
	 *            optional. If supplied, the process will be executed as this
	 *            user
	 * @param passWord
	 *            optional. If supplied, used as the password for the userName
	 *            parameter
	 * @return STAFResult STAFResult.rc is the process return code of the
	 *         process executed. STAFResult.result is a String containing the
	 *         combined stderr and stdout from the program.
	 * @throws STAFException
	 */
	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final String title,
			final String[] env, final String userName, final String passWord)
			throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title, env,
				userName, passWord, TIMEOUT_WAITFOREVER, null, false, true);
	}

	/**
	 * Start a process synchronously on the local or remote machine.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param env
	 *            an array of strings- key=value, which will become the entire
	 *            environment for the program to be executed. If null or empty,
	 *            defaults to the parent's (current) environment.
	 * @param userName
	 *            optional. If supplied, the process will be executed as this
	 *            user
	 * @param passWord
	 *            optional. If supplied, used as the password for the userName
	 *            parameter
	 * @param timeout
	 *            in seconds. Defaults to 10 minutes. '0' implies wait forever.
	 *            '-1' implies no waiting
	 * @return STAFResult STAFResult.rc is the process return code of the
	 *         process executed. STAFResult.result is a String containing the
	 *         combined stderr and stdout from the program.
	 * @throws STAFException
	 */
	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final String title,
			final String[] env, final String userName, final String passWord,
			final int timeout) throws STAFQAException {
		return startProcWait(programName, parameters, workDir, title, env,
				userName, passWord, timeout, null, false, true);
	}

	/**
	 * Start a process synchronously on the local or remote machine.
	 * 
	 * @param programName
	 *            the full path and name of program to execute
	 * @param parameters
	 *            any commandline-stype parameters
	 * @param workDir
	 *            the working directory to be in when executing this program.
	 *            Defaults to the directory where the program resides.
	 * @param env
	 *            an array of strings- key=value, which will become the entire
	 *            environment for the program to be executed. If null or empty,
	 *            defaults to the parent's (current) environment.
	 * @param userName
	 *            optional. If supplied, the process will be executed as this
	 *            user
	 * @param passWord
	 *            optional. If supplied, used as the password for the userName
	 *            parameter
	 * @param timeout
	 *            in seconds. Defaults to 10 minutes. '0' implies wait forever.
	 *            '-1' implies no waiting
	 * @param logFile
	 *            the output log to which results of the process execution are
	 *            redirected
	 * @param shellCommand
	 *            flag indicating if the process should be started via a
	 *            separate shell
	 * @param returnOutput
	 *            flag indicating whether the contents of the file to which
	 *            standard output and standard error were redirected should be
	 *            returned when the process completes
	 * @return STAFResult STAFResult.rc is the process return code of the
	 *         process executed. STAFResult.result is a String containing the
	 *         combined stderr and stdout from the program.
	 * @throws STAFException
	 */

	public STAFQAResult startProcWait(final String programName,
			final String parameters, final String workDir, final String title,
			final String[] env, final String userName, final String passWord,
			final int timeout, final String logFile,
			final boolean shellCommand, final boolean returnOutput)
			throws STAFQAException {

		final String command = buildCommand(programName, parameters, workDir,
				title, env, userName, passWord, timeout, logFile, shellCommand,
				returnOutput);
		LOGGER.debug("PROCESS on '" + getHostname() + "' => " + command);

		final STAFQAResult result = execute("Process", command);
		if (returnOutput) {
			final String ret = result.getProcessStdOut();
			if ((ret != null) && !ret.trim().isEmpty()) {
				LOGGER.trace("...returns: \n" + ret.trim());
			}
		}
		result.ifNotOkThrow(String
				.format("Cannot start process: '%s'", command));

		return result;
	}

	private String buildCommand(final String programName,
			final String parameters, final String workDir, final String title,
			final String[] env, final String userName, final String passWord,
			final int timeout, final String logFile,
			final boolean shellCommand, final boolean returnOutput) {

		final Map<String, String> envmap = new HashMap<String, String>();
		envmap.putAll(ENV_VARIABLES);
		// Update map with provide environment (which may be empty)
		for (final String i : env) {
			final String[] kv = i.split("=");
			envmap.put(kv[0], kv[1]);
		}

		// Let Start creating command.
		final StringBuilder startcmd = new StringBuilder();
		startcmd.append("START ");

		if (title != null) {
			startcmd.append("TITLE \"" + title + "\"");
		}
		if (shellCommand) {
			startcmd.append(" SHELL ");
		}
		// Add command.
		startcmd.append(" COMMAND ").append(STAFUtil.wrapData(programName));
		// Add parameters.
		startcmd.append(" PARMS ").append(STAFUtil.wrapData(parameters));
		startcmd.append(" ENV STAF_REPLACE_NULLS [NULL] WORKDIR  ").append(
				STAFUtil.wrapData(workDir));
		// Add user info
		if ((userName != null) && (userName.length() != 0)) {
			startcmd.append(" USERNAME ").append(STAFUtil.wrapData(userName))
					.append(" PASSWORD ").append(STAFUtil.wrapData(passWord));
		}
		// Add in extras.
		for (final String envExtra : PROCESS_EXTRAS) {
			if (envmap.containsKey(envExtra)) {
				startcmd.append(" ENV ").append(envExtra.replace("STAF_", ""))
						.append(" ").append(envmap.get(envExtra));
			}
		}
		// Add sync or async.
		if (timeout == TIMEOUT_NOWAIT) {
			startcmd.append(" ASYNC NOTIFY ONEND");
		} else if (timeout == TIMEOUT_WAITFOREVER) {
			startcmd.append(" WAIT");
		} else {
			startcmd.append(" WAIT " + timeout + "s");
		}
		// Add outputs.
		if (returnOutput) {
			startcmd.append(" STDERRTOSTDOUT RETURNSTDOUT ");
		}
		// Add in logging.
		if (logFile != null) {
			startcmd.append(" STDOUT ").append(logFile);
		}
		return startcmd.toString();
	}

	// ----------------------------FILE SERVICES--------------------------
	public void copyFileWithIn(final String sourceFile,
			final String destinationFile) throws STAFQAException {

		final String cmd = String.format(
				" COPY FILE %s TOFILE %s TOMACHINE %s", sourceFile,
				destinationFile, hostname);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format("On %s could not copy file %s to %s",
				hostname, sourceFile, destinationFile));
	}

	public void copyFile(final String sourceFile, String destinationHostname,
			final String destinationfile) throws STAFQAException {

		// Check if destination is localhost
		if ("localhost".equalsIgnoreCase(destinationHostname)
				|| "local".equalsIgnoreCase(destinationHostname)) {
			destinationHostname = getLocalIPAddress();
		}

		final String cmd = String.format(
				" COPY FILE %s TODIRECTORY %s TOMACHINE %s ", sourceFile,
				destinationfile, destinationHostname);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format(
				"Cannot copy file \\\\%s\\%s to \\\\%s\\%s", hostname,
				sourceFile, destinationHostname, destinationfile));
	}

	/**
	 * Copies a folder from the local machine to destination machine under
	 * specified folder.
	 * 
	 * @param sourceFolder
	 *            folder on the local machine to copy
	 * @param destinationHostname
	 *            destination's IP address
	 * @param destinationFolder
	 *            target folder on the remote machine
	 * @throws STAFQAException
	 *             if a STAF error occurs
	 */
	public void copyFolderRemotely(final String sourceFolder,
			String destinationHostname, final String destinationFolder)
			throws STAFQAException {

		// Check if destination is localhost
		if ("localhost".equalsIgnoreCase(destinationHostname)
				|| "local".equalsIgnoreCase(destinationHostname)) {
			destinationHostname = getLocalIPAddress();
		}

		final String cmd = String.format(
				" COPY DIRECTORY %s TODIRECTORY %s TOMACHINE %s ",
				sourceFolder, destinationFolder, destinationHostname);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format(
				"Cannot copy folder \\\\%s\\%s to \\\\%s\\%s", hostname,
				sourceFolder, destinationHostname, destinationFolder));
	}

	public void copyFolder(final String sourceFolder,
			String destinationHostname, final String destFolder)
			throws STAFQAException {

		final STAF staf = STAF.getInstance(destinationHostname);
		final Random rand = new Random();
		final String tempSourceFile = getTempDir() + "Temp.zip"
				+ rand.nextInt();
		final String tempDestinationFile = staf.getTempDir() + "Temp.zip"
				+ rand.nextInt();

		zipDirectory(sourceFolder, tempSourceFile);

		// Check if destination is localhost
		if ("localhost".equalsIgnoreCase(destinationHostname)
				|| "local".equalsIgnoreCase(destinationHostname)) {
			destinationHostname = getLocalIPAddress();
		}

		try {
			copyFile(tempSourceFile, destinationHostname, tempDestinationFile);
			staf.unzip(tempDestinationFile, destFolder, true);
			try {
				staf.rmFile(tempDestinationFile);
			} catch (final STAFQAException e) {
				LOGGER.warn(String.format(
						"Cannot delete temporary file \\\\%s\\%s",
						destinationHostname, tempDestinationFile), e);
			}
		} catch (final STAFQAException e) {
			throw new STAFQAException(String.format(
					"Cannot copy file \\\\%s\\%s to \\\\%s\\%s", hostname,
					sourceFolder, destinationHostname, destFolder), e);
		} finally {
			try {
				rmFile(tempSourceFile);
			} catch (final STAFQAException e) {
				LOGGER.warn(String.format(
						"Cannot delete temporary file \\\\%s\\%s", hostname,
						tempSourceFile), e);
			}

			staf.close();
		}
	}

	/**
	 * Copies a file from this <code>STAF</code> instance's machine to the local
	 * machine.
	 * 
	 * @param sourceFile
	 *            file on the remote machine to copy
	 * @param destinationFile
	 *            target file on the local machine
	 * @throws STAFQAException
	 *             if a STAF error occurs
	 * @deprecated Use {@link #copyFile(String, String, String)} and specify
	 *             <code>destinationHostname</code> as "localhost"
	 */
	@Deprecated
	public void copyFileFrom(final String sourceFile,
			final String destinationFile) throws STAFQAException {
		final String localMachine = getLocalIPAddress();
		copyFile(sourceFile, localMachine, destinationFile);
	}

	/**
	 * Copies a folder from this <code>STAF</code> instance's machine to the
	 * local machine.
	 * 
	 * @param sourceFolder
	 *            folder on the remote machine to copy
	 * @param destinationFolder
	 *            target folder on the local machine
	 * @throws STAFQAException
	 *             if a STAF error occurs
	 * @deprecated Use {@link #copyFolder(String, String, String)} and specify
	 *             <code>destinationHostname</code> as "localhost"
	 */
	@Deprecated
	public void copyFolderFrom(final String sourceFolder,
			final String destinationFolder) throws STAFQAException {
		final String localMachine = getLocalIPAddress();
		copyFolder(sourceFolder, localMachine, destinationFolder);
	}

	/**
	 * Copies a file from the local machine to this <code>STAF</code> instance's
	 * machine.
	 * 
	 * @param sourceFile
	 *            file on the local machine to copy
	 * @param destinationFile
	 *            target file on the remote machine
	 * @throws STAFQAException
	 *             if a STAF error occurs
	 */
	public void copyFileTo(final String sourceFile, final String destinationFile)
			throws STAFQAException {
		final STAF staf = STAF.getInstance("local");
		staf.copyFile(sourceFile, hostname, destinationFile);
		staf.close();
	}

	/**
	 * Copies a folder from the local machine to this <code>STAF</code> 
	 * instance's machine.
	 * 
	 * @param sourceFolder
	 *            folder on the remote machine to copy
	 * @param destinationFolder
	 *            target folder on the local machine
	 * @throws STAFQAException
	 *             if a STAF error occurs
	 */
	public void copyFolderTo(final String sourceFolder,
			final String destinationFolder) throws STAFQAException {
		final STAF staf = STAF.getInstance("local");
		staf.copyFolder(sourceFolder, hostname, destinationFolder);
		staf.close();
	}

	public boolean exists(final String file) {
		final String cmd = String.format(" query entry %s", file);
		final STAFQAResult result = execute("FS", cmd);
		return result.isOK();
	}

	public boolean exists(final File file) {
		return exists(file.getAbsolutePath());
	}

	@SuppressWarnings("unchecked")
	public FileDetails getFileDetials(final File file) {
		final String cmd = String.format(" query entry %s",
				STAFUtil.wrapData(file.getAbsolutePath()));
		final STAFQAResult result = execute("FS", cmd);
		return new FileDetails(
				(HashMap<String, String>) result.getResultObject());
	}

	/**
	 * Gets the contents of a file.
	 * 
	 * @param filename
	 *            The desired file to read.
	 * @return A String containing the contents.
	 * @throws STAFQAException
	 */
	public String getFileContents(final File filename) throws STAFQAException {
		return getFileContents(filename.getAbsolutePath());
	}

	/**
	 * Gets the contents of a file.
	 * 
	 * @param filename
	 *            The desired file to read.
	 * @return A String containing the contents.
	 * @throws STAFQAException
	 */
	public String getFileContents(final String filename) throws STAFQAException {
		final String cmd = String.format("GET FILE %s", filename);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format("Cannot get file \\\\%s\\%s",
				hostname, filename));
		return (String) result.getResultObject();
	}

	/**
	 * Gets the contents of a file as binary.
	 * 
	 * @param filename
	 *            The desired file to read.
	 * @return A byte[] containing the contents.
	 * @throws STAFQAException
	 */
	public byte[] getFileContentsBinary(final File filename)
			throws STAFQAException {
		return getFileContentsBinary(filename.getAbsolutePath());
	}

	/**
	 * Gets the contents of a file as binary.
	 * 
	 * @param filename
	 *            The desired file to read.
	 * @return A byte[] containing the contents.
	 * @throws STAFQAException
	 */
	public byte[] getFileContentsBinary(final String filename)
			throws STAFQAException {
		final String cmd = String.format("GET FILE %s BINARY", filename);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format("Cannot get file \\\\%s\\%s",
				hostname, filename));
		// STAF will return a HEX String of binary data.
		final String raw = (String) result.getResultObject();
		final byte[] data = new byte[raw.length() / 2];

		LOGGER.debug(
				"Got a file with hex size %d and will convert %d byte array.",
				raw.length(), data.length);

		// Convert the HEX String to a byte array.
		for (int idx = 0; idx < raw.length(); idx += 2) {
			data[idx / 2] = (byte) ((Character.digit(raw.charAt(idx), 16) << 4) + Character
					.digit(raw.charAt(idx + 1), 16));
		}
		return data;
	}

	/**
	 * returns true if the asset is a file.
	 * 
	 * @param asset
	 *            fileName
	 */
	public boolean isFile(final String asset) {
		return isObjectType(asset, "F");
	}

	/**
	 * returns true if the asset is a directory.
	 * 
	 * @param asset
	 *            directory
	 */
	public boolean isDirectory(final String asset) {
		return isObjectType(asset, "D");
	}

	private boolean isObjectType(final String asset, final String match) {
		final String cmd = String.format("get entry %s TYPE ", asset);
		final STAFQAResult result = execute("FS", cmd);

		if (!result.isOK()) {
			return false;
		}

		return result.getResultString().equals(match);
	}

	@SuppressWarnings("unchecked")
	public LinkedList<String> getDirectoryContents(final String directory)
			throws STAFQAException {
		final String cmd = String.format("LIST DIRECTORY %s SORTBYNAME",
				directory);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format(
				"Cannot list contents of directory \\\\%s\\%s", hostname,
				directory));
		return (LinkedList<String>) result.getResultObject();
	}

	/**
	 * This routine het the count of number of files recursively under
	 * directory.
	 * 
	 * 
	 * @param directory
	 *            path of the directory
	 * @return integer count of no of files under directory recursively
	 * @throws STAFQAException
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<String> getDirectoryContentsRecursively(
			final String directory) throws STAFQAException {
		final String cmd = String.format("LIST DIRECTORY %s RECURSE TYPE f",
				directory);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format(
				"Cannot list contents of directory \\\\%s\\%s", hostname,
				directory));
		return (LinkedList<String>) result.getResultObject();
	}

	/**
	 * creates a directory.
	 * 
	 * @param dir
	 *            directory
	 */
	public void mkdir(final String dir) throws STAFQAException {
		final String cmd = String.format("CREATE DIRECTORY :%d:%s FULLPATH ",
				dir.length(), dir);
		final STAFQAResult result = execute("FS", cmd);
		result.ifNotOkThrow(String.format("Cannot create directory \\\\%s\\%s",
				hostname, dir));
	}

	public void rmdir(final File dir) throws STAFQAException {
		rmdir(dir.getAbsolutePath());
	}

	/**
	 * deletes directory.
	 * 
	 * @param dir
	 *            directory
	 */
	public void rmdir(final String dir) throws STAFQAException {
		final String cmd = String.format(
				"DELETE ENTRY :%d:%s RECURSE CONFIRM  ", dir.length(), dir);

		final STAFQAResult result = execute("FS", cmd);
		LOGGER.debug("delete \\\\%s\\%s got the following result: %s",
				hostname, dir, result.toString());// switch to debug
		if (!result.isOK()
				&& (result.getReturnCode() != STAFResult.DoesNotExist)) {
			throw new STAFQAException(String.format(
					"Cannot delete directory \\\\%s\\%s", hostname, dir),
					result.getReturnCode());
		}
	}

	public void rmFile(final File filename) throws STAFQAException {
		rmFile(filename.getAbsolutePath());
	}

	/**
	 * deletes a file.
	 * 
	 * @param filename
	 *            name of file
	 */
	public void rmFile(final String filename) throws STAFQAException {
		final String cmd = String.format("DELETE ENTRY %s RECURSE CONFIRM  ",
				filename);

		final STAFQAResult result = execute("FS", cmd);
		if (!result.isOK()
				&& (result.getReturnCode() != STAFResult.DoesNotExist)) {
			throw new STAFQAException(String.format(
					"Cannot delete directory \\\\%s\\%s", hostname, filename),
					result.getReturnCode());
		}
	}

	/**
	 * Checks if the specified directory exists and creates it if it doesn't.
	 * 
	 * @param dir
	 *            the directory to check for/possibly create
	 * @return boolean true if the directory was successfully created or it
	 *         already existed
	 * @throws STAFQAException
	 */
	public void createDirIfNotExists(final String dir) throws STAFQAException {
		if (!exists(dir)) {
			mkdir(dir);
		}
	}

	/**
	 * Writes a file with a given contents.
	 * 
	 * @param filename
	 *            The desired name of the file including path.
	 * @param contents
	 *            The desired contents of the file.
	 * @throws STAFQAException
	 */
	public void writeFile(final String filename, final String contents)
			throws STAFQAException {
		writeFile(filename, contents.getBytes());
	}

	/**
	 * Writes a file with a given contents.
	 * 
	 * @param file
	 *            The file object.
	 * @param contents
	 *            The desired contents of the file.
	 * @throws STAFQAException
	 */
	public void writeFile(final File file, final String contents)
			throws STAFQAException {
		writeFile(file.getAbsolutePath(), contents.getBytes());
	}

	/**
	 * Writes a file with a given contents.
	 * 
	 * @param filename
	 *            The desired name of the file including path.
	 * @param contents
	 *            The desired contents of the file.
	 * @throws STAFQAException
	 */
	public void writeFile(final String filename, final byte[] contents)
			throws STAFQAException {
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(
				contents);
		writeFile(filename, inputStream);
	}

	/**
	 * Writes a file with a given contents.
	 * 
	 * @param filename
	 *            The desired name of the file including path.
	 * @param contents
	 *            The desired contents of the file.
	 * @throws STAFQAException
	 */
	public void writeFile(final String filename, final InputStream contents)
			throws STAFQAException {
		File tempFile = null;
		FileOutputStream output = null;
		try {
			tempFile = File.createTempFile("staf", ".txt");
			LOGGER.debug("temp file: " + tempFile.getAbsolutePath());
			output = new FileOutputStream(tempFile);
			IOUtils.copy(contents, output);
		} catch (final IOException e) {
			throw new STAFQAException("Could not create temp file!", e);
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(contents);
		}
		copyFileTo(tempFile.getAbsolutePath(), filename);
		if (!tempFile.delete()) {
			LOGGER.warn("Couldn't delete " + tempFile.getAbsolutePath());
		}
	}

	/**
	 * TBD.
	 * 
	 * @param filename
	 * @return
	 * @throws STAFQAException
	 */
	public String getFileMD5Sum(final String filename) throws STAFQAException {
		if (exists(filename)) {
			final String cmd = String.format(" GET ENTRY %s CHECKSUM md5",
					filename);
			final STAFQAResult result = execute("FS", cmd);
			result.ifNotOkThrow("Couldn't get MD5SUM for " + filename);
			return result.getResultString().toLowerCase();
		}

		// otherwise...
		return "";
	}

	// ---------------------------MISC SERVICES------------------------
	public String getSTAFInstallProperty(final String property)
			throws STAFQAException {

		final STAFQAResult result = execute("MISC", "LIST PROPERTIES");
		result.ifNotOkThrow("Cannot get property list on machine " + hostname);

		return result.getMapFromString().get(property);
	}

	public String getStafMachineInformation(final String property)
			throws STAFQAException {

		final STAFQAResult result = execute("MISC", "WHOAREYOU");
		result.ifNotOkThrow("Cannot get property list on machine " + hostname);

		return result.getMapFromString().get(property);
	}

	public String getStafMachineInformation2(final String property)
			throws STAFQAException {

		final STAFQAResult result = execute("MISC", "WHOAMI");
		result.ifNotOkThrow("Cannot get property list on machine " + hostname);
		final String s = result.getMapFromString().get(property);
		return s;
	}

	// -----------------------------PING SERVICES-------------------------
	/**
	 * Ping a hostname to see if it is alive.
	 * 
	 * @return True if ping succeeded.
	 */
	public boolean ping() {
		return execute("PING", "PING").isOK();
	}

	// ------------------------------VAR SERVICES--------------------------
	public String getSTAFEnvVar(final String key) {
		return execute("var", "system get var " + key).getResultString();
	}

	/**
	 * Set a STAF Env variable.
	 * 
	 * @param key
	 *            TBD
	 * @param value
	 *            TBD
	 */
	public final void setSTAFEnvVar(final String key, final String value) {
		execute("var", "system set var " + key + "=" + value);
	}

	// -----------------------------ZIP SERVICES-------------------------
	/**
	 * Unzips a given file to a given directory.
	 * 
	 * @param zipFile
	 *            file to be unzipped.
	 * @param destDirectory
	 *            directory in which to unzip files.
	 * @param replace
	 *            if <tt>true</tt> overwrite files in the destination directory.
	 *            If <tt>false</tt> throws a {@link STAFQAException} if any
	 *            zipped files already exist in the destination directory.
	 * @throws STAFQAException
	 *             if a STAF error occurs.
	 */
	public void unzip(final String zipFile, final String destDirectory,
			final boolean replace) throws STAFQAException {
		final String cmd = String.format(
				"UNZIP ZIPFILE %s TODIRECTORY %s RESTOREPERMISSION"
						+ (replace ? " REPLACE" : ""), zipFile, destDirectory);
		final STAFQAResult result = execute("ZIP", cmd);
		result.ifNotOkThrow(String.format(
				"Cannot unzip \"%s\" to \"%s\" on machine %s", zipFile,
				destDirectory, hostname));
	}

	public void zipDirectory(final File sourceDirectory, final File zipFile)
			throws STAFQAException {
		zipDirectory(sourceDirectory.getAbsolutePath(),
				zipFile.getAbsolutePath());
	}

	/**
	 * Zips the contents of a given directory. The directory itself is not
	 * included in the zip file.
	 * 
	 * @param sourceDirectory
	 *            directory to zip.
	 * @param zipFile
	 *            file to zip directory contents to.
	 * @throws STAFQAException
	 *             if a STAF error occurs.
	 */
	public void zipDirectory(final String sourceDirectory, final String zipFile)
			throws STAFQAException {
		final String cmd = String.format(
				"ZIP ADD ZIPFILE %s DIRECTORY %s RECURSE RELATIVETO %s",
				zipFile, sourceDirectory, sourceDirectory);
		final STAFQAResult result = execute("ZIP", cmd);
		result.ifNotOkThrow(String.format(
				"Cannot zip \"%s\" to \"%s\" on machine %s", sourceDirectory,
				zipFile, hostname));
	}

	/**
	 * Gets the list of entries in a zip file.
	 * 
	 * @param zipFile
	 *            The desire Zip file to work on.
	 * @return
	 * @throws STAFQAException
	 * @throws TestException
	 */
	public List<SimpleZipEntry> getZipManifest(final File zipFile)
			throws STAFQAException {
		if (!exists(zipFile)) {
			return Collections.emptyList();
		}

		final STAFQAResult result = execute("ZIP",
				"  LIST ZIPFILE " + zipFile.getAbsolutePath());
		result.ifNotOkThrow("Couldn't get ZipMainfest from "
				+ zipFile.getAbsolutePath());

		final List<SimpleZipEntry> entries = new ArrayList<SimpleZipEntry>();
		@SuppressWarnings("unchecked")
		final LinkedList<HashMap<String, String>> list = 
						(LinkedList<HashMap<String, String>>) result
				.getResultObject();
		final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy_HH:mm");

		for (final HashMap<String, String> map : list) {
			String name = null;
			long length = 0;
			String time = null;
			String date = null;
			long crc32 = 0;
			for (final String key : map.keySet()) {
				if ("name".equals(key)) {
					name = map.get(key).trim();
				} else if ("length".equals(key)) {
					length = Long.valueOf(map.get(key).trim());
				} else if ("crc-32".equals(key)) {
					crc32 = Long.parseLong(map.get(key).trim(), 16);
				} else if ("time".equals(key)) {
					time = map.get(key).trim();
				} else if ("date".equals(key)) {
					date = map.get(key).trim();
				}
			}

			final String datetime = date + "_" + time;
			Date mDate;

			try {
				mDate = sdf.parse(datetime);
			} catch (final ParseException e) {
				throw new STAFQAException("Couln't parse dateformat "
						+ datetime);
			}
			entries.add(new SimpleZipEntry(name, length, mDate, crc32));
		}

		Collections.sort(entries);
		return entries;
	}

	// -----------------------------SERVICES-------------------------
	public STAFQAResult execute(final String service, final String command) {
		LOGGER.trace(String.format("Hostname: %s Service: %s Command: %s",
				hostname, service, command));
		return new STAFQAResult(handle.submit2(hostname, service, command));
	}

	// ------------------------------OTHER---------------------------
	/**
	 * Returns the hostname associated with this <code>STAF</code> instance.
	 * 
	 * @return hostname
	 */
	public String getHostname() {
		return hostname;
	}

	public OSType getOSType() throws STAFQAException {
		if (osType == null) {
			osType = OSType.getValue(getSTAFInstallProperty("osname"));

			switch (osType) {
			case WINDOWS:
				separatorChar = "\\";
				break;
			case MAC:
				separatorChar = "/";
				break;
			case LINUX:
				separatorChar = "/";
				break;
			default:
				throw new IllegalStateException("Unknown OS!!");
			}
		}
		return osType;
	}

	/**
	 * Returns the system-dependent default name-separator character (i.e.
	 * {@link java.io.File#separator}) for this <code>STAF</code> instance's
	 * machine.
	 * 
	 * @return name-separator character, or "/" if unknown for the machine's OS
	 * @throws STAFQAException
	 */
	public String getSeparatorChar() {
		try {
			getOSType();
		} catch (final STAFQAException e) {
			throw new IllegalStateException("Could not get OS type!!", e);
		}
		return separatorChar;
	}

	/**
	 * Returns a directory on this <code>STAF</code> instance's machine where
	 * temporary data can be placed.
	 * 
	 * @return STAF's temporary data directory: the 'tmp' folder in
	 *         {STAF/DataDir} (will be terminated with a separator character)
	 */
	public String getTempDir() {
		return getSTAFEnvVar("STAF/DataDir") + getSeparatorChar() + "tmp"
				+ getSeparatorChar();
	}

	@Override
	public void close() {
		if (!closed) {
			count--;
			if ((handle != null) && (count == 0)) {
				try {
					handle.unRegister();
				} catch (final Exception e) {
					// Nothing to do - just go away
				}
			}
		}
	}

	/**
	 * Returns the local machine's IP address. Helpful for making a STAF call
	 * where the destination machine is the local system.
	 * 
	 * @return IP address of local machine
	 * @throws STAFQAException
	 *             if the IP address cannot be determined
	 */
	public static String getLocalIPAddress() throws STAFQAException {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (final UnknownHostException e) {
			throw new STAFQAException(
					"Cannot determine IP address of local system.", e);
		}
	}
	/**
     * Copy a directory (folder) from one machine to same or another
     *
     * @param srcmachine source machine of copy
     * @param srcfolder source folder for copy
     * @param destmachine destination machine for copy
     * @param destfolder destination folder for copy
     * @return a STAFResult result of call
     * @throws STAFException
     */
    public STAFResult copyFolder(
            String srcmachine,
            String srcfolder,
            String destmachine,
            String destfolder) throws STAFException {
     
        final String cmd = String
                .format(
                        " COPY DIRECTORY %s TODIRECTORY %s TOMACHINE %s "
                        + "RECURSE KEEPEMPTYDIRECTORIES IGNOREERRORS ",
                        srcfolder,
                        destfolder,
                        destmachine);
        final STAFResult result = handle.submit2(srcmachine, "FS", cmd);
        if ((result.rc != STAFResult.Ok)) {
            throw new STAFException(
                    result.rc,
                    String
                            .format(
                                    "Cannot copy directory %s on machine %s to directory %s on machine %s (rc=%d) \n %s",
                                    srcfolder,
                                    srcmachine,
                                    destfolder,
                                    destmachine,
                                    result.rc,
                                    result.result));
        }
        return (result);
    }
    
}
