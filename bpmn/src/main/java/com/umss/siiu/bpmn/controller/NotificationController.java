package com.umss.siiu.bpmn.controller;


import com.umss.siiu.bpmn.dto.NotificationDto;
import com.umss.siiu.bpmn.model.Notification;
import com.umss.siiu.bpmn.service.NotificationService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.service.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/notifications")
public class NotificationController extends GenericController<Notification, NotificationDto> {

    private SimpMessageSendingOperations messagingTemplate;
    private NotificationService notificationService;

    public NotificationController(SimpMessageSendingOperations messagingTemplate,
            NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @MessageMapping("/notification/{jobId}")
    public void processMessageFromClient(@Payload String message, @DestinationVariable String jobId) throws Exception {
        messagingTemplate.convertAndSend("/notifications/" + jobId, message);
    }

    @MessageExceptionHandler
    public void handleException(Throwable exception) {
        messagingTemplate.convertAndSend("/errors", exception.getMessage());
    }

    @GetMapping("/last")
    public ResponseEntity<List<NotificationDto>> findLastNotifications(@RequestParam("email") String email) {
        List<Notification> notifications = notificationService.findTop20ByUserEmailOrderByIdDesc(email);
        return new ResponseEntity<>(super.toDto(notifications), HttpStatus.OK);
    }

    @PutMapping("/checkAllAsRead")
    public ResponseEntity<?> checkAllAsRead(@RequestParam("email") String email) {
        notificationService.markAllAsRead(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    protected GenericService<Notification> getService() {
        return notificationService;
    }

}
