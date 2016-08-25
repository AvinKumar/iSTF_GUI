

package com.hp.test.framework.staf;

import com.hp.test.framework.htmparse.ParseAllEnvironmentReports;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sayedmo
 */
public class OperatingSystemInfo 
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        
        //System.out.println(OperatingSystemInfo.getLastRun("C:/Reports"));
        
        // The key for getting operating system name
//        String name = "os.name";        
//        // The key for getting operating system version
//        String version = "os.version";        
//        // The key for getting operating system architecture
//        String architecture = "os.arch";
//        
//        System.out.println("Name: " + System.getProperty(name));
//        System.out.println("Version: " + System.getProperty(version));
//        System.out.println("Architecture: " + System.getProperty(architecture));
//        //System.out.println("hostname: "+Runtime.getRuntime().exec("hostname"));
//        System.out.println("hostname: "+InetAddress.getLocalHost().getHostName());
        
    }
    public static String getOS() {
        String name = "os.name"; 
        return name;
    }
    
        public static int getLastRun(String path) {
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        
       List<String> Only_run_list=new ArrayList<String>();
      
       for (int i = 1; i < directories.length; i++) {
           if(directories[i].contains("_"));
           Only_run_list.add(directories[i]);
       }
        String temp_ar[] = Only_run_list.get(0).split("_");
        
        int max = Integer.parseInt(temp_ar[1]);
        for (int i = 1; i < Only_run_list.size(); i++) {
            String temp_ar1[] = Only_run_list.get(i).split("_");
            
            int run = Integer.parseInt(temp_ar1[1]);
            
            if (run > max) {
                max = run;
            }
            
        }
        System.out.println(Arrays.toString(directories));
        System.out.println("Latest Run" + max);
        return max;
    }
        
}
