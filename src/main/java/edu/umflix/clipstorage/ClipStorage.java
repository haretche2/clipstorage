package edu.umflix.clipstorage;

import edu.umflix.model.ClipData;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 1/06/13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */

@Local
public interface ClipStorage {
    void storeClipData(ClipData clipdata);
    ClipData getClipDataByClipId(long id);
}