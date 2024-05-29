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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadProperties {
    Properties properties;

    public LoadProperties() {
        loadProperties();
    }

    /**
     * Load the properties file
     */
    public void loadProperties() {
        File propertiesFile = new File("./config.properties");
        this.properties = new Properties();
        try (FileInputStream fr = new FileInputStream(propertiesFile)) {
            properties.load(fr);
        } catch (IOException e) {
            log.warn("Error loading properties file: " + e.getMessage());
        }
    }

    /**
     * Get the property value
     * 
     * @param key property key
     * @return optional with the property value
     */
    public Optional<String> getProperty(String key) {
        try {
            return Optional.of(properties.getProperty(key));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

}
