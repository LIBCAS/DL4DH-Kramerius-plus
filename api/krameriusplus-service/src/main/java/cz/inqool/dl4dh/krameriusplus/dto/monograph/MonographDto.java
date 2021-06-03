package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithPages;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithUnits;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.PublicationAssemblerVisitor;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.MONOGRAPH;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographDto extends PublicationDto<Monograph> {

    private String donator;

    private KrameriusModel model = MONOGRAPH;

    private boolean containUnits = false;

    @Override
    public Monograph toEntity() {
        Monograph entity;
        if (containUnits) {
            entity = super.toEntity(new MonographWithUnits());
        } else {
            entity = super.toEntity(new MonographWithPages());
        }

        entity.setDonator(donator);
        return entity;
    }

    @Override
    public Monograph accept(PublicationAssemblerVisitor visitor) {
        return visitor.assemble(this);
    }
}
