package com.xlr8code.server.user.controller;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.swagger.annotation.ErrorResponse;
import com.xlr8code.server.user.dto.*;
import com.xlr8code.server.user.exception.UserMetadataNotFoundException;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.exception.UsernameAlreadyTakenException;
import com.xlr8code.server.user.service.UserMetadataService;
import com.xlr8code.server.user.service.UserPreferencesService;
import com.xlr8code.server.user.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(Endpoint.User.BASE_PATH)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMetadataService userMetadataService;
    private final UserPreferencesService userPreferencesService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllUsers(@RequestParam Map<String, String> queryParameters) {
        var users = this.userService.findAll(queryParameters);
        return ResponseEntity.ok(users);
    }

    @ApiResponses(
            @ApiResponse(responseCode = "200")
    )
    @ErrorResponse(value = {
            UserNotFoundException.class,
            UserMetadataNotFoundException.class,
            UsernameAlreadyTakenException.class
    },
        useFilterExceptions = true
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String id) {
        var userDTO = this.userService.findByUUID(id);

        return ResponseEntity.ok(userDTO);
    }

    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        this.userService.deleteByUUID(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable String id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        var updatedUserDTO = this.userService.updateByUUID(id, updateUserDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    @PutMapping("/{id}" + Endpoint.User.METADATA)
    public ResponseEntity<UserMetadataDTO> updateUserMetadataById(@PathVariable String id, @Valid @RequestBody UpdateUserMetadataDTO updateUserMetadataDTO) {
        var updatedUserDTO = this.userMetadataService.updateMetadataByUserUUID(id, updateUserMetadataDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    @PutMapping("/{id}" + Endpoint.User.PREFERENCES)
    public ResponseEntity<UserPreferencesDTO> updateUserPreferencesByUserId(@PathVariable String id, @Valid @RequestBody UpdateUserPreferencesDTO updateUserPreferencesDTO) {
        var updatedUserDTO = this.userPreferencesService.updateUserPreferencesByUserUUID(id, updateUserPreferencesDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    @PatchMapping("/{id}" + Endpoint.User.PASSWORD)
    public ResponseEntity<Void> updateUserPasswordById(@PathVariable String id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        this.userService.updateUserPassword(id, updatePasswordDTO);

        return ResponseEntity.noContent().build();
    }

}
