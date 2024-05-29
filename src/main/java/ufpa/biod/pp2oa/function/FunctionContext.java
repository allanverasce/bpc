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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import ufpa.biod.pp2oa.handler.ClientHandler;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.utils.BotTelegram;
import lombok.Builder;
import lombok.Getter;

/**
 * 
 * The {@code FunctionContext} class holds the context for a function being
 * executed. It contains the
 * {@code input} stream for reading data from the client, the {@code output}
 * stream for writing data to the client,
 * the {@code message} object containing the function request information and
 * the {@code bot} object for interacting
 * with the Telegram bot.
 * 
 * @param input   the input stream for reading data from the client
 * @param output  the output stream for writing data to the client
 * @param message the message object containing the function request information
 * @param bot     the bot object for interacting with the Telegram bot
 */
@Builder
@Getter
public class FunctionContext {
    private final ClientHandler clientHandler;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final Message message;
    private final Optional<BotTelegram> bot;
}
