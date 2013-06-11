package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.ClipDataLocation;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.Clip;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 10/06/13
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public class ProcesoRecuperadorDeServidor extends Thread{
    private StorageServer servidorCaido=null;
    public ProcesoRecuperadorDeServidor(StorageServer storageServer){
        super();
        servidorCaido=storageServer;
    }

    public void run()
    {
       List<ClipDataLocation> clipDatasToRestore=Memory.getAndRemoveAllClipDataLocationOfServer(servidorCaido);
            List<StorageServer> disponibles= Memory.getOnlineServers();
            Clip c =new Clip();

          /*

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
        */
    }
}
