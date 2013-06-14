package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.tools.FtpTools;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;


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

    @Test
    public void testTemp() {

        //ClipData clip1 = crearClipDataRandom();
    //    ClipData c=ClipStorageImpl.getInstancia().getClipDataByClipId(878314);
//         Byte[] b=c.getBytes();
                   /*
        long tiempoInicio = System.currentTimeMillis();

                       try{
                           FtpTools.leer(s,(long)76);
                        System.out.println(FtpTools.leer(s,(long)9).length);
                       }catch (IOException e){
                           System.out.println("Excepcion");
                       }











        long totalTiempo = System.currentTimeMillis() - tiempoInicio;
        System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");




        ClipData c=new ClipStorageImpl().getClipDataByClipId(20);
        System.out.print("'");
        for (Byte b:c.getBytes()){
            System.out.print((char)(byte)b);
        }
        System.out.print("'");

        return;
          */
        //List<StorageServer> ss = bean.entityManager.createQuery("from StorageServer").getResultList();

        //for(StorageServer s :ss){
         //   System.out.println(s.getId());
        //}



    }

}
