package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.mets.MetsFileFinder;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.publication.xml.dto.MainMetsDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@StepScope
@Slf4j
public class PreparePagesNdkProcessor implements ItemProcessor<Page, Page> {

    private final MetsFileFinder metsFileFinder;

    private final MongoOperations mongoOperations;

    private Path publicationNdkPath;

    private Path pageMetsDir;

    private List<MainMetsDto.Div> mainMetsPages;

    @Autowired
    public PreparePagesNdkProcessor(MetsFileFinder metsFileFinder,
                                    @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId,
                                    MongoOperations mongoOperations) {
        this.metsFileFinder = metsFileFinder;
        this.mongoOperations = mongoOperations;
        initialize(publicationId);
    }

    @Override
    public Page process(@NonNull Page page) {
        Optional<MainMetsDto.Div> matchingDiv = mainMetsPages
                .stream()
                .filter(div -> div.getOrderLabel().equalsIgnoreCase(page.getTitle()))
                .findAny();

        if (matchingDiv.isPresent()) {
            Optional<MainMetsDto.DivChild> pageMetsElement = matchingDiv.get().getChildren()
                    .stream()
                    .filter(element -> element.getFileId().substring(0, 3).equalsIgnoreCase("amd"))
                    .findAny();

            if (pageMetsElement.isPresent()) {
                try (Stream<Path> pageMetsFiles = Files.list(pageMetsDir)) {
                    // get FILEID attribute value from mainMets matching the current page,
                    // which should help us identify the correct file in /amdSec folder corresponding to
                    // the current page
                    //
                    // FILEID can be in formats:
                    // - "AMD_0008" - should match - should match "AMD_METS_c9a53a10-d9a8-11e5-a3ff-001018b5eb5c_0008.xml"
                    // - "amd_mets_aba007-0005a1_0006" - should match "amd_mets_aba007-0005a1_0006.xml"
                    // current strategy: look for files which start with "amd_mets_*" and end with the last 8 chars
                    // from the FILEID (e.g. "0006.xml")
                    String matchingFileId = pageMetsElement.get().getFileId();

                    String matchSuffix = matchingFileId.substring(matchingFileId.length() - 4) + ".xml";
                    Optional<Path> matchingPageMetsFile = pageMetsFiles
                            .filter(file -> file.getFileName().toString().substring(0, 3).equalsIgnoreCase("amd"))
                            .filter(file -> file.endsWith(matchSuffix))
                            .findFirst();

                    if (matchingPageMetsFile.isPresent()) {
                        page.setNdkFilePath(matchingPageMetsFile.get().toAbsolutePath().toString());
                        return page;
                    }

                    log.warn("Could not find mets file for page " + page.getId());
                } catch (IOException e) {
                    throw new UncheckedIOException("Could not list files in dir '" + pageMetsDir + "'", e);
                }
            }
        }
        return null;
    }

    public void initialize(String publicationId) {
        if (publicationNdkPath == null) {
            publicationNdkPath = fetchPublicationNdkPath(publicationId);
        }

        if (mainMetsPages == null) {
            Path mainMets = metsFileFinder.findMainMetsFile(publicationNdkPath);
            mainMetsPages = metsFileFinder.getMetsDivElements(mainMets);

            pageMetsDir = publicationNdkPath.resolve("amdsec");
            if (!Files.exists(pageMetsDir)) {
                pageMetsDir = publicationNdkPath.resolve("amdSec");
            }

            if (!Files.exists(pageMetsDir)) {
                throw new IllegalStateException("Could not find /amdSec folder");
            }
        }
    }

    private Path fetchPublicationNdkPath(String publicationId) {
        Query query = query(where("_id").is(publicationId));
        query.fields().include("ndkDirPath");

        Publication publication = Objects.requireNonNull(mongoOperations.findOne(query, Publication.class));

        return Path.of(publication.getNdkDirPath());
    }
}
