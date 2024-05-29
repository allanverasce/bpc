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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.executor.Executor;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Project;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * The {@code FileUtils} class is responsible for managing file operations, such
 * as writing and
 * reading from input and output streams,
 * 
 * as well as deleting directories.
 */

@Slf4j
public class FileUtils {
    static final int SIZEBUFFER = 1024 * 1024 * 16;

    private FileUtils() {
    }

    /**
     * 
     * This method receives an {@link ObjectInputStream} and a {@link Message} as
     * input, gets the
     * 
     * {@link Project} and {@link File} list from the message, and writes the files
     * to the
     * 
     * project's directory. If the directory does not exist, it is created. If it
     * exists, it is
     * 
     * deleted before creating it again.
     * 
     * @param input   the {@link ObjectInputStream} to read the files from
     * 
     * @param message the {@link Message} containing the project and file list
     */
    public static void mountFile(ObjectInputStream input, Message message) {
        ProjectDto projectDto = message.getParameter(ParameterType.PROJECT);
        Project project = new ModelMapper().map(projectDto, Project.class);

        File directory = project.getInputDirectoryPath();

        ArrayList<File> files = message.getParameter(ParameterType.FILE_LIST);
        if (!directory.exists()) {
            deleteDir(directory);
        }

        directory.mkdirs();
        files.forEach(file1 -> writeFile(input, new File(directory.toString() + "/" + file1.getName())));

    }

    /**
     * 
     * This method receives an {@link ObjectInputStream} and a {@link File} as input
     * and writes the
     * contents of the input stream to the file.
     * 
     * @param input the {@link ObjectInputStream} to read the file contents from
     * @param file  the {@link File} to write the contents to
     */
    public static void writeFile(ObjectInputStream input, File file) {
        try {
            long size = input.readLong();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                log.info("Receiving file: {}", file.getName());
                // Informar o tamanho do arquivo e em megabytes
                log.info("File size: {} bytes", size);
                byte[] buffer = new byte[Optional.ofNullable(input.readInt()).orElse(SIZEBUFFER)];
                log.info("Buffer size: {} bytes", buffer.length);
                int read = 0;
                while (size > 0
                        && (read = bufferedInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                    size -= read;
                }
            }
        } catch (IOException ex) {
            log.error("Error to write file", ex);
        }
    }

    /**
     * 
     * Sends a file over a ObjectOutputStream.
     * 
     * @param objectOutputStream the output stream to write the file
     * 
     * @param file               the file to be sent
     * 
     * @throws IOException if an I/O error occurs while writing to the output stream
     */
    public static void sendFile(ObjectOutputStream objectOutputStream, File file) {
        try {
            long size = file.length();
            objectOutputStream.writeLong(size);
            objectOutputStream.flush();
            objectOutputStream.writeInt(SIZEBUFFER);

            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[SIZEBUFFER];
                int read;
                log.info("Sending file: {}", file.getName());
                log.info("File size: {}", size);

                while (size > 0
                        && (read = bufferedInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                    objectOutputStream.write(buffer, 0, read);
                    objectOutputStream.flush();
                    size -= read;
                }
                objectOutputStream.flush();
            }

        } catch (IOException ex) {
            log.error("Error to send file", ex);
        }

    }

    /**
     * 
     * Deletes a directory and its contents.
     * 
     * @param file the directory to delete
     */
    private static void deleteDir(File file) {
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (IOException ex) {
            log.error("Error to delete directory", ex);
        }
    }

    /**
     * Checks if the given directory is valid.
     *
     * @param directory The directory to check.
     * @return true if the directory is valid, false otherwise.
     */
    public static boolean isValidDirectory(File directory) {
        return directory != null && directory.isDirectory();
    }

    /**
     * Moves a single file to the specified directory.
     *
     * @param file                 The file to move.
     * @param destinationDirectory The directory to move the file to.
     */
    public static void moveFile(File file, File destinationDirectory) {
        Path targetPath = destinationDirectory.toPath().resolve(file.getName());
        try {
            Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Moved file: " + file.getName() + " to " + destinationDirectory);
        } catch (IOException e) {
            log.error("Error moving file: " + file.getName(), e);
        }
    }

    /**
     * Determines if a file is eligible to be moved.
     *
     * @param file The file to check.
     * @return true if the file is a non-directory, non-ZIP file, false otherwise.
     */
    public static boolean isNotZipFile(File file) {
        return file.isFile() && !file.getName().endsWith(".zip");
    }

}
