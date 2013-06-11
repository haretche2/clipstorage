package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.Exceptions.ClipStorageConfiguracionIncompletaException;
import edu.umflix.clipstorage.model.StorageServer;
import org.apache.log4j.Logger;

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

    public static void servidorCaido(StorageServer servidorCaido){
        servidorCaido.setOnline(false);


        new ProcesoRecuperadorDeServidor(servidorCaido).start();
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



    public static StorageServer getRandomFromList(List<StorageServer> list)    {
        int index = randomGenerator.nextInt(list.size());
        StorageServer item = list.get(index);
        return item;
    }
}
