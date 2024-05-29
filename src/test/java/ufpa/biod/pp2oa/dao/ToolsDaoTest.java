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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ufpa.biod.pp2oa.dao.Factory.ToolFactory;
import ufpa.biod.pp2oa.model.Tool;

public class ToolsDaoTest {
    ToolDao toolsDao;
    Tool tool;

    @BeforeEach
    public void setUp() {
        toolsDao = new ToolDao();
        tool = ToolFactory.getTool();
    }

    @AfterAll
    public static void suiteSetUp() {
        ToolDao toolsDao = new ToolDao();
        toolsDao.findAll().forEach(toolsDao::delete);
    }

    @Test
    public void testSave() {
        toolsDao.save(tool);
        Assertions.assertNotNull(tool.getId());

    }

    @Test
    public void update() {
        toolsDao.save(tool);
        Assertions.assertEquals("Tool Teste", toolsDao.find(tool).getName());
        tool.setName("Tool Teste 2");
        toolsDao.save(tool);
        Assertions.assertEquals("Tool Teste 2", toolsDao.find(tool).getName());
    }

    @Test
    public void testFind() {
        toolsDao.save(tool);
        Tool find = toolsDao.find(tool);
        Assertions.assertEquals(tool, find);

    }

    @Test
    public void testFindId() {
        toolsDao.save(tool);
        Tool find = toolsDao.find(tool.getId());
        Assertions.assertEquals(tool, find);

    }

    @Test
    public void testDelete() {
        toolsDao.save(tool);
        toolsDao.delete(tool);
        Assertions.assertNull(toolsDao.find(tool));

    }

}
