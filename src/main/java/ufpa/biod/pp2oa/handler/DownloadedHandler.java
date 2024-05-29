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

import lombok.extern.slf4j.Slf4j;
import ufpa.biod.pp2oa.model.MessageStatus;

@Slf4j
public class DownloadedHandler extends MessageHandler {

    /**
     * Process the message and send it to the next handler if the message
     * status is not DOWNLOADED
     */
    @Override
    public void handleMessage() {
        log.info("Download Success\n");
    }

    @Override
    protected boolean mustHandlerMessage() {
        return message.getMessageStatus() == MessageStatus.DOWNLOADED;
    }

    @Override
    protected void logHandler() {
        log.info("Success Executing Function: {}", message.getFunction());
    }
}
