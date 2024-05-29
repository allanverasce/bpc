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
package ufpa.biod.pp2oa.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import ufpa.biod.pp2oa.utils.LoadProperties;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * ManagerFactory class is used to create and manage a single instance of
 * EntityManager class.
 * EntityManager is used to access the database and perform operations such as
 * saving and retrieving data.
 */
@Slf4j
public class EntityManagerFactory {
    private static EntityManagerFactory instance = null;
    private static EntityManager em;
    private static LoadProperties loadProperties;

    /**
     * Returns the current instance of the entity manager. If the instance has not
     * yet been created, it will be created on the first access.
     *
     * @return An instance of EntityManager for accessing the database.
     */
    public static EntityManager getInstance() {
        loadProperties = new LoadProperties();
        if (instance == null) {
            instance = new EntityManagerFactory();
            log.info("EntityManager instance created");
        }

        return em;
    }

    /**
     * Creates a new instance of the ManagerFactory class. This constructor is
     * private because the ManagerFactory class is implemented as a singleton.
     * The EntityManager is created using the Persistence class and the "PP2OA"
     * persistence unit name.
     */
    private EntityManagerFactory() {
        Optional<String> db = loadProperties.getProperty("db");
        if (db.isPresent() && db.get().equals("MySQL")) {
            createFromMySql();
        } else {
            createFromSqlite();
        }
    }

    private void createFromSqlite() {
        log.info("Creating EntityManager from SQLite");
        Map<String, String> persistenceMap = new HashMap<>();
        persistenceMap.put("hibernate.dialect", "ufpa.biod.pp2oa.utils.SQLiteDialect");
        persistenceMap.put("javax.persistence.jdbc.driver", "org.sqlite.JDBC");
        persistenceMap.put("javax.persistence.jdbc.url", "jdbc:sqlite:./PP2OA.db");
        persistenceMap.put("javax.persistence.jdbc.user", "");
        persistenceMap.put("javax.persistence.jdbc.password", "");
        persistenceMap.put("hibernate.connection.charSet", "UTF-8");
        persistenceMap.put("hibernate.hbm2ddl.auto", "update");
        persistenceMap.put("hibernate.event.merge.entity_copy_observer", "allow");
        persistenceMap.put("hibernate.show_sql", "false");
        persistenceMap.put("hibernate.format_sql", "false");
        persistenceMap.put("hibernate.generate_statistics", "false");
        persistenceMap.put("hibernate.use_sql_comments", "false");
        em = Persistence.createEntityManagerFactory("PP2OA", persistenceMap).createEntityManager();
    }

    private void createFromMySql() {
        log.info("Creating EntityManager from MySQL");

        String url = loadProperties.getProperty("db.url").orElse("localhost");
        String port = loadProperties.getProperty("db.port").orElse("3306");
        String finalUrl = String.format("jdbc:mysql://%s:%s/pp2oa?createDatabaseIfNotExist=true", url, port);

        Map<String, String> persistenceMap = new HashMap<>();
        persistenceMap.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");

        persistenceMap.put("javax.persistence.jdbc.url", finalUrl);
        persistenceMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        persistenceMap.put("javax.persistence.jdbc.user", loadProperties.getProperty("db.user").orElse("root"));
        persistenceMap.put("javax.persistence.jdbc.password", loadProperties.getProperty("db.password").orElse(""));
        persistenceMap.put("hibernate.hbm2ddl.auto", "update");
        persistenceMap.put("hibernate.event.merge.entity_copy_observer", "allow");
        persistenceMap.put("hibernate.show_sql", "false ");
        persistenceMap.put("hibernate.format_sql", "false");
        persistenceMap.put("hibernate.generate_statistics", "false");
        em = Persistence.createEntityManagerFactory("PP2OA", persistenceMap).createEntityManager();
    }

}
