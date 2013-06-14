package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.config.StorageServersXMLLoader;
import edu.umflix.clipstorage.model.ClipDataLocation;
import edu.umflix.clipstorage.model.StorageServer;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase conserva las relaciones que permiten determinar en qué servidores se encuentra un ClipData.
 * Esas relaciones se almacenan en memoria por medio de una lista. Esta pensada para ser fácilmente adaptable para conservar las relaciones en una base de datos en caso de ser necesario.
 * Las relaciones se cargan a memoria al registrarse los servidores de almacenamiento, por lo que no se pierden ante una caída del sistema.
 */
@Startup
@Singleton
public class MemoryManager {
    private static Logger log= Logger.getLogger(MemoryManager.class);
    private static final List<StorageServer> SERVERS= StorageServersXMLLoader.loadXML();
    private static final List<ClipDataLocation> CLIPDATAS =new ArrayList<ClipDataLocation>();
    private static boolean ejecuteAtStartup=false;

    @PostConstruct
    static void atStartup() {
        if(!ejecuteAtStartup){
            ejecuteAtStartup=true;
            log.debug("Iniciando carga de ubicaciones de ClipDatas en servidores");
            for(StorageServer server : SERVERS){
                try{
                    StorageServerTools.conectarServidor(server);
                }catch (IOException e){
                    log.warn("Error al leventar un servidor: "+server.getAddress());
                    RecuperadorDeServidor.recuperar(server);
                }
            }
        }
    }
    static {
        atStartup();
    }

    public static List<StorageServer> getOnlineServers(){
        List<StorageServer> onlineServers=new ArrayList<StorageServer>();
        for(StorageServer storageServer :SERVERS){
            if(storageServer.isOnline()){
                onlineServers.add(storageServer);
            }
        }
        log.debug("Hay "+onlineServers.size()+" servidores online");
        return onlineServers;
    }

    public static void addClipDataLocation(ClipDataLocation clipDataLocation){
        CLIPDATAS.add(clipDataLocation);
        StorageServer server=clipDataLocation.getServidor();
        server.setAmountOfClipDataStored(server.getAmountOfClipDataStored()+1);
    }
    public static int getCantidadDeReplicasExistentesDeUnClip(long clipId){
        int cantidad=0;
        for(ClipDataLocation cadaClip : CLIPDATAS){
            if(cadaClip.getClipId()==clipId) cantidad++;
        }
        return cantidad;
    }

    public static boolean tengoLaCantidadDeReplicasNecesariasParaElClip(long clipId){
        return (Configuration.getIntConfiguration("Replicas")<=getCantidadDeReplicasExistentesDeUnClip(clipId));
    }

    public static List<StorageServer> getStorageServersConClipDataById(long id){
        List<StorageServer> serversWithClipDatasWithThisId=new ArrayList<StorageServer>();
        for(ClipDataLocation clipDataLocation :CLIPDATAS){
            if(clipDataLocation.getClipId()==id && clipDataLocation.getServidor().isOnline()){
                serversWithClipDatasWithThisId.add(clipDataLocation.getServidor());
            }
        }
        log.debug("Hay "+serversWithClipDatasWithThisId.size()+" servidores para Id="+id);
        return serversWithClipDatasWithThisId;
    }

    public static List<StorageServer> getStorageServersSinClipDataById(long id){
        List<StorageServer> serversSinClipDatasWithThisId=getOnlineServers();
        for(ClipDataLocation clipDataLocation :CLIPDATAS){
            if(clipDataLocation.getClipId()==id && serversSinClipDatasWithThisId.contains(clipDataLocation.getServidor())){
                serversSinClipDatasWithThisId.remove(clipDataLocation.getServidor());
            }
        }
        log.debug("Hay "+serversSinClipDatasWithThisId.size()+" servidores sin el Id="+id);
        return serversSinClipDatasWithThisId;
    }

    public static List<ClipDataLocation> getAndRemoveAllClipDataLocationOfServer(StorageServer storageServer){
        List<ClipDataLocation> clipDataLocationsOfServer=new ArrayList<ClipDataLocation>();
        for(ClipDataLocation clipDataLocation :CLIPDATAS){
            if(clipDataLocation.getServidor().equals(storageServer)){
                clipDataLocationsOfServer.add(clipDataLocation);
            }
        }
        CLIPDATAS.removeAll(clipDataLocationsOfServer);
        return clipDataLocationsOfServer;
    }

}
