package com.umss.siiu.bpmn.dto;

import com.umss.siiu.bpmn.model.Notification;
import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.dto.UserDto;

public class NotificationDto extends DtoBase<Notification> {

    private String message;
    private UserDto mainUrl;
    private NotificationTypeDto notificationType;
    private boolean isRead;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDto getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(UserDto mainUrl) {
        this.mainUrl = mainUrl;
    }

    public NotificationTypeDto getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationTypeDto notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

}
