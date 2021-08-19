package cz.inqool.dl4dh.krameriusplus.domain.entity.monograph;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;
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

    @Override
    public List<? extends Publication> getChildren() {
        return new ArrayList<>();
    }
}
