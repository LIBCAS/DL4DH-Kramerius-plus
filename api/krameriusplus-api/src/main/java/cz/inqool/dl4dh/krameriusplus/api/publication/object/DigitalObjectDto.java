package cz.inqool.dl4dh.krameriusplus.api.publication.object;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.api.publication.DigitalObjectContext;
import cz.inqool.dl4dh.krameriusplus.api.publication.page.PageDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.publication.KrameriusModel.*;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "model", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = MonographDto.class, name = MONOGRAPH),
        @JsonSubTypes.Type(value = MonographUnitDto.class, name = MONOGRAPH_UNIT),
        @JsonSubTypes.Type(value = PeriodicalDto.class, name = PERIODICAL),
        @JsonSubTypes.Type(value = PeriodicalVolumeDto.class, name = PERIODICAL_VOLUME),
        @JsonSubTypes.Type(value = PeriodicalItemDto.class, name = PERIODICAL_ITEM),
        @JsonSubTypes.Type(value = PageDto.class, name = PAGE),
        @JsonSubTypes.Type(value = InternalPartDto.class, name = INTERNAL_PART),
        @JsonSubTypes.Type(value = SupplementDto.class, name = SUPPLEMENT)
})
@EqualsAndHashCode(callSuper = true)
public abstract class DigitalObjectDto extends DatedObjectDto {

    private String parentId;

    private Integer index;

    private List<DigitalObjectContext> context = new ArrayList<>();
}
