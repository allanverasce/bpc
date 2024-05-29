/*
 * Copyright (C) 2023 BIOD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ufpa.biod.pp2oa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile {

    private ZipFile() {
    }

    /**
     * Zip a file or directory
     * 
     * @param zipArchive file or directory to be zipped
     * @return the zipped file
     */
    public static File zip(File zipArchive) {
        Path fileNameZipped = Paths.get(zipArchive + ".zip");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(fileNameZipped))) {
            if (zipArchive.isDirectory()) {
                zipDirectory(zipArchive, zip, null);
            } else {
                zipFile(zipArchive, zip);
            }
            zip.closeEntry();
            return fileNameZipped.toFile();
        } catch (IOException ex) {
            Logger.getLogger(ZipFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Zip a file
     * 
     * @param zipFile file to be zipped
     * @param zip     zip output stream
     */
    public static void zipFile(File zipFile, ZipOutputStream zip) {
        try (FileInputStream fileRead = new FileInputStream(zipFile)) {
            byte[] readAllBytes = fileRead.readAllBytes();
            ZipEntry zipEntry = new ZipEntry(zipFile.getName());
            zip.putNextEntry(zipEntry);
            zip.write(readAllBytes);

        } catch (IOException ex) {
            Logger.getLogger(ZipFile.class
                    .getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Zip a directory
     * 
     * @param zipDirectory    directory to be zipped
     * @param zip             zip output stream
     * @param directoryPrefix directory prefix
     */
    public static void zipDirectory(File zipDirectory, ZipOutputStream zip, String directoryPrefix) {
        if (directoryPrefix != null) {
            directoryPrefix += "/";
        } else {
            directoryPrefix = "";
        }
        for (File file : zipDirectory.listFiles()) {
            if (file.isDirectory()) {
                zipDirectory(file, zip, directoryPrefix + file.getName());
            } else {
                try (FileInputStream fileRead = new FileInputStream(file)) {
                    byte[] readAllBytes = fileRead.readAllBytes();
                    ZipEntry zipEntry = new ZipEntry(directoryPrefix + file.getName());
                    zip.putNextEntry(zipEntry);
                    zip.write(readAllBytes);

                } catch (IOException ex) {
                    Logger.getLogger(ZipFile.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
