package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.Notification;
import com.umss.siiu.core.service.GenericService;

import java.util.List;

public interface NotificationService extends GenericService<Notification> {
    List<Notification> findTop20ByUserEmailOrderByIdDesc(String email);

    int markAllAsRead(String email);

    void sendNotifications(String email, long notificationTypeId, String title, String message);
}
