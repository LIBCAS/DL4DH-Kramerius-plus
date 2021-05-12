package cz.inqool.dl4dh.krameriusplus.domain.service;

import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusMonographDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;

/**
 * @author Norbert Bodnar
 */
public interface EnricherService {

    Monograph enrich(KrameriusMonographDto dto);
}
