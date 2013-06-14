package edu.umflix.clipstorage.impl;

import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import org.junit.Test;

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

        ClipData clip1 = crearClipDataRandom();


        ClipStorageImpl.getInstancia().storeClipData(clip1);

        ClipData c=ClipStorageImpl.getInstancia().getClipDataByClipId(clip1.getClip().getId());
        System.out.println("Hola");

                   /*
        long tiempoInicio = System.currentTimeMillis();

                       try{
                           FTPStorage.leer(s,(long)76);
                        System.out.println(FTPStorage.leer(s,(long)9).length);
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
