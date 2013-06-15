package edu.umflix.clipstorage.config;

/**
 * En éste enumerado se definen los items de configuración.
 */
public enum ConfigurationItemsEnum {
    REPLICAS, // Indíca cuantas copias de los archivos se guardarán.
    TIMEOUTCREARCLIENTEFTP, // Indica el tiempo de timeout en milisegundos al crear un cliente FTP para acceder a los servidores de almacenamiento.
    TIEMPOENTREINTENTOSLEVANTARSERVIDORCAIDO, // Indica el tiempo que se espera entre intentos de conectarse a un servidor de almacenamiento que no responde.
    TIPODEALMACENAMIENTODECLIPS , // Indica si se utilizarán servidores FTP para almacenar los clips o servidores simulados en forma local y basados en ram. Esta última opción debería ser usada únicamente con propósitos de testing, ya que los datos se borrarán al finaliar la ejecución
}
