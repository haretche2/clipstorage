package edu.umflix.clipstorage.Exceptions;

/**
 * La excepción se lanza cuando hay un problema de configuración en el sistema. En general habrá que revisar el archivo config.properties y el StorageServers.xml
 */
public class ClipStorageConfiguracionIncompletaException extends RuntimeException {
    public ClipStorageConfiguracionIncompletaException(String msg) {
        super(msg);
    }
}
