package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class MonographWithPages extends Monograph {

    @Transient
    private List<Page> pages = new ArrayList<>();
}
