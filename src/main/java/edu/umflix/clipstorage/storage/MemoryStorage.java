package edu.umflix.clipstorage.storage;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 14/06/13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class MemoryStorage extends Storage {
    private Logger log = Logger.getLogger(MemoryStorage.class);
    private Dictionary<String,Dictionary<String,Byte[]>> listaDeServidoresSimulados = new Hashtable<String, Dictionary<String, Byte[]>>();

    public String[] listarArchivosEnServidor(StorageServer server){
        Dictionary<String,Byte[]> servidorSimulado = getServidorSimulado(server);
        ArrayList<String> nombres= Collections.list(servidorSimulado.keys());
        return nombres.toArray(new String[nombres.size()]);
    }

    public void guardar(StorageServer servidorEnQueAlmacenar, ClipData datos){
        Dictionary<String,Byte[]> servidorSimulado = getServidorSimulado(servidorEnQueAlmacenar);
        servidorSimulado.put(Long.toString(datos.getClip().getId()),datos.getBytes());
    }

    public Byte[] leer(StorageServer servidorDelQueLeer, long fileName){
        Dictionary<String,Byte[]> servidorSimulado = getServidorSimulado(servidorDelQueLeer);
        return servidorSimulado.get(Long.toString(fileName));
    }

    public void borrar(StorageServer servidorEnQueBorrar,long fileName){
        Dictionary<String,Byte[]> servidorSimulado = getServidorSimulado(servidorEnQueBorrar);
        servidorSimulado.remove(Long.toString(fileName));
    }

    private Dictionary<String,Byte[]> getServidorSimulado(StorageServer storageServer){
        if(listaDeServidoresSimulados.get(storageServer.getAddress())==null){
            listaDeServidoresSimulados.put(storageServer.getAddress(),new Hashtable<String, Byte[]>());
        }
        return listaDeServidoresSimulados.get(storageServer.getAddress());
    }
}
