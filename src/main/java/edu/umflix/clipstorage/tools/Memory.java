package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.config.StorageServersLoader;
import edu.umflix.clipstorage.model.ClipDataLocation;
import edu.umflix.clipstorage.model.StorageServer;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 15:39
 * To change this template use File | Settings | File Templates.
 */
public class Memory {
    private static Logger log= Logger.getLogger(Memory.class);
    private static final List<StorageServer> SERVERS=StorageServersLoader.loadXML();
    private static final List<ClipDataLocation> CLIPDATAS =new ArrayList<ClipDataLocation>();

    static{
        log.debug("Iniciando carga de ubicaciones de ClipDatas en servidores");
        for(StorageServer server : SERVERS){
            try{
                log.debug("Iniciando primera conexion FTP con: "+server.getAddress());
                FTPClient client = FtpTools.getClient(server);
                FTPFile[] files=client.listFiles();
                for(FTPFile file:files){
                    try{
                        ClipDataLocation newClipDataLocation=new ClipDataLocation();
                        newClipDataLocation.setServidor(server);
                        newClipDataLocation.setClipId(Long.parseLong(file.getName()));
                        server.setAmountOfClipDataStored(server.getAmountOfClipDataStored()+1);
                        log.debug("ClipData: Id="+newClipDataLocation.getClipId()+" Servidor="+newClipDataLocation.getServidor().getAddress());
                        CLIPDATAS.add(newClipDataLocation);

                    }catch(NumberFormatException e){
                        log.warn("Hay un archivo con nombre corrupto: "+file.getName()+" en el servidor: "+server.getAddress());
                    }
                }
                server.setAmountOfClipDataStored(files.length);
            }catch (IOException e){
                log.warn("Error al leventar un servidor: "+server.getAddress());
                StorageServerTools.servidorCaido(server);
            }
        }
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

    public static List<StorageServer> getStorageServersForClipDataById(long id){
        List<StorageServer> serversWithClipDatasWithThisId=new ArrayList<StorageServer>();
        for(ClipDataLocation clipDataLocation :CLIPDATAS){
            if(clipDataLocation.getClipId()==id && clipDataLocation.getServidor().isOnline()){
                serversWithClipDatasWithThisId.add(clipDataLocation.getServidor());
            }
        }
        log.debug("Hay "+serversWithClipDatasWithThisId.size()+" servidores para Id="+id);
        return serversWithClipDatasWithThisId;
    }

}
