package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.domain.dao.cascade.CascadeSave;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PERIODICAL;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "publications")
public class Periodical extends Publication {

    @DBRef
    @CascadeSave
    private List<PeriodicalVolume> periodicalVolumes = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return PERIODICAL;
    }

    @Override
    @JsonIgnore
    public List<Page> getPages() {
        if (periodicalVolumes == null || periodicalVolumes.isEmpty()) {
            throw new IllegalStateException("PeriodicalVolume contains no items");
        }

        return periodicalVolumes
                .stream()
                .map(PeriodicalVolume::getPages)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends Publication> getChildren() {
        return periodicalVolumes;
    }

    @Override
    public void addPages(PageRepository pageRepository, Pageable pageable) {
        for (PeriodicalVolume volume : periodicalVolumes) {
            volume.addPages(pageRepository, pageable);
        }
    }
}
