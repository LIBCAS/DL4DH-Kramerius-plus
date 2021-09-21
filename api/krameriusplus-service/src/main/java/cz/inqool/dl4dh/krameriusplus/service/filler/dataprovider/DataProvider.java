package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider;

import cz.inqool.dl4dh.krameriusplus.domain.entity.KrameriusObject;
import cz.inqool.dl4dh.krameriusplus.dto.DigitalObjectDto;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
public interface DataProvider {

    <T extends DigitalObjectDto<?>> T getDigitalObject(String objectId);

    <T extends KrameriusObject> List<DigitalObjectDto<T>> getDigitalObjectsForParent(String parentId);
}
