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

import javax.persistence.EntityManager;

import org.springframework.security.crypto.bcrypt.BCrypt;

import ufpa.biod.pp2oa.model.AdminPassword;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminPasswordDao {

    /** The entity manager for interacting with the database. */
    private EntityManager em;

    /**
     * Constructs a new `AdminPasswordDao` instance.
     */
    public AdminPasswordDao() {
        em = EntityManagerFactory.getInstance();
    }

    /**
     * Updates the admin password in the database.
     * 
     * @param adminPassword
     */
    public void update(AdminPassword adminPassword) {
        try {
            em.getTransaction().begin();
            em.merge(adminPassword);
            em.getTransaction().commit();
            log.info("Admin password updated");
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.error("Error updating admin password", e);
        }
    }

    /**
     * Finds the admin password in the database.
     * 
     * @param id
     * @return
     */
    private AdminPassword find(Integer id) {
        try {
            return em.find(AdminPassword.class, id);
        } catch (Exception e) {
            log.error("Error finding admin password", e);
            return null;
        }
    }

    /**
     * Changes the admin password.
     * 
     * @param password
     * @param newPassword
     */
    public void changePassword(String password, String newPassword) {
        try {
            if (login(password)) {
                update(new AdminPassword(newPassword));
                log.info("Admin password changed");
            } else {
                log.error("Error changing admin password: wrong password");
            }
        } catch (Exception e) {
            log.error("Error changing admin password", e);
        }
    }

    /**
     * Logs in the admin.
     * 
     * @param password
     * @return true if the password is correct, false otherwise
     */
    public boolean login(String password) {
        try {
            AdminPassword adminPassword = find(1);
            boolean valid = BCrypt.checkpw(password, adminPassword.getPassword());
            log.info("Admin password is valid: " + valid);
            return valid;
        } catch (Exception e) {
            log.error("Error logging in", e);
            return false;
        }
    }

    public boolean checkExistSavedPassword() {
        try {
            return em.createQuery("SELECT COUNT(ap) FROM AdminPassword ap", Long.class).getSingleResult() > 0;
        } catch (Exception e) {
            log.error("Error checking if admin password exists", e);
            return false;
        }
    }

}
