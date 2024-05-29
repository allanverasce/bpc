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
package ufpa.biod.pp2oa.model;

import java.io.File;
import java.util.List;

import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.dto.ToolDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ParameterType {
    PROJECT(ProjectDto.class),
    PROJECT_LIST(List.class),
    TOOL(ToolDto.class),
    TOOL_LIST(List.class),
    FILE_LIST(List.class),
    REFER_FILES(List.class),
    GENERATED_ZIP_FILE(File.class),
    PASSWORD(String.class),
    NEW_PASSWORD(String.class),
    AUTHORIZED(Boolean.class),
    PROCESS_IS_RUNNING(Boolean.class),
    LAST_RUN(Boolean.class),
    HASH(String.class);

    private final Class<?> clazz;

    public Class<?> getType() {
        return clazz;
    }
}
