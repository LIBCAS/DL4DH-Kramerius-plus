package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventDto;

public interface ExporterFacade {

    JobEventDto exportTei(String publicationId, TeiParams teiParams);

    JobEventDto export(String publicationId, String exportFormatStr, Params params);

    QueryResults<Export> list(String publicationId, int page, int pageSize);

    FileRef getFile(String fileRefId);
}