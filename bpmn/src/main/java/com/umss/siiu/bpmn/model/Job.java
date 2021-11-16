/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model;

import com.umss.siiu.bpmn.dto.JobDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;

@Entity
public class Job extends ModelBase<JobDto> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_JOB_JOBBPM"))
    private JobBpm jobBpm;

    public JobBpm getJobBpm() {
        return jobBpm;
    }

    public void setJobBpm(JobBpm jobBpm) {
        this.jobBpm = jobBpm;
    }
}
