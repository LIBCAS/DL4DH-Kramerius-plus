package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithUnits extends Monograph {

    private List<MonographUnit> monographUnits;
}
