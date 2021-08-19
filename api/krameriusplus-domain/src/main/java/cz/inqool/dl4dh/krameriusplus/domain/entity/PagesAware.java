package cz.inqool.dl4dh.krameriusplus.domain.entity;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;

import java.util.List;

public interface PagesAware {

    List<Page> getPages();

    void setPages(List<Page> pages);
}
