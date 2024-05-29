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

import java.util.Optional;
import java.util.Scanner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import ufpa.biod.pp2oa.dao.TelegramBotDao;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * The {@code ConfigBot} class is responsible for configuring the Telegram bot.
 * It allows the user to insert their API key
 * 
 * and tests if it is valid. If it is valid, the API key is saved in the
 * database.
 * 
 * @author Cleo
 */
@Getter
@Slf4j
public class ConfigBot {

    private TelegramBot telegramBot;
    private BotTelegram botTelegram;
    private Optional<String> apiKey;
    LoadProperties loadProperties = new LoadProperties();

    /**
     * Activate the bot if it is not active
     * 
     * @param botTelegram
     */
    public void activeBot() {

        apiKey = new TelegramBotDao().find(1);
        String docker = System.getenv("DOCKER_ENV");
        boolean botActive = loadProperties.getProperty("telegramBot").orElse("false").equals("true");

        /**
         * If the API key is not saved, the configuration asks for config the bot
         * if the API key is saved, start the bot
         */
        apiKey.ifPresentOrElse(api -> {
            this.botTelegram = new BotTelegram(api);
            // Thread bot = botTelegram.startBotListener
            botTelegram.startBotListener();
        }, () -> {
            if ("false".equals(docker) || docker == null) {
                boolean startConfigBot = startConfigBot();
                if (startConfigBot) {
                    startBot();
                }
            } else if ("true".equals(docker) && botActive) {
                startBot();
                autoInsertApiKey();
            }

        });

    }

    private void startBot() {
        this.botTelegram = new BotTelegram(apiKey.get());
        botTelegram.startBotListener();
    }

    /**
     * 
     * Prompts the user to activate the Telegram bot function and inserts the API
     * key if the user chooses to do so.
     */
    public boolean startConfigBot() {
        log.info("Starting bot configuration");
        log.info("Do you want to activate the telegram bot function? (Yes/No):");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String option = sc.next().toLowerCase();
            switch (option) {
                case "yes" -> {
                    manualInsertApiKey();
                    sc.close();
                    return true;
                }
                case "y" -> {
                    manualInsertApiKey();
                    sc.close();
                    return true;
                }
                case "no" -> {
                    sc.close();
                    return false;
                }
                case "n" -> {
                    sc.close();
                    return false;
                }
                default -> {
                    log.info("Invalid option! Please, insert Yes or No");
                }
            }

        }
    }

    /**
     * 
     * Prompts the user to insert their API key and tests if it is valid. If it is
     * valid, the API key is saved in the
     * database.
     */
    private void manualInsertApiKey() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            log.info("Insert Your Telegram ApiBot Key: ");
            apiKey = isValidAPIKey(sc.next());

            if (apiKey.isPresent()) {
                log.info("Api key Valid!");
                break;
            }
            log.info("Please, insert a valid Api key!");
        }
        insertApiKey(apiKey.get());

        log.info("Success to bot settings");
        sc.close();
    }

    private void autoInsertApiKey() {
        String key = loadProperties.getProperty("telegramBot.apiKey")
                .orElseThrow(() -> new IllegalArgumentException("Api key not found"));
        apiKey = isValidAPIKey(key);

        if (apiKey.isPresent()) {
            log.info("Api key Valid!");
            log.info("Please, insert a valid Api key!");
            return;
        }
        insertApiKey(apiKey.get());
        log.info("Success to bot settings");
    }

    private void insertApiKey(String apikey) {
        new TelegramBotDao().save(new ufpa.biod.pp2oa.model.TelegramBot(apikey));
    }

    /**
     * 
     * Verifies if the given API key is valid by sending a request to the Telegram
     * 
     * @param apikey the API key to be verified
     * @return true if the API key is valid, false otherwise
     */
    private Optional<String> isValidAPIKey(String apikey) {

        this.telegramBot = new TelegramBot(apikey);
        GetUpdatesResponse updatesResponse = this.telegramBot.execute(new GetUpdates().limit(100).offset(0));
        if (updatesResponse.isOk()) {
            return Optional.ofNullable(apikey);
        }
        return Optional.empty();
    }
}
