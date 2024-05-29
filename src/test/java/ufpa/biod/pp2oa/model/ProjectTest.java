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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProjectTest {
    Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
    }

    @Test
    void testAddProjectTool() {

    }

    @Test
    void testBuilder() {

    }

    @Test
    void testCanEqual() {

    }

    @Test
    void testEquals() {

    }

    @Test
    void testGetActiveTool() {

    }

    @Test
    void testGetChatId() {
        // project.setChatId(123456789L);
        Optional<Long> chatId = project.getChatId();
        System.out.println(chatId.isPresent() ? chatId.get() : "null");
    }

    @Test
    void testGetDownloadMode() {

    }

    @Test
    void testGetId() {

    }

    @Test
    void testGetName() {

    }

    @Test
    void testGetPid() {

    }

    @Test
    void testGetProjectStatus() {

    }

    @Test
    void testGetProjectTools() {

    }

    @Test
    void testHashCode() {

    }

    @Test
    void testRemoveProjectTool() {

    }

    @Test
    void testSetActiveTool() {

    }

    @Test
    void testSetChatId() {

    }

    @Test
    void testSetDownloadMode() {

    }

    @Test
    void testSetId() {

    }

    @Test
    void testSetName() {

    }

    @Test
    void testSetPid() {

    }

    @Test
    void testSetProjectStatus() {

    }

    @Test
    void testSetProjectTools() {

    }

    @Test
    void testToString() {

    }

    @Test
    void testGetParameter() {

    }
}
