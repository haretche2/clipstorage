package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.impl.ClipStorageImpl;
import edu.umflix.clipstorage.model.ClipDataLocation;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 10/06/13
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public class RecuperadorDeServidor{
    private static Logger log= Logger.getLogger(RecuperadorDeServidor.class);

    public static void recuperar(StorageServer servidorCaido)
    {
        servidorCaido.setOnline(false);
        List<ClipDataLocation> listaDeClipsARecuperar= MemoryManager.getAndRemoveAllClipDataLocationOfServer(servidorCaido);
        for(ClipDataLocation unClipARecuperar:listaDeClipsARecuperar){
            log.debug("Iniciando recuperacion del clip: "+unClipARecuperar.getClipId());
            if(!MemoryManager.tengoLaCantidadDeReplicasNecesariasParaElClip(unClipARecuperar.getClipId())){
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" se creara una copia");
                ClipData clipData=ClipStorageImpl.getInstancia().getClipDataByClipId(unClipARecuperar.getClipId());
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" se leyó el clip de otro servidor");
                List<StorageServer> servidoresDisponiblesSinEsteClip= MemoryManager.getStorageServersSinClipDataById(unClipARecuperar.getClipId());
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" hay: " +servidoresDisponiblesSinEsteClip.size()+" servidores disponibles para guardar la copia leída");
                StorageServerTools.guardarClipEnLosAlgunosDeLosServidores(servidoresDisponiblesSinEsteClip, 1,clipData);
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" la copia ha sido guardada");
            }
        }
        servidorCaido.setAmountOfClipDataStored(0);
        while(!servidorCaido.isOnline()){
            try {
                Thread.sleep(Configuration.getIntConfiguration("TiempoEntreIntentosLevantarServidorCaido"));
                StorageServerTools.conectarServidor(servidorCaido);
            } catch (InterruptedException e) {
                log.warn(e.getStackTrace());
                log.warn("Ha ocurrido una InterruptedException cuando estaba bloqueado el proceso para recuperar el servidor: "+servidorCaido.getAddress()+ " Se continuará la ejecución.");
            } catch (IOException e) {
                log.warn(e.getStackTrace());
                log.warn("El servidor: "+servidorCaido.getAddress()+" continúa lanzando IOException.");
            }
        }
    }
}
