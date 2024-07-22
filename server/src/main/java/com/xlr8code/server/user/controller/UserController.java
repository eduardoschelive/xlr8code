package com.xlr8code.server.user.controller;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.openapi.annotation.ErrorResponse;
import com.xlr8code.server.openapi.annotation.FilterEndpoint;
import com.xlr8code.server.user.dto.*;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.service.UserMetadataService;
import com.xlr8code.server.user.service.UserPreferencesService;
import com.xlr8code.server.user.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final UserMetadataService userMetadataService;
    private final UserPreferencesService userPreferencesService;

    @FilterEndpoint(User.class)
    @GetMapping
    public ResponseEntity<Page<UserDTO>> listUsers(@RequestParam Map<String, String> queryParameters) {
        var users = this.userService.findAll(queryParameters);
        return ResponseEntity.ok(users);
    }

    @ApiResponses(
            @ApiResponse(responseCode = "200")
    )
    @ErrorResponse(value = {
            UserNotFoundException.class,
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUser(@PathVariable String id) {
        var userDTO = this.userService.findByUUID(id);

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        this.userService.deleteByUUID(id);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        var updatedUserDTO = this.userService.updateByUUID(id, updateUserDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PutMapping("/{id}" + Endpoint.User.METADATA)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserMetadataDTO> updateUserMetadata(@PathVariable String id, @Valid @RequestBody UpdateUserMetadataDTO updateUserMetadataDTO) {
        var updatedUserDTO = this.userMetadataService.updateMetadataByUserUUID(id, updateUserMetadataDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PutMapping("/{id}" + Endpoint.User.PREFERENCES)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserPreferencesDTO> updateUserPreferences(@PathVariable String id, @Valid @RequestBody UpdateUserPreferencesDTO updateUserPreferencesDTO) {
        var updatedUserDTO = this.userPreferencesService.updateUserPreferencesByUserUUID(id, updateUserPreferencesDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PatchMapping("/{id}" + Endpoint.User.PASSWORD)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<Void> updateUserPassword(@PathVariable String id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        this.userService.updateUserPassword(id, updatePasswordDTO);

        return ResponseEntity.noContent().build();
    }

}
