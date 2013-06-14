package edu.umflix.clipstorage.config;

import edu.umflix.clipstorage.Exceptions.ClipStorageConfiguracionIncompletaException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Esta clase permite leer los valores de configuración del módulo. Se utiliza un archivo 'config.properties'.
 * Si el mismo no se encuentra se utilizan valores predefinidos.
 */
public class Configuration {
    private static Logger log= Logger.getLogger(StorageServersXMLLoader.class);
    private static final String UBICACION= "/codnfig.properties";

    /**
     * Lee un item de configuración.
     * @param key La clave del item a leer.
     * @return El valor configurado para la clave brindada. Si el archivo de configuración no es encontrado, se utilizan valores predefinidos.
     */
    public static String getConfiguracion(ConfigurationItemsEnum key){
        Properties defaultProps = new Properties();

        InputStream in = Configuration.class.getResourceAsStream (UBICACION);
        if(in!=null){
            try {
                log.debug("Se cargará el archivo de configuración.");
                defaultProps.load(in);
                in.close();
                switch (key){
                    case REPLICAS:
                        return defaultProps.getProperty("Replicas");
                    case TIMEOUTCREARCLIENTEFTP:
                        return defaultProps.getProperty("TimeoutCrearClienteFTP");
                    case TIEMPOENTREINTENTOSLEVANTARSERVIDORCAIDO:
                        return defaultProps.getProperty("TiempoEntreIntentosLevantarServidorCaido");
                    case TIPODEALMACENAMIENTODECLIPS:
                        return defaultProps.getProperty("TipoDeAlmacenamientoDeClips");
                    default:
                        throw new  ClipStorageConfiguracionIncompletaException("Se recibió una key inválida: "+key.toString());
                }
            } catch (IOException e) {
                log.error(e.getStackTrace());
                throw new ClipStorageConfiguracionIncompletaException("Error al acceder al archivo de configuracion en "+UBICACION);
            }
        }else{
            log.warn("No se encontró el archivo de configuración, se utilizarán los valores por omisión.");
            switch (key){
                case REPLICAS:
                    return "2";
                case TIMEOUTCREARCLIENTEFTP:
                    return "10000";
                case TIEMPOENTREINTENTOSLEVANTARSERVIDORCAIDO:
                    return "30000";
                case TIPODEALMACENAMIENTODECLIPS:
                    return "memoria";
                default:
                    throw new  ClipStorageConfiguracionIncompletaException("Se recibió una key inválida: "+key.toString());
            }
        }
    }

    /**
     * Lee un item de configuración de tipo int.
     * @param key La clave del item a leer.
     * @return El valor configurado para la clave brindada. Si el archivo de configuración no es encontrado, se utilizan valores predefinidos.
     */
    public static int getIntConfiguration(ConfigurationItemsEnum key){
        String stringResult=getConfiguracion(key);
        try{
            return Integer.parseInt(stringResult);
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw new ClipStorageConfiguracionIncompletaException("Error de formato en la configuración de: "+key);
        }
    }
}