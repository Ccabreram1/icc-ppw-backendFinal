package ec.edu.ups.icc.portafolio.modules.users.controllers;

import ec.edu.ups.icc.portafolio.modules.users.dtos.UserRequestDto;
import ec.edu.ups.icc.portafolio.modules.users.dtos.UserResponseDto;
import ec.edu.ups.icc.portafolio.modules.users.dtos.UserUpdateDto;
import ec.edu.ups.icc.portafolio.modules.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#id)")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto createdUser = userService.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#id)")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto userDto) {
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#id)")
    public ResponseEntity<UserResponseDto> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateDto userDto) {
        return ResponseEntity.ok(userService.partialUpdate(id, userDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/programmers")
    public ResponseEntity<List<UserResponseDto>> getProgrammers() {
        return ResponseEntity.ok(userService.findProgrammers());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            Pageable pageable) {
        return ResponseEntity.ok(userService.search(name, email, role, pageable));
    }
}