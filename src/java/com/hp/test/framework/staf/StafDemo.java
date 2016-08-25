
package com.hp.test.framework.staf;

import com.hp.test.framework.htmparse.ParseAllEnvironmentReports;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import static com.hp.test.framework.staf.STAF.getLocalIPAddress;
import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFResult;
import com.ibm.staf.STAFUtil;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sayedmo
 */
public class StafDemo {

    /**
     * @param args the command line arguments
     */
    STAFHandle handle;
    
    
    public static void main(String[] args) throws STAFException, STAFQAException, UnknownHostException, ClassNotFoundException, IOException {
        StafDemo s=new StafDemo();
        ReadStafProperties stafprop=new ReadStafProperties();
        String STAXjob=stafprop.getProperty("STAXJobPath");
        
        
        // TODO code application logic here
           //handle = new STAFHandle("MyAp");
            //System.out.println("My handle is: " + handle.getHandle());
        
        //Windows
        //s.copyFolder("16.166.242.58", "C:\\Gen_JellyNov11\\ISTF_Build", "16.183.92.205", "C:\\ISTF_Build");
        //Linux
        //s.copyFolder("16.166.242.58", "C:\\Gen_JellyNov11\\ISTF_Build", "16.184.30.92", "/usr/local/ISTF_Build");
        //Windows
        //s.ExecuteProcessCommand("staf 16.183.92.205 process start shell command ant rundptest -buildfile C:\\ISTF_Build\\build.xml WORKDIR C:\\ISTF_Build RETURNSTDOUT STDERRTOSTDOUT WAIT");
        //s.ExecuteProcessCommand("staf 16.183.92.205 process start shell command ant initdpvar -buildfile C:\\ISTF_Build\\build.xml WORKDIR C:\\ISTF_Build RETURNSTDOUT STDERRTOSTDOUT WAIT");
        //Linux
        //s.ExecuteProcessCommand("staf 16.184.22.208 process start shell command /opt/ant/apache-ant-1.9.6/bin/ant rundptest -buildfile /usr/local/ISTF_Build/build.xml WORKDIR /usr/local/ISTF_Build RETURNSTDOUT STDERRTOSTDOUT WAIT");
        //s.ExecuteProcessCommand("staf 16.184.30.92 process start shell command ant initdpvar -buildfile /usr/local/ISTF_Build/build.xml WORKDIR /usr/local/ISTF_Build RETURNSTDOUT STDERRTOSTDOUT WAIT");
        
          //Distributed Environment or parallel execution
        //s.ExecuteProcessCommand("STAF local STAX EXECUTE FILE "+STAXjob+" WAIT RETURNRESULT");
        
        s.ExecuteProcessCommand("STAF local STAX EXECUTE FILE "+STAXjob+" WAIT RETURNRESULT");
       //for Consolidated Reports
        ParseAllEnvironmentReports.ConsolidatedReport();
    }
    
    /*
    This program execute command on local or remote machine
    */
     public void ExecuteProcessCommand(String command) 
    {
        try
        {
            // Create a STAFHandle

            STAFHandle handle = new STAFHandle("MyApplication");
            System.out.println("My handle is: " + handle.getHandle());

            try
            {
                // Submit a request to the PROCESS service to run a command on
                // a machine and wait for it to complete and returns its stdout
                // and stderr.  Note that the result from this request returns
                // a marshalled map so we'll use STAFResult's resultObj variable
                // to access the map root object
                
                
                //String freepool="MCK_FreePool";
                //String file="File";
                //String machine = "yanamalp2";
                String machine = "local";
                //String command = "dir \\temp";
                //String command = "staf local process start shell command C:\\PROGRA~1\\OmniBack\\bin\\omnimm.exe parms -create_free_pool " + freepool + " " + file + " WORKDIR C:\\PROGRA~1\\OmniBack\\bin  RETURNSTDOUT STDERRTOSTDOUT WAIT";
               
                //String command = "staf local process start shell command omnimm.exe parms -create_free_pool " + freepool + " " + file + " WORKDIR C:\\PROGRA~1\\OmniBack\\bin  RETURNSTDOUT STDERRTOSTDOUT WAIT";
                //String command = "STAF local PROCESS START SHELL COMMAND ant test -buildfile C:/Gen_JellyNov11/build.xml RETURNSTDOUT STDERRTOSTDOUT WAIT";
                
                String service = "PROCESS";
                String request = "START SHELL COMMAND " +
                    STAFUtil.wrapData(command) +
                    " WAIT RETURNSTDOUT STDERRTOSTDOUT";

                STAFResult result = handle.submit2(machine, service, request);
                    
                if (result.rc != 0)
                {
                    System.out.println(
                        "ERROR: STAF " + machine + " " + service + " " + request +
                        " RC: " + result.rc + ", Result: " + result.result);
                    System.exit(1);
                }
                
                // The command was started successfully. Check the process
                // return code and if non-zero, also print an error message

                Map resultMap = (Map)result.resultObj;
                String processRC = (String)resultMap.get("rc");

                if (!processRC.equals("0"))
                {
                    System.out.println(
                        "ERROR: Process RC is not 0.\n" + result.resultContext);
                    System.exit(1);
                }

                // Print the stdout/stderr data for the command

                List returnedFileList = (List)resultMap.get("fileList");
                Map stdoutMap = (Map)returnedFileList.get(0);
                String stdoutData = (String)stdoutMap.get("data");

                System.out.println("Process Stdout:\n" + stdoutData);
            }
            finally
            {
                handle.unRegister();
            }
        }
        catch (STAFException e)
        {
            System.out.println(
                "Error (un)registering with STAF, RC:" + e.rc);
            System.exit(1);
        }
 
    }   // End of main()
    
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
			throws STAFQAException, STAFException {
            handle = new STAFHandle("MyAp");
            
		// Check if destination is localhost
		if ("localhost".equalsIgnoreCase(destinationHostname)
				|| "local".equalsIgnoreCase(destinationHostname)) {
			destinationHostname = getLocalIPAddress();
		}

		final String cmd = String.format(
				" COPY DIRECTORY %s TODIRECTORY %s TOMACHINE %s ",
				sourceFolder, destinationFolder, destinationHostname);
		//final STAFQAResult result = execute("FS", cmd);
                STAFResult result = handle.submit2("16.185.205.184", "FS", cmd);
		/*result.ifNotOkThrow(String.format(
				"Cannot copy folder \\\\%s\\%s to \\\\%s\\%s", "My machine",
				sourceFolder, destinationHostname, destinationFolder));*/
                 if (result.rc != 0)
                {
                    System.out.println(
                        "ERROR: STAF " + "sayedmo5" + " " + "FS" + " " + cmd +
                        " RC: " + result.rc + ", Result: " + result.result);
                    System.exit(1);
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
            String destfolder) throws STAFException, STAFQAException {
        
        handle = new STAFHandle("MyAp");
        
        if ("localhost".equalsIgnoreCase(srcmachine)
				|| "local".equalsIgnoreCase(srcmachine)) {
			srcmachine = getLocalIPAddress();
		}
     
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
        else {
            System.out.println("Successfully copied the folder");
        }
        return (result);
        
    }
    
}
