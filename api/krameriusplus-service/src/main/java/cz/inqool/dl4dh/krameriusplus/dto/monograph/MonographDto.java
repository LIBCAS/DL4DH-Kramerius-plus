package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public abstract class MonographDto<T extends Monograph> extends PublicationDto<T> {

    private String donator;

    @Override
    public KrameriusModel getModel() {
        return KrameriusModel.MONOGRAPH;
    }

    protected T toEntity(T entity) {
        entity = super.toEntity(entity);
        entity.setDonator(donator);

        return entity;
    }
}
