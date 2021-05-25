package cz.inqool.dl4dh.krameriusplus.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalItemDto;
import cz.inqool.dl4dh.krameriusplus.dto.periodical.PeriodicalVolumeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
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
        @JsonSubTypes.Type(value = PeriodicalItemDto.class, name = "periodicalitem")
})
public abstract class PublicationDto<T extends Publication> implements KrameriusModelAware, KrameriusDto<T> {

    protected String pid;

    protected String title;

    protected List<String> collections;

    protected String policy;

    protected T toEntity(T entity) {
        entity.setPid(pid);
        entity.setTitle(title);
        entity.setCollections(collections);
        entity.setPolicy(policy);

        return entity;
    }
}
