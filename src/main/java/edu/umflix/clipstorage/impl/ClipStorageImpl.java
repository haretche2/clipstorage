package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.ClipStorage;
import edu.umflix.clipstorage.Exceptions.NoServersOnlineException;
import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.tools.FtpTools;
import edu.umflix.clipstorage.tools.MemoryManager;
import edu.umflix.clipstorage.tools.RecuperadorDeServidor;
import edu.umflix.clipstorage.tools.StorageServerTools;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 1/06/13
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
@Stateless
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
        List<StorageServer> disponibles= MemoryManager.getOnlineServers();
        if(!StorageServerTools.guardarClipEnLosAlgunosDeLosServidores(disponibles, Configuration.getIntConfiguration("Replicas"),clipdata)){
            throw new NoServersOnlineException();
        }
    }
    @Override
    public ClipData getClipDataByClipId(long id){
        List<StorageServer> servidoresConElClip= MemoryManager.getStorageServersConClipDataById(id);
        Byte[] bytes=null;
        while(servidoresConElClip.size()>0 && bytes==null){
            StorageServer servidorAUsar=StorageServerTools.getRandomFromList(servidoresConElClip);
            try {
                bytes=FtpTools.leer(servidorAUsar,id);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                RecuperadorDeServidor.recuperar(servidorAUsar);
                servidoresConElClip.remove(servidorAUsar);
            }
        }
        if(bytes==null){
            log.error("El archivo "+id+" no fue encontrado en el sistema");
            throw new IllegalArgumentException("El id recibido no se encuentra en el sistema: "+id);
        }
        Clip clipDeResult=new Clip();
        clipDeResult.setId(id);
        ClipData result= new ClipData();
        result.setBytes(bytes);
        result.setId(id);
        result.setClip(clipDeResult);
        return result;
    }
}
