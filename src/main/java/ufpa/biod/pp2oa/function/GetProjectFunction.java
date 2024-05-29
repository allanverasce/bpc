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

import org.modelmapper.ModelMapper;

import ufpa.biod.pp2oa.dao.ProjectDao;
import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.MessageStatus;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Project;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetProjectFunction implements CallableFunction {

    /**
     * 
     * The execute method of {@link GetProjectFunction} retrieves a
     * {@link Project} from the database and
     * sets it as a parameter in the {@link Message}. If an exception occurs during
     * this process, the status of the {@link Message} is set to
     * {@link MessageStatus#ERROR}.
     */
    @Override
    public void execute(FunctionContext context) {
        Message message = context.getMessage();
        try {

            ProjectDto projectDto = message.getParameter(ParameterType.PROJECT);
            Project project = new ModelMapper().map(projectDto, Project.class);
            log.info("Start to get project");

            // message = new Message();
            Project updatedProject = new ProjectDao().find(project);
            ProjectDto updatedProjectDto = new ModelMapper().map(updatedProject, ProjectDto.class);

            message.addParameter(ParameterType.PROJECT, updatedProjectDto);
            message.setMessageStatus(MessageStatus.PROCESSED);
            log.info("End to get project");
        } catch (Exception e) {
            log.error("Error to get project", e);
            message.setMessageStatus(MessageStatus.ERROR);
        }

    }

}
