package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
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
@AllArgsConstructor
public class ZipArchiver {

    private final Path resultFilePath;

    /**
     * zips directoryToZip
     *
     * filename is preserved, entry is created under the field directory
     *
     * @param directoryToZip Path to directory to be zipped
     * @throws Exception IOException in case of FS issues
     */
    public void zip(Path directoryToZip) throws Exception {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(resultFilePath))) {
            Files.walk(directoryToZip)
                    .filter(file -> !Files.isDirectory(file))
                    .forEach(file -> {
                        ZipEntry zipEntry = new ZipEntry(directoryToZip.relativize(file).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(file, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException("Error when zipping directory '" + directoryToZip + "'.", e);
                        }
                    });
        }
    }

    /**
     * unzipping method
     *
     * file name is preserved, entry is created under the field directory
     *
     * @param zipFilePath path to zip file to be unzipped
     * @throws IOException in case of FS issues
     */
    public void unzip(Path zipFilePath, String publicationDirName) throws IOException {
        try (InputStream is = Files.newInputStream(zipFilePath)) {
            unZip(is, publicationDirName);
        }
    }

    /**
     * code from <a href="https://www.baeldung.com/java-compress-and-uncompress">baeldung</a>
     *
     * @param zipFileIs input stream of file to unzip
     * @throws IOException in case of FS issues
     */
    private void unZip(InputStream zipFileIs, String dirName) throws IOException {
        File outDir = Files.createDirectory(Path.of(resultFilePath.toString(), dirName)).toFile();
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(zipFileIs);
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
