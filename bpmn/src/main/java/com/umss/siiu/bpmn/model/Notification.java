package com.umss.siiu.bpmn.model;

import com.umss.siiu.bpmn.dto.NotificationDto;
import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.core.model.User;

import javax.persistence.*;

@Entity
public class Notification extends ModelBase<NotificationDto> {

    @Column(nullable = false, length = 250)
    private String message;
    @Column(nullable = true, length = 500)
    private String mainUrl;
    @ManyToOne
    @JoinColumn(nullable = false)
    private NotificationType notificationType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private boolean isRead;

    public Notification() {
        super();
    }

    public Notification(String message, NotificationType notificationType, User user, boolean isRead) {
        super();
        this.message = message;
        this.notificationType = notificationType;
        this.user = user;
        this.isRead = isRead;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

}
