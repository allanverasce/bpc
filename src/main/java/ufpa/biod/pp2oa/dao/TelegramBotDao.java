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

import java.util.Optional;

import javax.persistence.EntityManager;

import ufpa.biod.pp2oa.model.TelegramBot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramBotDao {

    /** The entity manager for interacting with the database. */
    private EntityManager em;

    /**
     * Constructs a new `TelegramBotDao` instance.
     */
    public TelegramBotDao() {
        em = EntityManagerFactory.getInstance();
    }

    /**
     * Saves a telegramBot to the database.
     * If {@link #checkIfExistApiKeySaved} is true, it is assumed to be an existing
     * telegramBot and its values are merged with the values in the database.
     * If {@link #checkIfExistApiKeySaved} is false, it is assumed to be a new
     * telegramBot
     * and is persisted to the database.
     * 
     * @param telegramBot The telegramBot to save or update.
     */
    public void save(TelegramBot telegramBot) {
        try {
            em.getTransaction().begin();
            em.merge(telegramBot);
            em.getTransaction().commit();
            log.error("TelegramBot saved: {}", telegramBot);
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error saving telegramBot", e);
        }

    }

    public Optional<String> find(Integer id) {
        try {
            log.info("Find telegramBot");
            return em.createQuery("SELECT tb.apiKey FROM TelegramBot tb WHERE tb.id = :id", String.class)
                    .setParameter("id", id).getResultStream().findFirst();
        } catch (Exception e) {
            log.error("Error find telegramBot", e);
            return Optional.empty();
        }
    }

    

    public boolean checkIfExistApiKeySaved() {
        try {
            return em.createQuery("SELECT COUNT(tb) FROM TelegramBot tb", Long.class).getSingleResult() > 0;
        } catch (Exception e) {
            log.error("Error find telegramBot", e);
            return false;
        }
    }

}
