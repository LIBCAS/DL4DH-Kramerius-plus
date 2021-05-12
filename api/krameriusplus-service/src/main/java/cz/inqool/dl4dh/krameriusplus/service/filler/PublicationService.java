package cz.inqool.dl4dh.krameriusplus.service.filler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.MonographStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Periodical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Norbert Bodnar
 */
@Service
public class PublicationService {

    private final MonographStore monographStore;

    @Autowired
    public PublicationService(MonographStore monographStore) {
        this.monographStore = monographStore;
    }

    public void save(Monograph monograph) {
        monographStore.save(monograph);
    }

    public Monograph findMonograph(String pid, boolean includePages) {
        return includePages ? monographStore.findWithPages(pid) : monographStore.find(pid);
    }

    public void save(Periodical periodical) {

    }
}
