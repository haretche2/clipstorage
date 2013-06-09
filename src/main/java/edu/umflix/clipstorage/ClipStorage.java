package edu.umflix.clipstorage;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 1/06/13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
import javax.ejb.Remote;

@Remote
public interface ClipStorage {
    void storeClipData(int clipdata);
    int getClipDataByClipId(long id);
}
