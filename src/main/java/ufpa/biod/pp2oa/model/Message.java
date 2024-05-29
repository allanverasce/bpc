
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

import java.io.Serializable;
import java.util.EnumMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cleo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;
    private Functions function;
    private EnumMap<ParameterType, Object> parameter;
    private MessageStatus messageStatus;

    /**
     * Adds a parameter to the message.
     *
     * @param type  {@link #ParameterType}
     * @param value {@link #Object} to be added
     * @throws IllegalArgumentException
     *                                  if the parameter value is null
     */
    public void addParameter(ParameterType type, Object value) {
        if (parameter == null) {
            parameter = new EnumMap<>(ParameterType.class);
        }

        if (value == null) {
            throw new IllegalArgumentException("Parameter value cannot be null");
        }
        parameter.put(type, value);
    }

    /**
     * Returns the parameter value for the given type.
     *
     * @param type {@link #ParameterType} to be returned
     * @return object casted to the expected type
     * @throws IllegalArgumentException
     *                                  if the parameter is not found or the
     *                                  parameter value is not of the expected type
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(ParameterType type) {
        Class<T> clazz = (Class<T>) type.getType();

        if (parameter == null) {
            throw new IllegalArgumentException("Parameter not found: " + type);
        }

        if (!parameter.containsKey(type)) {
            throw new IllegalArgumentException("Parameter not found: " + type);
        }
        Object value = parameter.get(type);
        if (!clazz.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException(
                    "Invalid parameter type. Expected: " + clazz + ", got: " + value.getClass());
        }
        return clazz.cast(value);
    }

}
