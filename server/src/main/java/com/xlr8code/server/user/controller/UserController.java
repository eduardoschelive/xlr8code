package com.xlr8code.server.user.controller;

import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.filter.annotation.FilterEndpoint;
import com.xlr8code.server.openapi.annotation.ErrorResponse;
import com.xlr8code.server.user.dto.*;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.EmailAlreadyInUseException;
import com.xlr8code.server.user.exception.IncorrectOldPasswordException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import com.xlr8code.server.user.service.UserMetadataService;
import com.xlr8code.server.user.service.UserPreferencesService;
import com.xlr8code.server.user.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.User.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final UserMetadataService userMetadataService;
    private final UserPreferencesService userPreferencesService;

    @FilterEndpoint(User.class)
    @GetMapping
    public ResponseEntity<Page<UserDTO>> listUsers(Specification<User> specification, Pageable pageable) {
        var users = this.userService.findAll(specification, pageable);
        return ResponseEntity.ok(users);
    }


    @ApiResponses(
            @ApiResponse(responseCode = "200")
    )
    @ErrorResponse(UserNotFoundException.class)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUser(@PathVariable String id) {
        var userDTO = this.userService.findByUUID(id);

        return ResponseEntity.ok(userDTO);
    }

    @ApiResponses(
            @ApiResponse(responseCode = "204")
    )
    @ErrorResponse(value = {
            UserNotFoundException.class
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        this.userService.deleteByUUID(id);

        return ResponseEntity.noContent().build();
    }

    @ApiResponses(
            @ApiResponse(responseCode = "200")
    )
    @ErrorResponse(value = {
            UserNotFoundException.class,
            UsernameAlreadyTakenException.class,
            EmailAlreadyInUseException.class
    })
    @PutMapping("/{id}")
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        var updatedUserDTO = this.userService.updateByUUID(id, updateUserDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @ApiResponses(
            @ApiResponse(responseCode = "200")
    )
    @ErrorResponse(value = {
            UserNotFoundException.class
    })
    @PutMapping("/{id}" + Endpoint.User.METADATA)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserMetadataDTO> updateUserMetadata(@PathVariable String id, @Valid @RequestBody UpdateUserMetadataDTO updateUserMetadataDTO) {
        var updatedUserDTO = this.userMetadataService.updateMetadataByUserUUID(id, updateUserMetadataDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @ApiResponses(
            @ApiResponse(responseCode = "200")
    )
    @ErrorResponse(value = {
            UserNotFoundException.class
    })
    @PutMapping("/{id}" + Endpoint.User.PREFERENCES)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserPreferencesDTO> updateUserPreferences(@PathVariable String id, @Valid @RequestBody UpdateUserPreferencesDTO updateUserPreferencesDTO) {
        var updatedUserDTO = this.userPreferencesService.updateUserPreferencesByUserUUID(id, updateUserPreferencesDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @ApiResponses(
            @ApiResponse(responseCode = "200")
    )
    @ErrorResponse(value = {
            UserNotFoundException.class,
            IncorrectOldPasswordException.class,
            PasswordMatchException.class
    })
    @PatchMapping("/{id}" + Endpoint.User.PASSWORD)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<Void> updateUserPassword(@PathVariable String id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        this.userService.updateUserPassword(id, updatePasswordDTO);

        return ResponseEntity.noContent().build();
    }

}
