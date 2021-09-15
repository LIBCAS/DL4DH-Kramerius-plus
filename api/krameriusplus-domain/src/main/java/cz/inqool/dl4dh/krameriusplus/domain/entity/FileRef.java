//package cz.inqool.dl4dh.krameriusplus.domain.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.Setter;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.Transient;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.io.Closeable;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.time.Instant;
//
//import static java.nio.file.Files.newInputStream;
//
///**
// * Reference to a file stored in file system.
// * <p>
// * Should be created only through {@link FileService} and not cascaded from another entity.
// */
//@Getter
//@Setter
//@Document(collection = "files")
//public class FileRef extends DatedObject implements Closeable {
//
//    @CreatedDate
//    protected Instant created;
//
//    private static final int DIR_NAME_LENGTH = 2;
//
//    /**
//     * Filename
//     */
//    private String name;
//
//    /**
//     * MIME type
//     */
//    private String contentType;
//
//    /**
//     * Size of the file content (in bytes)
//     */
//    private Long size;
//
//    /**
//     * Path to the storage base directory. Initialized only during fetching in {@link FileService}
//     */
//    @JsonIgnore
//    @Transient
//    private String basePath;
//
//    /**
//     * Opened stream to read file content ({@code null} if the file was not opened yet)
//     */
//    @JsonIgnore
//    @Transient
//    private InputStream stream;
//
//
//    /**
//     * Get path to this file in corresponding local file system. This instance must be obtained via {@link FileService}
//     * or an exception is thrown.
//     */
//    @JsonIgnore
//    @Transient
//    public Path getPath() {
//        if (basePath == null) {
//            throw new IllegalStateException("FileRef not initialized");
//        }
//
//        return computeFilePath(basePath, id, hierarchicalLevel);
//    }
//
//
//    /**
//     * Return {@code true} if this file is opened (meaning that corresponding input stream is initialized), {@code
//     * false} otherwise.
//     */
//    @JsonIgnore
//    @Transient
//    public boolean isOpened() {
//        return stream != null;
//    }
//
//    /**
//     * Open this file.
//     *
//     * @return initialized input stream ready to be read from
//     * @throws IllegalStateException if this file is already opened
//     */
//    public InputStream open() {
//        if (stream != null) {
//            throw new IllegalStateException("File is already opened.");
//        }
//
//        try {
//            stream = newInputStream(getPath(), StandardOpenOption.READ);
//        } catch (IOException exception) {
//            throw new IllegalStateException("Failed to open file");
//        }
//
//        return stream;
//    }
//
//    /**
//     * Close this file.
//     */
//    public void close() {
//        if (stream != null) {
//            try {
//                stream.close();
//            } catch (IOException exception) {
//                throw new IllegalStateException("Failed to close file");
//            }
//            stream = null;
//        }
//    }
//
//    private static Path computeFilePath(@NonNull String basePath, @NonNull String id, int level) {
//        String[] path = new String[level + 1];
//        path[level] = id;
//
//        String uuid = id.replaceAll("-", "");
//        for (int i = 0; i < level; i++) {
//            path[i] = uuid.substring(i * DIR_NAME_LENGTH, i * DIR_NAME_LENGTH + DIR_NAME_LENGTH);
//        }
//
//        return Paths.get(basePath, path);
//    }
//}
