package edu.umflix.clipstorage.storage;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.config.ConfigurationItemsEnum;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Esta clase permite almacenar los clips en servidores FTP, es la que se debería utilizar en producción.
 */
public class FTPStorage extends Storage{
    private Logger log = Logger.getLogger(FTPStorage.class);

    public String[] listarArchivosEnServidor(StorageServer server) throws IOException {
        FTPClient client = getClient(server);
        FTPFile[] archivosComoFTPFileArray=client.listFiles();
        String[] archivosComoStringArray= new String[archivosComoFTPFileArray.length];
        for(int i=0;i<archivosComoFTPFileArray.length;i++){
            archivosComoStringArray[i]=archivosComoFTPFileArray[i].getName();
        }
        return(archivosComoStringArray);
    }

    public void guardar(StorageServer servidorEnQueAlmacenar, ClipData datos) throws IOException {
        FTPClient clienteFtp = getClient(servidorEnQueAlmacenar);
        byte[] bytes= new byte[datos.getBytes().length];
        for(int i=0;i<bytes.length;i++){
            bytes[i]=datos.getBytes()[i];
        }
        clienteFtp.storeFile((datos.getClip().getId()).toString(), new ByteArrayInputStream(bytes));
    }

    public Byte[] leer(StorageServer servidorDelQueLeer, long fileName) throws IOException {
        log.debug("Iniciando lectura del archivo: "+Long.toString(fileName)+" del servidor: "+servidorDelQueLeer.getAddress());
        FTPClient clienteFtp = getClient(servidorDelQueLeer);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        clienteFtp.retrieveFile(Long.toString(fileName), outputStream);

        byte[] bytes=outputStream.toByteArray();
        Byte[] convertedBytes= new Byte[bytes.length];
        for(int i=0;i<bytes.length;i++){
            convertedBytes[i]=bytes[i];
        }
        return convertedBytes;
    }

    public void borrar(StorageServer servidorEnQueBorrar,long fileName) throws IOException{
        FTPClient clienteFtp = getClient(servidorEnQueBorrar);
        clienteFtp.deleteFile(Long.toString(fileName));
    }

    /**
     * Genera un cliente FTP para acceder al servidor
     * @param storageServer Un StorageServer con los datos del servidor al que conectarse y crear el cliente
     * @return Un cliente FTP para acceder al servidor indicado por storageServer
     * @throws IOException En caso de que no se pueda conectar con el servidor
     */
    private FTPClient getClient(StorageServer storageServer) throws IOException {
        FTPClient client=new FTPClient();
        log.debug("creating client");
        String sFTP = storageServer.getAddress();
        String sUser = storageServer.getUsername();
        String sPassword = storageServer.getPassword();
        try
        {
            log.debug("trying to connect to:" +storageServer.getAddress());
            client.setConnectTimeout(Configuration.getIntConfiguration(ConfigurationItemsEnum.TIMEOUTCREARCLIENTEFTP));
            client.connect(sFTP);
            log.debug("trying to login to: "+storageServer.getAddress());
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
