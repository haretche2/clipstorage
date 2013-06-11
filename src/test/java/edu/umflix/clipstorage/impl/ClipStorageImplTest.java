package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.tools.FtpTools;
import edu.umflix.model.ClipData;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ClipStorageImplTest {



    @Test
    public void testTemp() {
        StorageServer s=new StorageServer();
        s.setAmountOfClipDataStored(3);
        s.setOnline(true);
        s.setAddress("192.168.1.104");
        s.setPassword("telematica2013");
        s.setUsername("telematica");


        long tiempoInicio = System.currentTimeMillis();

                       try{
                           FtpTools.leer(s,(long)76);
                        System.out.println(FtpTools.leer(s,(long)9).length);
                       }catch (IOException e){
                           System.out.println("Excepcion");
                       }











        long totalTiempo = System.currentTimeMillis() - tiempoInicio;
        System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");




        /*ClipData c=new ClipStorageImpl().getClipDataByClipId(20);
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
