package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.dto.KrameriusDto;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
public interface DataProvider<T extends KrameriusDto<?>> {

    T getDigitalObject(String objectId);

    List<T> getDigitalObjectsForParent(String parentId);
}
