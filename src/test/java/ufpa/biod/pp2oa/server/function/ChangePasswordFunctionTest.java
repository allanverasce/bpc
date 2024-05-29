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
package ufpa.biod.pp2oa.server.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ufpa.biod.pp2oa.model.Functions;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.socket.ClientConnect;

public class ChangePasswordFunctionTest {
    ClientConnect clientConnect;
    Message sendMessage;

    @BeforeEach
    void setUp() {
        clientConnect = new ClientConnect();
        sendMessage = Message.builder().function(Functions.CHANGE_PASSWORD).build();
    }

    @Test
    void testSuccessChangePassword() {
        sendMessage.addParameter(ParameterType.PASSWORD, "123456");
        sendMessage.addParameter(ParameterType.NEW_PASSWORD, "admin");

        clientConnect.sendMessage(sendMessage);

    }
}
