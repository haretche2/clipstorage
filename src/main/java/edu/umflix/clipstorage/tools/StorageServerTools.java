package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.Exceptions.ClipStorageConfiguracionIncompletaException;
import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.StorageServer;
import javassist.bytecode.ExceptionsAttribute;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class StorageServerTools {

    private static Logger log= Logger.getLogger(StorageServerTools.class);

    public static List<StorageServer> getServidoresParaAlmacenarClip(List<StorageServer> disponibles){
        List<StorageServer> result= new ArrayList<StorageServer>();
        for(int i=0;i< Configuration.getIntConfiguration("Replicas");i++){
            if(disponibles.size()>0){
                StorageServer siguiente=getSiguienteServidoresParaAlmacenarClip(disponibles);
                result.add(siguiente);
                disponibles.remove(siguiente);
            }else {
                log.warn("Insuficientes servidores de almacenamiento");
            }
        }
        return result;
    }
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
}
