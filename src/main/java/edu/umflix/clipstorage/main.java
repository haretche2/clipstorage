package edu.umflix.clipstorage;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Santago
 * Date: 2/06/13
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class main {
    public static void main(String[] arr) throws IOException{

          FtpClient.getClient().getListHiddenFiles();
    }
}
