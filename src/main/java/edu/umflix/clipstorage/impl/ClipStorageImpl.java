package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.ClipStorage;
import edu.umflix.clipstorage.model.StorageServer;
import edu.umflix.clipstorage.persistence.StorageServerDao;
import edu.umflix.clipstorage.tools.StorageServerTools;
import edu.umflix.model.ClipData;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 1/06/13
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "clipstorage")
public class ClipStorageImpl implements ClipStorage{

    private static Logger log= Logger.getLogger(ClipStorageImpl.class);

    @PersistenceContext(unitName="clipstorage")
    EntityManager entityManager;

    @Override
    public void storeClipData(ClipData clipdata) {
        List<StorageServer> onlineServers= StorageServerTools.getServidoresParaAlmacenarClip(StorageServerDao.getOnlineServers());


        for(int i=0;i<onlineServers.size();i++){
            System.out.println(onlineServers.get(i).getId());
        }


        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ClipData getClipDataById(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
