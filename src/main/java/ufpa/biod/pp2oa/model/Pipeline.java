package ufpa.biod.pp2oa.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pipeline")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pipeline implements Serializable {
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

    public Pipeline(Project project, Tool tool) {
        this.project = project;
        this.tool = tool;
        this.id = new ProjectToolId(project.getId(), tool.getId());
    }



}
