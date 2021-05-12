package cz.inqool.dl4dh.krameriusplus.domain.dto;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.enums.PublicationModel;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException.ErrorCode.NO_PAGES;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class KrameriusMonographDto extends KrameriusPublicationDto {

    private List<KrameriusPageDto> pages = new ArrayList<>();

    public Monograph toEntity() {
        if (pages.isEmpty()) {
            throw new KrameriusException(NO_PAGES, "KrameriusMonographDto has no pages");
        }
        Monograph monograph = new Monograph();
        monograph.setPid(pid);
        monograph.setCollections(collections);
        monograph.setTitle(title);
        monograph.setPolicy(policy);
        monograph.setPages(pages.stream().map(KrameriusPageDto::toEntity).collect(Collectors.toList()));

        return monograph;
    }

    public PublicationModel getModel() {
        return PublicationModel.MONOGRAPH;
    }
}
