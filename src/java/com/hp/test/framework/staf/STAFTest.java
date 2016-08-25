
package com.hp.test.framework.staf;

import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFResult;
import com.ibm.staf.STAFUtil;
import java.util.*;
 
public class STAFTest
{
    public static void main(String argv[])
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
                
                
                String freepool="MCK_FreePool";
                String file="File";
                //String machine = "yanamalp2";
                String machine = "local";
                //String command = "dir \\temp";
                String command = "staf local process start shell command C:\\PROGRA~1\\OmniBack\\bin\\omnimm.exe parms -create_free_pool " + freepool + " " + file + " WORKDIR C:\\PROGRA~1\\OmniBack\\bin  RETURNSTDOUT STDERRTOSTDOUT WAIT";
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
 
}  // End of STAFTest
