package cz.inqool.dl4dh.krameriusplus.domain;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PublicationRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographWithUnits;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Norbert Bodnar
 */
@SpringBootTest
public class PublicationRepositoryTest {

    @Autowired
    private PublicationRepository publicationRepository;

    @Test
    public void testCreate() {
        MonographUnit unit = new MonographUnit();
        unit.setId(java.util.UUID.randomUUID().toString());

        MonographWithUnits monograph = new MonographWithUnits();
        monograph.setId(java.util.UUID.randomUUID().toString());
        monograph.getMonographUnits().add(unit);

        publicationRepository.save(monograph);

        List<Publication> publicationList = publicationRepository.findAll();

        assertEquals(2, publicationList.size());
    }
}
