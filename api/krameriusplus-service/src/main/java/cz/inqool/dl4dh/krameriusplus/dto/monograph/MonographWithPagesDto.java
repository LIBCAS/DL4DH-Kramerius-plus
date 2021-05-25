package cz.inqool.dl4dh.krameriusplus.dto.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithPagesDto extends MonographDto {

    private List<PageDto> pages;

    @Override
    public Monograph toEntity() {
        return null;
    }
}
