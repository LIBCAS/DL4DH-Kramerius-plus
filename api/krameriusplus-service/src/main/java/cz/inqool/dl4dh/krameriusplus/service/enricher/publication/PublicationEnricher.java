package cz.inqool.dl4dh.krameriusplus.service.enricher.publication;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.metadata.ModsWrapper;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.MetsFileFinder;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PublicationEnricher {

    private final TeiConnector teiConnector;
    private final StreamProvider streamProvider;
    private final MetsFileFinder metsFileFinder;
    private String ndkPath;

    @Autowired
    public PublicationEnricher(TeiConnector teiConnector, StreamProvider streamProvider, MetsFileFinder metsFileFinder) {
        this.teiConnector = teiConnector;
        this.streamProvider = streamProvider;
        this.metsFileFinder = metsFileFinder;
    }

    public void enrich(Publication publication) {
        enrichPublicationWithMods(publication);
        enrichPublicationWithTeiHeader(publication);
        setMainMetsPath(publication);
    }

    private void setMainMetsPath(Publication publication) {
        Path ndkDir = Path.of(ndkPath);

        try (Stream<Path> publicationNdkDirs = Files.list(ndkDir)) {
            List<Path> matchingDirs = publicationNdkDirs
                    .filter(publicationNdkDir -> Files.isDirectory(publicationNdkDir)
                            && publicationNdkDir.getFileName().toString().equals(publication.getId().substring(5)))
                    .collect(Collectors.toList());

            if (matchingDirs.size() != 1) {
                log.warn("NDK directory with id=\"" + publication.getId() + "\" not found");
                return;
            }

            Path publicationMetsDir = matchingDirs.get(0);
            publication.setNdkDir(publicationMetsDir);

            if (publication instanceof PagesAware) {
                PagesAware publicationWithPages = (PagesAware) publication;
                metsFileFinder.setMetsPathForPages(publicationWithPages.getPages(), publicationMetsDir);
            }
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
