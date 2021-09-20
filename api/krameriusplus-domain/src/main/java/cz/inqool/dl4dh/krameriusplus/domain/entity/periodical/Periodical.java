package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.dao.cascade.CascadeSave;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PERIODICAL;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class Periodical extends Publication {

    @DBRef
    @CascadeSave
    private List<PeriodicalVolume> periodicalVolumes = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return PERIODICAL;
    }

    @Override
    public List<? extends Publication> getChildren() {
        return periodicalVolumes;
    }
}
