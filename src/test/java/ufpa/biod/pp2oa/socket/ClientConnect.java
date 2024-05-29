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
package ufpa.biod.pp2oa.socket;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ufpa.biod.pp2oa.model.Functions;
import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.model.ParameterType;
import ufpa.biod.pp2oa.model.Project;
import ufpa.biod.pp2oa.model.Tool;
import ufpa.biod.pp2oa.utils.FileUtils;

/**
 *
 * @author Cleo
 */
public final class ClientConnect {

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String serverIp;
    private int serverPort;

    public ClientConnect() {
        createSocket();

    }

    public void createSocket() {

        try {
            serverIp = "localhost";
            serverPort = 16002;
            socket = new Socket(serverIp, serverPort);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Socket connection successful");
        } catch (IOException ex) {
            System.err.println("Socket is Closed!");
        }

    }

    public Message sendMessage(Message message) {
        checkSocket();
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();

            return (Message) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void checkSocket() {
        try {
            objectOutputStream.writeObject(Message.builder().function(Functions.CHECK_SOCKET).build());
            objectOutputStream.flush();
            objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            createSocket();
        }

    }

    public void sendFile(List<File> file, Project project) {
        try {

            Message message = new Message();
            message.setFunction(Functions.SEND_FILE);
            message.addParameter(ParameterType.PROJECT, project);
            message.addParameter(ParameterType.FILE_LIST, file);
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();

            file.forEach(file1 -> {
                FileUtils.sendFile(objectOutputStream, file1);
            });
        } catch (IOException ex) {
            Logger.getLogger(ClientConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Falta implementar a lógica do download
    public void downloadFile(File file, Project project) {
        try {
            Message message = Message.builder().function(Functions.DOWNLOAD_RESULT).build();
            message.addParameter(ParameterType.PROJECT, project);
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            FileUtils.writeFile(objectInputStream, file);
        } catch (IOException ex) {
            Logger.getLogger(ClientConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveProject(Project project) {
        Message message = Message.builder().function(Functions.SAVE_PROJECT).build();
        message.addParameter(ParameterType.PROJECT, project);
        sendMessage(message);
    }

    public void deleteProject(Project project) {
        Message message = Message.builder().function(Functions.DELETE_PROJECT).build();
        message.addParameter(ParameterType.PROJECT, project);
        sendMessage(message);
    }

    public List<Project> getProjectList() {
        Message message = Message.builder().function(Functions.GET_PROJECT_LIST).build();
        Message receivedMessage = sendMessage(message);

        return receivedMessage.getParameter(ParameterType.PROJECT_LIST);
    }

    public void saveTool(Tool tool) {
        Message message = Message.builder()
                .function(Functions.SAVE_TOOL).build();
        message.addParameter(ParameterType.TOOL, tool);

        sendMessage(message);
    }

    public void deleteTool(Tool tool) {
        Message message = Message.builder()
                .function(Functions.DELETE_TOOL).build();
        message.addParameter(ParameterType.TOOL, tool);

        sendMessage(message);
    }

    public List<Tool> getToolList() {
        Message message = Message.builder().function(Functions.GET_TOOL_LIST).build();
        Message receivedMessage = sendMessage(message);

        return receivedMessage.getParameter(ParameterType.TOOL_LIST);
    }

    public Message login(String password) {
        Message message = Message.builder().function(Functions.LOGIN).build();
        message.addParameter(ParameterType.PASSWORD, password);
        return sendMessage(message);
    }

    public void changePassword(String password, String newPassword) {
        Message message = Message.builder().function(Functions.CHANGE_PASSWORD).build();
        message.addParameter(ParameterType.PASSWORD, password);
        message.addParameter(ParameterType.NEW_PASSWORD, newPassword);
        sendMessage(message);
    }

    // Falta implementar a lógica do stopProject
    public void checkProcessIsRunning(Project project) {
        Message message = Message.builder().function(Functions.CHECK_PROCESS_IS_RUNNING).build();
        message.addParameter(ParameterType.PROJECT, project);
        sendMessage(message);
    }

    // Falta implementar a lógica do startProject
    public void startProject(Project project) {
        Message message = Message.builder().function(Functions.EXECUTE_TOOL).build();
        message.addParameter(ParameterType.PROJECT, project);
        sendMessage(message);
    }
}
