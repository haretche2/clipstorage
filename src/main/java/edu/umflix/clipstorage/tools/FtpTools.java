package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.StorageServer;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class FtpTools {
    private static Logger log = Logger.getLogger(FtpTools.class);

    public static FTPClient getClient(StorageServer storageServer) throws IOException {
        FTPClient client=new FTPClient();
        log.debug("creating client");
        String sFTP = storageServer.getAddress();
        String sUser = storageServer.getUsername();
        String sPassword = storageServer.getPassword();
        try
        {
            log.debug("trying to connect");
            client.setConnectTimeout(Configuration.getIntConfiguration("TimeoutCrearClienteFTP"));
            client.connect(sFTP);
            log.debug("trying to login");
            boolean login = client.login(sUser, sPassword);
            log.debug("ftp login result: "+login);
        }
        catch (IOException ioe) {
            log.error(ioe);
            throw ioe;
        }
        return client;
    }
}
