package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.Notification;
import com.umss.siiu.bpmn.repository.NotificationRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import com.umss.siiu.core.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl extends GenericServiceImpl<Notification> implements NotificationService {

    private NotificationRepository repository;
    private UserService userService;
    private NotificationTypeService notificationTypeService;

    public NotificationServiceImpl(NotificationRepository repository,
            EmailService emailService, UserService userService,
            NotificationTypeService notificationTypeService,
            ModelMapper modelMapper) {
        this.repository = repository;
        this.userService = userService;
        this.notificationTypeService = notificationTypeService;
    }

    @Override
    protected GenericRepository<Notification> getRepository() {
        return repository;
    }

    @Override
    public List<Notification> findTop20ByUserEmailOrderByIdDesc(String email) {
        return repository.findTop20ByUserEmailOrderByIdDesc(email);
    }

    @Override
    public int markAllAsRead(String email) {
        return repository.markAllAsRead(email);
    }

    @Override
    public void sendNotifications(String email, long notificationTypeId, String title, String message) {
        // Save notification
        Notification notification = new Notification(message,
                notificationTypeService.findById(notificationTypeId), userService.findByEmail(email), false);
        repository.save(notification);
        // Send email
        // Map<String, Object> parameters = new HashMap<>();
        // String[] to = {email};
        // parameters.put("message", message);
        // emailService.sendMail(new MailDto(to, title, "notification-template",
        // parameters));
        // Send push notification
        // messagingTemplate.convertAndSendToUser(email, "/notifications",
        // new NotificationDto().toDto(notification, modelMapper));
    }

}
