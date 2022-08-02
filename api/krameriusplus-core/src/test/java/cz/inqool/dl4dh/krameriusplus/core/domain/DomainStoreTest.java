package cz.inqool.dl4dh.krameriusplus.core.domain;

import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.periodical.Periodical;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class DomainStoreTest extends CoreBaseTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PublicationStore publicationStore;

    @Autowired
    private PageStore pageStore;

    @AfterEach
    void cleanUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void storeOne() {
        Publication monograph = new Monograph();

        publicationStore.save(monograph);

        Iterable<Publication> publications = publicationStore.findAll();
        assertTrue(publications.iterator().hasNext());
        assertEquals(monograph, publications.iterator().next());
    }

    @Test
    public void storeMultipleSameType() {
        List<Publication> publications = new ArrayList<>();
        publications.add(new Monograph());
        publications.add(new Monograph());

        publicationStore.saveAll(publications);

        Iterable<Publication> actual = publicationStore.findAll();
        List<Publication> result = new ArrayList<>();
        actual.iterator().forEachRemaining(result::add);
        assertEquals(2, result.size());
    }

    @Test
    public void storeMultipleDifferentType() {
        List<Publication> publications = new ArrayList<>();
        publications.add(new Monograph());
        publications.add(new Periodical());
        publications.add(new MonographUnit());

        publicationStore.saveAll(publications);

        Iterable<Publication> actual = publicationStore.findAll();
        List<Publication> result = new ArrayList<>();
        actual.iterator().forEachRemaining(result::add);

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(pub -> pub instanceof Monograph));
        assertTrue(result.stream().anyMatch(pub -> pub instanceof Periodical));
        assertTrue(result.stream().anyMatch(pub -> pub instanceof MonographUnit));
    }

    @Test
    public void find() {
        Publication publication = new Monograph();
        publication.setId("1");
        publication.setTitle("Title 1");
        publicationStore.save(publication);

        Publication actual = publicationStore.findById("1").orElse(null);

        Assertions.assertEquals(publication.getId(), actual.getId());
        assertEquals(publication.getTitle(), actual.getTitle());
    }

    @Test
    public void findWithCustomFields() {
        Publication publication = new Monograph();
        publication.setId("1");
        publication.setTitle("Title 1");
        publication.setPolicy("Policy 1");
        publicationStore.save(publication);

        List<String> includeFields = new ArrayList<>();
        includeFields.add("_id");
        includeFields.add("title");

        Publication actual = publicationStore.find("1", includeFields);

        Assertions.assertEquals(publication.getId(), actual.getId());
        assertEquals(publication.getTitle(), actual.getTitle());
        assertNull(actual.getPolicy());
    }

    @Test
    public void update() {
        Publication expected = new Monograph();
        expected.setId("1");
        expected.setTitle("title");

        publicationStore.save(expected);

        expected = publicationStore.findById("1").orElse(null);
        expected.setTitle("new title");

        publicationStore.save(expected);

        Publication actual = publicationStore.findById("1").orElse(null);

        assertEquals("new title", actual.getTitle());
    }

    @Test
    public void IterableByIds() {
        for (int i = 0; i < 10; i++) {
            Publication publication = new MonographUnit();
            publication.setId(String.valueOf(i));
            publicationStore.save(publication);
        }

        Iterable<Publication> actual = publicationStore.findAllById(
                IntStream.range(0, 10)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.toList()));

        List<Publication> result = new ArrayList<>();
        actual.iterator().forEachRemaining(result::add);
        assertEquals(10, result.size());
        assertTrue(IntStream.range(0, 10)
                .mapToObj(String::valueOf)
                .allMatch(id -> result.stream().anyMatch(pub -> pub.getId().equals(id))));
    }

    @Test
    public void findWithoutPages() {
        List<Page> pages = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            Page page = new Page();
            page.setId(String.valueOf(j));
            page.setContent("Content of the page " + j);
            pages.add(pageStore.save(page));
        }

        Publication publication = new Monograph();
        publication.setId("1");
        publication.setTitle("Title");
        publication.setPolicy("Policy");
        publication.setPages(pages);
        publicationStore.save(publication);

        Publication actual = publicationStore.find("1", getIncludeFields("title", "policy"));

        assertEquals(0, actual.getPages().size());
        Assertions.assertEquals("1", actual.getId());
        assertEquals("Title", actual.getTitle());
        assertEquals("Policy", actual.getPolicy());
    }

    @Test
    public void deleteOne() {
        Publication publication = new MonographUnit();
        publication.setId("1");
        publication.setTitle("Title");
        publication.setPolicy("Policy");

        publicationStore.save(publication);

        assertNotNull(publicationStore.findById("1").orElse(null));

        publicationStore.delete(publication);

        assertNull(publicationStore.findById("1").orElse(null));
    }

    @Test
    public void deleteMultiple() {
        List<Publication> publications = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            List<Page> pages = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Page page = new Page();
                page.setId(String.valueOf(i * 10 + j));
                page.setContent("Content of the page " + i * 10 + j);
                pages.add(pageStore.save(page));
            }

            Publication publication = new MonographUnit();
            publication.setId(String.valueOf(i));
            publication.setTitle("Title" + i);
            publication.setPolicy("Policy" + i);
            publication.setPages(pages);
            publications.add(publicationStore.save(publication));
        }

        publicationStore.deleteAll(publications);
    }

    private List<String> getIncludeFields(String... fields) {
        return new ArrayList<>(Arrays.asList(fields));
    }

}
