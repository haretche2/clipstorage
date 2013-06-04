package edu.umflix.clipstorage.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class ClipDataLocation implements Serializable{
    private StorageServer servidor;
    private long clipId;

    public long getClipId() {
        return clipId;
    }

    public void setClipId(long clipId) {
        this.clipId = clipId;
    }

    public StorageServer getServidor() {
        return servidor;
    }

    public void setServidor(StorageServer servidor) {
        this.servidor = servidor;
    }
}
