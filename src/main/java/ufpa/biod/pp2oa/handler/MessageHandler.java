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
package ufpa.biod.pp2oa.handler;

import java.io.IOException;
import java.io.ObjectOutputStream;

import lombok.extern.slf4j.Slf4j;
import ufpa.biod.pp2oa.function.FunctionContext;
import ufpa.biod.pp2oa.model.Message;

/**
 * Abstract class that represents a message handler
 */
@Slf4j
public abstract class MessageHandler {

    private MessageHandler next;
    private ObjectOutputStream output;
    protected Message message;

    public MessageHandler setNext(MessageHandler next) {
        if (this.next != null) {
            this.next.setNext(next);
        } else {
            this.next = next;
        }
        return this;
    }

    public void runHandle(FunctionContext functionContext) {
        initVariables(functionContext);

        if (mustHandlerMessage()) {
            log.info("Running Handler: {}", this.getClass().getSimpleName());
            try {
                logHandler();
                handleMessage();
            } catch (IOException e) {
                log.error("Error sending message to client", e);
            }

        } else {
            runNext(functionContext);
        }

    }

    private void runNext(FunctionContext functionContext) {
        if (next != null) {
            next.runHandle(functionContext);
        }
    }

    protected void initVariables(FunctionContext functionContext) {
        this.output = functionContext.getOutput();
        this.message = functionContext.getMessage();
    }

    /**
     * Process the message and send it to the next handler if the message
     * status is not ERROR
     * 
     * @throws IOException
     */
    protected void handleMessage() throws IOException {
        output.reset();
        output.writeObject(message);
        output.flush();
    }

    protected abstract void logHandler();

    protected abstract boolean mustHandlerMessage();

}