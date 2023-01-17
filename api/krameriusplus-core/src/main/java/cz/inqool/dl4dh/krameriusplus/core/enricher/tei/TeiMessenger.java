package cz.inqool.dl4dh.krameriusplus.core.enricher.tei;

import cz.inqool.dl4dh.krameriusplus.api.export.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;

import java.io.InputStream;

public interface TeiMessenger {

    /**
     * Converts a single Page using TEI Converter to TEI body and returns the resulting content
     * (TEI) as String
     * @param page page to convert
     * @return response body from TEI Converter
     */
    String convertPage(Page page);

    /**
     * Converts a Publication using TEI Converter to TEI header and returns the resulting content
     * (TEI) as String
     * @param publication publication to convert
     * @return response body from TEI Converter
     */
    String convertHeader(Publication publication);

    /**
     * Starts a multi-step merge process. Sends the header of the given publication
     * @param teiHeader inputStream of the teiHeader
     * @return ID of the created session from TEI Converter
     */
    SessionDto startMerge(InputStream teiHeader);

    SessionDto addMerge(String sessionId, InputStream teiPage);

    SessionDto finishMerge(String sessionId, TeiParams teiParams);
}
