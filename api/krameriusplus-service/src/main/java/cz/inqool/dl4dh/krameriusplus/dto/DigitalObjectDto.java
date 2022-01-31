package cz.inqool.dl4dh.krameriusplus.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalItemDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalVolumeDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Interface that every dto class that implements a model from Kramerius should implement.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "model")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = MonographDto.class, name = "monograph"),
        @JsonSubTypes.Type(value = MonographUnitDto.class, name = "monographunit"),
        @JsonSubTypes.Type(value = PeriodicalDto.class, name = "periodical"),
        @JsonSubTypes.Type(value = PeriodicalVolumeDto.class, name = "periodicalvolume"),
        @JsonSubTypes.Type(value = PeriodicalItemDto.class, name = "periodicalitem"),
        @JsonSubTypes.Type(value = PageDto.class, name = "page"),
        @JsonSubTypes.Type(value = InternalPartDto.class, name = "internalpart")
})
public abstract class DigitalObjectDto<T extends DigitalObject> implements Visitable<T> {

    private String pid;

    protected T toEntity(T entity) {
        entity.setId(pid);

        return entity;
    }

    public abstract KrameriusModel getModel();

    public abstract T toEntity();
}
