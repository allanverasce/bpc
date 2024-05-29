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
package ufpa.biod.pp2oa.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ufpa.biod.pp2oa.dao.ProjectDao;
import ufpa.biod.pp2oa.model.Project;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for executing the tools
 */
@Slf4j
public class ProcessExecutor {

    /**
     * 
     * Private constructor to prevent instantiation of this utility class.
     */
    private ProcessExecutor() {
    }

    /**
     * Executes a command
     * 
     * @param command the command to be executed
     * @param project the project
     * @return true if the command was executed successfully
     * @throws IOException
     * @throws InterruptedException
     */
    public static void execute(String[] command, Project project) throws IOException, InterruptedException {
        log.info("Starting tool execution");
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process;
        try {
            log.info("Executing command {}", String.join(" ", command));
            process = processBuilder.start();
        } catch (Exception e) {
            log.error("Error executing command", e);
            throw new IOException(e);
        }
        project.setPid(process.pid());

        new ProjectDao().save(project);
        watchProcess(process);

        process.waitFor();
        log.info("Tool execution finished {}", process.exitValue());
    }

    /**
     * 
     * Watches the given process and logs its output.
     * 
     * @param process the process to watch
     */
    private static void watchProcess(final Process process) {
        new Thread() {
            @Override
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                try {
                    while ((line = input.readLine()) != null) {
                        log.info(line);

                    }
                } catch (IOException e) {
                    log.error("Error reading process output {}", e);
                }
            }
        }.start();
    }

}
