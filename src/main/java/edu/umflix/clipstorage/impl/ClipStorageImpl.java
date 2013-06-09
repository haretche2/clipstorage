package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.ClipStorage;
import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.tools.Memory;
import edu.umflix.clipstorage.tools.StorageServerTools;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 1/06/13
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "clipstorage")
public class ClipStorageImpl implements ClipStorage{

    private static Logger log= Logger.getLogger(ClipStorageImpl.class);

    @Override
    public void storeClipData(ClipData clipdata) {
        List<StorageServer> disponibles= Memory.getOnlineServers();
                   Clip c =new Clip();



        for(int i=0;i< Configuration.getIntConfiguration("Replicas");i++){
            if(disponibles.size()>0){
                StorageServer siguiente=StorageServerTools.getSiguienteServidoresParaAlmacenarClip(disponibles);
                disponibles.remove(siguiente);
                try{
                    StorageServerTools.guardar(siguiente,clipdata);
                }catch (IOException e){
                   i--;
                    StorageServerTools.servidorCaido(siguiente);
                }
            }else {
                log.warn("Insuficientes servidores de almacenamiento");
            }
        }
    }

    @Override
    public ClipData getClipDataByClipId(long id) throws FileNotFoundException {
        List<StorageServer> clipDataLocations= Memory.getStorageServersForClipDataById(id);
        Byte[] bytes=null;
        while(clipDataLocations.size()>0 && bytes==null){
            StorageServer servidorAUsar=StorageServerTools.getRandomFromList(clipDataLocations);
            try {
                bytes=StorageServerTools.leer(servidorAUsar,id);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                StorageServerTools.servidorCaido(servidorAUsar);
                clipDataLocations.remove(servidorAUsar);
            }
        }
        if(bytes==null){
            log.error("El archivo "+id+" no fue encontrado en el sistema");
            throw new FileNotFoundException("El archivo "+id+" no fue encontrado en el sistema ");
        }
            ClipData result= new ClipData();
        result.setBytes(bytes);
        return result;
    }
}
