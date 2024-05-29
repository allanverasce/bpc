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
package ufpa.biod.pp2oa.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import ufpa.biod.pp2oa.database.ManagerDb;
import ufpa.biod.pp2oa.handler.ClientHandler;
import ufpa.biod.pp2oa.utils.ConfigBot;
import ufpa.biod.pp2oa.utils.LoadProperties;

@Slf4j
public class Server {
    private int maxClients;

    public final LoadProperties loadProperties;

    private final ThreadPoolExecutor maxClientsConnectionExecutor;

    private final ClientHandler clientHandler;

    public Server() {
        ManagerDb.create();
        ConfigBot configBot = new ConfigBot();
        configBot.activeBot();
        loadProperties = new LoadProperties();

        // Load the properties
        maxClients = Integer.parseInt(loadProperties.getProperty("maxClients").orElse("5"));

        // Create the thread pool to handle the clients connections
        this.maxClientsConnectionExecutor = new ThreadPoolExecutor(maxClients, maxClients, 5000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        this.clientHandler = new ClientHandler(configBot.getBotTelegram());
    }

    /**
     * Start the server socket and start listening the clients connections
     * 
     * @throws IOException
     * 
     */
    public void start() {
        try (ServerSocket server = new ServerSocket(16002)) {
            log.info("Server is Listening on Port " + server.getLocalPort());
            log.info("Waiting for the clients");
            clientListener(server);
        } catch (IOException e) {
            log.error("Error starting the server", e);
        }
    }

    /**
     * Listen the clients connections
     *
     * @param server the server socket
     * @throws IOException
     */
    public void clientListener(ServerSocket server) throws IOException {
        while (true) {
            Socket client = server.accept();
            if (maxClientsConnectionExecutor.getActiveCount() == maxClients) {
                log.info("Max clients connected");
                log.info("Closing connection with " + client.getInetAddress());
                client.close();
                continue;
            }

            log.info("Starting the client handler");
            maxClientsConnectionExecutor.submit(() -> clientHandler.handleClient(client));
        }
    }

    /**
     * Start the server
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
