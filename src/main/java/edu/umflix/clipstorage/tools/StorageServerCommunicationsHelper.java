package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.config.ConfigurationItemsEnum;
import edu.umflix.clipstorage.impl.ClipStorageImpl;
import edu.umflix.clipstorage.model.ClipDataToServerRelation;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.storage.Storage;
import edu.umflix.clipstorage.storage.StorageMappingManager;
import edu.umflix.model.ClipData;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Clase destinada a simplificar acciones de comunicación con los servidores de almacenamiento.
 */
public class StorageServerCommunicationsHelper {
    private static Logger log= Logger.getLogger(StorageServerCommunicationsHelper.class);

    /**
     * Persiste un clip en tantos servidores como el valor de replica. Cuando la cantidad de servidores provistos es mayor a la cantidad de replicas a realizar, se seleccionan los que tienen menos cantidad de archivos.
     * @param servidores Servidores en los que se puede almacenar el clip.
     * @param replicas Cantidad de replicas a conservar del clip (en cuantos servidores redundantes).
     * @param clipdata El clip a guardar
     * @return
     */
    public static boolean guardarClipEnLosAlgunosDeLosServidores(List<StorageServer> servidores, int replicas,ClipData clipdata){
        boolean pudeGuardarAlMenosUnaCopia=false;
        for(int i=0;i< replicas;i++){
            if(servidores.size()>0){
               StorageServer mejorServidorParaAlmacenarClip=StorageServerLists.getSiguienteServidorParaAlmacenarClip(servidores);
               servidores.remove(mejorServidorParaAlmacenarClip);
                try{
                    Storage.getInstance().guardar(mejorServidorParaAlmacenarClip, clipdata);
                    ClipDataToServerRelation clipDataServerRelation =new ClipDataToServerRelation();
                    clipDataServerRelation.setClipId(clipdata.getClip().getId());
                    clipDataServerRelation.setServidor(mejorServidorParaAlmacenarClip);
                    StorageMappingManager.addClipDataServerRelation(clipDataServerRelation);
                    pudeGuardarAlMenosUnaCopia=true;
                }catch (IOException e){
                    i--;
                    StorageServerCommunicationsHelper.recuperar(mejorServidorParaAlmacenarClip);
                }
            }else {
                log.warn("guardarClipEnLosAlgunosDeLosServidores recibió una lista vacía de servidores");
                break;
            }
        }
        return pudeGuardarAlMenosUnaCopia;
    }

    /**
     * Intenta conectarse con el servidor de almacenamiento provisto, en caso de que eso funcione correctamente, se crea el mapeo en memoria con los archivos contenidos en el. Si el servidor a conectar tiene copios de un clip en exeso (hay mas copias que las requeridas), ésta se borra para liberar espacio.
     * @param server Servidor al que conectarse.
     * @throws IOException En caso de que la conexión falle.
     */
    public static void conectarServidor(StorageServer server) throws IOException {
        log.debug("Iniciando conexion con: "+server.getAddress());
        String[] fileNames= Storage.getInstance().listarArchivosEnServidor(server);
        for(String unFileName:fileNames){
            try{
                ClipDataToServerRelation newClipDataServerRelation =new ClipDataToServerRelation();
                newClipDataServerRelation.setServidor(server);
                newClipDataServerRelation.setClipId(Long.parseLong(unFileName));
                if(StorageMappingManager.tengoLaCantidadDeReplicasNecesariasParaElClip(newClipDataServerRelation.getClipId())){
                    Storage.getInstance().borrar(server, newClipDataServerRelation.getClipId());
                }else{
                    StorageMappingManager.addClipDataServerRelation(newClipDataServerRelation);
                    log.debug("ClipData: Id="+ newClipDataServerRelation.getClipId()+" Servidor="+ newClipDataServerRelation.getServidor().getAddress());
                }

            }catch(NumberFormatException e){
                log.warn("Hay un archivo con nombre corrupto: "+unFileName+" en el servidor: "+server.getAddress());
            }
        }
        server.setAmountOfClipDataStored(fileNames.length);
        server.setOnline(true);
    }

    /**
     * Una vez que un servidor se ha caido, se inicia un proceso de recuperación del mismo, en el que se recuperan las copias redundantes perdidas y se procura periódicamente volver a establecer la conexión.
     * @param servidorCaido El servidor que se ha caido.
     */
    public static void recuperar(StorageServer servidorCaido)
    {
        servidorCaido.setOnline(false);
        List<ClipDataToServerRelation> listaDeClipsARecuperar= StorageMappingManager.getAndRemoveAllClipDataServerRelationsOfServer(servidorCaido);
        for(ClipDataToServerRelation unClipARecuperar:listaDeClipsARecuperar){
            log.debug("Iniciando recuperacion del clip: "+unClipARecuperar.getClipId());
            if(!StorageMappingManager.tengoLaCantidadDeReplicasNecesariasParaElClip(unClipARecuperar.getClipId())){
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" se creara una copia");
                ClipData clipData= ClipStorageImpl.getInstancia().getClipDataByClipId(unClipARecuperar.getClipId());
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" se leyó el clip de otro servidor");
                List<StorageServer> servidoresDisponiblesSinEsteClip= StorageMappingManager.getStorageServersSinClipDataById(unClipARecuperar.getClipId());
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" hay: " +servidoresDisponiblesSinEsteClip.size()+" servidores disponibles para guardar la copia leída");
                StorageServerCommunicationsHelper.guardarClipEnLosAlgunosDeLosServidores(servidoresDisponiblesSinEsteClip, 1, clipData);
                log.debug("Recuperacion del clip: "+unClipARecuperar.getClipId()+" la copia ha sido guardada");
            }
        }
        servidorCaido.setAmountOfClipDataStored(0);
        while(!servidorCaido.isOnline()){
            try {
                Thread.sleep(Configuration.getIntConfiguration(ConfigurationItemsEnum.TIEMPOENTREINTENTOSLEVANTARSERVIDORCAIDO));
                StorageServerCommunicationsHelper.conectarServidor(servidorCaido);
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
