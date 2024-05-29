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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import lombok.extern.slf4j.Slf4j;
import ufpa.biod.pp2oa.model.Tool;

@Slf4j
public class ToolDao {

    private EntityManager em;

    public ToolDao() {
        em = EntityManagerFactory.getInstance();
    }

    /**
     * Save a tool in the database
     * 
     * @param tool tool to be saved
     * 
     */
    public void save(Tool tool) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (tool.getId() == null) {
                em.persist(tool);
            } else {
                em.merge(tool);
            }
            transaction.commit();
            log.info("Tool saved: {}\n", tool);
        } catch (Exception e) {
            transaction.rollback();
            log.error("Error saving tool", e);
        }
    }

    /**
     * Delete a tool from the database
     * 
     * @param tool tool to be deleted
     */
    public void delete(Tool tool) {
        em.getTransaction().begin();
        em.remove(find(tool));
        em.getTransaction().commit();
        log.info("Tool deleted: {}\n", tool);
    }

    /**
     * Delete a tool from the database by id
     * 
     * @param id id of the tool to be deleted
     */
    public void delete(Integer id) {
        em.getTransaction().begin();
        em.remove(find(id));
        em.getTransaction().commit();
        log.info("Tool deleted: {}\n", id);
    }

    /**
     * Find a tool in the database
     * 
     * @param tool tool to be found
     * @return the tool found
     */
    public Tool find(Tool tool) {

        try {
            log.info("Tool found: {}\n", tool);
            return em.find(Tool.class, tool.getId());
        } catch (Exception e) {
            log.error("Error finding tool", e);
            return null;
        }
    }

    /**
     * Find a tool in the database by id
     * 
     * @param id id of the tool to be found
     * @return the tool found
     */
    public Tool find(Integer id) {
        try {
            log.info("Tool found: {}\n", id);
            return em.find(Tool.class, id);
        } catch (Exception e) {
            log.error("Error finding tool", e);
            return null;
        }
    }

    /**
     * Find all tools in the database
     * 
     * @return a list of tools
     */
    public List<Tool> findAll() {
        try {
            log.info("Successfully found all tools");
            return em.createQuery("SELECT t FROM Tool t", Tool.class).getResultList();
        } catch (Exception e) {
            log.error("Error finding tools", e);
            return new ArrayList<>();
        }
    }

}
