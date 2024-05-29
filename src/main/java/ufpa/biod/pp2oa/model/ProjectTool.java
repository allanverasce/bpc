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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = { "project", "tool" })
@EqualsAndHashCode(of = { "project", "tool" })
@Table(name = "project_tool")
public class ProjectTool implements Serializable {

    private static final long serialVersionUID = 121212L;

    @EmbeddedId
    private ProjectToolId id;

    @ManyToOne
    @MapsId("projectId") // Mapeia o campo "projectId" da chave primária incorporada
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    @MapsId("toolId") // Mapeia o campo "toolId" da chave primária incorporada
    @JoinColumn(name = "tool_id", referencedColumnName = "id")
    private Tool tool;

    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;


    @OneToMany(mappedBy = "projectTool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parameter> parameters;

    @ElementCollection
    @CollectionTable(name = "tool_expected_output", joinColumns = {
            @JoinColumn(name = "project_id", referencedColumnName = "project_id"),
            @JoinColumn(name = "tool_id", referencedColumnName = "tool_id")
    })
    @Column(name = "output_file", nullable = false, unique = true)
    private List<String> expectedOutput;

    public List<File> getExpectedOutputFiles() {
        if (expectedOutput == null) {
            return new ArrayList<>();
        }
        return expectedOutput.stream().map(output -> new File(getProject().getOutputDirectoryPath() + "/" + output))
                .collect(Collectors.toList());
    }

}
