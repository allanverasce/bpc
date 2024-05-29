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
package ufpa.biod.pp2oa.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ufpa.biod.pp2oa.model.DownloadMode;
import ufpa.biod.pp2oa.model.ExecutionStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "projectTools" })
@EqualsAndHashCode(of = "id")
public class ProjectDto implements Serializable {

    private static final long serialVersionUID = -5585539670805137770L;

    private Integer id;

    private String name;

    private Long chatId;

    private DownloadMode downloadMode;

    private ExecutionStatus projectStatus;

    private List<ProjectToolDto> projectTools;

    private List<PipelineDTO> pipeline;

    private ToolDto firstTool;

    private ToolDto currentTool;

    private Long pid;

    private boolean telegramBot;

    private String hash;

    public Optional<Long> getChatId() {
        return Optional.ofNullable(chatId);
    }

}
