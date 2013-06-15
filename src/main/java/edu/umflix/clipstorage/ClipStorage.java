package edu.umflix.clipstorage;

import edu.umflix.model.ClipData;

import javax.ejb.Local;

/**
 * Esta interfaz define el comportamiento que debe tener el repositorio de datos
 */
@Local
public interface ClipStorage {
    /**
     * Este método almacena las bytes del clip provisto con redundancia configurable y en forma transparente hacia quien lo invoca.
     * @param clipdata El clip que se desea almacenar.
     */
    void storeClipData(ClipData clipdata);

    /**
     * Este método devuelve los bytes del clip con el id provisto.
     * @param id El id del clip que se quiere leer.
     * @return El ClipData solicitado junto a sus bytes.
     */
    ClipData getClipDataByClipId(long id);
}