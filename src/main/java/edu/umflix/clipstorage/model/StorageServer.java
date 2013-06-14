package edu.umflix.clipstorage.model;

/**
 * Esta clase representa un servidor de almacenamiento al que nos podemos conectar por FTP.
 */
public class StorageServer{
    /**
     * Dirección ip del servidor para conectarse por FTP.
     */
    private String address;
    /**
     * Usuario para conectarse al servidor FTP.
     */
    private String username;
    /**
     * Clave para conectarse con el usuario especificado al servidor FTP.
     */
    private String password;
    /**
     * Cantidad de clips almacenados en el servidor, no se considera la cantidad de bytes de cada clip.
     */
    private long amountOfClipDataStored;
    /**
     * Indica que hasta el momento no han habido problemas para conectarse por FTP con el servidor
     */
    private boolean isOnline;

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
