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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;

import lombok.extern.slf4j.Slf4j;
import ufpa.biod.pp2oa.dao.ProjectDao;
import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.model.ExecutionStatus;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Project;
import ufpa.biod.pp2oa.model.ProjectTool;
import ufpa.biod.pp2oa.utils.FileUtils;

/**
 * 
 * The {@code Executor} class is responsible for executing the command to run a
 * tool and processing the output files.
 */
@Slf4j
public class Executor {

    /**
     * 
     * Private constructor to prevent instantiation of this utility class.
     */
    private Executor() {
    }

    /**
     * 
     * Executes the command to run a tool, processes the output files and updates
     * the project status.
     * 
     * @param objectOutputStream the object output stream to send the response
     *                           message
     * 
     * @param message            the request message containing the project, file
     *                           list and reference files
     * 
     * @return {@code true} if the execution was successful, {@code false} otherwise
     * 
     * @throws IOException          if an I/O error occurs while executing the
     *                              command or processing the output files
     * 
     * @throws InterruptedException if the current thread is interrupted while
     *                              waiting for the process to complete
     */
    public static void execute(Message message)
            throws IOException, InterruptedException {

        ProjectDto projectDto = message.getParameter(ParameterType.PROJECT);
        Project project = new ModelMapper().map(projectDto, Project.class);

        ProjectTool currentProjectTool = project.getProjectTools()
                .stream()
                .filter(projectTool1 -> Objects.equals(projectTool1.getTool(), project.getCurrentTool()))
                .findFirst()
                .orElse(null);

        List<File> expectedOutput = currentProjectTool.getExpectedOutputFiles();

        // List<File> files = InputFileLocation.getFileLocation(message);
        List<File> files = message.getParameter(ParameterType.FILE_LIST);

        List<File> referFiles = message.getParameter(ParameterType.REFER_FILES);

        String[] command = MountExecutorCommand.build(project, files, referFiles);

        log.info("Executing command: " + String.join(" ", command));

        OutputDirectoryManager.prepareOutputDirectory(project);

        // If the process returns a non-zero exit value, the execution failed
        try {
            ProcessExecutor.execute(command, project);
        } catch (IOException e) {
            log.error("Execution failed because the process returned a non-zero exit value");
            currentProjectTool.setStatus(ExecutionStatus.ERROR);
            new ProjectDao().save(project);
            projectDto = new ModelMapper().map(project, ProjectDto.class);

            message.addParameter(ParameterType.PROJECT, projectDto);
            throw new IOException("Execution failed because the process returned a non-zero exit value");
        }

        moveFilesToOutputDirectory(project);

        // If the expected output files do not exist, the execution failed
        // throw new FileNotFoundException("No expected output file found");
        try {
            CheckExpectedFilesExist.check(expectedOutput);
        } catch (FileNotFoundException e) {
            currentProjectTool.setStatus(ExecutionStatus.ERROR);
            new ProjectDao().save(project);
            projectDto = new ModelMapper().map(project, ProjectDto.class);

            message.addParameter(ParameterType.PROJECT, projectDto);

            log.error("Execution failed because expected output files were not found");
            log.error("Expected output files: " + expectedOutput);
            throw e;
        }

        // Set the project status to FINISHED and save the project
        currentProjectTool.setStatus(ExecutionStatus.FINISHED);
        new ProjectDao().save(project);

        // Covert Project to ProjectDto
        projectDto = new ModelMapper().map(project, ProjectDto.class);

        // Update the project in the message
        message.addParameter(ParameterType.PROJECT, projectDto);

        // Return true to indicate that the execution was successful
        log.info("Execution successful");
    }

    /**
     * Moves all files that are not directories or ZIP files to the output
     * directory.
     *
     * @param project the {@link Project} object containing the output directory
     *                path
     */
    public static void moveFilesToOutputDirectory(Project project) {
        File outputDirectory = project.getOutputDirectoryPath();
        File parentDirectory = outputDirectory.getParentFile();

        if (FileUtils.isValidDirectory(parentDirectory)) {
            moveEligibleFiles(parentDirectory, outputDirectory);
        } else {
            log.error("Invalid parent directory: " + (parentDirectory != null ? parentDirectory.getPath() : "null"));
        }
    }

    /**
     * Moves files that are not directories or ZIP files from the source to the
     * destination.
     *
     * @param sourceDirectory      The directory to move files from.
     * @param destinationDirectory The directory to move files to.
     */
    private static void moveEligibleFiles(File sourceDirectory, File destinationDirectory) {
        File[] files = sourceDirectory.listFiles();

        if (files == null) {
            log.warn("No files found in directory: " + sourceDirectory.getPath());
            return;
        }

        for (File file : files) {
            if (FileUtils.isNotZipFile(file)) {
                FileUtils.moveFile(file, destinationDirectory);
            }
        }
    }

}
