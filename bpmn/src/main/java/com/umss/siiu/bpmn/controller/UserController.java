package com.umss.siiu.bpmn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umss.siiu.bpmn.dto.MailDto;
import com.umss.siiu.bpmn.dto.OperationResultDto;
import com.umss.siiu.bpmn.dto.TokenDto;
import com.umss.siiu.bpmn.service.EmailService;
import com.umss.siiu.core.controller.GenericController;
import com.umss.siiu.core.dto.EmployeeDto;
import com.umss.siiu.core.dto.UserDto;
import com.umss.siiu.core.model.User;
import com.umss.siiu.core.service.GenericService;
import com.umss.siiu.core.service.TokenService;
import com.umss.siiu.core.service.UserService;
import com.umss.siiu.core.util.ApplicationConstants;
import io.jsonwebtoken.JwtException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController("users")
public class UserController extends GenericController<User, UserDto> {

    private UserService userService;
    private TokenService tokenService;
    private EmailService emailService;
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService, TokenService tokenService, EmailService emailService,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeDto> getUserEmployee(@RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(getEmployee(userService.findByEmail(userDto.getEmail())), HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> signIn(@RequestBody UserDto userDto) throws JsonProcessingException {
        ResponseEntity<Object> responseEntity = null;
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
            responseEntity = new ResponseEntity<>(
                    new TokenDto(tokenService.generateTokenByDay(10, authentication.getPrincipal(), true)),
                    HttpStatus.OK);
        } catch (AuthenticationException e) {
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("messages.user.invalidCredentials"),
                    HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    /**
     * This method check if the token is valid and also checks if the user
     * performing the action is the same for whom the token was created.
     *
     * @throws IOException
     */
    @PostMapping("/signUp")
    public ResponseEntity<Object> signUp(@RequestBody UserDto userDto, @RequestParam("token") String token)
            throws IOException {
        ResponseEntity<Object> responseEntity = null;
        try {
            UserDto tokenInformation = tokenService.getTokenInformation(token, UserDto.class);

            if (!userService.isUserRegistered(tokenInformation.getEmail())) {
                userService.save(userDto.getFirstName(), userDto.getLastName(), tokenInformation.getEmail(),
                        userDto.getPassword());
                responseEntity = new ResponseEntity<>(new TokenDto(tokenService.generateTokenByDay(10,
                        userService.findUserDetails(tokenInformation.getEmail()), true)), HttpStatus.OK);
            } else {
                throw new ValidationException();
            }
        } catch (JwtException e) {
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("messages.user.unauthorized"),
                    HttpStatus.UNAUTHORIZED);
        } catch (ValidationException e) {
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("messages.user.duplicatedUser"),
                    HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/sendSignUpInvitation")
    public ResponseEntity<Object> inviteNewUser(@Valid @RequestBody UserDto user,
            @RequestParam("redirect") String redirect)
            throws JsonProcessingException {
        Map<String, Object> parameters = new HashMap<>();
        String[] to = {user.getEmail()};
        String url = redirect + "?token=" + tokenService.generateTokenByDay(1, user, false);
        parameters.put("invitationLink", url);
        emailService.sendMail(new MailDto(to, "Subscription link", "invitation-template", parameters));
        return new ResponseEntity<>(new OperationResultDto<>("messages.user.invitedUser"), HttpStatus.OK);
    }

    @PostMapping("/forgottenPassword")
    public ResponseEntity<Object> sendForgottenPasswordEmail(@Valid @RequestBody UserDto user,
            @RequestParam("redirect") String redirect)
            throws JsonProcessingException {
        ResponseEntity<Object> responseEntity = null;
        if (userService.isUserRegistered(user.getEmail())) {
            Map<String, Object> parameters = new HashMap<>();
            String[] to = {user.getEmail()};
            String url = redirect + "?token=" + tokenService.generateTokenByDay(1, user, false);
            parameters.put("forgottenPasswordLink", url);
            emailService.sendMail(new MailDto(to, "Forgotten password", "forgotten-password-template", parameters));
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("messages.user.forgottenPasswordSent"),
                    HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("messages.user.userNotFound"),
                    HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @PostMapping("/restorePassword")
    public ResponseEntity<Object> restorePassword(@RequestBody UserDto userDto, @RequestParam("token") String token)
            throws IOException {
        ResponseEntity<Object> responseEntity = null;
        try {
            UserDto tokenInformation = tokenService.getTokenInformation(token, UserDto.class);
            int operationResult = userService.updatePasswordByEmail(tokenInformation.getEmail(), userDto.getPassword());
            responseEntity = (operationResult == ApplicationConstants.ONE_RESULT_MODIFIED)
                    ? new ResponseEntity<>(new TokenDto(tokenService.generateTokenByDay(10,
                    userService.findUserDetails(tokenInformation.getEmail()), true)), HttpStatus.OK)
                    : new ResponseEntity<>(new OperationResultDto<>("messages.user.notRestoredPassword"),
                    HttpStatus.BAD_REQUEST);
        } catch (JwtException e) {
            responseEntity = new ResponseEntity<>(new OperationResultDto<>("messages.token.invalidToken"),
                    HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    @GetMapping("/paginated")
    public ResponseEntity<Object> findPaginatedUsers(@RequestParam("page") int page, @RequestParam("size") int size,
            @RequestParam("filter") String filter,
            @RequestParam("isAsc") boolean isAsc) {
        Page<UserDto> paginatedResults = userService
                .findUsers(PageRequest.of(page - 1, size, super.getSortType(isAsc, filter))).map(this::toDto);
        return new ResponseEntity<>(paginatedResults, HttpStatus.OK);
    }

    @Override
    protected GenericService<User> getService() {
        return userService;
    }

    private EmployeeDto getEmployee(User user) {
        if (user != null && user.getEmployee() != null) {
            return (EmployeeDto) new EmployeeDto().toDto(user.getEmployee(), modelMapper);
        }
        throw new NoSuchElementException("User does not have an employee asociated or does not exist");
    }

    @Override
    @GetMapping(value = "/model/{id}")
    public User findModelById(Long id) {
        return super.findModelById(id);
    }

}
