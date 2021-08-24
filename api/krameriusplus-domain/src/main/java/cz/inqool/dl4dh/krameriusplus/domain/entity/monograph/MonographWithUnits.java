package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.dao.cascade.CascadeSave;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

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
    public List<? extends Publication> getChildren() {
        return monographUnits;
    }
}
