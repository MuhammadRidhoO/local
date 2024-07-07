package task.company.local.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import task.company.local.config.JwtService;
import task.company.local.dto.request.login_request;
import task.company.local.dto.request.register_request;
import task.company.local.dto.response.UserAlreadyExistException;
import task.company.local.dto.response.errorResponse;
import task.company.local.dto.response.login_response;
import task.company.local.dto.response.register_response;
import task.company.local.dto.response.response_user_login;
import task.company.local.dto.response.user_response;
import task.company.local.entity.CustomUserDetails;
import task.company.local.entity.user_entity;
import task.company.local.service.user_service;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/todo")
public class user_controller {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  private final user_service user_service;

  public user_controller(user_service user_service) {
    this.user_service = user_service;
  }

  @GetMapping("/users")
  public List<user_response> getAllUsers() {
    return user_service.getAllUsers();
  }

  @PostMapping("/users/register")
  public ResponseEntity<?> registerUser(@RequestBody register_request request) {
    try {
      user_entity newUser = user_service.RegisterUser(request);
      register_response response = register_response.builder()
          .id(newUser.getId())
          .email(newUser.getEmail())
          .password(newUser.getPassword())
          .fullName(newUser.getFullName())
          .build();
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (UserAlreadyExistException uaeEx) {
      errorResponse errorResponse = new errorResponse(HttpStatus.BAD_REQUEST, "Email already registered");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
  }

  @PostMapping("/users/login")
  public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody login_request login_Request,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
    }
    System.out.println(bindingResult.hasErrors() + " Check isinya");

    String email = login_Request.getEmail();

    Optional<user_entity> userOptional = user_service.findByEmail(email);

    if (userOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Email belum terdaftar. Lakukan registrasi terlebih dahulu.");
    }

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, login_Request.getPassword()));

    System.out.println(authentication + " check dalam controller");

    if (authentication.isAuthenticated()) {
      Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
      String jwtToken = jwtService.generateToken(email, userId);
      return ResponseEntity.ok(new login_response(email, jwtToken));
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

  }

  @GetMapping("/userlogin")
  public ResponseEntity<?> getUserInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();
      try {
        response_user_login userDto = user_service.getUserByUsername(username);
        return ResponseEntity.ok(userDto);
      } catch (UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
  }

}
