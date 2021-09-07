package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.UDPipeParadata;

public class ParadataFiller {

    private final PagesAware publication;

    public ParadataFiller(PagesAware publication) {
        this.publication = publication;
    }

    public void fill() {
        OCRParadata ocrParadata = null;
        UDPipeParadata udPipeParadata = null;
        NameTagParadata nameTagParadata = null;

        for (Page page : publication.getPages()) {
            if (ocrParadata == null || (page.getOcrParadata() != null && !ocrParadata.equals(page.getOcrParadata()))) {
                ocrParadata = page.getOcrParadata();
            }

            if (udPipeParadata == null || (page.getUdPipeParadata() != null && !udPipeParadata.equals(page.getUdPipeParadata()))) {
                udPipeParadata = page.getUdPipeParadata();
            }

            if (nameTagParadata == null || (page.getNameTagParadata() != null && !nameTagParadata.equals(page.getNameTagParadata()))) {
                nameTagParadata = page.getNameTagParadata();
            }
        }

        publication.setOcrParadata(ocrParadata);
        publication.setUdPipeParadata(udPipeParadata);
        publication.setNameTagParadata(nameTagParadata);
    }
}
