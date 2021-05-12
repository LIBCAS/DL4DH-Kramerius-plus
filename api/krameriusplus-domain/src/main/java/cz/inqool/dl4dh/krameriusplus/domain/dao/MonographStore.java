package cz.inqool.dl4dh.krameriusplus.domain.dao;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Norbert Bodnar
 */
@Repository
public class MonographStore {

    private final int DEFAULT_PAGE_SIZE = 10;

    private final MonographRepository monographRepository;

    private final PageRepository pageRepository;

    @Autowired
    public MonographStore(MonographRepository monographRepository, PageRepository pageRepository) {
        this.monographRepository = monographRepository;
        this.pageRepository = pageRepository;
    }

    public void save(Monograph monograph) {
        if (monograph == null || monograph.getPages() == null) {
            throw new IllegalArgumentException("Monograph or its pages is null");
        }
        pageRepository.saveAll(monograph.getPages());
        monographRepository.save(monograph);
    }

    public Monograph find(String pid) {
        Optional<Monograph> monograph = monographRepository.findById(pid);
        if (monograph.isEmpty()) {
            throw new IllegalArgumentException("Monograph with pid=" + pid + " not found.");
        }

        return monograph.get();
    }

    public Monograph findWithPages(String pid) {
        return findWithPages(pid, null);
    }

    public Monograph findWithPages(String pid, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        }
        Optional<Monograph> monograph = monographRepository.findById(pid);
        if (monograph.isEmpty()) {
            throw new IllegalArgumentException("Monograph with pid=" + pid + " not found.");
        }

        Monograph found = monograph.get();
        found.setPages(pageRepository.findAllByRootIdOrderByPageIndexAsc(pid, pageable));
        return found;
    }
}
