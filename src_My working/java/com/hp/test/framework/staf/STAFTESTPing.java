

package com.hp.test.framework.staf;

import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;

/**
 *
 * @author sayedmo
 */
public class STAFTESTPing
{
    public static void main(String argv[])
    {
        try
        {
            // Create a STAFHandle

            STAFHandle handle = new STAFHandle("MyApplication");
            System.out.println("My handle is: " + handle.getHandle());
 
            // Submit some requests to STAF services

            String machine = "16.184.30.92";
            String service = "PING";
            String request = "PING";

            try
            {
                // Submit a synchronous request to the PING service on
                // the local machine
                
                String result = handle.submit(machine, service, request);
                System.out.println("PING Result: " + result);

                // Submit an asynchronous STAF request to the PING service
                // on the local machine
                
                result = handle.submit(
                    STAFHandle.ReqQueueRetain, machine, service, request);
                System.out.println("PING Request number: " + result);
            }
            catch (STAFException e)
            {
                System.out.println(
                    "Error submitting STAF " + machine + " " + service +
                    " " + request);
                System.out.println("RC: " + e.rc);
                System.out.println(e.getMessage());
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
