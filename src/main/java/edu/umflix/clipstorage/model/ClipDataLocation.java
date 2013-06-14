package edu.umflix.clipstorage.model;

/**
 * Indica que un ClipData se encuentra en un determinado StorageSerever.
 * Si un mismo ClipData se encuentra replicado en varios StorageServers, se dispondrá de varias instancias de esta clase con el mismo clipId.
 */
public class ClipDataLocation{
    /**
     * Servidor en que se encuentra el ClipData
     */
    private StorageServer servidor;
    /**
     * clipId del ClipData.
     */
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
