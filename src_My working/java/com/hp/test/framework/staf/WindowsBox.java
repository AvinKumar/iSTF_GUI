package com.hp.test.framework.staf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.hp.test.framework.staf.ProductDamagedException;
import com.hp.test.framework.staf.TestException;
//import com.hp.qa.framework.staf.STAF;
//import com.hp.qa.framework.staf.STAFQAException;
//import com.hp.qa.framework.staf.STAFQAResult;
//import com.hp.qa.utilities.logging.QALogger;

/**
 * TODO Describe this <code>WindowsBox</code> type.
 * 
 */
public class WindowsBox extends STAF {

	private static QALogger logger = QALogger.getLogger(WindowsBox.class);

	private static final File LOCK_FILE = new File("C:\\qa", "LOCKED");

	private String qaCommon = "C:\\qa";

	public WindowsBox(final String hostname) throws STAFQAException {
		super(hostname);
	}

	@Override
	public String getQACommon() {
		return qaCommon;
	}

	public void setQaCommon(final String path) {
		qaCommon = path;
	}

	// ----------------------------------------------------------------------------
	// ---------------------------Registry
	// Methods---------------------------------
	// ----------------------------------------------------------------------------
	/**
	 * Returns the value for the specified registry key and value name.
	 * 
	 * @param key
	 *            a registry key, example: "HKLM\\Software\\Connected\\Agent"
	 * @param valueName
	 *            a registry value name, example: "TargetDir"
	 * @return the value from the registry or <code>null</code> if the value
	 *         wasn't found
	 * @throws STAFQAException
	 */
	public String getValue(final String key, final String valueName)
			throws STAFQAException {
		return getValue(key, valueName, false);
	}

	/**
	 * Returns the value for the specified registry key and value name.
	 * 
	 * @param key
	 *            a registry key, example: "HKLM\\Software\\Connected\\Agent"
	 * @param valueName
	 *            a registry value name, example: "TargetDir"
	 * @param force64bitView
	 *            flag indicating if reg.exe should be forced to provide a
	 *            64-bit view of the registry
	 * @return the value from the registry or <code>null</code> if the value
	 *         wasn't found
	 * @throws STAFQAException
	 */
	public String getValue(final String key, final String valueName,
			final boolean force64bitView) throws STAFQAException {

		final String cmd = String.format("QUERY \"%s\"%s /v %s", key,
				force64bitView ? " /reg:64" : "", valueName);
		final STAFQAResult result = startProcWait("reg.exe", cmd);

		if (result.getProcessReturnCode() != 0) {
			logger.debug("No value found for " + key + "\\" + valueName);
			return null;
		}

		String data = result.getResultString();

		// Whittle the result string down to the type and the value itself
		data = data.substring(data.indexOf(valueName) + valueName.length())
				.trim();

		// Figure out where the type string ends, so we can return just the
		// value
		int index = 0;
		while ((index < data.length()) && (data.charAt(index) != '\t')
				&& (data.charAt(index) != ' ')) {
			index++;
		}

		if (index == data.length()) {
			logger.info("Found blank value for " + key + "\\" + valueName);
			return "";
		}

		return data.substring(index).trim();
	}

	/**
	 * Returns true if the specified key exists in the Windows registry.
	 * 
	 * @param key
	 *            a registry key, example: "HKLM\\Software\\Connected\\Agent"
	 * @throws STAFQAException
	 */
	public boolean keyExists(final String key) throws STAFQAException {
		return startProcWait("reg.exe", "QUERY \"" + key + "\"")
				.getProcessReturnCode() == 0;
	}

	/**
	 * Deletes the specified registry key and all of its subkeys and values.
	 * 
	 * @param key
	 *            a registry key, example: "HKLM\\Software\\Connected\\Agent"
	 * @return true if the deletion was successful
	 * @throws STAFQAException
	 */
	public boolean deleteKey(final String key) throws STAFQAException {
		return startProcWait("reg.exe", "DELETE \"" + key + "\" /f")
				.getProcessReturnCode() == 0;
	}

