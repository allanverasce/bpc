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
package ufpa.biod.pp2oa.function;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ufpa.biod.pp2oa.model.Message;
import ufpa.biod.pp2oa.utils.BotTelegram;

public interface CallableFunction {

  /**
   * @param context the {@link FunctionContext} that contains the necessary
   *                information to execute this function, such as the
   *                {@link Message} object, the {@link BotTelegram} object and the
   *                {@link ObjectInputStream} and {@link ObjectOutputStream}
   *                streams.
   * @return the result of the function execution true if the function was
   *         executed successfully, false otherwise.
   */
  void execute(FunctionContext context);

}
