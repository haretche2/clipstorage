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
@Entity
public class ClipDataLocation implements Serializable{
    @Id @GeneratedValue()
    private long id;
    private int servidor;
    private long clipId;

    public ClipDataLocation(){

    }

    public long getClipId() {
        return clipId;
    }

    public void setClipId(long clipId) {
        this.clipId = clipId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getServidor() {
        return servidor;
    }

    public void setServidor(int servidor) {
        this.servidor = servidor;
    }
}
