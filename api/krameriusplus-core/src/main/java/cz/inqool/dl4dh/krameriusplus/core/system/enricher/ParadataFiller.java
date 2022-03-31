package cz.inqool.dl4dh.krameriusplus.core.system.enricher;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.UDPipeParadata;

public class ParadataFiller {

    private final Publication publication;

    public ParadataFiller(Publication publication) {
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