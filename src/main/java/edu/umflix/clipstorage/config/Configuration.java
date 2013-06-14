package edu.umflix.clipstorage.config;

import edu.umflix.clipstorage.Exceptions.ClipStorageConfiguracionIncompletaException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {
    private static final String UBICACION= "/config.properties";

    public static String getConfiguracion(String key){
        Properties defaultProps = new Properties();
        try {
            InputStream in = Configuration.class.getResourceAsStream (UBICACION);
            defaultProps.load(in);
            in.close();
            return defaultProps.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ClipStorageConfiguracionIncompletaException("Error al acceder al archivo de configuracion en "+UBICACION);
        }
    }

    public static int getIntConfiguration(String key){
        String stringResult=getConfiguracion(key);
        try{
            return Integer.parseInt(stringResult);
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw new ClipStorageConfiguracionIncompletaException("Error de formato en el archivo de configuracion: "+key);
        }
    }
}