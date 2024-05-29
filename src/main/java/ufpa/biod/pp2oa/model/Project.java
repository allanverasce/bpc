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
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "chat_id", nullable = true)
    private Long chatId;

    @Column(name = "download_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private DownloadMode downloadMode;

    @Column(name = "project_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionStatus projectStatus;

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.MERGE)
    @Fetch(FetchMode.JOIN)
    private List<ProjectTool> projectTools;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pipeline> pipeline;

    // @OneToMany
    // @JoinTable(name = "pipeline", joinColumns = @JoinColumn(name = "project_id"),
    // inverseJoinColumns = @JoinColumn(name = "tool_id"))
    // private List<Project> pipeline;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "first_tool_id", referencedColumnName = "id", nullable = true)
    private Tool firstTool;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "current_tool_id", referencedColumnName = "id", nullable = true)
    private Tool currentTool;

    private Long pid;

    @Column(name = "telegram_bot", nullable = false)
    private boolean telegramBot;

    private String hash;

    public void addProjectTool(ProjectTool projectTool) {
        if (this.projectTools == null) {
            this.projectTools = new ArrayList<>();
        }
        this.projectTools.add(projectTool);
    }

    public void removeProjectTool(ProjectTool projectTool) {
        this.projectTools.remove(projectTool);
    }

    public Optional<Long> getChatId() {
        return Optional.ofNullable(chatId);
    }

    public File getInputDirectoryPath() {
        return new File("inputs/" + this.getName() + "/" + this.getCurrentTool().getName() + "/");
    }

    public File getOutputDirectoryPath() {
        return new File("outputs/" + this.getName() + "/" + this.getCurrentTool().getName() + "/");
    }

    public List<Parameter> getParameters() {
        return this.getProjectTools().stream()
                .filter(projectTool -> projectTool.getTool().getName().equals(this.getCurrentTool().getName()))
                .findFirst()
                .get()
                .getParameters();
    }

}
