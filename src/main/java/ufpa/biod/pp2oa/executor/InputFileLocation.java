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
import java.util.List;
import java.util.stream.Collectors;

import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.ParameterType;

public class InputFileLocation {

    private InputFileLocation() {
    }

    /**
     * Get the location of the input files considering the project and tool name
     * 
     * @param message message
     * @return list of files with the location
     */
    public static List<File> getFileLocation(Message message) {
        ProjectDto projectDto = message.getParameter(ParameterType.PROJECT);
        List<File> files = message.getParameter(ParameterType.FILE_LIST);
        return files.stream().map(file -> new File(
                "inputs/" + projectDto.getName() + "/" +
                        projectDto.getCurrentTool().getName() + "/" + file.getName()))
                .collect(Collectors.toList());
    }
}
