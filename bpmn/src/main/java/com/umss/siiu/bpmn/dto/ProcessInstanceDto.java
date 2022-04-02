package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.processes.ProcessInstance;
import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.dto.UserDto;

import org.modelmapper.ModelMapper;

import java.util.List;

public class ProcessInstanceDto extends DtoBase<ProcessInstance> {

    private List<TaskInstanceDto> tasks;

    private UserDto user;

    private boolean manual = false;
    private boolean paused = false;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<TaskInstanceDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskInstanceDto> tasks) {
        this.tasks = tasks;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ProcessInstanceDto toDto(ProcessInstance process, ModelMapper mapper) {
        super.toDto(process, mapper);
        setTasks(new TaskInstanceDto().toListDto(process.getTaskInstances(), mapper));
        setUser((UserDto) new UserDto().toDto(process.getUser(), mapper));
        return this;
    }

}
