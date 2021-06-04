package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.dao.cascade.CascadeSave;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithUnits extends Monograph {

    @DBRef
    @CascadeSave
    private List<MonographUnit> monographUnits = new ArrayList<>();

    @Override
    public List<Page> getPages() {
        if (monographUnits == null || monographUnits.isEmpty()) {
            throw new IllegalStateException("PeriodicalVolume contains no items");
        }

        return monographUnits
                .stream()
                .map(MonographUnit::getPages)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
