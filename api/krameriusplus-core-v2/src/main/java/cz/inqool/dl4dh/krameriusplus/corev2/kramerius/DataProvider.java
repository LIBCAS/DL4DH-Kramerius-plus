package cz.inqool.dl4dh.krameriusplus.corev2.kramerius;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.DigitalObject;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
public interface DataProvider {

    DigitalObject getDigitalObject(String objectId);

    List<DigitalObject> getDigitalObjectsForParent(String parentId);

    /**
     * Retrieve ALTO stream for given page. If it returns 404, try 'alto' stream as well.
     * @param pageId id of page
     * @return ALTO object
     */
//    Alto getAlto(String pageId);

    /**
     * Retrieve ALTO stream for given page as String
     * @param pageId id of page
     * @return ALTO as String
     */
    String getAltoString(String pageId);

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
//    ModsCollectionDefinition getMods(String publicationId);
}
