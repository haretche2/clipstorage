package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.ClipDataLocation;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class FtpTools {
    private static Logger log = Logger.getLogger(FtpTools.class);
    private static Dictionary clientesCreados=new Hashtable();

    public static FTPFile[] listarArchivosEnServidor(StorageServer server) throws IOException{
        FTPClient client = getClient(server);
        return(client.listFiles());
    }

    public static void guardar(StorageServer servidorEnQueAlmacenar, ClipData datos) throws IOException {
        FTPClient clienteFtp = FtpTools.getClient(servidorEnQueAlmacenar);
        byte[] bytes= new byte[datos.getBytes().length];
        for(int i=0;i<bytes.length;i++){
            bytes[i]=datos.getBytes()[i];
        }
        clienteFtp.storeFile((datos.getClip().getId()).toString(), new ByteArrayInputStream(bytes));
    }

    public static Byte[] leer(StorageServer servidorDelQueLeer, long fileName) throws IOException {
        log.debug("Iniciando lectura del archivo: "+Long.toString(fileName)+" del servidor: "+servidorDelQueLeer.getAddress());
        FTPClient clienteFtp = FtpTools.getClient(servidorDelQueLeer);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        clienteFtp.retrieveFile(Long.toString(fileName), outputStream);

        byte[] bytes=outputStream.toByteArray();
        Byte[] convertedBytes= new Byte[bytes.length];
        for(int i=0;i<bytes.length;i++){
            convertedBytes[i]=bytes[i];
        }
        return convertedBytes;
    }

    public static void borrar(StorageServer servidorEnQueBorrar,long fileName) throws IOException{
        FTPClient clienteFtp = FtpTools.getClient(servidorEnQueBorrar);
        clienteFtp.deleteFile(Long.toString(fileName));
    }



    private static FTPClient getClient(StorageServer storageServer) throws IOException {
        if(clientesCreados.get(storageServer.getAddress())!=null)
            return (FTPClient)clientesCreados.get(storageServer.getAddress());

        FTPClient client=new FTPClient();
        log.debug("creating client");
        String sFTP = storageServer.getAddress();
        String sUser = storageServer.getUsername();
        String sPassword = storageServer.getPassword();
        try
        {
            log.debug("trying to connect to:" +storageServer.getAddress());
            client.setConnectTimeout(Configuration.getIntConfiguration("TimeoutCrearClienteFTP"));
            client.connect(sFTP);
            log.debug("trying to login to: "+storageServer.getAddress());
            boolean login = client.login(sUser, sPassword);
            log.debug("ftp login result: "+login);
        }
        catch (IOException ioe) {
            log.error(ioe);
            throw ioe;
        }
        clientesCreados.put(storageServer.getAddress(),client);
        return client;
    }
}
