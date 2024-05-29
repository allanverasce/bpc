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
package ufpa.biod.pp2oa.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ufpa.biod.pp2oa.function.FunctionContext;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.utils.BotTelegram;
import ufpa.biod.pp2oa.utils.LoadProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler {
    private final BotTelegram botTelegram;

    // Chain of responsibility pattern to handle the messages
    private MessageHandler messageHandler = new ErrorHandler()
            .setNext(new ProcessedHandler())
            .setNext(new DownloadedHandler());

    // private ThreadSafeQueue queue = new ThreadSafeQueue();

    private final ThreadPoolExecutor maxParallelTasksExecutor;

    public ClientHandler(BotTelegram botTelegram) {
        this.botTelegram = botTelegram;

        // load the properties file
        LoadProperties loadProperties = new LoadProperties();

        // load the maxParallelTasks from the properties file or set the default value
        int maxParallelTools = Integer.parseInt(loadProperties.getProperty("maxParallelTools").orElse("1"));

        // create a thread pool with the maxParallelTasks
        this.maxParallelTasksExecutor = new ThreadPoolExecutor(maxParallelTools, maxParallelTools, 5000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    public void handleClient(Socket client) {
        try {
            log.info("New Client Connect {}\n", client.getInetAddress().getHostAddress());

            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            while (true) {
                output.reset();
                log.info("Checking for new messages\n");

                // Read the message sent by the client
                Message message = (Message) input.readObject();

                log.info("Start executing function: {}\n", message.getFunction());

                // Create the context of the function to be executed
                FunctionContext context = FunctionContext.builder()
                        .clientHandler(this)
                        .message(message)
                        .bot(Optional.ofNullable(botTelegram))
                        .input(input)
                        .output(output).build();

                // Execute the function of the message
                message.getFunction().execute(context);

                log.info("Message Status: {}\n", message.getMessageStatus());

                // Handle the message
                messageHandler.runHandle(context);
            }
        } catch (IOException | ClassNotFoundException ex) {
            log.info("client disconnected. Socket closing...");
            closeConnection(client);
        }
    }

    private void closeConnection(Socket client) {
        try {
            client.close();
        } catch (IOException ex) {
            log.error("Error closing socket connection", ex);
        }
    }

    /**
     * @return the maxParallelTasksExecutor
     */
    public ThreadPoolExecutor getMaxParallelTasksExecutor() {
        return maxParallelTasksExecutor;
    }

}
