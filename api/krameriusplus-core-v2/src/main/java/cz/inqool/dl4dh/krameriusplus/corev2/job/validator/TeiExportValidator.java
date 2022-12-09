package cz.inqool.dl4dh.krameriusplus.corev2.job.validator;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRefStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeiExportValidator implements ExportValidator {

    private PublicationStore publicationStore;

    private FileRefStore fileRefStore;

    /**
     * Returns true if every publication in given publication tree has TEI header file
     * @param publicationId
     * @return
     */
    @Override
    public boolean canCreate(String publicationId) {
        return hasTeiHeader(publicationId);
    }

    private boolean hasTeiHeader(String publicationId) {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        if (publication.getTeiHeaderFileId() == null || !fileRefStore.exist(publication.getTeiHeaderFileId())) {
            return false;
        }

        List<Publication> children = publicationStore.findAllChildren(publicationId);

        return children.stream().allMatch(child -> hasTeiHeader(child.getTeiHeaderFileId()));
    }

    @Override
    public boolean supports(ExportFormat exportFormat) {
        return exportFormat.equals(ExportFormat.TEI);
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setFileRefStore(FileRefStore fileRefStore) {
        this.fileRefStore = fileRefStore;
    }
}
