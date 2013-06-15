package edu.umflix.clipstorage.storage;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.config.ConfigurationItemsEnum;
import edu.umflix.clipstorage.config.StorageServersXMLLoader;
import edu.umflix.clipstorage.model.ClipDataToServerRelation;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.tools.StorageServerCommunicationsHelper;
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
 * Las relaciones se cargan a memoria al iniciar la ejecución mediante consultas a todos los servidores de almacenamiento, por lo que no se pierden ante una caída del sistema.
 */
@Startup
@Singleton
public class StorageMappingManager {
    private static Logger log= Logger.getLogger(StorageMappingManager.class);

    /**
     * Lista en la cual se encuentran los datos de todos los servidores de almacenamiento definidos.
     */
    private static final List<StorageServer> SERVERS= StorageServersXMLLoader.loadXML();
    /**
     * Lista en la cual se definen todos los Clips almacenados y los relaciona con el servidor en que se encuentran.
     */
    private static final List<ClipDataToServerRelation> MAPEOCLIPDATASTOSERVERS =new ArrayList<ClipDataToServerRelation>();
    /**
     * Esta variable indica si ya cargaron las referencias que permiten acceder a los servidores de almacenamiento.
     */
    private static boolean cargueLosServidoresAlInicio =false;

    @PostConstruct
    void atStartup() {
        if(!cargueLosServidoresAlInicio){
            cargueLosServidoresAlInicio = true;
            log.debug("Iniciando carga de ubicaciones de ClipDatas en servidores");
            for(StorageServer server : SERVERS){
                try{
                    StorageServerCommunicationsHelper.conectarServidor(server);
                }catch (IOException e){
                    log.warn("Error al leventar un servidor: "+server.getAddress());
                    StorageServerCommunicationsHelper.recuperar(server);
                }
            }
        }
    }

    /**
     * Si nos encontramos en entornos de testing y no estamos ejecutando en el servidor de aplicaciones, esto inicia la carga de servidores.
     */
    static {
        new StorageMappingManager().atStartup();
    }

    /**
     * Devuelve la lista de servidores de almacenamiento definidos que se encuentran funcionando.
     * @return Lista con los servidores online.
     */
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

    /**
     * Agrega una realación entre un clipdata y un servidor en el mapeo en memoria de los datos.
     * @param clipDataServerRelation La relación a agregar.
     */
    public static void addClipDataServerRelation(ClipDataToServerRelation clipDataServerRelation){
        MAPEOCLIPDATASTOSERVERS.add(clipDataServerRelation);
        StorageServer server= clipDataServerRelation.getServidor();
        server.setAmountOfClipDataStored(server.getAmountOfClipDataStored()+1);
    }

    /**
     * Obtiene todas las relaciones ClipData-Servidor contenidas en el mapeo y que pertenecen a un determinado servidor, las devuelve y las borra.
     * @param storageServer Servidor del que obtener las relaciones.
     * @return La lista de relaciones ClipData-Servidor con datos en el servidor.
     */
    public static List<ClipDataToServerRelation> getAndRemoveAllClipDataServerRelationsOfServer(StorageServer storageServer){
        List<ClipDataToServerRelation> clipDataLocationsOfServer=new ArrayList<ClipDataToServerRelation>();
        for(ClipDataToServerRelation clipDataServerRelation : MAPEOCLIPDATASTOSERVERS){
            if(clipDataServerRelation.getServidor().equals(storageServer)){
                clipDataLocationsOfServer.add(clipDataServerRelation);
            }
        }
        MAPEOCLIPDATASTOSERVERS.removeAll(clipDataLocationsOfServer);
        return clipDataLocationsOfServer;
    }

    /**
     * Busca en el mapeo de ClipDatas - Servers todas las instancias de un determinado ClipData.
     * @param clipId Id del clip a buscar.
     * @return La cantidad de instancias encontradas en el mapa.
     */
    public static int getCantidadDeReplicasExistentesDeUnClip(long clipId){
        int cantidad=0;
        for(ClipDataToServerRelation cadaClip : MAPEOCLIPDATASTOSERVERS){
            if(cadaClip.getClipId()==clipId) cantidad++;
        }
        return cantidad;
    }

    /**
     * Indica si la cantidad de instancias ClipDatas - Servers en el mapeo es suficiente de acuerdo a la cantidad de replicas establecidas en el archivo config.properties
     * @param clipId Id del clip a buscar.
     * @return True si la cantidad de replicas existentes es mayor o igual a las requeridas.
     */
    public static boolean tengoLaCantidadDeReplicasNecesariasParaElClip(long clipId){
        return (Configuration.getIntConfiguration(ConfigurationItemsEnum.REPLICAS)<=getCantidadDeReplicasExistentesDeUnClip(clipId));
    }

    /**
     * Devuelve una lista con los servidores que están disponibles y que tienen copias del clip según el mapeo.
     * @param id Id del clip a buscar.
     * @return Una lista con los servidores de los que se puede leer el clip.
     */
    public static List<StorageServer> getStorageServersConClipDataById(long id){
        List<StorageServer> serversWithClipDatasWithThisId=new ArrayList<StorageServer>();
        for(ClipDataToServerRelation cadaClipDataServerRelation : MAPEOCLIPDATASTOSERVERS){
            if(cadaClipDataServerRelation.getClipId()==id && cadaClipDataServerRelation.getServidor().isOnline()){
                serversWithClipDatasWithThisId.add(cadaClipDataServerRelation.getServidor());
            }
        }
        log.debug("Hay "+serversWithClipDatasWithThisId.size()+" servidores para el clip con id: "+id);
        return serversWithClipDatasWithThisId;
    }


    /**
     * Devuelve una lista con los servidores que están disponibles y que NO tienen copias del clip según el mapeo.
     * @param id Id del clip que no deben tener los servidores.
     * @return Una lista con los servidores en los que se puede escribir el clip.
     */
    public static List<StorageServer> getStorageServersSinClipDataById(long id){
        List<StorageServer> serversWithoutClipDatasWithThisId=getOnlineServers();
        for(ClipDataToServerRelation cadaClipDataServerRelation : MAPEOCLIPDATASTOSERVERS){
            if(cadaClipDataServerRelation.getClipId()==id && serversWithoutClipDatasWithThisId.contains(cadaClipDataServerRelation.getServidor())){
                serversWithoutClipDatasWithThisId.remove(cadaClipDataServerRelation.getServidor());
            }
        }
        log.debug("Hay "+serversWithoutClipDatasWithThisId.size()+" servidores sin el clip con id: "+id);
        return serversWithoutClipDatasWithThisId;
    }
}