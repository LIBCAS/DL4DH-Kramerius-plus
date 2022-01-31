package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject;

import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;

public class InternalPart extends DigitalObject {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.INTERNAL_PART;
    }
}
