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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ufpa.biod.pp2oa.model.Parameter;
import ufpa.biod.pp2oa.model.Project;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * This class is responsible for building the command to be executed by the
 * process executor. <br>
 * <br>
 * It receives a {@link Project} object and lists of {@link File} objects
 * representing the input and reference
 * files for the process. <br>
 * <br>
 * It then builds the command by appending the executable path and replacing the
 * placeholders in
 * the command string with the file paths.
 * 
 */
@Slf4j
public class MountExecutorCommand {

    /**
     * 
     * Private constructor to prevent instantiation of this utility class.
     */
    private MountExecutorCommand() {
    }

    /**
     * 
     * Builds the command array to be executed by the process executor.
     * 
     * @param project    the {@link Project} object containing the information about
     *                   the process to be executed
     * 
     * @param files      the list of {@link File} objects representing the input
     *                   files for execution of tool
     * 
     * @param referFiles the list of {@link File} objects representing the reference
     *                   files for execution of tool
     * 
     * @return the command array to be executed
     */
    public static String[] build(Project project, List<File> files, List<File> referFiles) {
        String currentWorkingDir = System.getProperty("user.dir");

        List<String> command = new ArrayList<>();
        command.add(project.getCurrentTool().getExecutablePath());
        List<Parameter> parameters = project.getParameters();

        // Add all parameters to command
        for (Parameter parameter : parameters) {
            command.add(parameter.getName());
            if (parameter.getValue() != null && !parameter.getValue().isBlank() && !parameter.getValue().isEmpty()) {
                command.add(parameter.getValue());
            }
        }

        // Replace "%output%" placeholder with output directory path and use forward
        // slashes.
        String finalParameter = String.join(" ", command)
                .replace("%firstInputDir%",
                        "inputs/" + project.getName() + "/" + project.getFirstTool().getName() + "/")
                .replace("%currentInputDir%", project.getInputDirectoryPath().getPath().replace("\\", "/"))
                .replace("%currentOutputDir%", project.getOutputDirectoryPath().getPath().replace("\\", "/"))
                .replace("%pwd%", currentWorkingDir);

        // Filter out files that do not exist. This is useful in cases where a tool may
        // or may not generate a file, e.g. SPAdes may or may not generate scaffolds.
        files = files.stream().filter(File::exists).collect(Collectors.toList());

        // Replace "%file%", "%previousOutputDir%" placeholders with file paths
        // and replace backslashes with forward slashes.
        for (File file : files) {
            try {
                finalParameter = finalParameter.replaceFirst("%file%", file.toString().replace("\\", "/"))
                        .replace("%previousOutputDir%", file.getCanonicalFile().getParentFile().toString().replace("\\", "/"));
            } catch (IOException e) {
                log.error("Error while replacing %file% and %previousOutputDir% in command", e);
            }
        }

        // Replace "%refer%" placeholder with reference file paths
        // and replace backslashes with forward slashes.
        for (File file : referFiles) {
            try {
                finalParameter = finalParameter.replace("%refer%", file.toString().replace("\\", "/"));
            } catch (Exception e) {
                log.error("Error while replacing %refer% in command", e);
            }
        }

        return finalParameter.split("\\s+");
    }

}
