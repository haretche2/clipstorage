package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.model.StorageServer;

import java.util.List;
import java.util.Random;

/**
 * Esta clase permite realizar diversas acciones propias de las listas de StorageServer
 */
public class StorageServerLists{
    private static Random randomGenerator=new Random();

    /**
     * Devuelve el servidor con menor cantidad de archivos almacenados.
     * @param storageServers Lista de la que elegir.
     * @return El servidor con menor cantidad de archivos.
     */
    public static StorageServer getSiguienteServidorParaAlmacenarClip(List<StorageServer> storageServers){
        StorageServer elDeMenorCantidadDeArchivos=null;
        for(StorageServer unServer:storageServers){
            if(elDeMenorCantidadDeArchivos==null || unServer.getAmountOfClipDataStored()<elDeMenorCantidadDeArchivos.getAmountOfClipDataStored()){
                elDeMenorCantidadDeArchivos=unServer;
            }
        }
        return elDeMenorCantidadDeArchivos;
    }

    /**
     * Devuelve un servidor aleatorio.
     * @param list Lista de la que elegir.
     * @return Un servidor al azar.
     */
    public static StorageServer getRandomFromList(List<StorageServer> list)    {
        int index = randomGenerator.nextInt(list.size());
        StorageServer item = list.get(index);
        return item;
    }
}
