package com.shubham.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
 
/**
 * A program that demonstrates how to upload files from local computer
 * to a remote FTP server using Apache Commons Net API.
 * @author www.codejava.net
 */
public class FTPUploadFileDemo {
 
    public void imageupload(int i) {
        String server = "ftp.byethost7.com";
        int port = 21;
        String user = "g";
        String pass = "g";
//      http://shubhamyeole.byethost8.com/public_html/shubham.png

        
//      String user = "g";
//      String pass = "g";
//      http://pace-c691-team3.byethost7.com/public_html/shubham.png

        FTPClient ftpClient = new FTPClient();
        try {
 
            ftpClient.connect(server, port);
            System.out.println(ftpClient.login(user, pass));
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean changeWorkingDirectory = ftpClient.changeWorkingDirectory("htdocs/public_html/");

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File("C:/Users/20188/AppData/Local/Temp/Z4CbY0euwikqt6zg4zZ-dOu1.jpg");
 
            String firstRemoteFile = "shubham-"+i+".jpg";
            InputStream inputStream = new FileInputStream(firstLocalFile);
 
            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
 
            // APPROACH #2: uploads second file using an OutputStream
//            File secondLocalFile = new File("C:/Users/20188/Desktop/ShubhamYeoleResume.pdf");
//            String secondRemoteFile = "testReport.pdf";
//            inputStream = new FileInputStream(secondLocalFile);
// 
//            System.out.println("Start uploading second file");
//            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
//            byte[] bytesIn = new byte[4096];
//            int read = 0;
// 
//            while ((read = inputStream.read(bytesIn)) != -1) {
//                outputStream.write(bytesIn, 0, read);
//            }
//
//            inputStream.close();
//            outputStream.close();
// 
//            boolean completed = ftpClient.completePendingCommand();
//            if (completed) {
//                System.out.println("The second file is uploaded successfully.");
//            }
 
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
 
}


