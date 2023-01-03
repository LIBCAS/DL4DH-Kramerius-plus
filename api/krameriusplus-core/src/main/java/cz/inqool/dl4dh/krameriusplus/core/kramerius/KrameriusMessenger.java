package cz.inqool.dl4dh.krameriusplus.core.kramerius;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.DigitalObject;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;

import java.util.List;

public interface KrameriusMessenger {

    /**
     * Fetch {@link DigitalObject} from Kramerius API
     * @param objectId UUID of the digital object
     * @return DigitalObject
     */
    DigitalObject getDigitalObject(String objectId);

    /**
     * Fetch a list of {@link DigitalObject}s which are the children of the DigitalObject with given
     * parentId
     * @param parentId UUID of the parent DigitalObject
     * @return List of child DigitalObjects
     */
    List<DigitalObject> getDigitalObjectsForParent(String parentId);

    /**
     * Retrieve ALTO stream for given page. If it returns 404, try 'alto' stream as well.
     * @param pageId id of page
     * @return ALTO object
     */
    Alto getAlto(String pageId);

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
    String getOcr(String pageId);

    /**
     * Retrieve BIBLIO_MODS stream for given publication
     * @param publicationId id of publication
     * @return MODS object
     */
    ModsCollectionDefinition getMods(String publicationId);
}
