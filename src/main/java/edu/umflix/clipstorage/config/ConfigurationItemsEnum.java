package edu.umflix.clipstorage.config;

/**
 * En éste enumerado se definen los items de configuración.
 */
public enum ConfigurationItemsEnum {
    REPLICAS, // Indíca cuantas copias de los archivos se guardarán
    TIMEOUTCREARCLIENTEFTP, // Indica el tiempo de timeout en milisegundos al crear un cliente FTP para acceder a los servidores de almacenamiento
    TIEMPOENTREINTENTOSLEVANTARSERVIDORCAIDO, // Indica el tiempo que se espera entre intentos de conectarse a un servidor de almacenamiento que no responde
    TIPODEALMACENAMIENTODECLIPS ,
}