	/**
	 * This method writes the given to data to given key and value to the
	 * registry on the remote machine.
	 * 
	 * If the env is a mirrored pair then the update is also done on the
	 * secondary server.
	 * 
	 * @throws STAFQAException
	 */
	public void writeKey(final RegistryEntry entry) throws STAFQAException {
		final STAFQAResult result = startProcWait("reg",
				entry.getWriteCommand());
		result.ifNotOkAndProcessNotOkThrow("Could not write to windows regirstry.");
	}

	public RegistryEntry readKey(final RegistryEntry entry)
			throws STAFQAException {
		final STAFQAResult result = startProcWait("reg", entry.getReadCommand());
		result.ifNotOkAndProcessNotOkThrow("Could not read from windows regirstry.");
		for (final String temp : result.getProcessStdOut().split("\n")) {
			if (temp.contains(entry.getValue())) {
				final int start = temp.indexOf("REG_");
				final int stop = temp.indexOf(" ", start);
				final String type = temp.substring(start, stop);
				final String data = temp.substring(stop).trim();
				return new RegistryEntry(entry.getKey(), entry.getValue(),
						data, type);
			}
		}

		return null;
	}

	public List<String> getChildKeys(final String key,
			final boolean force64bitView) throws STAFQAException {
		final String cmd = String.format("QUERY \"%s\"%s", key,
				force64bitView ? " /reg:64" : "");
		final STAFQAResult result = startProcWait("reg.exe", cmd);

		if (result.getProcessReturnCode() != 0) {
			logger.debug("No value found for " + key);
			return Collections.emptyList();
		}

		final String knockOut = key.replace("HKLM", "HKEY_LOCAL_MACHINE");
		final String data = result.getResultString();
		final List<String> out = new ArrayList<String>();
		for (final String line : data.split("\\n")) {
			out.add(line.replace(knockOut, "").replace("\r", ""));
		}
		out.remove(0);
		return out;
	}

	public String convertDisplayNameToGUID(final String displayName,
			final boolean force64bitView) throws STAFQAException {
		final String masterKey = "HKLM\\Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall";
		final List<String> child = getChildKeys(masterKey, force64bitView);

		for (final String key : child) {
			// Filter out none GUID & GUIDS with .ext names.
			if (key.isEmpty() || (key.charAt(1) != '{') || key.contains(".")) {
				continue;
			}
			final String value = getValue(masterKey + key, "DisplayName",
					force64bitView);
			if (displayName.equals(value)) {
				return key.replace("\\", "");
			}
		}
		return null;
	}

	// ----------------------------------------------------------------------------
	// ----------------------------Process
	// Methods---------------------------------
	// ----------------------------------------------------------------------------

	/**
	 * Returns the process ID for the specified process.
	 * 
	 * @param processName
	 *            the name of the process
	 * @return the process ID for the specified process, or <code>null</code> if
	 *         the specified process wasn't found
	 * @throws STAFQAException
	 *             if a STAF error occurs while launching the processes to
	 *             retrieve the process information
	 */
	public int getPid(final String processName) throws STAFQAException {
		return getPid(processName, null);
	}

	/**
	 * Returns the process ID for the specified Java process.
	 * 
	 * @param javaClass
	 *            the name of the executable Java class
	 * @return the process ID for the specified Java process, or <code>-1</code>
	 *         if the specified process wasn't found
	 * @throws STAFQAException
	 *             if a STAF error occurs while launching the processes to
	 *             retrieve the process information
	 */
	public int getJavaPid(final String javaClass) throws STAFQAException {
		return getPid("java.exe", javaClass);
	}

