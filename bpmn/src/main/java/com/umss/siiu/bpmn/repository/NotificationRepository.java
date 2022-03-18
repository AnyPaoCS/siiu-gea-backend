package com.umss.siiu.bpmn.repository;

import com.umss.siiu.bpmn.model.Notification;
import com.umss.siiu.core.repository.GenericRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends GenericRepository<Notification> {

    List<Notification> findTop20ByUserEmailOrderByIdDesc(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE notification _notification INNER JOIN user _user ON _notification.user_id = _user.id "
            + "SET _notification.is_read = true WHERE _user.email = :email AND _notification.is_read = false",
            nativeQuery = true)
    int markAllAsRead(@Param("email") String email);
}
