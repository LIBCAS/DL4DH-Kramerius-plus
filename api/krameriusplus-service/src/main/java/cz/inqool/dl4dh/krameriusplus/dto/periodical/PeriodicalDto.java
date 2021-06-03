package cz.inqool.dl4dh.krameriusplus.dto.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.PublicationAssemblerVisitor;
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
    public Periodical accept(PublicationAssemblerVisitor visitor) {
        return visitor.assemble(this);
    }
}
