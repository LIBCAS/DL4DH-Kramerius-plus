package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithPages;
import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithPagesDto extends MonographDto<MonographWithPages> {

    private List<PageDto> pages;

    @Override
    public MonographWithPages toEntity() {
        MonographWithPages entity = super.toEntity(new MonographWithPages());
        entity.setPages(pages
                .stream()
                .map(PageDto::toEntity)
                .collect(Collectors.toList()));

        return entity;
    }
}
