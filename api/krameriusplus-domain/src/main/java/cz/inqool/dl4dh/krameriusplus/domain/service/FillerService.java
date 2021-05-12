package cz.inqool.dl4dh.krameriusplus.domain.service;

import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import org.springframework.stereotype.Service;

/**
 * @author Norbert Bodnar
 */
@Service
public interface FillerService {

    void enrichPublication(String pid);

    void enrichPublicationAsync(String pid, EnrichmentTask task);
}
