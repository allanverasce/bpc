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

import ufpa.biod.pp2oa.executor.OutputFileProcessor;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.MessageStatus;
import ufpa.biod.pp2oa.model.ParameterType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompressResultFunction implements CallableFunction {

    @Override
    public void execute(FunctionContext context) {
        Message message = context.getMessage();

        try {
            log.info("Compressing Result File");
            OutputFileProcessor.compress(message);
            log.info("File compressed successfully: " + message.getParameter(ParameterType.GENERATED_ZIP_FILE));
            message.setMessageStatus(MessageStatus.PROCESSED);
        } catch (Exception e) {
            log.error("Error to compress file", e);
            message.setMessageStatus(MessageStatus.ERROR);

        }

    }

}
