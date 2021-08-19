package cz.inqool.dl4dh.krameriusplus.domain.entity;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;

import java.util.List;

public interface PagesAware {

    void setPages(List<Page> pages);

    List<Page> getPages();

    void setOcrParadata(OCRParadata ocrParadata);
}
