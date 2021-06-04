package cz.inqool.dl4dh.krameriusplus.service.dataaccess;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.MonographWithPagesRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.MonographUnitRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.periodical.Periodical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
public class PublicationService {

    private final MonographWithPagesRepository monographWithPagesRepository;

    private final MonographUnitRepository monographUnitRepository;

    private final PageRepository pageRepository;

    @Autowired
    public PublicationService(MonographWithPagesRepository monographWithPagesRepository, MonographUnitRepository monographUnitRepository,
                              PageRepository pageRepository) {
        this.monographWithPagesRepository = monographWithPagesRepository;
        this.monographUnitRepository = monographUnitRepository;
        this.pageRepository = pageRepository;
    }

    public Publication find(String publicationId) {
        //todo: napisat medzivrstvu
//        return publicationRepository.findById(publicationId)
//                .orElseThrow(() -> new IllegalArgumentException("Publication with pid=" + publicationId + " not found."));
        return null;
    }

    public Publication findWithChildren(String publicationId) {
        Publication publication = find(publicationId);

        if (publication instanceof Monograph) {
            return findMonographWithPages(publicationId);
        } else if (publication instanceof MonographUnit) {
            return findMonographUnitWithPages(publicationId);
        } else {
            throw new IllegalArgumentException("Publication of type " + publication.getModel() + " not supported yet.");
        }
    }

    @Transactional
    public void save(Monograph monograph) {
//        monographWithPagesRepository.save(monograph);
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
        return monographWithPagesRepository.findById(pid)
                .orElseThrow(() -> new IllegalArgumentException("Monograph with pid=" + pid + " not found."));
    }

    public Monograph findMonographWithPages(String pid) {
        return findMonographWithPages(pid, null);
    }

    public Monograph findMonographWithPages(String pid, Pageable pageable) {
        Monograph monograph = findMonograph(pid);
//
//        if (pageable != null) {
//            monograph.setPages(pageRepository.findByParentIdOrderByPageIndexAsc(pid, pageable));
//        } else {
//            monograph.setPages(pageRepository.findAllByParentIdOrderByPageIndexAsc(pid));
//        }
//
//        monograph.setMonographUnits(monographUnitRepository.findAllByParentIdOrderByPartNumberAsc(pid));
//        for (MonographUnit monographUnit : monograph.getMonographUnits()) {
//            if (pageable != null) {
//                monographUnit.setPages(pageRepository.findByParentIdOrderByPageIndexAsc(pid, pageable));
//            } else {
//                monographUnit.setPages(pageRepository.findAllByParentIdOrderByPageIndexAsc(pid));
//            }
//        }

        return monograph;
    }

    public MonographUnit findMonographUnit(String pid) {
        return monographUnitRepository.findById(pid)
                .orElseThrow(() -> new IllegalArgumentException("MonographUnit with pid=" + pid + " not found."));
    }

    public MonographUnit findMonographUnitWithPages(String pid) {
        return findMonographUnitWithPages(pid, null);
    }

    public MonographUnit findMonographUnitWithPages(String pid, Pageable pageable) {
        MonographUnit monographUnit = findMonographUnit(pid);

        if (pageable == null) {
            monographUnit.setPages(pageRepository.findAllByParentIdOrderByPageIndexAsc(pid));
        } else {
            monographUnit.setPages(pageRepository.findByParentIdOrderByPageIndexAsc(pid, pageable));
        }

        return monographUnit;
    }

    public void save(Periodical periodical) {

    }
}
