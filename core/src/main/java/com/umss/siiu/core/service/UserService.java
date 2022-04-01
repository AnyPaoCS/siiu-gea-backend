package com.umss.siiu.core.service;

import com.umss.siiu.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService extends GenericService<User> {

    Page<User> findUsers(Pageable pageable);

    User findByEmail(String email);

    User findSystemUser();

    User findOnlyEnabledByEmailWithRoles(String email);

    boolean isUserRegistered(String email);

    int updatePasswordByEmail(String email, String newPassword);

    User save(String firstName, String lastName, String email, String password, String typeUser);

    UserDetails findUserDetails(String email);

    User update(User model);
}
