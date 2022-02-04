package cz.inqool.dl4dh.krameriusplus.service.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
public interface DataProvider {

    <T extends DigitalObject> T getDigitalObject(String objectId);

    <T extends DigitalObject> List<T> getDigitalObjectsForParent(Publication parent);
}