	/**
	 * Returns the process ID for the specified process.
	 * 
	 * @param processName
	 *            the name of the process for which to retrieve the process ID
	 * @param filterText
	 *            the name of an executable Java class, for cases when we
	 *            specify "java.exe" for the <code>processName</code> (this
	 *            parameter can be null)
	 * @return the process ID for the specified process, or <code>-1</code> if
	 *         the specified process wasn't found
	 * @throws STAFQAException
	 *             if a STAF error occurs while launching the processes to
	 *             retrieve the process information
	 */
	protected int getPid(final String processName, final String filterText)
			throws STAFQAException {
		// WMIC returns Unicode output, so redirect it to a text file, so we can
		// TYPE it to get
		// ASCII output
		STAFQAResult result = startProcWait("WMIC",
				"/OUTPUT:WMIC_output.txt PROCESS where \"name = '"
						+ processName + "'\" get CommandLine,ProcessId /VALUE");

		result.ifNotOkThrow("Error getting process information");

		if (result.getResultString().startsWith("No Instance(s) Available.")) {
			logger.trace("No process found for '" + processName + "'");
			return -1;
		}

		result = startProcWait("TYPE", "WMIC_output.txt", getQACommon(), true);
		result.ifNotOkThrow("Error getting process information");

		String wmicOutput = result.getProcessStdOut();
		logger.trace("WMIC found the following processes:\n%s", wmicOutput);
		if (filterText != null) {
			final int javaClassIndex = wmicOutput.indexOf(filterText);
			if (javaClassIndex == -1) {
				logger.info("No " + processName
						+ " instance found for filter text '" + filterText
						+ "'");
				return -1;
			}
			// We found the Java class in the WMIC output, so chop off the
			// output for any java.exe
			// processes that preceded it, so the first process ID in the output
			// will be for the
			// specified Java class.
			wmicOutput = wmicOutput.substring(javaClassIndex);
		}

		final int processIdIndex = wmicOutput.indexOf("ProcessId=");
		final int endIndex = wmicOutput.indexOf("CommandLine=", processIdIndex);
		String pid;
		if ((processIdIndex == -1) && (endIndex == -1)) {
			return -1;
		} else if (endIndex != -1) {
			// There are other processes listed after this one.
			pid = wmicOutput.substring(processIdIndex + "ProcessId=".length(),
					endIndex).trim();
		} else {
			pid = wmicOutput.substring(processIdIndex + "ProcessId=".length())
					.trim();
		}
		logger.debug("%s has a pid of %d", processName, Integer.valueOf(pid));
		return Integer.valueOf(pid);
	}

	/**
	 * Gets all of the current process that are running.
	 * 
	 * @return An array of process information. Each element is a semi-colon
	 *         separated value containing CommandLine;Name;ProcessId
	 * @throws STAFQAException
	 */
	public String[] getAllProcess() throws STAFQAException {
		// WMIC returns Unicode output, so redirect it to a text file, so we can
		// TYPE it to get
		// ASCII output
		STAFQAResult result = startProcWait("WMIC",
				"/OUTPUT:C:\\WMIC_output.txt PROCESS get ProcessId, Name, CommandLine /VALUE");

		result.ifNotOkThrow("Error getting process information");

		if (result.getResultString().startsWith("No Instance(s) Available.")) {
			return new String[0];
		}

		result = startProcWait("TYPE", "C:\\WMIC_output.txt", getQACommon(),
				true);
		result.ifNotOkThrow("Error getting process information");

		final String wmicOutput = result.getResultString();
		final List<String> out = new ArrayList<String>();
		final StringBuilder buf = new StringBuilder();
		for (final String element : wmicOutput.split("\n")) {
			if (element.startsWith("CommandLine") || element.startsWith("Name")
					|| element.startsWith("ProcessId")) {
				buf.append(element.replace("\r", "")).append(";");
			} else if (buf.length() > 0) {
				buf.deleteCharAt(buf.length() - 1);
				out.add(buf.toString());
				buf.setLength(0);
			}
		}
		return out.toArray(new String[out.size()]);
	}

