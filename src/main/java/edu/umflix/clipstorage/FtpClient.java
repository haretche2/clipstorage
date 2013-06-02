package edu.umflix.clipstorage;

import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 2/06/13
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class FtpClient {

    private static Logger log = Logger.getLogger(FtpClient.class);
    private static FTPClient client;


    public static FTPClient getClient() throws IOException{
        if(client==null) {
            client=new FTPClient();
            log.debug("creating client");
            String sFTP = "192.168.1.108";
            String sUser = "";
            String sPassword = "";
            try
            {
                log.debug("trying to connect");
                client.connect(sFTP);
                log.debug("trying to login");
                boolean login = client.login(sUser, sPassword);
            }
            catch (IOException ioe) {
                log.error(ioe);
                throw ioe;
            }
        }
        log.error("errorsito");
        return client;
    }
}
