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

import org.modelmapper.ModelMapper;

import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.model.DownloadMode;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Project;
import ufpa.biod.pp2oa.utils.ZipFile;

/**
 * 
 * ProcessExecutor is a utility class that is responsible for executing a
 * compress result file.
 * 
 */
public class OutputFileProcessor {

    /**
     * 
     * Private constructor to prevent instantiation of this utility class.
     */
    private OutputFileProcessor() {
    }

    /**
     * 
     * Compress the result files.
     * 
     * @param message the message
     */
    public static void compress(Message message) {
        ProjectDto projectDto = message.getParameter(ParameterType.PROJECT);
        Project project = new ModelMapper().map(projectDto, Project.class);

        File createZipFile = createZipFile(project.getOutputDirectoryPath(),
                project.getDownloadMode());

        message.addParameter(ParameterType.GENERATED_ZIP_FILE, createZipFile);

    }

    /**
     * 
     * Creates a zip file with the result files.
     * 
     * @param outputDirectory the output directory
     * @param downloadMode    the download mode
     * @return the zip file
     */
    private static File createZipFile(File outputDirectory, DownloadMode downloadMode) {
        // Determine the directory to be zipped
        File directoryToZip = outputDirectory;
        if (downloadMode == DownloadMode.FULL_RESULT) {
            directoryToZip = outputDirectory.getParentFile();
        }
        // Create the zip file
        return ZipFile.zip(directoryToZip);
    }

}
