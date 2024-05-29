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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class CheckExpectedFilesExist {

    private CheckExpectedFilesExist() {
    }

    /**
     * Check if at least one file exists in the list of files
     * 
     * @param expectedOutput list of files
     * @return true if at least one file exists
     * @throws FileNotFoundException
     */
    public static void check(List<File> expectedOutput) throws FileNotFoundException {
        if (expectedOutput.stream().noneMatch(File::exists)) {
            throw new FileNotFoundException("No expected output file found");
        }

    }

}
