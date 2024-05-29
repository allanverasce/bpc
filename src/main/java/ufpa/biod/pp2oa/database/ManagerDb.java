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
package ufpa.biod.pp2oa.database;

import java.util.Optional;

import ufpa.biod.pp2oa.utils.LoadProperties;

public class ManagerDb {
    private ManagerDb() {
    }

    public static void create() {
        LoadProperties loadProperties = new LoadProperties();
        Optional<String> db = loadProperties.getProperty("db");
        if (db.isPresent() && db.get().equals("MySQL")) {
            MySqlCreate.create();
        } else {
            SqliteCreate.create();
        }
    }
}
