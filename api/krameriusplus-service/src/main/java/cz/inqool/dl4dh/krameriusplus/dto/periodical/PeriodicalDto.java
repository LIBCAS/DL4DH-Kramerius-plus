package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusPublicationAssemblerVisitor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalDto extends PublicationDto<Periodical> {

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.PERIODICAL;
    }

    @Override
    public Periodical toEntity() {
        Periodical entity = super.toEntity(new Periodical());

        return entity;
    }

    @Override
    public Periodical accept(KrameriusPublicationAssemblerVisitor visitor) {
        return visitor.assemble(this);
    }
}
