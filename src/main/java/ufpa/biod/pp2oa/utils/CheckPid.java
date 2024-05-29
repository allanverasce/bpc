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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * The CheckPid class provides a method to check whether a process with a given
 * process ID is currently running.
 * 
 */
public class CheckPid {

    /**
     * 
     * Determines whether a process with the given process ID is currently running.
     * 
     * @param pid the process ID to check
     * 
     * @return true if the process is running, false otherwise
     */
    public boolean isProcessIdRunning(Long pid) {

        try {
            String cmd = "ps -p " + pid;
            String[] command = cmd.split(" ");
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String strLine;
            while ((strLine = bReader.readLine()) != null) {
                if (strLine.contains(" " + pid + " ")) {
                    return true;
                }
            }
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

}
