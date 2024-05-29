package ufpa.biod.pp2oa.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProjectToolId implements Serializable {

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "tool_id")
    private Integer toolId;

}
