package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.metadata.ModsWrapper;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.EnrichingException.ErrorCode.KRAMERIUS_ERROR;

@Service
public class PublicationEnricher {

    private final TeiConnector teiConnector;
    private final StreamProvider streamProvider;
    private String ndkPath;

    @Autowired
    public PublicationEnricher(TeiConnector teiConnector, StreamProvider streamProvider) {
        this.teiConnector = teiConnector;
        this.streamProvider = streamProvider;
    }

    public void enrich(Publication publication) {
        enrichPublicationWithMods(publication);
        enrichPublicationWithTeiHeader(publication);
        enrichPublicationWithMets(publication);
    }

    private void enrichPublicationWithMets(Publication publication) {
        Path ndkDir = Path.of(ndkPath);

        try (Stream<Path> publicationNdkDirs = Files.list(ndkDir)) {
            List<Path> matchingDirs = publicationNdkDirs
                    .filter(publicationNdkDir -> Files.isDirectory(publicationNdkDir)
                            && publicationNdkDir.getFileName().toString().equals(publication.getId().substring(5)))
                    .collect(Collectors.toList());

            if (matchingDirs.size() != 1) {
                throw new IllegalStateException("NDK directory with id=\"" + publication.getId() + "\" not found");
            }

            publication.setNdkDir(matchingDirs.get(0));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void enrichPublicationWithTeiHeader(Publication publication) {
        publication.setTeiHeader(teiConnector.convertToTeiHeader(publication));
    }

    private void enrichPublicationWithMods(Publication publication) {
        try {
            ModsWrapper modsMetadataAdapter = new ModsWrapper(streamProvider.getMods(publication.getId()));
            publication.setModsMetadata(modsMetadataAdapter.getTransformedMods());
        } catch (KrameriusException exception) {
            throw new EnrichingException(KRAMERIUS_ERROR, exception);
        }
    }

    @Autowired
    public void setNdkPath(@Value("${system.enrichment.ndk.path}") String path) {
        this.ndkPath = path;
    }
}
