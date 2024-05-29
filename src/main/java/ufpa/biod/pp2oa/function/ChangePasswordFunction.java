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
public class ChangePasswordFunction implements CallableFunction {

    /**
     * 
     * The {@code ChangePasswordFunction} class is responsible for changing the
     * password of the administrator.<br>
     * <br>
     * 
     * 
     * The message object should contain the parameters "password" and "newPassword"
     * of type {@link ParameterType#PASSWORD}
     * 
     * and {@link ParameterType#NEW_PASSWORD} respectively. <br>
     * <br>
     * 
     * If the operation is successful, the message's status will be set to
     * {@link MessageStatus#PROCESSED}. <br>
     * <br>
     * 
     * Otherwise, the message's status will be set to {@link MessageStatus#ERROR}.
     */
    @Override
    public void execute(FunctionContext context) {
        Message message = context.getMessage();
        try {
            new AdminPasswordDao().changePassword(message.getParameter(ParameterType.PASSWORD),
                    message.getParameter(ParameterType.NEW_PASSWORD));
            message.setMessageStatus(MessageStatus.PROCESSED);
        } catch (Exception e) {
            log.error("Error to change password", e);
            message.setMessageStatus(MessageStatus.ERROR);
        }

    }

}
