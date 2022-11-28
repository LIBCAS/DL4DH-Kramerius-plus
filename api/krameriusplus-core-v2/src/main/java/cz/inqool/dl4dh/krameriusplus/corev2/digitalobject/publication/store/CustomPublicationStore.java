package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;

import java.time.Instant;
import java.util.List;

public interface CustomPublicationStore {

    List<Publication> findAllPublishedModified(Instant publishedModifiedAfter);

    /**
     * List all child publications. Only includes one level deep children, no nesting. Does not include
     * pages.
     * @param parentId parent publication ID
     * @return list of publications for given parent ID
     */
    List<Publication> findAllChildren(String parentId);

    List<String> findAllChildrenIds(String parentId);

    /**
     * Returns a list of complete publication subtree IDs, including root, not including pages
     * @param publicationID root publication ID
     * @return list of IDs of every publication in subtree
     */
    List<String> findAllEditions(String publicationID);
}