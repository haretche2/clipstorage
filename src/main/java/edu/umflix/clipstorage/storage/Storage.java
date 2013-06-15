package edu.umflix.clipstorage.storage;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.config.ConfigurationItemsEnum;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 *  Esta clase abstracta define las reglas que debe cumplir una tecnología de almacenamiento de clips y define cual utilizar dependiendo de la configuración definida en config.properties.
 *  La implementación de las funciones de acceso y escritura se encuentran abiertas a su implementación por cada una de esas tecnologías.
 */
public abstract class Storage {
    private static Logger log = Logger.getLogger(Storage.class);
    private static Storage instance=null;

    public static Storage getInstance(){
        if(instance==null){
            if ("ftp".equals(Configuration.getConfiguracion(ConfigurationItemsEnum.TIPODEALMACENAMIENTODECLIPS))){
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
