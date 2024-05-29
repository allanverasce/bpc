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
import ufpa.biod.pp2oa.dao.AdminPasswordDao;
import ufpa.biod.pp2oa.model.AdminPassword;

/**
 * 
 * Class responsible for creating the database tables, if they do not exist.
 */

@Slf4j
public class SqliteCreate {

    /**
     * 
     * Private constructor to prevent instantiation of this utility class.
     */
    private SqliteCreate() {
    }

    /**
     * Get a connection to the database
     * 
     * @return the Connection object
     */
    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:./PP2OA.db";
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Error connecting to database", e);
        }
        return conn;
    }

    /**
     * Execute sql statement
     * 
     * @param sql the sql statement
     * @param con Connection to the database
     */
    private static void executeSql(String sql, Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            log.error("Error executing sql: " + sql, e);
        }
    }

    /**
     * Check if the database already exists
     * 
     * @param con Connection to the database
     * @return true if the database exists
     */
    private static boolean checkExistDB(Connection conn) {
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();

            try (ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] { "TABLE" })) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            log.error("Error checking if database exists", ex);
        }
        return false;
    }

    /**
     * Create the database tables
     */
    public static void create() {
        Connection conn = connect();
        if (!checkExistDB(conn)) {
            createAdminPassword(conn);
            createTool(conn);
            createProject(conn);
            createTelegramBot(conn);
            createProjectTool(conn);
            createParameter(conn);
            createToolExpectedOutput(conn);
            createToolPermittedExtension(conn);
            createPipeline(conn);
            insertData(conn);
            new AdminPasswordDao().update(new AdminPassword("admin"));

        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the Table AdminPassword
     * 
     * @param con Connection to the database
     */
    private static void createAdminPassword(Connection con) {
        String sql = """
                CREATE TABLE [admin_password](
                    [id] integer NOT NULL,
                    [password] varchar NOT NULL,
                    PRIMARY KEY([id]));
                    """;
        executeSql(sql, con);
    }

    /**
     * Create the Table Tool
     * 
     * @param con Connection to the database
     */
    public static void createTool(Connection con) {
        String sql = """
                CREATE TABLE [tool](
                    [id] integer PRIMARY KEY,
                    [name] varchar NOT NULL UNIQUE,
                    [executable_path] varchar NOT NULL,
                    [should_create_output_directory] boolean NOT NULL DEFAULT FALSE,
                    [active] boolean NOT NULL DEFAULT FALSE);
                    """;
        executeSql(sql, con);
    }

    /**
     * Create the Table Project
     * 
     * @param con Connection to the database
     */
    public static void createProject(Connection con) {
        String sql = """
                CREATE TABLE [project](
                    [id] integer PRIMARY KEY,
                    [chat_id] bigint,
                    [download_mode] varchar NOT NULL,
                    [name] varchar NOT NULL UNIQUE,
                    [pid] bigint,
                    [project_status] varchar NOT NULL,
                    [telegram_bot] boolean NOT NULL DEFAULT FALSE,
                    [current_tool_id] integer REFERENCES [Tool]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [hash] varchar);
                            """;
        executeSql(sql, con);
    }

    public static void createTelegramBot(Connection con) {
        String sql = """
                CREATE TABLE [telegram_bot](
                    [id] integer,
                    [api_key] varchar NOT NULL,
                    PRIMARY KEY([id]));
                                """;
        executeSql(sql, con);
    }

    /**
     * Create the Table ProjectTool
     * 
     * @param con Connection to the database
     */
    public static void createProjectTool(Connection con) {
        String sql = """
                CREATE TABLE [project_tool](
                    [project_id] integer NOT NULL REFERENCES [Project]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [tool_id] integer NOT NULL REFERENCES [Tool]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [status] varchar,
                    PRIMARY KEY([project_id], [tool_id]));
                                """;
        executeSql(sql, con);
    }

    /**
     * Create the Table Parameter
     * 
     * @param con Connection to the database
     */
    public static void createParameter(Connection con) {
        String sql = """
                CREATE TABLE [parameter](
                    [id] integer PRIMARY KEY,
                    [project_id] integer REFERENCES [Project]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [tool_id] integer REFERENCES [Tool]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [name] varchar NOT NULL,
                    [value] varchar);
                                """;
        executeSql(sql, con);
    }

    /**
     * Create the Table ToolExpectedOutput
     * 
     * @param con the connection
     */
    public static void createToolExpectedOutput(Connection con) {
        String sql = """
                CREATE TABLE [tool_expected_output](
                    [project_id] integer REFERENCES [Project]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [tool_id] integer REFERENCES [Tool]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [output_file] varchar NOT NULL);
                                        """;
        executeSql(sql, con);
    }

    /**
     * Create the Table ToolPermittedExtension
     * 
     * @param con Connection to the database
     */
    public static void createToolPermittedExtension(Connection con) {
        String sql = """
                CREATE TABLE [tool_permitted_extension](
                    [tool_id] integer NOT NULL REFERENCES [Tool]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [extension] varchar NOT NULL);
                                    """;
        executeSql(sql, con);
    }

    public static void createPipeline(Connection con) {
        String sql = """
                CREATE TABLE [pipeline](
                    [project_id] integer NOT NULL REFERENCES [Project]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
                    [tool_id] integer NOT NULL REFERENCES [Tool]([id]) ON DELETE CASCADE ON UPDATE CASCADE);
                                        """;
        executeSql(sql, con);
    }

    /**
     * Insert data in Tool table.<br>
     * <br>
     * Structure of the table: ID, Name, ExecutablePath,
     * shouldCreateOutputDirectory, active.
     * 
     * @param con Connection to the database
     */
    private static void populateToolTable(Connection con) {

        String sql = """
                INSERT OR IGNORE INTO tool VALUES
                    (1, 'prokka', '/opt/prokka/bin/prokka', FALSE, TRUE),
                    (2, 'spades', '/opt/SPAdes/bin/spades.py', FALSE, TRUE),
                    (3, 'ngsreads', 'java -jar /opt/ngsreads1.4.jar', TRUE, TRUE),
                    (4, 'fastqc', 'java -jar /opt/FastQC/FastQC.jar', FALSE, TRUE),
                    (5, 'megahit', '/opt/MEGAHIT/bin/megahit', FALSE, TRUE),
                    (6, 'busco', '/opt/busco/bin/busco', FALSE, TRUE),
                    (7, 'mauve', 'java -jar /opt/mauve/mauve.jar', FALSE, TRUE),
                    (8, 'retreat2quality', 'java -jar /opt/Retreat2Quality.jar', TRUE, TRUE);
                """;
        executeSql(sql, con);
    }

    /**
     * Insert data in ToolPermittedExtension table.<br>
     * <br>
     * Structure of the table: ToolID, Extension.
     * 
     * @param con Connection to the database
     */
    private static void populateToolPermittedExtensionTable(Connection con) {

        String sql = """
                INSERT OR IGNORE INTO tool_permitted_extension VALUES
                    (1, 'fasta'),

                    (2, 'fastq'),

                    (3, 'fastq'),

                    (4, 'fastq'),

                    (5, 'fastq'),
                    (5, 'fq.gz'),

                    (6, 'fasta'),

                    (7, 'fasta'),
                    (7, 'gb'),

                    (8, 'fastq');
                """;
        executeSql(sql, con);
    }

    /**
     * Insert data in Project table.<br>
     * <br>
     * Structure of the table: ID, ChatID, DownloadMode, Name, PID, ProjectStatus,
     * TelegramBot, CurrentTool, hash.
     * 
     * @param con Connection to the database
     */
    public static void populateProjectTable(Connection con) {

        String sql = """
                   INSERT OR IGNORE INTO project VALUES(1, NULL, 'FULL_RESULT', 'default', NULL, 'PENDING', FALSE, NULL, NULL);
                """;
        executeSql(sql, con);
    }

    /**
     * Insert data in ProjectTool table.<br>
     * <br>
     * Structure of the table: ProjectID, ToolID, Status.
     * 
     * @param con Connection to the database
     */
    private static void populateProjectToolTable(Connection con) {

        String sql = """
                INSERT OR IGNORE INTO project_tool VALUES
                    (1, 1, 'PENDING'),
                    (1, 2, 'PENDING'),
                    (1, 3, 'PENDING'),
                    (1, 4, 'PENDING'),
                    (1, 5, 'PENDING'),
                    (1, 6, 'PENDING'),
                    (1, 7, 'PENDING'),
                    (1, 8, 'PENDING');
                """;
        executeSql(sql, con);
    }

    /**
     * Insert data in ToolExpectedOutput table.<br>
     * <br>
     * Structure of the table: ProjectID, ToolID, OutputFile.
     * 
     * @param con Connection to the database
     */
    private static void populateToolExpectedOutputTable(Connection con) {

        String sql = """
                INSERT OR IGNORE INTO tool_expected_output VALUES
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
                    (1, 8, 'treated2.fastq');
                """;
        executeSql(sql, con);
    }

    /**
     * Insert data in Parameter table.<br>
     * <br>
     * Structure of the table: ID, ProjectID, ToolID, Parameter, Value.
     * 
     * @param con Connection to the database.
     */
    private static void populateParameterTable(Connection con) {

        String sql = """
                INSERT OR IGNORE INTO parameter VALUES
                    (1, 1, 1,'--locustag','default'),
                    (2, 1, 1, '--cpus', 4),
                    (3, 1, 1, '--prefix', 'default'),
                    (4, 1, 1, '--kingdom', 'Bacteria'),
                    (5, 1, 1, '--outdir', '%output%'),
                    (6, 1, 1, '--force', NULL),
                    (7, 1, 1, '%file%', NULL),

                    (8, 1, 2, '--careful', NULL),
                    (9, 1, 2, '--pe1-1', '%file%'),
                    (10, 1, 2, '--pe1-2', '%file%'),
                    (11, 1, 2, '-o', '%output%'),

                    (12, 1, 3, '--tag1', '%file%'),
                    (13, 1, 3, '--tag2', '%file%'),
                    (14, 1, 3, '--p', 4),
                    (15, 1, 3, '--output', '%output%'),

                    (16, 1, 4, '--input','%input%'),
                    (17, 1, 4, '--output', '%output%'),

                    (18, 1, 5, '-1', '%file%'),
                    (19, 1, 5, '-2', '%file%'),
                    (20, 1, 5, '-0', '%output%'),

                    (21, 1, 6, '--in', '%file%'),
                    (22, 1, 6, '--out', '%output%'),
                    (23, 1, 6, '--mode', 'genome'),
                    (24, 1, 6, '--auto-lineage-prok', NULL),
                    (25, 1, 6, '--cpu', 4),
                    (26, 1, 6, '--force', NULL),

                    (27, 1, 7, '--ref', '%refer%'),
                    (28, 1, 7, '--draft', '%file%'),
                    (29, 1, 7, '--output', '%output%'),

                    (30, 1, 8, '--tag1', '%file%'),
                    (31, 1, 8, '--tag2', '%file%'),
                    (32, 1, 8, '--output', '%output%');

                """;
        executeSql(sql, con);
    }

    /**
     * Insert data in all tables with default values.
     * 
     * @param con Connection to the database.
     */
    private static void insertData(Connection con) {
        populateToolTable(con);
        populateToolPermittedExtensionTable(con);
        populateProjectTable(con);
        populateProjectToolTable(con);
        populateToolExpectedOutputTable(con);
        populateParameterTable(con);
    }
}
