package com.umss.siiu.bpmn.dto;


import com.umss.siiu.bpmn.model.NotificationType;
import com.umss.siiu.core.dto.DtoBase;

public class NotificationTypeDto extends DtoBase<NotificationType> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
