package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.UDPipeParadata;
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
public class MonographWithPages extends Monograph implements PagesAware {

    @Transient
    private List<Page> pages = new ArrayList<>();

    private OCRParadata ocrParadata;

    private NameTagParadata nameTagParadata;

    private UDPipeParadata udPipeParadata;

    @Override
    public List<? extends Publication> getChildren() {
        return new ArrayList<>();
    }
}
