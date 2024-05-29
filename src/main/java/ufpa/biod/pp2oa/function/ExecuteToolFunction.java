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

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;
import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.executor.Executor;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.MessageStatus;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.utils.BotTelegram;

@Slf4j
public class ExecuteToolFunction implements CallableFunction {

    @Override
    public void execute(FunctionContext context) {
        Optional<BotTelegram> bot = context.getBot();
        Message message = context.getMessage();
        ProjectDto projectDto = message.getParameter(ParameterType.PROJECT);
        ThreadPoolExecutor maxParallelTasksExecutor = context.getClientHandler().getMaxParallelTasksExecutor();

        // Create a async task to execute the tool
        FutureTask<Void> future = new FutureTask<>(() -> {
            executeTool(bot, message, projectDto);
            return null;
        });

        // Execute the async task
        maxParallelTasksExecutor.execute(future);

        try {
            // Wait the async task finish
            future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("Error to execute tool", e);
        }

        // executeTool(bot, message, projectDto);

    }

    /**
     * Execute the tool and notify the user
     * 
     * @param bot
     * @param message
     * @param projectDto
     */
    private void executeTool(Optional<BotTelegram> bot, Message message, ProjectDto projectDto) {
        try {
            log.info("Starting project: " + projectDto.getName());
            bot.ifPresent(b -> b.notifyStartProcessingTool(message));

            Executor.execute(message);
            bot.ifPresent(b -> b.notifyFinishProcessingTool(message));
            message.setMessageStatus(MessageStatus.PROCESSED);
            log.info("Project finished: " + projectDto.getName());

        } catch (IOException | InterruptedException e) {
            log.error("Error to execute tool", e);
            bot.ifPresent(b -> b.notifyErrorProcessingTool(message));
            message.setMessageStatus(MessageStatus.ERROR);
            Thread.currentThread().interrupt();
        }
    }

}
