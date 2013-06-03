package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.config.Configuration;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.model.ClipData;
import org.junit.Test;

import javax.persistence.EntityManager;


public class ClipStorageImplTest {

    private void loadTestStorageServers(EntityManager entityManager){
        StorageServer s=new StorageServer();
        s.setAddress("casa");
        s.setAmountOfClipDataStored(2);
        s.setOnline(true);
        entityManager.persist(s);
    }

    @Test
    public void testTemp() {
        new ClipStorageImpl().storeClipData(new ClipData());
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
