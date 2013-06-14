package edu.umflix.clipstorage.storage;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 14/06/13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class Storage {
    private static Logger log = Logger.getLogger(Storage.class);
    private static Storage instance=null;

    public static Storage getInstance(){
        if(instance==null){
            if ("ftp".equals(Configuration.getConfiguracion("TipoDeAlmacenamientoDeClips"))){
                log.debug("Se utilizará almacenamiento de clips en servidores ftp");
                instance=new FTPStorage();
            }else{
                log.info("Atención: Se utilizará almacenamiento de clips en memoria. Sólo debería ser utilizado por motivos de testing. Los clips se perderán al terminar la ejecución");
                instance=new MemoryStorage();
            }
        }
        return instance;
    }

    public abstract String[] listarArchivosEnServidor(StorageServer server) throws IOException;

    public abstract void guardar(StorageServer servidorEnQueAlmacenar, ClipData datos) throws IOException;

    public abstract Byte[] leer(StorageServer servidorDelQueLeer, long fileName) throws IOException ;

    public abstract void borrar(StorageServer servidorEnQueBorrar,long fileName) throws IOException;

}
