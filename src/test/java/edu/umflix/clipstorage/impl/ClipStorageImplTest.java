package edu.umflix.clipstorage.impl;

import edu.umflix.model.ClipData;
import org.junit.Test;

import java.io.FileNotFoundException;


public class ClipStorageImplTest {



    @Test
    public void testTemp() {
        if(1==1) return;
       try {
            ClipData c=new ClipStorageImpl().getClipDataByClipId(20);
            System.out.print("'");
            for (Byte b:c.getBytes()){
                System.out.print((char)(byte)b);
            }
            System.out.print("'");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return;

        //new ClipStorageImpl().storeClipData(new ClipData());
                          //System.out.println(Configuration.getConfiguracion("Replicas"));
       /* ClipStorageImpl bean = new ClipStorageImpl();
        bean.entityManager = Mockito.mock(EntityManager.class);
        loadTestStorageServers(bean.entityManager);

        StorageServer s=new StorageServer();
        s.setAddress("casa");
        s.setAmountOfClipDataStored(2);
        s.setOnline(true);
        bean.entityManager.persist(s);

        s=new StorageServer();
        s.setAddress("casa");
        s.setAmountOfClipDataStored(2);
        s.setOnline(true);

        System.out.println(bean.entityManager.createQuery("from StorageServer"));

        Mockito.verify(bean.entityManager).persist(s);

         */



        //List<StorageServer> ss = bean.entityManager.createQuery("from StorageServer").getResultList();

        //for(StorageServer s :ss){
         //   System.out.println(s.getId());
        //}



    }

}
