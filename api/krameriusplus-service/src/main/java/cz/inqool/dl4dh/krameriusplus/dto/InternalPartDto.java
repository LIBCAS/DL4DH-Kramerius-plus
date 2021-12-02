package cz.inqool.dl4dh.krameriusplus.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.InternalPart;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;

public class InternalPartDto extends DigitalObjectDto<InternalPart> {
    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.INTERNAL_PART;
    }

    @Override
    public InternalPart toEntity() {
        return new InternalPart();
    }

    @Override
    public InternalPart accept(KrameriusPublicationAssemblerVisitor visitor) {
        return null;
    }
}
