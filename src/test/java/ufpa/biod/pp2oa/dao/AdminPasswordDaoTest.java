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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ufpa.biod.pp2oa.model.AdminPassword;

public class AdminPasswordDaoTest {
    AdminPasswordDao adminPasswordDao;

    @BeforeEach
    void setUp() throws Exception {
        adminPasswordDao = new AdminPasswordDao();
        adminPasswordDao.update(new AdminPassword("admin"));
    }

    @Test
    void testSavePassword() {
        AdminPassword adminPassword = new AdminPassword("admin");
        adminPasswordDao.update(adminPassword);
        assertTrue(adminPasswordDao.login("admin"));
    }

    @Test
    void testChangePassword() {
        String newPassword = "123456";
        adminPasswordDao.changePassword("admin", newPassword);
        assertTrue(adminPasswordDao.login(newPassword));

    }

    @Test
    void testChangePasswordInvalidLogin() {
        assertThrows(RuntimeException.class, () -> adminPasswordDao.changePassword("invalid", "123456"));
    }

    @Test
    void testLogin() {
        assertTrue(new AdminPasswordDao().login("admin"));
    }

    @Test
    void testLoginInvalid() {
        assertFalse(new AdminPasswordDao().login("123456"));
    }

}
