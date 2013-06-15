package edu.umflix.clipstorage.storage;

import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Esta clase implementa la interfaz Storage y permite leer y escribir en servidores de almacenamiento simulados en memoria. Debería ser utilizada únicamente con propósitos de testing.
 */
public class MemoryStorage extends Storage {
    // En esta variable se almacenan los servidores simulados con sus datos.
    private Dictionary<String,Dictionary<String,Byte[]>> listaDeServidoresSimulados = new Hashtable<String, Dictionary<String, Byte[]>>();

    public String[] listarArchivosEnServidor(StorageServer storageServer){
        Dictionary<String,Byte[]> servidorSimulado = getServidorSimulado(storageServer);
        ArrayList<String> nombresDeArchivos= Collections.list(servidorSimulado.keys());
        return nombresDeArchivos.toArray(new String[nombresDeArchivos.size()]);
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

    /**
     * Devuelve una instancia de servidor de almacenamiento simulado (representada por un diccionario {Id del clip, arreglo de bytes de datos}).
     * @param storageServer Referencia con la 'ip' del servidor al que se quiere acceder.
     * @return Una instancia de servidor de almacaneamiento simulado con la 'ip' indicada por el storageServer
     */
    private Dictionary<String,Byte[]> getServidorSimulado(StorageServer storageServer){
        if(listaDeServidoresSimulados.get(storageServer.getAddress())==null){
            listaDeServidoresSimulados.put(storageServer.getAddress(),new Hashtable<String, Byte[]>());
        }
        return listaDeServidoresSimulados.get(storageServer.getAddress());
    }
}
