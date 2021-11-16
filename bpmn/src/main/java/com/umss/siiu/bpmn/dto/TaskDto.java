package com.umss.siiu.bpmn.dto;

import com.umss.siiu.core.dto.DtoBase;
import org.springframework.scheduling.config.Task;

public class TaskDto extends DtoBase<Task> {

    private String name;
    private String code;

    public TaskDto() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
