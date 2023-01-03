package cz.inqool.dl4dh.krameriusplus.core.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * ZipArchiver provides operation zip and unzip,
 * provide Path for resulting file in constructor
 *
 * @author Filip Kollar
 */
@Component
public class ZipArchiver {

    /**
     * zips directoryToZip
     *
     * filename is preserved, entry is created under the field directory
     *
     * @param source path to the directory, which should be zipped
     * @param destination path where the final zip will be placed
     * @throws Exception IOException in case of FS issues
     */
    public void zip(Path source, Path destination) throws Exception {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(destination))) {
            Files.walk(source)
                    .filter(file -> !Files.isDirectory(file))
                    .forEach(file -> {
                        ZipEntry zipEntry = new ZipEntry(source.relativize(file).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(file, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException("Error when zipping directory '" + source + "'.", e);
                        }
                    });
        }
    }

    /**
     * Unzip given file from Path
     *
     * @param source path to zip file to be unzipped
     * @param destination path to the destination directory
     * @throws IOException in case of FS issues
     */
    public void unzip(Path source, Path destination) throws IOException {
        try (InputStream is = Files.newInputStream(source)) {
            unzip(is, destination);
        }
    }

    /**
     * Unzip given file from InputStream
     *
     * @param inputStream source zip stream to unzip
     * @param destination destination of the unzipped file
     */
    public void unzip(InputStream inputStream, Path destination) throws IOException {
        File outDir = Files.createDirectory(destination).toFile();
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(outDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    /**
     * helper for unzipping
     *
     * code from <a href="https://www.baeldung.com/java-compress-and-uncompress">baeldung</a>
     * @param destinationDir directory
     * @param zipEntry one zipEntry
     * @return File to created dir
     * @throws IOException in case of FS issues
     */
    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
