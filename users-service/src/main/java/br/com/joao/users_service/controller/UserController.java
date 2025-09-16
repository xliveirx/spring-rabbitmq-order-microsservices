package br.com.joao.users_service.controller;

import br.com.joao.users_service.domain.User;
import br.com.joao.users_service.dto.UserCreateRequest;
import br.com.joao.users_service.dto.UserEditRequest;
import br.com.joao.users_service.dto.UserResponse;
import br.com.joao.users_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest req) {

        var user = userService.createUser(req);

        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    @DeleteMapping
    public ResponseEntity<Void> disableUser(@AuthenticationPrincipal User logged){

        userService.disableUser(logged);

        return ResponseEntity.noContent().build();

    }

    @PutMapping
    public ResponseEntity<UserResponse> editUser(@AuthenticationPrincipal User logged, UserEditRequest req){

        var user = userService.editUser(logged, req);

        return ResponseEntity.ok(UserResponse.fromDomain(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableUserById(@PathVariable Long id){

        userService.disableUserById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> enableUserById(@PathVariable Long id){

        userService.enableUserById(id);

        return  ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);

        var response = userService.getUsers(pageable);

        return ResponseEntity.ok(response.map(UserResponse::fromDomain));
    }
}
