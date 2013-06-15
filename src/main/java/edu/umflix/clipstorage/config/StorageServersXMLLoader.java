package edu.umflix.clipstorage.config;

import edu.umflix.clipstorage.Exceptions.ClipStorageConfiguracionIncompletaException;
import edu.umflix.clipstorage.model.StorageServer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase permite levantar la definición de los servidores de almacenamiento configurados en el archivo StorageServers.xml .
 * Si el archivo no existe se utilizarán servidores predefinidos.
 */
public class StorageServersXMLLoader {
    private static final String UBICACION= "/StorageServers.xml";
    private static Logger log= Logger.getLogger(StorageServersXMLLoader.class);

    /**
     * Lee el archivo de definición de servidores.
     * @return Una lista con los servidores definidos en el archivo. Si este no existe, genera una lista de 10 servidores predefinidos.
     */
    public static List<StorageServer> loadXML(){
        List<StorageServer> result=new ArrayList<StorageServer>();
        InputStream fXmlFile = StorageServersXMLLoader.class.getResourceAsStream(UBICACION);
        if(fXmlFile!=null){
            try {
                log.debug("Se cargarán la definición de servidores definida en el archivo");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();

                log.debug("Root element :" + doc.getDocumentElement().getNodeName());

                NodeList nList = doc.getElementsByTagName("server");

                log.debug("----------------------------");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    log.debug("Current Element :" + nNode.getNodeName());

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        StorageServer newElement=new StorageServer();
                        newElement.setOnline(false);
                        newElement.setAddress(eElement.getElementsByTagName("address").item(0).getTextContent());
                        log.debug("address : " + eElement.getElementsByTagName("address").item(0).getTextContent());
                        newElement.setUsername(eElement.getElementsByTagName("ftpusername").item(0).getTextContent());
                        log.debug("ftpusername : " + eElement.getElementsByTagName("ftpusername").item(0).getTextContent());
                        newElement.setPassword(eElement.getElementsByTagName("ftppassword").item(0).getTextContent());
                        log.debug("ftppassword : " + eElement.getElementsByTagName("ftppassword").item(0).getTextContent());
                        result.add(newElement);
                    }
                }
                return result;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                throw new ClipStorageConfiguracionIncompletaException("No se pudo cargar el archivo de servidores, error de formato: "+UBICACION);
            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                throw new ClipStorageConfiguracionIncompletaException("No se pudo cargar el archivo de servidores, SAXException: "+UBICACION);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                throw new ClipStorageConfiguracionIncompletaException("No se pudo cargar el archivo de servidores, no se puede leer el archivo: "+UBICACION);
            }
        }else{
            log.warn("No se encontró el archivo de definición de servidores, se utilizarán valores predefinidos");
            List<StorageServer> defaultResult= new ArrayList<StorageServer>();
            for(int i=0;i<10;i++){
                StorageServer unServidor= new StorageServer();
                unServidor.setAddress("192.168.1.10"+i);
                unServidor.setUsername("telematica");
                unServidor.setPassword("telematica2013");
                unServidor.setAmountOfClipDataStored(0);
                unServidor.setOnline(false);
                defaultResult.add(unServidor);
            }
            return defaultResult;
        }
    }
}
