package cz.inqool.dl4dh.krameriusplus.domain.service;

import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPublicationDto;

/**
 * @author Norbert Bodnar
 */
public interface DataProviderService {

    KrameriusPublicationDto getPublication(String pid);
}
