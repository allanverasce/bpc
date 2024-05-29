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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;
import ufpa.biod.pp2oa.model.AdminPassword;
import ufpa.biod.pp2oa.utils.LoadProperties;

@Slf4j
public class MySqlCreate {

    private MySqlCreate() {
    }

    public static Connection getDriver() {
        LoadProperties loadProperties = new LoadProperties();
        String url = loadProperties.getProperty("db.url").orElse("localhost");
        String port = loadProperties.getProperty("db.port").orElse("3306");
        String connectionUrl = String.format("jdbc:mysql://%s:%s/pp2oa?createDatabaseIfNotExist=true", url, port);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(connectionUrl,
                    loadProperties.getProperty("db.user").orElse("root"),
                    loadProperties.getProperty("db.password").orElse(""));
        } catch (Exception e) {
            log.error("Error on getDriver", e);
            return null;
        }
    }

    public static boolean checkExistDB(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables("pp2oa", null, null, new String[] { "TABLE" })) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            log.error("Error on isSchemaExists", e);
        }
        return false;
    }

    /**
     * Create the Database and Tables if not exists
     */
    public static void create() {
        try (Connection conn = getDriver()) {
            if (!checkExistDB(conn)) {
                log.info("Creating database");
                createAdminPassword(conn);
                createTool(conn);
                createProject(conn);
                createTelegramBot(conn);
                createProjectTool(conn);
                createParameter(conn);
                createToolExpectedOutput(conn);
                createToolPermittedExtension(conn);
                createPipeline(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the Table AdminPassword
     * 
     * @param con Connection to the database
     */
    public static void createAdminPassword(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`admin_password` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `password` VARCHAR(100) NOT NULL,
                        PRIMARY KEY (`id`)
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                        """;
            String password = new AdminPassword("admin").getPassword();

            String insertDataSql = String.format("INSERT IGNORE INTO admin_password VALUES (1, '%s');", password);

            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(insertDataSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating admin_password table", e);
        }
    }

    //

    /**
     * Create the Table Tool
     * 
     * @param con Connection to the database
     */
    public static void createTool(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE IF NOT EXISTS `pp2oa`.`tool` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `name` VARCHAR(255) NOT NULL,
                        `executable_path` VARCHAR(255) NOT NULL,
                        `should_create_output_directory` TINYINT(1) NOT NULL DEFAULT '0',
                        `active` TINYINT(1) NOT NULL DEFAULT '0',
                        PRIMARY KEY (`id`),
                        UNIQUE INDEX `name` (`name` ASC) VISIBLE
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                    """;

            String insertDataSql = """
                    INSERT IGNORE INTO `pp2oa`.`tool` (id, name, executable_path, should_create_output_directory, active)
                    VALUES
                        (1, 'prokka', '/opt/prokka/bin/prokka', FALSE, TRUE),
                        (2, 'spades', '/opt/SPAdes/bin/spades.py', FALSE, TRUE),
                        (3, 'ngsreads', 'java -jar /opt/ngsreads1.4.jar', TRUE, TRUE),
                        (4, 'fastqc', 'java -jar /opt/FastQC/FastQC.jar', FALSE, TRUE),
                        (5, 'megahit', '/opt/MEGAHIT/bin/megahit', FALSE, TRUE),
                        (6, 'busco', '/opt/busco/bin/busco', FALSE, TRUE),
                        (7, 'mauve', 'java -jar /opt/mauve/mauve.jar', FALSE, TRUE),
                        (8, 'retreat2quality', 'java -jar /opt/Retreat2Quality.jar', TRUE, TRUE),
                        (9, 'Salmon_Index', '/opt/salmon/bin/salmon', FALSE, TRUE),
                        (10, 'Salmon_Quant', '/opt/salmon/bin/salmon', FALSE, TRUE),
                        (11, 'Trinity', '/opt/trinity/Trinity', FALSE, TRUE);
                    """;

            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(insertDataSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating tool table and inserting data", e);
        }
    }

    /**
     * Create the Table Project
     * 
     * @param con Connection to the database
     */
    public static void createProject(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`project` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `chat_id` BIGINT NULL DEFAULT NULL,
                        `download_mode` VARCHAR(255) NOT NULL,
                        `name` VARCHAR(255) NOT NULL,
                        `pid` BIGINT NULL DEFAULT NULL,
                        `project_status` VARCHAR(255) NOT NULL,
                        `telegram_bot` TINYINT(1) NOT NULL DEFAULT '0',
                        `first_tool_id` INT NULL DEFAULT NULL,
                        `current_tool_id` INT NULL DEFAULT NULL,
                        `hash` VARCHAR(255) NULL DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE INDEX `name` (`name` ASC) VISIBLE
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                        """;
            String insertDataSql = "INSERT IGNORE INTO project VALUES (1, NULL, 'FULL_RESULT', 'default', NULL, 'PENDING', FALSE, NULL, NULL, NULL);";
            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(insertDataSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating project table", e);
        }
    }

    /**
     * Create the Table TelegramBot
     * 
     * @param con Connection to the database
     */
    public static void createTelegramBot(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`telegram_bot` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `api_key` VARCHAR(255) NOT NULL,
                        PRIMARY KEY (`id`)
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                            """;
            stmt.executeUpdate(createTableSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating project telegram_bot", e);
        }
    }

    /**
     * Create the Table ProjectTool
     * 
     * @param con Connection to the database
     */
    public static void createProjectTool(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`project_tool` (
                        `project_id` INT NOT NULL,
                        `tool_id` INT NOT NULL,
                        `status` VARCHAR(255) NULL DEFAULT NULL,
                        PRIMARY KEY (`project_id`, `tool_id`),
                        INDEX `tool_id` (`tool_id` ASC) VISIBLE,
                        CONSTRAINT `project_tool_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `pp2oa`.`project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT `project_tool_ibfk_2` FOREIGN KEY (`tool_id`) REFERENCES `pp2oa`.`tool` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                            """;

            String insertDataSql = """
                    INSERT IGNORE INTO project_tool VALUES
                    (1, 1, 'PENDING'),
                    (1, 2, 'PENDING'),
                    (1, 3, 'PENDING'),
                    (1, 4, 'PENDING'),
                    (1, 5, 'PENDING'),
                    (1, 6, 'PENDING'),
                    (1, 7, 'PENDING'),
                    (1, 8, 'PENDING'),
                    (1, 9, 'PENDING'),
                    (1, 10, 'PENDING'),
                    (1, 11, 'PENDING');
                        """;
            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(insertDataSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating project table", e);
        }
    }

    /**
     * Create the Table Parameter
     * 
     * @param con Connection to the database
     */
    public static void createParameter(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`parameter` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `project_id` int DEFAULT NULL,
                        `tool_id` int DEFAULT NULL,
                        `name` varchar(255) NOT NULL,
                        `value` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FKt0h2cqq2qr7an074qrkaofkr8` (`project_id`,`tool_id`),
                        CONSTRAINT `FKt0h2cqq2qr7an074qrkaofkr8` FOREIGN KEY (`project_id`, `tool_id`) REFERENCES `project_tool` (`project_id`, `tool_id`)
                        ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                                """;

            String insertDataSql = """
                    INSERT IGNORE INTO parameter VALUES
                    (1, 1, 1, '--locustag', 'default'),
                    (2, 1, 1, '--cpus', 4),
                    (3, 1, 1, '--prefix', 'default'),
                    (4, 1, 1, '--kingdom', 'Bacteria'),
                    (5, 1, 1, '--outdir', '%currentOutputDir%'),
                    (6, 1, 1, '--force', NULL),
                    (7, 1, 1, '%file%', NULL),
                    (8, 1, 2, '--careful', NULL),
                    (9, 1, 2, '--pe1-1', '%file%'),
                    (10, 1, 2, '--pe1-2', '%file%'),
                    (11, 1, 2, '-o', '%currentOutputDir%'),
                    (12, 1, 3, '--tag1', '%file%'),
                    (13, 1, 3, '--tag2', '%file%'),
                    (14, 1, 3, '--p', 4),
                    (15, 1, 3, '--output', '%currentOutputDir%'),
                    (16, 1, 4, '--input', '%previousOutputDir%'),
                    (17, 1, 4, '--output', '%currentOutputDir%'),
                    (18, 1, 5, '-1', '%file%'),
                    (19, 1, 5, '-2', '%file%'),
                    (20, 1, 5, '-0', '%currentOutputDir%'),
                    (21, 1, 6, '--in', '%file%'),
                    (22, 1, 6, '--out', '%currentOutputDir%'),
                    (23, 1, 6, '--mode', 'genome'),
                    (24, 1, 6, '--auto-lineage-prok', NULL),
                    (25, 1, 6, '--cpu', 4),
                    (26, 1, 6, '--force', NULL),
                    (27, 1, 7, '--ref', '%refer%'),
                    (28, 1, 7, '--draft', '%file%'),
                    (29, 1, 7, '--output', '%currentOutputDir%'),
                    (30, 1, 8, '--tag1', '%file%'),
                    (31, 1, 8, '--tag2', '%file%'),
                    (32, 1, 8, '--output', '%currentOutputDir%'),
                    (33, 1, 9, 'index', NULL),
                    (34, 1, 9, '-t', '%refer%'),
                    (35, 1, 9, '-i', '%currentOutputDir%'),
                    (36, 1, 10, 'quant', NULL),
                    (37, 1, 10, '-i', '%previousOutputDir%'),
                    (38, 1, 10, '-l', NULL),
                    (39, 1, 10, 'A', NULL),
                    (40, 1, 10, '-1', '%firstInputDir%/DRR016125_1.fastq.gz'),
                    (41, 1, 10, '-2', '%firstInputDir%/DRR016125_2.fastq.gz'),
                    (42, 1, 10, '-p', 20),
                    (43, 1, 10, '--validateMappings', NULL),
                    (44, 1, 10, '-o', '%currentOutputDir%'),
                    (45, 1, 11, '--seqType', 'fq'),
                    (46, 1, 11, '--left', '%file'),
                    (47, 1, 11, '--right', '%file'),
                    (48, 1, 11, '--max_memory', '20G'),
                    (49, 1, 11, '--CPU', 20),
                    (50, 1, 11, '--output', '%currentOutputDir%');
                        """;
            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(insertDataSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating project table", e);
        }
    }

    /**
     * Create the Table ToolExpectedOutput
     * 
     * @param con Connection to the database
     */
    public static void createToolExpectedOutput(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`tool_expected_output` (
                        `project_id` INT NOT NULL,
                        `tool_id` INT NOT NULL,
                        `output_file` VARCHAR(255) NOT NULL,
                        INDEX `fk_tool_expected_output_project_tool1_idx` (`project_id` ASC, `tool_id` ASC) VISIBLE,
                        CONSTRAINT `fk_tool_expected_output_project_tool1` FOREIGN KEY (`project_id`, `tool_id`) REFERENCES `pp2oa`.`project_tool` (`project_id`, `tool_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                                """;

            String insertDataSql = """
                    INSERT IGNORE INTO tool_expected_output VALUES
                    (1, 1, 'default.gbk'),
                    (1, 2, 'scaffolds.fasta'),
                    (1, 2, 'contigs.fasta'),
                    (1, 3, 'SRR1144800_raw_1_1_trated.fastq'),
                    (1, 3, 'SRR1144800_raw_2_2_trated.fastq'),
                    (1, 4, 'read1_fastqc'),
                    (1, 5, 'final.contigs.fa'),
                    (1, 6, 'short_summary.generic.bacteria_odb10.busco.txt'),
                    (1, 7, 'alignment1'),
                    (1, 8, 'treated.fastq'),
                    (1, 8, 'treated1.fastq'),
                    (1, 8, 'treated2.fastq'),
                    (1, 9, 'seq.bin'),
                    (1, 10, 'quant.sf'),
                    (1, 11, 'Trinity.Trinity.fasta');
                        """;
            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(insertDataSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating project table", e);
        }
    }

    /**
     * Create the Table ToolPermittedExtension
     * 
     * @param con Connection to the database
     */
    public static void createToolPermittedExtension(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`tool_permitted_extension` (
                        `tool_id` INT NOT NULL,
                        `extension` VARCHAR(255) NOT NULL,
                        INDEX `fk_tool_permitted_extension_tool1_idx` (`tool_id` ASC) VISIBLE,
                        CONSTRAINT `fk_tool_permitted_extension_tool1` FOREIGN KEY (`tool_id`) REFERENCES `pp2oa`.`tool` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                                    """;

            String insertDataSql = """
                    INSERT IGNORE INTO tool_permitted_extension VALUES
                    (1, 'fasta'),
                    (2, 'fastq'),
                    (3, 'fastq'),
                    (4, 'fastq'),
                    (5, 'fastq'),
                    (5, 'fq.gz'),
                    (6, 'fasta'),
                    (7, 'fasta'),
                    (7, 'gb'),
                    (8, 'fastq'),
                    (9, 'gz'),
                    (10, 'gz'),
                    (11, 'gz');
                        """;
            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(insertDataSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating project table", e);
        }
    }

    /**
     * Create the Table Pipeline
     * 
     * @param con Connection to the database
     */
    public static void createPipeline(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createTableSql = """
                    CREATE TABLE `pp2oa`.`pipeline` (
                        `project_id` INT NOT NULL,
                        `tool_id` INT NOT NULL,
                        PRIMARY KEY (`project_id`, `tool_id`),
                        INDEX `fk_pipeline_project1_idx` (`project_id` ASC) VISIBLE,
                        CONSTRAINT `fk_pipeline_project1` FOREIGN KEY (`project_id`) REFERENCES `pp2oa`.`project` (`id`),
                        CONSTRAINT `fk_pipeline_tool1` FOREIGN KEY (`tool_id`) REFERENCES `pp2oa`.`tool` (`id`)
                    ) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                                        """;

            stmt.executeUpdate(createTableSql);
        } catch (SQLException e) {
            log.error("Error executing SQL statements for creating project table", e);
        }
    }

}
