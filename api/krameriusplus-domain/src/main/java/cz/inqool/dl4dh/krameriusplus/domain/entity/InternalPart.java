package cz.inqool.dl4dh.krameriusplus.domain.entity;

import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;

public class InternalPart extends KrameriusObject {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.INTERNAL_PART;
    }
}
