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
package ufpa.biod.pp2oa.executor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import ufpa.biod.pp2oa.model.Project;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * This class is responsible for managing the output directory of the process.
 * It has methods for creating, deleting and checking the existence of result
 * files in the output directory.
 */
@Slf4j
public class OutputDirectoryManager {

    /**
     * 
     * Private constructor to prevent instantiation of this utility class.
     */
    private OutputDirectoryManager() {
    }

    /**
     * Prepares the output directory for a given project. If the output directory
     * already exists, it is deleted.
     * If the current tool for the project specifies that the output directory
     * should be created, it is created.
     *
     * @param project the project to prepare the output directory for
     * @throws IOException if an error occurs while deleting the output directory
     */
    public static void prepareOutputDirectory(Project project) throws IOException {
        log.info("Preparing output directory for tool " + project.getCurrentTool().getName());
        File outputDirectory = project.getOutputDirectoryPath();
        // Delete Tool.zip file if it exists
        Files.deleteIfExists(
                new File(outputDirectory.getParent() + "/" + project.getCurrentTool().getName() + ".zip").toPath());

        if (outputDirectory.exists()) {
            log.info("Output directory already exists, deleting it");
            deleteDirectory(outputDirectory.toPath());
        }

        if (project.getCurrentTool().isShouldCreateOutputDirectory()) {
            outputDirectory.mkdirs();
        }
        log.info("Output directory prepared");
    }

    /**
     * Deletes a directory and all its contents.
     *
     * @param directory the directory to delete
     * @throws IOException if an error occurs while deleting the directory
     */
    public static void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
