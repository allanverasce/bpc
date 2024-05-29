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
package ufpa.biod.pp2oa.function;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import ufpa.biod.pp2oa.dao.ProjectDao;
import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.MessageStatus;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Project;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetProjectListFunction implements CallableFunction {

    /**
     * 
     * The execute method of {@link GetProjectListFunction} retrieves a list of all
     * {@link Project}s from the database and
     * sets it as a parameter in the {@link Message}. If an exception occurs during
     * this process, the status of the {@link Message} is set to
     * {@link MessageStatus#ERROR}.
     */
    @Override
    public void execute(FunctionContext context) {
        Message message = context.getMessage();
        try {
            log.info("Start to get all projects");

            // message = new Message();
            List<Project> projects = new ProjectDao().findAll();

            List<ProjectDto> projectsDto = projects.stream()
                    .map(project -> new ModelMapper().map(project, ProjectDto.class))
                    .collect(Collectors.toList());

            message.addParameter(ParameterType.PROJECT_LIST, projectsDto);
            message.setMessageStatus(MessageStatus.PROCESSED);
            log.info("End to get all projects");
        } catch (Exception e) {
            log.error("Error to get all projects", e);
            message.setMessageStatus(MessageStatus.ERROR);
        }

    }

}
