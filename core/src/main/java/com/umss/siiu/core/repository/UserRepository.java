package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {

    User findByEmail(@Param("email") String email);

    User findBySystemUserTrue();

    @Query("FROM User user INNER JOIN FETCH user.roles WHERE user.email = :email AND user.enabled = true")
    User findOnlyEnabledByEmailWithRoles(@Param("email") String email);

    @Query("FROM User user INNER JOIN FETCH user.roles WHERE user.id = :id AND user.enabled = true")
    Optional<User> findById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.password = :newPassword WHERE user.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("newPassword") String newPassword);

    @Query(value = "FROM User user INNER JOIN FETCH user.roles WHERE user.enabled = true AND user.systemUser = false",
            countQuery = "SELECT COUNT(user.id) FROM User user WHERE user.enabled = true AND user.systemUser = false")
    Page<User> findAllWithRoles(Pageable pageable);
}
