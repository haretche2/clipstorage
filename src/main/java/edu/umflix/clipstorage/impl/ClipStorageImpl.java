package edu.umflix.clipstorage.impl;

import edu.umflix.clipstorage.ClipStorage;

import javax.ejb.Stateless;


/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 1/06/13
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "clipstorage")
public class ClipStorageImpl implements ClipStorage{

    @Override
    public void storeClipData(int clipdata) {

    }

    @Override
    public int getClipDataByClipId(long id) {

        return 3;
    }
}
