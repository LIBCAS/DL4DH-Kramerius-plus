package cz.inqool.dl4dh.krameriusplus.core.job.validator;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRefStore;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeiExportValidator implements ExportValidator {

    private PublicationStore publicationStore;

    private FileRefStore fileRefStore;

    /**
     * Validates if all publications and their children have TEI header field in MongoDB
     * @param exportRequest to validate
     * @throws JobParametersInvalidException if a publication or some of its children has no TEI header
     */
    @Override
    public void validate(ExportRequest exportRequest) throws JobParametersInvalidException {
        if (exportRequest.getCreateRequestJob().getJobType().equals(KrameriusJobType.EXPORT_TEI)) {
            for (String publicationId : exportRequest.getPublicationIds().values()) {
                validateHasTeiHeader(publicationId);
            }
        }
    }

    private void validateHasTeiHeader(String publicationId) throws JobParametersInvalidException {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new JobParametersInvalidException("Publication with ID: " + publicationId + " not found in MongoDB."));

        if (publication.getTeiHeaderFileId() == null || !fileRefStore.existsById(publication.getTeiHeaderFileId())) {
            throw new JobParametersInvalidException("Publication with ID: " + publicationId + " has no TEI header file. " +
                    "ExportRequest with TEI format cannot be created.");
        }

        List<Publication> children = publicationStore.findAllChildren(publicationId);

        for (Publication child : children) {
            validateHasTeiHeader(child.getId());
        }
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
