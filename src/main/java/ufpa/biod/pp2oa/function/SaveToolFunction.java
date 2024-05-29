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

import org.modelmapper.ModelMapper;

import ufpa.biod.pp2oa.dao.ToolDao;
import ufpa.biod.pp2oa.dto.ToolDto;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.MessageStatus;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Tool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SaveToolFunction implements CallableFunction {

    /**
     * 
     * This class implements the {@link CallableFunction} interface and represents
     * the function that is responsible for saving a tool in the system.<br>
     * <br>
     * The execution of this function will save the tool in the database and set the
     * message status to {@link MessageStatus#PROCESSED}. <br>
     * <br>
     * If an error occurs during the execution of this function, the message status
     * will be set to {@link MessageStatus#ERROR}.
     */
    @Override
    public void execute(FunctionContext context) {
        Message message = context.getMessage();
        try {

            ToolDto toolDto = message.getParameter(ParameterType.TOOL);
            Tool tool = new ModelMapper().map(toolDto, Tool.class);
            new ToolDao().save(tool);
            ToolDto savedTool = new ModelMapper().map(tool, ToolDto.class);
            log.info("Tool saved successfully: {}", savedTool);
            message.addParameter(ParameterType.TOOL, savedTool);
            message.setMessageStatus(MessageStatus.PROCESSED);
        } catch (Exception e) {
            log.error("Error to save tool", e);
            message.setMessageStatus(MessageStatus.ERROR);
        }

    }

}
