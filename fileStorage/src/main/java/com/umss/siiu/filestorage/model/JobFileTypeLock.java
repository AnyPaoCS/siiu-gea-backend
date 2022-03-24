/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.model;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_JobFileTypeLock_job_fileType",
                columnNames = {"job_id", "file_type_id"})})
public class JobFileTypeLock extends ModelBase {
    @OneToOne
    private Job job;
    @ManyToOne
    private FileType fileType;
    @OneToOne
    private Employee employee;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
