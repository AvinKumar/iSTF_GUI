/*
This function copy some file from remote linux box to kick start machine and from ther it copies to any windows share location.  
Parameters order username, password, hostname, port, remotecommand, copyfrom and copyto
 */

package com.hp.test.framework.DSSpecific;

import com.hp.test.framework.jelly.*;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 *
 * @author sayedmo
 */
public class ftpfromLinuxtoWindowsTag extends SeleniumTagSupport {
     static Logger logger = Logger.getLogger(ftpfromLinuxtoWindowsTag.class);

    private String value;
    String splitvalue[];
    String username, password, hostname,remotecopycommand, CopyFileFrom, CopyFileTo;
    int port;
    
    public String getvalue() {
        return value;
    }

    public void setvalue(String value) {
        this.value = value;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        JSch jsch = new JSch();
        Session session = null;
        
        splitvalue = value.split("::");
        username=splitvalue[0].toString();
        password=splitvalue[1].toString();
        hostname=splitvalue[2].toString();
        port=Integer.parseInt(splitvalue[3].toString());
        remotecopycommand=splitvalue[4].toString();
        CopyFileFrom=splitvalue[5].toString();
        CopyFileTo=splitvalue[6].toString();
        
        System.out.println(username+password+hostname+port+remotecopycommand+CopyFileFrom+CopyFileTo);
                
        System.out.println("Trying to connect.....");
        try {
            session = jsch.getSession(username, hostname, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect(); 
            
            //check if directory test exists, if not create under root
            Channel channel0 = session.openChannel("sftp");
            channel0.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel0;
            String currentDirectory = channelSftp.pwd();
            String dir = "test";
            SftpATTRS attrs = null;
            try {
                attrs = channelSftp.stat(currentDirectory + "/" + dir);
            } catch (Exception e) {
                System.out.println(currentDirectory + "/" + dir + " not found");
            }

            if (attrs != null) {
                System.out.println("Directory exists IsDir=" + attrs.isDir());
            } else {
                System.out.println("Creating dir " + dir);
                channelSftp.mkdir(dir);
            }
            
           
            //copy zip file from any remote machine to controller
              Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(remotecopycommand);
               channel.connect();

            //sftp the same files to local windows machine   
            Channel channel1 = session.openChannel("sftp");
            channel1.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel1; 
            sftpChannel.get(CopyFileFrom, CopyFileTo);
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            e.printStackTrace(); 
        }
        System.out.println("Done !!");
    }
    
}
