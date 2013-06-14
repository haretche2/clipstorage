package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.Exceptions.ClipStorageConfiguracionIncompletaException;
import edu.umflix.clipstorage.model.ClipDataLocation;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.storage.FTPStorage;
import edu.umflix.clipstorage.storage.Storage;
import edu.umflix.model.ClipData;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class StorageServerTools {
    private static Logger log= Logger.getLogger(StorageServerTools.class);
    private static Random randomGenerator=new Random();

    public static StorageServer getSiguienteServidoresParaAlmacenarClip(List<StorageServer> disponibles){
        StorageServer elDeMenorEspacioOcupado=null;
        for(StorageServer actual:disponibles){
            if(elDeMenorEspacioOcupado==null || actual.getAmountOfClipDataStored()<elDeMenorEspacioOcupado.getAmountOfClipDataStored()){
                elDeMenorEspacioOcupado=actual;
            }
        }
        if(elDeMenorEspacioOcupado!=null){
            return elDeMenorEspacioOcupado;
        }else{
            throw new ClipStorageConfiguracionIncompletaException("No hay mas servidores online disponibles");
        }

    }
    public static boolean guardarClipEnLosAlgunosDeLosServidores(List<StorageServer> servidores, int replicas,ClipData clipdata){
        boolean pudeGuardarAlMenosUnaCopia=false;
        for(int i=0;i< replicas;i++){
            if(servidores.size()>0){
               StorageServer mejorServidorParaAlmacenarClip=StorageServerTools.getSiguienteServidoresParaAlmacenarClip(servidores);
               servidores.remove(mejorServidorParaAlmacenarClip);
                try{
                    Storage.getInstance().guardar(mejorServidorParaAlmacenarClip, clipdata);
                    ClipDataLocation clipDataLocation=new ClipDataLocation();
                    clipDataLocation.setClipId(clipdata.getClip().getId());
                    clipDataLocation.setServidor(mejorServidorParaAlmacenarClip);
                    MemoryManager.addClipDataLocation(clipDataLocation);
                    pudeGuardarAlMenosUnaCopia=true;
                }catch (IOException e){
                    i--;
                    RecuperadorDeServidor.recuperar(mejorServidorParaAlmacenarClip);
                }
            }else {
                log.warn("guardarClipEnLosAlgunosDeLosServidores recibió una lista vacía de servidores");
                break;
            }
        }
        return pudeGuardarAlMenosUnaCopia;
    }

    public static StorageServer getRandomFromList(List<StorageServer> list)    {
        int index = randomGenerator.nextInt(list.size());
        StorageServer item = list.get(index);
        return item;
    }

    public static void conectarServidor(StorageServer server) throws IOException {
        log.debug("Iniciando conexion con: "+server.getAddress());
        String[] fileNames= Storage.getInstance().listarArchivosEnServidor(server);
        for(String unFileName:fileNames){
            try{
                ClipDataLocation newClipDataLocation=new ClipDataLocation();
                newClipDataLocation.setServidor(server);
                newClipDataLocation.setClipId(Long.parseLong(unFileName));
                if(MemoryManager.tengoLaCantidadDeReplicasNecesariasParaElClip(newClipDataLocation.getClipId())){
                    Storage.getInstance().borrar(server, newClipDataLocation.getClipId());
                }else{
                    MemoryManager.addClipDataLocation(newClipDataLocation);
                    log.debug("ClipData: Id="+newClipDataLocation.getClipId()+" Servidor="+newClipDataLocation.getServidor().getAddress());
                }

            }catch(NumberFormatException e){
                log.warn("Hay un archivo con nombre corrupto: "+unFileName+" en el servidor: "+server.getAddress());
            }
        }
        server.setAmountOfClipDataStored(fileNames.length);
        server.setOnline(true);
    }

}
