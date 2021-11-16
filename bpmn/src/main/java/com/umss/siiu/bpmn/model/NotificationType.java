package com.umss.siiu.bpmn.model;


import com.umss.siiu.bpmn.dto.NotificationTypeDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NotificationType extends ModelBase<NotificationTypeDto> {

    @Column(nullable = false, length = 50)
    private String name;

    public NotificationType() {
        super();
    }

    public NotificationType(long id, String name) {
        super();
        super.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
