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
package ufpa.biod.pp2oa.utils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import ufpa.biod.pp2oa.dto.ProjectDto;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.ParameterType;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Cleo
 */
@Slf4j
public class BotTelegram {
    Logger logger = Logger.getLogger(BotTelegram.class.getName());
    TelegramBot bot;
    int offset = 0;

    public BotTelegram(String apiKey) {
        this.bot = new TelegramBot(apiKey);
    }

    public BotTelegram(TelegramBot telegramBot) {
        this.bot = telegramBot;
    }

    /**
     * Start the listener to receive messages from Telegram
     * For receive messages is /start command
     * The bot response the client chat id
     */
    public void startBotListener() {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {

            GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates().limit(100).offset(offset).timeout(30));
            List<Update> updates = updatesResponse.updates();
            for (Update update : updates) {
                this.setOffset(update.updateId() + 1);

                String receiveMessage = update.message().text();
                Long chatID = update.message().chat().id();

                log.info("Receive message from chatID: " + chatID);

                if (receiveMessage.equals("/start")) {
                    sendTelegramMessage("Your chat id is: " + chatID, chatID);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void sendTelegramMessage(String message, Long chatId) {
        log.info("Send message to chatID: " + chatId);
        bot.execute(new SendMessage(chatId, message));
    }

    public void notifyFinishDownloadFiles(Message message) {
        ProjectDto project = message.getParameter(ParameterType.PROJECT);
        String msg = "Finish download files from project " + project.getName();
        project.getChatId().ifPresent(chatId -> sendTelegramMessage(msg, chatId));
    }

    public void notifyErrorDownloadFiles(Message message) {
        ProjectDto project = message.getParameter(ParameterType.PROJECT);
        String msg = "Error to download files from project " + project.getName();
        project.getChatId().ifPresent(chatId -> sendTelegramMessage(msg, chatId));
    }

    /**
     * Notifies the user through a message in Telegram that the project has started.
     *
     * @param message the message that contains the project to be started
     */
    public void notifyProjectStarted(Message message) {
        ProjectDto project = message.getParameter(ParameterType.PROJECT);
        String msg = "Starting project " + project.getName();
        project.getChatId().ifPresent(chatId -> sendTelegramMessage(msg, chatId));
    }

    /**
     * Notifies the user through a message in Telegram that the project has
     * finished.
     *
     * @param message the message that contains the project to be finished
     */
    public void notifyProjectFinished(Message message) {
        ProjectDto project = message.getParameter(ParameterType.PROJECT);
        String msg = "Finish project " + project.getName();
        project.getChatId().ifPresent(chatId -> sendTelegramMessage(msg, chatId));
    }

    /**
     * Notifies the start of processing a tool in a project.
     *
     * @param message the message containing the project and tool information
     */
    public void notifyStartProcessingTool(Message message) {
        ProjectDto project = message.getParameter(ParameterType.PROJECT);
        String msg = "Starting processing " + project.getCurrentTool().getName() + " from project "
                + project.getName();
        project.getChatId().ifPresent(chatId -> sendTelegramMessage(msg, chatId));
    }

    /**
     * Notifies the end of processing a tool in a project.
     *
     * @param message the message containing the project and tool information
     */
    public void notifyFinishProcessingTool(Message message) {
        ProjectDto project = message.getParameter(ParameterType.PROJECT);
        String msg = "Finish processed " + project.getCurrentTool().getName() + " from project "
                + project.getName();
        project.getChatId().ifPresent(chatId -> sendTelegramMessage(msg, chatId));
    }

    /**
     * Notifies the user through a message in Telegram that an error occurred
     * during the processing of a tool.
     *
     * @param message the message that contains the project and tool information
     */
    public void notifyErrorProcessingTool(Message message) {
        ProjectDto project = message.getParameter(ParameterType.PROJECT);
        String msg = "Error processing " + project.getCurrentTool().getName() + " from project "
                + project.getName();
        project.getChatId().ifPresent(chatId -> sendTelegramMessage(msg, chatId));
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

}
