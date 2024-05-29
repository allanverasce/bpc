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

import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.MessageStatus;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Project;
import ufpa.biod.pp2oa.utils.CheckPid;

public class CheckProcessIsRunningFunction implements CallableFunction {

    /**
     * This class implements the {@link CallableFunction} interface and represents
     * the function that is responsible for checking if a process is running or
     * not.<br>
     * <br>
     * Add the parameter {@link ParameterType#PROCESS_IS_RUNNING} to the message
     * with
     * the value of the check.<br>
     * <br>
     * Set the message status to {@link MessageStatus#PROCESSED} after the execution
     * of this function.<br>
     * <br>
     * 
     */
    @Override
    public void execute(FunctionContext context) {
        Message message = context.getMessage();
        Project project = message.getParameter(ParameterType.PROJECT);
        message.addParameter(ParameterType.PROCESS_IS_RUNNING,
                new CheckPid().isProcessIdRunning(project.getPid()));
        message.setMessageStatus(MessageStatus.PROCESSED);

    }

}
