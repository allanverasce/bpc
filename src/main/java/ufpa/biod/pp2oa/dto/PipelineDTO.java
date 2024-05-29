package ufpa.biod.pp2oa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString(exclude = { "project", "tool" })
@EqualsAndHashCode(of = { "project", "tool" })
public class PipelineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private PipelineIdDTO id;

    private ProjectDto project;

    private ToolDto tool;

    public PipelineDTO(ProjectDto project, ToolDto tool) {
        this.project = project;
        this.tool = tool;
        this.id = new PipelineIdDTO(project.getId(), tool.getId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public class PipelineIdDTO implements Serializable {
        private Integer projectId;

        private Integer toolId;
    }
}
