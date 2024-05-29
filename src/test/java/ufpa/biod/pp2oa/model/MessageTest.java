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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {
    Message message;

    @BeforeEach
    void setUp() {
        message = new Message();
    }

    @Test
    void testAddParameter() {
        message.addParameter(ParameterType.AUTHORIZED, true);
        // Verify that the parameter was added in the map containing the parameters
        assertEquals(1, message.getParameter().size());
        // Verify that the parameter was added in the map containing the parameters
        assertEquals(true, message.getParameter().containsKey(ParameterType.AUTHORIZED));
    }

    @Test
    void testAddParameterWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> message.addParameter(ParameterType.AUTHORIZED, null),
                "Parameter value cannot be null");
    }

    @Test
    void testGetParameter() {
        message.addParameter(ParameterType.AUTHORIZED, true);
        // Verify that the parameter contains the correct value
        assertEquals(true, message.getParameter(ParameterType.AUTHORIZED));
        // Verify that the parameter as casted to the correct type
        assertEquals(true, message.getParameter(ParameterType.AUTHORIZED) instanceof Boolean);
    }

    @Test
    void testGetParameterWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> message.getParameter(ParameterType.TOOL_LIST),
                "Parameter not found: " + ParameterType.TOOL_LIST);
    }
}
