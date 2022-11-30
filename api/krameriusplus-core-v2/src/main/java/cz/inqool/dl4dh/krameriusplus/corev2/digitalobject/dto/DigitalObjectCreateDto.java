package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.monograph.MonographCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.monograph.MonographUnitCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.other.InternalPartCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.other.SupplementCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.periodical.PeriodicalCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.periodical.PeriodicalItemCreateDto;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.dto.periodical.PeriodicalVolumeCreateDto;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.*;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "model", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = MonographCreateDto.class, name = MONOGRAPH),
        @JsonSubTypes.Type(value = MonographUnitCreateDto.class, name = MONOGRAPH_UNIT),
        @JsonSubTypes.Type(value = PeriodicalCreateDto.class, name = PERIODICAL),
        @JsonSubTypes.Type(value = PeriodicalVolumeCreateDto.class, name = PERIODICAL_VOLUME),
        @JsonSubTypes.Type(value = PeriodicalItemCreateDto.class, name = PERIODICAL_ITEM),
        @JsonSubTypes.Type(value = PageCreateDto.class, name = PAGE),
        @JsonSubTypes.Type(value = InternalPartCreateDto.class, name = INTERNAL_PART),
        @JsonSubTypes.Type(value = SupplementCreateDto.class, name = SUPPLEMENT)
})
public abstract class DigitalObjectCreateDto {

    @JsonProperty("datanode")
    private Boolean dataNode;

    private String pid;

    private String model;

    private String title;

    @JsonProperty("root_title")
    private String rootTitle;

    @JsonProperty("root_pid")
    private String rootPid;

    private String policy;

}
