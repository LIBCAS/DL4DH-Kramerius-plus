package cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;

public interface StreamProvider {

    /**
     * Retrieve ALTO stream for given page
     * @param pageId id of page
     * @return ALTO object
     */
    Alto getAlto(String pageId);

    /**
     * Retrieve OCR stream for given page
     * @param pageId id of page
     * @return page content as String
     */
    String getOCR(String pageId);

    /**
     * Retrieve BIBLIO_MODS stream for given publication
     * @param publicationId id of publication
     * @return MODS object
     */
    ModsCollectionDefinition getMods(String publicationId);
}
