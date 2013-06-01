package edu.umflix.clipstorage;

import edu.umflix.model.ClipData;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 1/06/13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public interface ClipStorage {
    void storeClipData(ClipData clipdata);
    ClipData getClipDataById(int id);
}
