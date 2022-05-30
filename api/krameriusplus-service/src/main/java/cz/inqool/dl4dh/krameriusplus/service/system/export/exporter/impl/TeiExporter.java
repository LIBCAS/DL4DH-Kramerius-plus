package cz.inqool.dl4dh.krameriusplus.service.system.export.exporter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ExportException;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.service.system.export.exporter.AbstractExporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ExportException.ErrorCode.TEI_MERGE_ERROR;

@Component
@Slf4j
public class TeiExporter extends AbstractExporter {

    private final TeiConnector teiConnector;

    @Autowired
    public TeiExporter(ObjectMapper objectMapper, TeiConnector teiConnector, FileService fileService, PublicationService publicationService) {
        super(objectMapper, fileService, publicationService);
        this.teiConnector = teiConnector;
    }

    @Override
    public Export export(String publicationId, Params params) {
        TeiParams teiParams = convert(params);

        Publication publication = publicationService.findWithPages(publicationId, teiParams.cleanForTei());

        File tmpFile = getTei(publication, teiParams);

        try {
            FileRef file = fileService.create(new FileInputStream(tmpFile), tmpFile.length(),
                    getFormat().getFileName(publicationId), ContentType.TEXT_XML.getMimeType());

            return createExport(publication, file);
        } catch (FileNotFoundException e) {
            throw new ExportException(TEI_MERGE_ERROR, e);
        }
    }

    private TeiParams convert(Params params) {
        if (params instanceof TeiParams) {
            return (TeiParams) params;
        } else {
            return new TeiParams(params);
        }
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.TEI;
    }

    private File getTei(Publication publication, TeiParams params) {
        FileRef teiHeader = fileService.find(publication.getTeiHeaderFileId());
        List<FileRef> teiPages = new ArrayList<>();

        for (Page page : publication.getPages()) {
            teiPages.add(fileService.find(page.getTeiBodyFileId()));
        }

        teiHeader.open();
        teiPages.forEach(FileRef::open);

        File file = teiConnector.merge(teiHeader.getStream(),
                teiPages.stream()
                        .map(FileRef::getStream)
                        .collect(Collectors.toList()),
                params);

        teiHeader.close();
        teiPages.forEach(FileRef::close);

        return file;
    }
}
