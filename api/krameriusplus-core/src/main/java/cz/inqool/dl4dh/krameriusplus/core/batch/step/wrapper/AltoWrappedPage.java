package cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AltoWrappedPage {

    /**
     * Original page entity
     */
    private Page page;

    /**
     * Raw Alto from ALTO stream
     */
    private Alto.Layout altoLayout;

    /**
     * Content of the page extracted from ALTO format
     */
    private String content;

    public AltoWrappedPage(Page item) {
        this.page = item;
    }
}
