package ufpa.biod.pp2oa.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PipelineId implements Serializable {
    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "tool_id")
    private Integer toolId;
}
