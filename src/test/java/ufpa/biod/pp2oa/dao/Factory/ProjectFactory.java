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
package ufpa.biod.pp2oa.dao.Factory;

import ufpa.biod.pp2oa.model.Project;

public class ProjectFactory {

    public static Project getProject() {
        Project project = new Project();
        project.setName("Projeto Teste");
        project.setChatId(123456789L);
        // project.setStatus(Status.INACTIVE);
        // project.addTool(ToolFactory.getTool());
        // project.setToolRunningId(ToolFactory.getTool().getId());
        // project.setToolRunningStatus(Status.RUNNING);
        return project;
    }

}
