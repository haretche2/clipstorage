package edu.umflix.clipstorage.persistence;

import edu.umflix.clipstorage.model.StorageServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 15:39
 * To change this template use File | Settings | File Templates.
 */
public class StorageServerDao {
    public static List<StorageServer> getOnlineServers(){
        List<StorageServer> result = new ArrayList<StorageServer>();

        StorageServer s= new StorageServer();
        s.setAddress("192.168.1.108");
        s.setOnline(true);
        s.setAmountOfClipDataStored(120);
        s.setId(0);
        s.setUsername("telematica");
        s.setPassword("telematica2013");
        result.add(s);

        s= new StorageServer();
        s.setAddress("192.168.1.130");
        s.setOnline(true);
        s.setAmountOfClipDataStored(100);
        s.setId(1);
        s.setUsername("telematica");
        s.setPassword("telematica2013");
        result.add(s);

        s= new StorageServer();
        s.setAddress("192.168.1.131");
        s.setOnline(true);
        s.setAmountOfClipDataStored(104);
        s.setId(2);
        s.setUsername("telematica");
        s.setPassword("telematica2013");
        result.add(s);

        return result;


    }
}
