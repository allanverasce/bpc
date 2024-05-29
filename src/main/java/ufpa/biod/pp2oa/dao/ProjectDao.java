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

import ufpa.biod.pp2oa.model.Project;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProjectDao {

    /** The entity manager for interacting with the database. */
    private EntityManager em;

    /**
     * Constructs a new `ProjectDao` instance.
     */
    public ProjectDao() {
        em = EntityManagerFactory.getInstance();
    }

    /**
     * Saves a project to the database. If the project has a null ID, it is assumed
     * to be a new project
     * and is persisted to the database. If the project has a non-null ID, it is
     * assumed to be an
     * existing project and its values are merged with the values in the database.
     *
     * @param project The project to save or update.
     */
    public void save(Project project) {
        em.getTransaction().begin();
        try {
            if (project.getId() == null) {
                em.persist(project);
            } else {
                em.merge(project);
            }
            em.getTransaction().commit();
            log.info("Project saved: {}", project);
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error saving project", e);
        }
    }

    /**
     * Deletes a project from the database.
     *
     * @param project The project to delete.
     */
    public void delete(Project project) {
        try {
            em.getTransaction().begin();
            em.remove(find(project));
            em.getTransaction().commit();
            log.info("Project deleted: {}", project);
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error deleting project", e);
        }
    }

    /**
     * Finds a project by its ID.
     *
     * @param id The ID of the project to find.
     * @return The found project, or null if no project with the given ID was found.
     */
    public Project find(Integer id) {
        try {
            return em.find(Project.class, id);
        } catch (Exception e) {
            log.error("Error finding project", e);
            return null;
        }
    }

    /**
     * Finds a project by its ID.
     *
     * @param project The project object containing the ID of the project to find.
     * @return The found project, or null if no project with the given ID was found.
     */
    public Project find(Project project) {
        try {
            return em.find(Project.class, project.getId());
        } catch (Exception e) {
            log.error("Error finding project", e);
            return null;
        }
    }

    public Project find(String hash) {
        try {
            log.info("Finding project with hash {}", hash);

            return em.createQuery("SELECT p FROM Project p WHERE p.hash = :hash", Project.class)
                    .setParameter("hash", hash)
                    .getSingleResult();
        } catch (Exception e) {
            log.error("Error finding project", e);
            return null;
        }
    }

    /**
     * Finds all projects in the database.
     *
     * @return A list containing all found projects.
     */
    public List<Project> findAll() {
        try {
            return em.createQuery("SELECT p FROM Project p", Project.class).getResultList();
        } catch (Exception e) {
            log.error("Error finding projects", e);
            return new ArrayList<>();
        }
    }
}
