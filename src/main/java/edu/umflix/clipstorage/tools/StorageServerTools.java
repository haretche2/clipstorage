package edu.umflix.clipstorage.tools;

import edu.umflix.clipstorage.Exceptions.ClipStorageConfiguracionIncompletaException;
import edu.umflix.clipstorage.model.ClipDataLocation;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    public static void servidorCaido(StorageServer servidorCaido){
        servidorCaido.setOnline(false);


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

    public static void guardar(StorageServer servidorEnQueAlmacenar, ClipData datos) throws IOException {
        FTPClient clienteFtp = FtpTools.getClient(servidorEnQueAlmacenar);
        byte[] bytes= new byte[datos.getBytes().length];
        for(int i=0;i<bytes.length;i++){
            bytes[i]=datos.getBytes()[i];
        }
        clienteFtp.storeFile((datos.getClip().getId()).toString(), new ByteArrayInputStream(bytes));
        ClipDataLocation clipDataLocation=new ClipDataLocation();
        clipDataLocation.setClipId(datos.getClip().getId());
        clipDataLocation.setServidor(servidorEnQueAlmacenar);
        Memory.addClipDataLocation(clipDataLocation);
    }

    public static Byte[] leer(StorageServer servidorDelQueLeer, long fileName) throws IOException {
        log.debug("Iniciando lectura del archivo: "+Long.toString(fileName)+" del servidor: "+servidorDelQueLeer.getAddress());
        FTPClient clienteFtp = FtpTools.getClient(servidorDelQueLeer);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        clienteFtp.retrieveFile(Long.toString(fileName), outputStream);

        byte[] bytes=outputStream.toByteArray();
        Byte[] convertedBytes= new Byte[bytes.length];
        for(int i=0;i<bytes.length;i++){
            convertedBytes[i]=bytes[i];
        }
        return convertedBytes;
    }

    public static StorageServer getRandomFromList(List<StorageServer> list)    {
        int index = randomGenerator.nextInt(list.size());
        StorageServer item = list.get(index);
        return item;
    }
}
