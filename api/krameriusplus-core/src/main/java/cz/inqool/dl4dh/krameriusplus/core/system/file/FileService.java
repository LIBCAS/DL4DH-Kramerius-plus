package cz.inqool.dl4dh.krameriusplus.core.system.file;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.params.Paging;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;
import static java.nio.file.Files.*;

/**
 * File storage manager.
 * <p>
 * Inserted files are stored in file system in configured directory.
 */
@Service
@Slf4j
public class FileService {

    private final FileRefStore store;

    /**
     * Return the level of file hierarchy (number of sub-directories to be created) to be used in storage. The value
     * must be between 1-16.
     */
    @Getter
    private int hierarchicalLevel;

    /**
     * Returns the path to a directory where all files are stored.
     */
    @Getter
    private String basePath;

    @Autowired
    public FileService(FileRefStore store) {
        this.store = store;
    }

    public void cleanUp() {
        wipeStrayFiles();

        List<FileRef> files = listAndInitialize();

        files.forEach(file -> {
            if (file.getCreated().plus(1, ChronoUnit.DAYS).isBefore(Instant.now())) {
                delete(file);
            }
        });
    }

    private List<FileRef> listAndInitialize() {
        List<FileRef> files = list();

        files.forEach(file -> file.setBasePath(getBasePath()));

        return files;
    }

    /**
     * Initialize configured file system storage. Ensures, that the storage folders on the path returned by {@link
     * #getBasePath()} are created.
     */
    @PostConstruct
    public void init() {
        int hierarchicalLevel = getHierarchicalLevel();
        if (hierarchicalLevel < 0 || hierarchicalLevel > 16) {
            throw new IllegalStateException("Hierarchical level has to be a number between 1-16");
        }
        if (getFileSizeLimit() < 0L) {
            throw new IllegalStateException("File size limit has to be a positive number.");
        }

        Path folder = Paths.get(getBasePath());

        if (exists(folder)) {
            if (!isDirectory(folder)) {
                throw new IllegalArgumentException("Path '" + getBasePath() + "' not a folder");
            }
        } else {
            try {
                createDirectories(folder);
            } catch (IOException e) {
                log.error("Error creating directories");
            }
        }
    }

    /**
     * Find {@link FileRef} in database by ID.
     *
     * @param id ID of the file
     * @return {@link FileRef} with given ID
     */
    @Transactional
    public FileRef find(@NonNull String id) {
        FileRef fileRef = store.find(id);
        notNull(fileRef, () -> new MissingObjectException(FileRef.class, id));

        fileRef.setBasePath(getBasePath());

        if (isRegularFile(fileRef.getPath())) {
            return fileRef;
        } else {
            throw new IllegalArgumentException("FileRef not found");
        }
    }

    public List<FileRef> list() {
        return store.listAll();
    }

    /**
     * Saves a file.
     *
     * <p>
     * Failure to index the content will not produce exception.
     * </p>
     *
     * @return newly created {@link FileRef}
     */
    @Transactional
    public FileRef create(InputStream stream, long size, String name, String contentType) {
        notNull(stream, () -> new IllegalArgumentException("Stream cannot be null"));
        notNull(contentType, () -> new IllegalArgumentException("ContentType cannot be null"));
        notNull(name, () -> new IllegalArgumentException("Name cannot be null"));


        FileRef fileRef = new FileRef();
        fileRef.setName(name);
        fileRef.setContentType(contentType);
        fileRef.setBasePath(getBasePath());
        fileRef.setHierarchicalLevel(getHierarchicalLevel());
        fileRef.setSize(size);

        try {
            Path path = fileRef.getPath();
            createDirectories(path.getParent());
            copy(stream, path);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create file", exception);
        }

        FileRef created = store.create(fileRef);

        log.info("Created file " + fileRef.getName());

        created.setBasePath(getBasePath());
        return created;
    }

    /**
     * Deletes a file.
     *
     * @param fileRef file to be deleted
     */
    @Transactional
    public void delete(@NonNull FileRef fileRef) {
        Path path = fileRef.getPath();
        if (!exists(path) || isDirectory(path)) {
            throw new IllegalArgumentException("FileRef not found");
        }

        store.delete(fileRef);

        if (exists(fileRef.getPath())) {
            try {
                Files.delete(fileRef.getPath());
            } catch (IOException exception) {
                throw new IllegalStateException("Could not delete file " + fileRef);
            }
        } else {
            log.warn("File {} not found.", fileRef.getPath());
        }
    }

    /**
     * Debug / maintenance function. Deletes any files that for some reason remain in store folder but are no longer
     * referenced from any database record.
     */
    public void wipeStrayFiles() {
        int page = 0;
        int pageSize = 1000;

        Set<String> existingFileIDs = new HashSet<>();

        List<FileRef> fileRefs;
        do {
            Paging paging = new Paging();
            paging.setPage(page++);
            paging.setPageSize(pageSize);

            Params params = new Params();
            params.setPaging(paging);

            fileRefs = store.listAll(params).getContent();
            fileRefs.forEach(fileRef -> existingFileIDs.add(fileRef.getId()));
        } while (fileRefs.size() == pageSize);

        wipeStrayFiles(Paths.get(getBasePath()), existingFileIDs);
    }

    private void wipeStrayFiles(Path parentPath, Set<String> existingIds) {
        try {
            Files.list(parentPath).forEach(path -> {
                        if (Files.isDirectory(path)) {
                            wipeStrayFiles(path, existingIds);
                        } else if (Files.isRegularFile(path)) {
                            String fileName = path.getFileName().toString();
                            if (!existingIds.contains(fileName)) {
                                try {
                                    Files.delete(path);
                                } catch (IOException exception) {
                                    log.error("Error deleting file " + path);
                                }
                                log.info("Deleted {}", path);
                            }
                        }
                    }
            );
        } catch (IOException exception) {
            log.error("Error opening directory");
        }
    }

    /**
     * Sets the upper limit of file size to be able to upload. Subclasses can configure this limit using configuration
     * property injection.
     */
    protected long getFileSizeLimit() {
        return Long.MAX_VALUE;
    }

    @Autowired
    public void setHierarchicalLevel(@Value("${file.storage.default.hierarchical-level:0}") int hierarchicalLevel) {
        this.hierarchicalLevel = hierarchicalLevel;
    }

    @Autowired
    public void setBasePath(@Value("${file.storage.default.path:data}") String basePath) {
        this.basePath = basePath;
    }
}
