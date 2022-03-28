package cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
public interface DataProvider {

    DigitalObject getDigitalObject(String objectId);

    List<DigitalObject> getDigitalObjectsForParent(String parentId);
}
