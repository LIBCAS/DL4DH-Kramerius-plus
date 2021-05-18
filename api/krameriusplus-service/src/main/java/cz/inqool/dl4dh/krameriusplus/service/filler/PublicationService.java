package cz.inqool.dl4dh.krameriusplus.service.filler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.MonographRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dao.MonographUnitRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dao.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Periodical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
public class PublicationService {

    private final MonographRepository monographRepository;

    private final MonographUnitRepository monographUnitRepository;

    private final PageRepository pageRepository;

    @Autowired
    public PublicationService(MonographRepository monographRepository, MonographUnitRepository monographUnitRepository,
                              PageRepository pageRepository) {
        this.monographRepository = monographRepository;
        this.monographUnitRepository = monographUnitRepository;
        this.pageRepository = pageRepository;
    }

    @Transactional
    public void save(Monograph monograph) {
        monographRepository.save(monograph);
    }

    @Transactional
    public void save(MonographUnit monographUnit) {
        monographUnitRepository.save(monographUnit);
    }

    @Transactional
    public void save(List<Page> pages) {
        pageRepository.saveAll(pages);
    }

    public Monograph findMonograph(String pid) {
        return monographRepository.findById(pid)
                .orElseThrow(() -> new IllegalArgumentException("Monograph with pid=" + pid + " not found."));
    }

    public Monograph findMonographWithPages(String pid) {
        return findMonographWithPages(pid, null);
    }

    public Monograph findMonographWithPages(String pid, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }

        Monograph monograph = findMonograph(pid);
        monograph.setPages(pageRepository.findAllByRootIdOrderByPageIndexAsc(pid, pageable));

        return monograph;
    }

    public void save(Periodical periodical) {

    }
}
