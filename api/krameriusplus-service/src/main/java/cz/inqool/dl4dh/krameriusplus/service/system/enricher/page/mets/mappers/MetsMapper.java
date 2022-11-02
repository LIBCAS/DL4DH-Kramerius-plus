package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.mappers;

public interface MetsMapper<T> {

    T map(String source);

    Class<T> supports();
}
