package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.storage.StorageMappingManager;
import edu.umflix.clipstorage.tools.StorageServerCommunicationsHelper;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;


public class ClipStorageImplTest {
    private  Random randomGenerator=new Random();

    private Byte[] crearByteArrayRandom(){
        int largo=randomGenerator.nextInt(1000000);
        byte[] resultadobytes= new byte[largo];
        Byte[] resultadoBytes= new Byte[largo];
        randomGenerator.nextBytes(resultadobytes);
        for(int i=0;i<largo;i++){
            resultadoBytes[i]=resultadobytes[i];
        }
        return resultadoBytes;
    }
    private ClipData crearClipDataRandom(){
        Clip clip= new Clip();
        clip.setId((long)randomGenerator.nextInt(999999));
        ClipData clipdata=new ClipData();
        clipdata.setBytes(crearByteArrayRandom());
        clipdata.setClip(clip);
        return clipdata;
    }

    /**
     * Test basado en crear 3 clips aleatorios, guardar los 3 por separado y luego leerlos y verificar que lo que se lee se corresponde con lo previamente guardado.
     */
    @Test
    public void testGuardarYLeerTresClipDatas() {
        ClipData clip1 = crearClipDataRandom();
        ClipData clip2 = crearClipDataRandom();
        ClipData clip3 = crearClipDataRandom();

        ClipStorageImpl.getInstancia().storeClipData(clip1);
        ClipStorageImpl.getInstancia().storeClipData(clip2);
        ClipStorageImpl.getInstancia().storeClipData(clip3);

        assertEquals(ClipStorageImpl.getInstancia().getClipDataByClipId(clip1.getClip().getId()).getBytes(),clip1.getBytes());
        assertEquals(ClipStorageImpl.getInstancia().getClipDataByClipId(clip2.getClip().getId()).getBytes(),clip2.getBytes());
        assertEquals(ClipStorageImpl.getInstancia().getClipDataByClipId(clip3.getClip().getId()).getBytes(),clip3.getBytes());
    }

    /**
     * test basado en almacenar un clip en dos servidores, luego simular la caida de uno de ellos y verificar que se puede continuar leyendo el archivo.
     */
    @Test
    public void testRecuperarServidor() {
        ClipData clip1 = crearClipDataRandom();
        List<StorageServer> servidoresDisponibles= StorageMappingManager.getOnlineServers();
        List<StorageServer> servidores0y1=new ArrayList<StorageServer>();
        servidores0y1.add(servidoresDisponibles.get(0));
        servidores0y1.add(servidoresDisponibles.get(1));

        StorageServerCommunicationsHelper.guardarClipEnLosAlgunosDeLosServidores(servidores0y1, 2, clip1);
        StorageServerCommunicationsHelper.recuperar(servidoresDisponibles.get(0));

        assertEquals(ClipStorageImpl.getInstancia().getClipDataByClipId(clip1.getClip().getId()).getBytes(),clip1.getBytes());
    }

    /**
     * test basado en almacenar un clip en dos servidores, luego simular la caida de uno de ellos, acceder varias veces al clip para asegurarnos de que el balanceador de carga detecte la caida, luego tirar el otro servidor y leer el dato.
     * Si funciona Ok, significa que el recuperador de servidores caidos funcionó correctamente.
     */
    @Test
    public void testRecuperarDosServidoresEnLosQueSeEncontrabaUnClip() {
        ClipData clip1 = crearClipDataRandom();
        List<StorageServer> servidoresDisponibles= StorageMappingManager.getOnlineServers();
        List<StorageServer> servidores0y1=new ArrayList<StorageServer>();
        servidores0y1.add(servidoresDisponibles.get(0));
        servidores0y1.add(servidoresDisponibles.get(1));

        StorageServerCommunicationsHelper.guardarClipEnLosAlgunosDeLosServidores(servidores0y1, 2, clip1);
        StorageServerCommunicationsHelper.recuperar(servidoresDisponibles.get(0));

        for(int i=0;i<1000;i++){
            assertEquals(ClipStorageImpl.getInstancia().getClipDataByClipId(clip1.getClip().getId()).getBytes(),clip1.getBytes());
        }
        StorageServerCommunicationsHelper.recuperar(servidoresDisponibles.get(1));
        assertEquals(ClipStorageImpl.getInstancia().getClipDataByClipId(clip1.getClip().getId()).getBytes(),clip1.getBytes());
    }
}
