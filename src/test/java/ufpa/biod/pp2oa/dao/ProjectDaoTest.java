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
package ufpa.biod.pp2oa.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ufpa.biod.pp2oa.dao.Factory.ProjectFactory;
import ufpa.biod.pp2oa.model.Project;

public class ProjectDaoTest {

    private static ProjectDao projectDao;
    private static ToolDao toolsDao;
    private Project project;

    @BeforeAll
    public static void suiteSetUp() {
        toolsDao = new ToolDao();
        projectDao = new ProjectDao();
    }

    // Setup and Teardown
    @BeforeEach
    public void setUp() {
        project = ProjectFactory.getProject();
        // project.getTool().forEach(toolsDao::save);
    }

    @AfterAll
    public static void suitTearDown() {
        new ProjectDao().findAll().forEach(projectDao::delete);
        new ToolDao().findAll().forEach(toolsDao::delete);
    }

    @Test
    public void testSave() {
        projectDao.save(project);
        Assertions.assertNotNull(project.getId());
    }

    @Test
    public void testUpdate() {
        projectDao.save(project);
        Assertions.assertEquals("Projeto Teste", projectDao.find(project).getName());
        project.setName("Projeto Teste 2");
        projectDao.save(project);
        Assertions.assertEquals("Projeto Teste 2",
                projectDao.find(project).getName());
    }

    @Test
    public void testFindById() {
        projectDao.save(project);
        Project find = projectDao.find(project.getId());
        Assertions.assertEquals(project, find);
    }

    @Test
    public void testFind() {
        projectDao.save(project);
        Project find = projectDao.find(project);
        Assertions.assertEquals(project, find);
    }

    @Test
    public void testDelete() {
        projectDao.save(project);
        Project find = projectDao.find(project);
        projectDao.delete(find);
        Assertions.assertNull(projectDao.find(project));
    }

    @Test
    public void testRemoveToolFromProject() {
        Project project = projectDao.find(1);
        project.removeProjectTool(project.getProjectTools().get(0));
        projectDao.save(project);
    }
}
