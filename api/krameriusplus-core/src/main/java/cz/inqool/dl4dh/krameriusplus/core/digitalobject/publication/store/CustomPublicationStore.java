package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFilter;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;

import java.util.List;

public interface CustomPublicationStore {

    /**
     * List all child publications. Only includes one level deep children, no nesting. Does not include
     * pages.
     * @param parentId parent publication ID
     * @return list of publications for given parent ID
     */
    List<Publication> findAllChildren(String parentId);

    /**
     * Returns a list of complete publication subtree IDs, including root, not including pages
     * @param publicationID root publication ID
     * @return list of IDs of every publication in subtree
     */
    Publication findPublicationTree(String publicationID);

    Result<Publication> list(PublicationFilter filter, int page, int pageSize);
}