	/**
	 * Kills a process.
	 * 
	 * @param pid
	 *            the process ID of the process to kill
	 * @throws STAFQAException
	 *             if a STAF error occurs while launching the process to kill
	 *             the process
	 */
	public void killProcess(final int pid) throws STAFQAException {
		final STAFQAResult result = startProcWait("TASKKILL", "/F /PID " + pid);

		result.ifNotOkThrow("Error killing process with PID " + pid);
	}

	public void killProcess(final String processName) throws STAFQAException {
		final int pid = getPid(processName);
		if (pid != -1) {
			logger.info("Killing " + processName + " with pid " + pid);
			killProcess(pid);
		}
	}

	public void killProcess(final String processName, final String filter)
			throws STAFQAException {
		final int pid = getPid(processName, filter);
		if (pid != -1) {
			logger.info("Killing " + processName + " with filter text "
					+ filter + " " + " with pid " + pid);
			killProcess(pid);
		}
	}

	/**
	 * Waits for the specified process to start, for up to the maximum amount of
	 * time specified by the <code>timeoutMs</code> parameter.
	 * 
	 * @param processName
	 *            the name of the process
	 * @param timeoutMs
	 *            the maximum time (in seconds) to wait for the process to start
	 * @throws STAFQAException
	 *             if there is a STAF error invoking the process to check the
	 *             status of the process
	 */
	public void waitForProcessToStart(final String processName,
			final int timeoutMs) throws STAFQAException {
		waitForProcessToStart(processName, null, timeoutMs);
	}

	/**
	 * Waits for the specified Java process to start, for up to the maximum
	 * amount of time specified by the <code>timeoutMs</code> parameter.
	 * 
	 * @param javaClass
	 *            the name of an executable Java class
	 * @param timeout
	 *            the maximum time (in seconds) to wait for the process to start
	 * @throws STAFQAException
	 *             if there is a STAF error invoking the process to check the
	 *             status of the process
	 */
	public void waitForJavaProcessToStart(final String javaClass,
			final int timeoutMs) throws STAFQAException {
		waitForProcessToStart("java.exe", javaClass, timeoutMs);
	}

