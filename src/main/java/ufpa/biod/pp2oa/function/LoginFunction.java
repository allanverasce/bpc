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

import ufpa.biod.pp2oa.dao.AdminPasswordDao;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.MessageStatus;
import ufpa.biod.pp2oa.model.ParameterType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFunction implements CallableFunction {

    /**
     * 
     * This class implements the {@link CallableFunction} interface and represents
     * the function that is responsible for login.<br>
     * <br>
     * The execution of this function will check the password and set the message
     * status to {@link MessageStatus#PROCESSED}. <br>
     * <br>
     * If an error occurs during the execution of this function, the message status
     * will be set to {@link MessageStatus#ERROR}.
     */
    @Override
    public void execute(FunctionContext context) {
        Message message = context.getMessage();
        if (new AdminPasswordDao().login(message.getParameter(ParameterType.PASSWORD))) {
            message.addParameter(ParameterType.AUTHORIZED, true);
            message.setMessageStatus(MessageStatus.PROCESSED);
        } else {
            log.error("Invalid Password");
            message.addParameter(ParameterType.AUTHORIZED, false);
            message.setMessageStatus(MessageStatus.ERROR);
        }

    }

}
