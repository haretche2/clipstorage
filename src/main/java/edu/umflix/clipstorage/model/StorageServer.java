package edu.umflix.clipstorage.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 2/06/13
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public class StorageServer implements Serializable {
    private String address;
    private String username;
    private String password;
    private long amountOfClipDataStored;
    private boolean isOnline;

    public StorageServer(){
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getAmountOfClipDataStored() {
        return amountOfClipDataStored;
    }

    public void setAmountOfClipDataStored(long amountOfClipDataStored) {
        this.amountOfClipDataStored = amountOfClipDataStored;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

}
