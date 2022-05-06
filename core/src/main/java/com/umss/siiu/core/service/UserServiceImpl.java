package com.umss.siiu.core.service;

import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.model.Role;
import com.umss.siiu.core.model.RoleType;
import com.umss.siiu.core.model.User;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService,
        UserDetailsService {

    private UserRepository userRepository;
    private EmployeeService employeeService;

    private static final String TYPE_APPLICANT = "APPLICANT";

    public UserServiceImpl(UserRepository userSystemRepository, EmployeeService employeeService) {
        this.userRepository = userSystemRepository;
        this.employeeService = employeeService;
    }

    @Override
    public Page<User> findUsers(Pageable pageable) {
        return userRepository.findAllWithRoles(pageable);
    }

    @Override
    @Transactional
    public User save(String firstName, String lastName, String email, String password, String typeUser) {
        var role = new Role();
        Set<Role> roles = new HashSet<>();
        var employee = new Employee();
        var user = new User();

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employeeService.save(employee);

        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEnabled(true);
        if (typeUser.equals(TYPE_APPLICANT)){
            role.setName(RoleType.APPLICANT.getType());
            role.setId(RoleType.APPLICANT.getId());
        } else {
            role.setName(RoleType.GENERAL.getType());
            role.setId(RoleType.GENERAL.getId());
        }
        roles.add(role);
        user.setRoles(roles);
        user.setEmployee(employee);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findSystemUser() {
        return userRepository.findBySystemUserTrue();
    }

    @Override
    public User findById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new NoResultException();
    }

    @Override
    public User findOnlyEnabledByEmailWithRoles(String email) {
        return userRepository.findOnlyEnabledByEmailWithRoles(email);
    }

    @Override
    public boolean isUserRegistered(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public int updatePasswordByEmail(String email, String newPassword) {
        return userRepository.updatePasswordByEmail(email, new BCryptPasswordEncoder().encode(newPassword));
    }

    @Override
    public UserDetails findUserDetails(String email) {
        var user = findOnlyEnabledByEmailWithRoles(email);
        user.setPassword("");
        return convertUserToUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = findOnlyEnabledByEmailWithRoles(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return convertUserToUserDetails(user);
    }

    @Override
    protected GenericRepository<User> getRepository() {
        return userRepository;
    }

    private UserDetails convertUserToUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = getAuthorities(user.getRoles());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User update(User model) {
        var user = findById(model.getId());
        var employee = user.getEmployee();
        // Setting employee values
        employee.setFirstName(model.getEmployee().getFirstName());
        employee.setLastName(model.getEmployee().getLastName());
        employeeService.save(employee);
        // Setting user values
        user.getRoles().clear();
        for (Role role : model.getRoles()) {
            user.getRoles().add(role);
        }
        return userRepository.save(user);
    }
}
