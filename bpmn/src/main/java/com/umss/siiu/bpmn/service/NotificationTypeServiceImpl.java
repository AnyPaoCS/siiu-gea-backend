package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.NotificationType;
import com.umss.siiu.bpmn.repository.NotificationTypeRepository;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class NotificationTypeServiceImpl extends GenericServiceImpl<NotificationType>
        implements NotificationTypeService {

    private NotificationTypeRepository repository;

    public NotificationTypeServiceImpl(NotificationTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<NotificationType> getRepository() {
        return repository;
    }

}
