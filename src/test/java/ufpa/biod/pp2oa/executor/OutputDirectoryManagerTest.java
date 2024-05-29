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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ufpa.biod.pp2oa.dao.ProjectDao;
import ufpa.biod.pp2oa.dao.ToolDao;
import ufpa.biod.pp2oa.model.Project;
import ufpa.biod.pp2oa.model.Tool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutputDirectoryManagerTest {
    private static Project project;

    @BeforeAll
    static void setUp() {
        project = new ProjectDao().find(1);
        Tool ngsreads = new ToolDao().find(3);
        project.setCurrentTool(ngsreads);
    }

    @Test
    void testDeleteDirectory() {
        try {
            OutputDirectoryManager.deleteDirectory(project.getOutputDirectoryPath().toPath());
        } catch (IOException e) {
            log.error("Error preparing output directory", e);
        }
        assertFalse(project.getOutputDirectoryPath().exists());
    }

    @Test
    void testPrepareOutputDirectory() {
        try {
            OutputDirectoryManager.prepareOutputDirectory(project);
        } catch (IOException e) {
            log.error("Error preparing output directory", e);
        }
        assertTrue(project.getOutputDirectoryPath().exists());

    }
}
