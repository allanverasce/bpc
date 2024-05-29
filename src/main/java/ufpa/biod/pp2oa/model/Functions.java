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
package ufpa.biod.pp2oa.model;

import java.util.function.Consumer;

import ufpa.biod.pp2oa.function.ChangePasswordFunction;
import ufpa.biod.pp2oa.function.CheckProcessIsRunningFunction;
import ufpa.biod.pp2oa.function.CheckSocketFunction;
import ufpa.biod.pp2oa.function.CompressResultFunction;
import ufpa.biod.pp2oa.function.DeleteProjectFunction;
import ufpa.biod.pp2oa.function.DeleteToolFunction;
import ufpa.biod.pp2oa.function.DownloadFileFunction;
import ufpa.biod.pp2oa.function.ExecuteToolFunction;
import ufpa.biod.pp2oa.function.FunctionContext;
import ufpa.biod.pp2oa.function.GetProjectByHashFunction;
import ufpa.biod.pp2oa.function.GetProjectFunction;
import ufpa.biod.pp2oa.function.GetProjectListFunction;
import ufpa.biod.pp2oa.function.GetToolListFunction;
import ufpa.biod.pp2oa.function.LoginFunction;
import ufpa.biod.pp2oa.function.SaveProjectFunction;
import ufpa.biod.pp2oa.function.SaveToolFunction;
import ufpa.biod.pp2oa.function.SendFileFunction;

/**
 * - CHECKSOCKET: checks if the socket connection is active.<br>
 * <br>
 * - SENDFILE: sends a file through the socket connection.<br>
 * <br>
 * - DOWNLOADFILE: downloads a file through the socket connection.<br>
 * <br>
 * - SAVEPROJECT: saves a project specified in the message to a database.<br>
 * <br>
 * - DELETEPROJECT: removes a project specified in the message from the
 * database.<br>
 * <br>
 *
 * - GETPROJECTLIST: returns a list of all projects in the database.<br>
 * <br>
 *
 * - SAVETOOL: saves a tool specified in the message to a database.<br>
 * <br>
 *
 * - DELETETOOL: removes a tool specified in the message from the database.<br>
 * <br>
 *
 * - GETTOOLLIST: returns a list of all tools in the database.<br>
 * <br>
 *
 * - STARTPROJECT: starts a project specified in the message.<br>
 * <br>
 *
 * - LOGIN: login to the system.<br>
 * <br>
 *
 * - CHANGEPASSWORD: change the password of the user.<br>
 * <br>
 *
 * - CHECKPROCESSISRUNNING: checks if the process is running.<br>
 * <br>
 *
 *
 */
public enum Functions {
        CHECK_SOCKET(new CheckSocketFunction()::execute),
        SEND_FILE(new SendFileFunction()::execute),
        DOWNLOAD_RESULT(new DownloadFileFunction()::execute),
        COMPRESS_RESULT(new CompressResultFunction()::execute),
        SAVE_PROJECT(new SaveProjectFunction()::execute),
        DELETE_PROJECT(new DeleteProjectFunction()::execute),
        GET_PROJECT(new GetProjectFunction()::execute),
        GET_PROJECT_BY_HASH(new GetProjectByHashFunction()::execute),
        GET_PROJECT_LIST(new GetProjectListFunction()::execute),
        SAVE_TOOL(new SaveToolFunction()::execute),
        DELETE_TOOL(new DeleteToolFunction()::execute),
        GET_TOOL_LIST(new GetToolListFunction()::execute),
        LOGIN(new LoginFunction()::execute),
        CHANGE_PASSWORD(new ChangePasswordFunction()::execute),
        CHECK_PROCESS_IS_RUNNING(new CheckProcessIsRunningFunction()::execute),
        EXECUTE_TOOL(new ExecuteToolFunction()::execute);

        private Consumer<FunctionContext> function;

        Functions(Consumer<FunctionContext> function) {
                this.function = function;
        }

        public void execute(FunctionContext context) {
                function.accept(context);
        }
}