	/**
	 * Waits for the specified process to start, for up to the maximum amount of
	 * time specified by the <code>timeoutMs</code> parameter.
	 * 
	 * @param processName
	 *            the name of the process
	 * @param javaClass
	 *            the name of an executable Java class, if this method should
	 *            wait for a "java.exe" process to start (for non-Java
	 *            processes, this parameter should be null)
	 * @param timeout
	 *            the maximum time (in seconds) to wait for the process to start
	 * @throws STAFQAException
	 *             if there is a STAF error invoking the process to check the
	 *             status of the process
	 */
	public void waitForProcessToStart(final String processName,
			final String javaClass, final int timeout) throws STAFQAException {

		String processDisplayName;

		if (javaClass == null) {
			processDisplayName = "process '" + processName + "'";
		} else {
			processDisplayName = "Java process '" + javaClass + "'";
		}

		int elapsedTime = 0;
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(5);
				elapsedTime += 5;

				int pid;
				if (javaClass == null) {
					pid = getPid(processName);
				} else {
					pid = getJavaPid(javaClass);
				}
				if (pid != -1) {
					logger.info("The " + processDisplayName + " has started.");
					break;
				} else {
					logger.info("Waiting for " + processDisplayName
							+ " to start...");
				}

				if (elapsedTime >= timeout) {
					throw new STAFQAException("Timed out waiting for "
							+ processDisplayName + " to start");
				}
			} catch (final InterruptedException e) {
				throw new STAFQAException(processDisplayName
						+ " was trying to start.", e);
			}
		}
	}

	/**
	 * Returns true if the specified Windows service exists.
	 * 
	 * @param serviceName
	 *            the name of the Windows service
	 * @throws STAFQAException
	 */
	public boolean serviceExists(final String serviceName)
			throws STAFQAException {
		return getServiceState(serviceName) != WindowServiceState.NOT_INSTALLED;
	}

	/**
	 * Returns true if the specified Windows service is running.
	 * 
	 * @param serviceName
	 *            the name of the Windows service
	 * @throws STAFQAException
	 */
	public WindowServiceState getServiceState(final String serviceName)
			throws STAFQAException {
		final String results = startProcWait("sc.exe",
				"QUERY \"" + serviceName + "\"").getResultString();
		final String[] parts = results.split("\r\n");
		String state = "UNKNOWN";
		if (parts.length == 3) {
			if (parts[2].contains("service does not exist")) {
				state = "NOT_INSTALLED";
			}
		} else {
			final String line = parts[3];
			final int start = line.indexOf(':') + 3;
			state = line.substring(start).trim();
		}
		return WindowServiceState.valueOf(state);
	}

	/**
	 * Returns true if service starts successfully, or if the service is already
	 * started.
	 * 
	 * @param serviceName
	 *            the name of the Windows service
	 * @param timeout
	 *            the timeout value in seconds.
	 * @throws STAFQAException
	 * @throws InterruptedException
	 */
	public boolean startService(final String serviceName, final int timeout)
			throws STAFQAException {

		if (!serviceExists(serviceName)) {
			return true;
		}

		if (getServiceState(serviceName) == WindowServiceState.RUNNING) {
			return true;
		}

		final String result = startProcWait("sc.exe",
				"START \"" + serviceName + "\"").getResultString();

		// Successful start returns START_PENDING in result.result.
		if (!result.contains("START_PENDING") && !result.contains("STARTED")) {
			return false;
		}
		return waitForService(serviceName, timeout, WindowServiceState.RUNNING);
	}

	/**
	 * Returns true if service stops successfully, or if the service is already
	 * stopped.
	 * 
	 * @param serviceName
	 *            the name of the Windows service.
	 * @param timeout
	 *            the timeout value in seconds.
	 * @throws STAFQAException
	 */
	public boolean stopService(final String serviceName, final int timeout)
			throws STAFQAException {
		if (!serviceExists(serviceName)) {
			return true;
		}

		if (getServiceState(serviceName) == WindowServiceState.STOPPED) {
			return true;
		}

		final String result = startProcWait("sc.exe",
				"STOP \"" + serviceName + "\"").getProcessStdOut();
		// Successful stop returns STOP_PENDING in result.result.
		if (!result.contains("STOP_PENDING") && !result.contains("STOPPED")) {
			return false;
		}
		return waitForService(serviceName, timeout, WindowServiceState.STOPPED);
	}

	/**
	 * Stops a web site.
	 * 
	 * @param webSite
	 *            The name of web site to stop.
	 * @return True if web site is stopped.
	 * @throws STAFQAException
	 */
	public boolean stopWebSite(final String webSite) throws STAFQAException {
		final String result = startProcWait(
				"C:\\Windows\\System32\\inetsrv\\appcmd.exe",
				"stop site \"" + webSite + "\"",
				"C:\\Windows\\System32\\inetsrv", "StopWebSite")
				.getProcessStdOut();

		if (!result.contains("successfully stopped")) {
			logger.warn(webSite + " Could not be stopped! " + result);
			return false;
		}

		return true;
	}

	/**
	 * Starts a web site.
	 * 
	 * @param webSite
	 *            The name of web site to start.
	 * @return True if web site is started.
	 * @throws STAFQAException
	 */
	public boolean startWebSite(final String webSite) throws STAFQAException {
		final String result = startProcWait(
				"C:\\Windows\\System32\\inetsrv\\appcmd.exe",
				"start site \"" + webSite + "\"",
				"C:\\Windows\\System32\\inetsrv", "StartWebSite")
				.getProcessStdOut();

		if (!result.contains("successfully start")) {
			logger.warn(webSite + " Could not be started! " + result);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param serviceName
	 * @param timeOut
	 *            In seconds.
	 * @param stop
	 *            True to wait for stopped state.
	 * @return
	 * @throws STAFQAException
	 */
	private boolean waitForService(final String serviceName, final int timeOut,
			final WindowServiceState state) throws STAFQAException {

		int elapsedTime = 0;
		while ((getServiceState(serviceName) != state)
				&& (elapsedTime < timeOut)) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (final InterruptedException e) {
				// Eat this, since we poked.
			}
			elapsedTime += 5;
		}

		return getServiceState(serviceName) == state;
	}

	/**
	 * This method maps the any network drive to the path specified. (e.g
	 * net.exe use Z: \\r100\DPbuilds\ /user:Domain\User Password).
	 * 
	 * @param drive
	 *            Drive letter on which path will be mapped (e.g Z:)
	 * @param pathInfo
	 *            Full path that needs to be mapped to a drive letter.
	 * @param userInfo
	 *            User login information with domain. The format should be like
	 *            (e.g Domain\User Password). This can be left blank also if
	 *            machine does not validates for user account.
	 * @throws STAFQAException
	 *             if there is STAF error while mapping drive on remote machine.
	 */
	public void mapNetworkDrive(final String drive, final String pathInfo,
			final String userInfo) throws STAFQAException {

		// Delete any pre-existing mapping. E.g. net.exe use z: /delete
		startProcWait("net.exe", "use " + drive + " /delete");

		// Map network drive
		final String domainInfo = !userInfo.isEmpty() ? " /user:"
				+ userInfo.trim() : "";
		final String cmdLine = "use " + drive + " " + pathInfo + domainInfo;
		final STAFQAResult result = startProcWait("net.exe", cmdLine);

		result.ifNotOkThrow("Couldn't map " + pathInfo);
	}

	/**
	 * Deny Permission.
	 * 
	 * @param fileAdmins
	 *            - Group or username of file owner
	 * @param permission
	 *            - <code>permission</code> is a permission mask and can be
	 *            specified in one of two forms: <br>
	 *            a sequence of simple rights:<br>
	 *            F - full access<br>
	 *            M - modify access<br>
	 *            RX - read and execute access<br>
	 *            R - read-only access<br>
	 *            W - write-only access<br>
	 *            a comma-separated list in parenthesis of specific rights:<br>
	 *            D - delete<br>
	 *            RC - read control<br>
	 *            WDAC - write DAC<br>
	 *            WO - write owner<br>
	 *            S - synchronize<br>
	 *            AS - access system security<br>
	 *            MA - maximum allowed<br>
	 *            GR - generic read<br>
	 *            GW - generic write<br>
	 *            GE - generic execute<br>
	 *            GA - generic all<br>
	 *            RD - read data/list directory<br>
	 *            WD - write data/add file<br>
	 *            AD - append data/add subdirectory<br>
	 *            REA - read extended attributes<br>
	 *            WEA - write extended attributes<br>
	 *            X - execute/traverse<br>
	 *            DC - delete child<br>
	 *            RA - read attributes<br>
	 *            WA - write attributes<br>
	 *            inheritance rights may precede either form and are applied<br>
	 *            only to directories:<br>
	 *            (OI) - object inherit<br>
	 *            (CI) - container inherit<br>
	 *            (IO) - inherit only<br>
	 *            (NP) - don't propagate inherit<br>
	 * @throws STAFQAException
	 */
	public void denyAccess(final String path, final String fileAdmin,
			final Permission permission) throws STAFQAException {
		final STAFQAResult result = startProcWait("icacls.exe", path
				+ "\\* /deny " + fileAdmin + ":" + permission.getRight()
				+ " /T");
		result.ifNotOkThrow("Couldn't change permissions to deny access to "
				+ path + "for user" + fileAdmin);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Changes file object access.
	 * 
	 * @param fileAdmins
	 *            - Group or username of file owner
	 * @param permission
	 *            - <code>permission</code> is a permission mask and can be
	 *            specified in one of two forms: <br>
	 *            a sequence of simple rights:<br>
	 *            F - full access<br>
	 *            M - modify access<br>
	 *            RX - read and execute access<br>
	 *            R - read-only access<br>
	 *            W - write-only access<br>
	 *            a comma-separated list in parenthesis of specific rights:<br>
	 *            D - delete<br>
	 *            RC - read control<br>
	 *            WDAC - write DAC<br>
	 *            WO - write owner<br>
	 *            S - synchronize<br>
	 *            AS - access system security<br>
	 *            MA - maximum allowed<br>
	 *            GR - generic read<br>
	 *            GW - generic write<br>
	 *            GE - generic execute<br>
	 *            GA - generic all<br>
	 *            RD - read data/list directory<br>
	 *            WD - write data/add file<br>
	 *            AD - append data/add subdirectory<br>
	 *            REA - read extended attributes<br>
	 *            WEA - write extended attributes<br>
	 *            X - execute/traverse<br>
	 *            DC - delete child<br>
	 *            RA - read attributes<br>
	 *            WA - write attributes<br>
	 *            inheritance rights may precede either form and are applied<br>
	 *            only to directories:<br>
	 *            (OI) - object inherit<br>
	 *            (CI) - container inherit<br>
	 *            (IO) - inherit only<br>
	 *            (NP) - don't propagate inherit<br>
	 * @throws STAFQAException
	 */
	public void grantAccess(final String path, final String fileAdmin,
			final Permission permission) throws STAFQAException {
		final STAFQAResult result = startProcWait("icacls.exe", path
				+ "\\* /grant " + fileAdmin + ":" + permission.getRight()
				+ " /T");
		result.ifNotOkThrow("Couldn't change permissions to deny access to "
				+ path + "for user" + fileAdmin);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Set Read-only file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void setReadOnly(final String path) throws STAFQAException {

		final STAFQAResult result = startProcWait("attrib.exe", "+R " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Set Read-only file attribute for "
				+ path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Clear Read-only file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void clearReadOnly(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "-R " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Clear Read-only file attribute for "
				+ path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Set Hidden file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void setHidden(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "+H " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Set Hidden file attribute for " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Clear Hidden file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void clearHidden(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "-H " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Clear Hidden file attribute for " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Set Archive file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void setArchive(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "+A " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Set Archive file attribute for " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Clear Archive file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void clearArchive(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "-A " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Clear Archive file attribute for "
				+ path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Set System file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void setSystem(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "+S " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Set System file attribute for " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Clear System file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void clearSystem(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "-S " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Clear System file attribute for " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Set Not content indexed file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void setNotIndexed(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "+I " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Set Not content indexed file attribute for "
				+ path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Clear Not content indexed file attribute.
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void clearNotIndexed(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("attrib.exe", "-I " + path
				+ "\\* /S /D");
		result.ifNotOkThrow("Failed to Clear Not content indexed file attribute for "
				+ path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Compresses contents to save disk space (using windows compact utility).
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void setCompact(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("compact.exe", path
				+ "\\* /C");
		result.ifNotOkThrow("Failed to compress file " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * UNCompresses contents (using windows compact utility).
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void clearCompact(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("compact.exe", path
				+ "\\* /U");
		result.ifNotOkThrow("Failed to compress file " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Encrypts all files in specified directory (using windows cipher.exe
	 * utility).
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void setEncrypt(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("cipher.exe", "/e \"" + path
				+ "\\*\"");
		result.ifNotOkThrow("Failed to encrypt file " + path);
		logger.info(result.getProcessStdOut());
	}

	/**
	 * Decrypts all files in specified directory (using windows cipher.exe
	 * utility).
	 * 
	 * @param path
	 * @throws STAFQAException
	 */
	public void clearEncrypt(final String path) throws STAFQAException {
		final STAFQAResult result = startProcWait("cipher.exe", "/d \"" + path
				+ "\\*\"");
		result.ifNotOkThrow("Failed to encrypt file " + path);
		logger.info(result.getProcessStdOut());
	}

	public void executeSQLScript(final String parameters)
			throws STAFQAException {
		final STAFQAResult result = startProcWait("sqlcmd.exe", parameters);
		if ((result.getReturnCode() != 0)
				|| (result.getProcessReturnCode() != 1)) {
			throw new STAFQAException("Did not run sqlcmd " + parameters
					+ " correctly!");
		}
	}

	public String getSSLCertificateHash() throws STAFQAException {
		final STAFQAResult result = startProcWait("netsh", "http show sslcert");
		result.ifNotOkThrow("Could not list SSL Certificates!!");
		final String out = result.getProcessStdOut();
		for (final String temp : out.split("\n")) {
			if (temp.contains("Certificate Hash")) {
				final int start = temp.indexOf(":") + 1;
				return temp.substring(start).trim();
			}
		}
		return null;
	}

	/**
	 * Indicate if system is locked against changes.
	 * 
	 * @return true if the system is locked against modification
	 */
	public boolean isLocked() {
		return exists(LOCK_FILE);
	}

	/**
	 * Lock or unlock the current system.
	 * 
	 * @param mode
	 *            create lock file if true, clear it if false
	 */
	public void lock(final boolean mode) throws STAFQAException {
		if (mode && !isLocked()) {
			logger.info("Locking " + getHostname()
					+ " to prevent accidental destruction.");
			writeFile(LOCK_FILE, "Locked against install!");
		} else if (!mode && isLocked()) {
			logger.info(getHostname() + " is now unlocked and vulnerable.");
			rmFile(LOCK_FILE);
		}
	}

	public void cancelHoldAccount(final String args,
			final boolean expectedResult) throws ProductDamagedException,
			TestException {
		try {
			final STAFQAResult result = startProcWait("CancelHoldAccount", args);
			if ((result.getReturnCode() != 0) && expectedResult) {
				throw new ProductDamagedException("CancelHoldAccount " + args
						+ " on host " + getHostname()
						+ " was expected to pass but didn't "
						+ result.getResultString());
			} else if ((result.getReturnCode() == 0) && !expectedResult) {
				throw new ProductDamagedException("CancelHoldAccount " + args
						+ " on host " + getHostname()
						+ " was expected to fail but didn't "
						+ result.getResultString());
			}
		} catch (final STAFQAException e) {
			throw new TestException("CancelHoldAccount " + args + " on host "
					+ getHostname() + " blew up with this . . . ", e);
		}
	}

	/**
	 * This routine will give list of shares on the hostanme specified.
	 * 
	 * 
	 * @return string list of shares
	 * @throws STAFQAException
	 */
	public String getListOfShares() throws STAFQAException {
		final STAFQAResult result = startProcWait("net.exe", "share ");
		String listOfShares = result.getProcessStdOut();
		if (listOfShares.equals(" ")) {
			logger.info("No shares present");
		}
		return listOfShares;

	}

	/**
	 * Removes shares when share name is specified.
	 * 
	 * 
	 * @param shareName
	 *            Name of the share
	 * 
	 * @throws STAFQAException
	 */
	public void removeShare(String shareName) throws STAFQAException {
		final String command = "net.exe";
		final String args = "share " + shareName + " /Delete /Y";

		final STAFQAResult result = startProcWait(command, args);
		final String output = result.getProcessStdOut();
		if (output.contains("was deleted successfully")) {

			logger.info(shareName + " was deleted successfully.");
		}
	}

}
