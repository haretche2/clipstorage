package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.ClipStorage;
import edu.umflix.clipstorage.Exceptions.NoServersOnlineException;
import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.config.ConfigurationItemsEnum;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.storage.Storage;
import edu.umflix.clipstorage.storage.StorageMappingManager;
import edu.umflix.clipstorage.tools.StorageServerCommunicationsHelper;
import edu.umflix.clipstorage.tools.StorageServerLists;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import java.io.IOException;
import java.util.List;

/**
 * Esta clase es la implementación de ClipStorage, puerta de entrada al Bean.
 */
@Stateless (name = "ClipStorage")
public class ClipStorageImpl implements ClipStorage{
    private static Logger log= Logger.getLogger(ClipStorageImpl.class);
    private static final ClipStorageImpl INSTANCIA=new ClipStorageImpl();

    public static ClipStorageImpl getInstancia(){
        return INSTANCIA;
    }

    @Override
    public void storeClipData(ClipData clipdata) {
        //Comprobacion de validez de los parametros
        if(clipdata==null){
            log.error("Se recibio un clipdata null");
            throw new IllegalArgumentException("Se recibio un clipdata null");
        }
        if(clipdata.getBytes()==null){
            log.error("Se recibio un clipdata con bytes null");
            throw new IllegalArgumentException("Se recibio un clipdata con bytes null");
        }
        if(clipdata.getBytes().length<1){
            log.error("Se recibio un clipdata con bytes de largo menor a 1");
            throw new IllegalArgumentException("Se recibio un clipdata con bytes de largo menor a 1");
        }
        if(clipdata.getClip()==null){
            log.error("Se recibio un clipdata con clip null");
            throw new IllegalArgumentException("Se recibio un clipdata con clip null");
        }
        if(clipdata.getClip().getId()<0){
            log.error("Se recibio un clipdata con clip con id negativo");
            throw new IllegalArgumentException("Se recibio un clipdata con clip con id negativo");
        }
        List<StorageServer> servidoresDisponibles= StorageMappingManager.getOnlineServers();
        if(!StorageServerCommunicationsHelper.guardarClipEnLosAlgunosDeLosServidores(servidoresDisponibles, Configuration.getIntConfiguration(ConfigurationItemsEnum.REPLICAS), clipdata)){
            // No se pudo guardar el clipdata en ninguno de los servidores.
            throw new NoServersOnlineException();
        }
    }

    /**
     * Esta implementación del método getClipDataByClipId definido en la interfaz, realiza el balanceo de cargas para acceder a los clips assignando un servidor aleatorio (de entre los que tienen el clip) para cada invocación.
     * @param id identificador del Clip con el que buscar el clipdata.
     * @return El ClipData del id especificado con su Byte[] de datos.
     */
    @Override
    public ClipData getClipDataByClipId(long id){
        List<StorageServer> servidoresConElClip= StorageMappingManager.getStorageServersConClipDataById(id);
        Byte[] bytesDelClipData=null;
        while(servidoresConElClip.size()>0 && bytesDelClipData==null){
            StorageServer servidorAUsar= StorageServerLists.getRandomFromList(servidoresConElClip);
            try {
                bytesDelClipData= Storage.getInstance().leer(servidorAUsar, id);
            } catch (IOException e) {
                e.printStackTrace();
                StorageServerCommunicationsHelper.recuperar(servidorAUsar);
                servidoresConElClip.remove(servidorAUsar);
            }
        }
        if(bytesDelClipData==null){
            log.error("El archivo "+id+" no fue encontrado en el sistema");
            throw new IllegalArgumentException("El id recibido no se encuentra en el sistema: "+id);
        }
        Clip clipDeResult=new Clip();
        clipDeResult.setId(id);
        ClipData result= new ClipData();
        result.setBytes(bytesDelClipData);
        result.setId(id);
        result.setClip(clipDeResult);
        return result;
    }
}
