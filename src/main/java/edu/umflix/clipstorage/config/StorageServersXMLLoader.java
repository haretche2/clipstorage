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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 3/06/13
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class StorageServersXMLLoader {

    private static final String UBICACION="/config/StorageServers.xml";

    private static Logger log= Logger.getLogger(StorageServersXMLLoader.class);

    public static List<StorageServer> loadXML(){
        try {
            List<StorageServer> result=new ArrayList<StorageServer>();
            InputStream fXmlFile = StorageServersXMLLoader.class.getResourceAsStream(UBICACION);
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
    }
}
