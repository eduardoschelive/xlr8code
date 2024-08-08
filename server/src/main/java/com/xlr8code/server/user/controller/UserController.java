package com.xlr8code.server.user.controller;

import com.xlr8code.server.authentication.exception.PasswordMatchException;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.filter.annotation.FilterEndpoint;
import com.xlr8code.server.filter.utils.FilterUtils;
import com.xlr8code.server.openapi.annotation.ErrorResponses;
import com.xlr8code.server.user.dto.*;
import com.xlr8code.server.user.entity.User;
import com.xlr8code.server.user.exception.*;
import com.xlr8code.server.user.service.UserMetadataService;
import com.xlr8code.server.user.service.UserPreferencesService;
import com.xlr8code.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
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

    @Operation(
            summary = "List users",
            description = "Use this endpoint to list users and filter user by their fields."
    )
    @FilterEndpoint(User.class)
    @GetMapping
    public ResponseEntity<PagedModel<UserDTO>> listUsers(Specification<User> specification, Pageable pageable) {
        var users = this.userService.findAll(specification, pageable);
        var pagedModel = new PagedModel<>(users);
        return FilterUtils.buildResponseEntity(pagedModel);
    }

    @Operation(
            summary = "Find user",
            description = "Use this endpoint to find a user by its UUID."
    )
    @ErrorResponses(UserNotFoundException.class)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUser(@Schema(description = "The user unique identifier") @PathVariable String id) {
        var userDTO = this.userService.findByUUID(id);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(
            summary = "Delete user",
            description = "Use this endpoint to delete a user. Non-admin users can only delete their own user.",
            responses = @ApiResponse(responseCode = "204")
    )
    @ErrorResponses(value = UserNotFoundException.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<Void> deleteUser(@Schema(description = "The user unique identifier") @PathVariable String id) {
        this.userService.deleteByUUID(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update user",
            description = "Use this endpoint to update a user. Non-admin users can only update their own user."
    )
    @ErrorResponses(value = {
            UserNotFoundException.class,
            UsernameAlreadyTakenException.class,
            EmailAlreadyInUseException.class
    })
    @PutMapping("/{id}")
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserDTO> updateUser(
            @Schema(description = "The user unique identifier") @PathVariable String id,
            @Valid @RequestBody UpdateUserDTO updateUserDTO
    ) {
        var updatedUserDTO = this.userService.updateByUUID(id, updateUserDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @Operation(
            summary = "Update user metadata",
            description = "Use this endpoint to update the metadata of a user. Non-admin users can only update their own metadata."
    )
    @ErrorResponses(value = UserNotFoundException.class)
    @PutMapping("/{id}" + Endpoint.User.METADATA)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserMetadataDTO> updateUserMetadata(
            @Schema(description = "The user unique identifier") @PathVariable String id,
            @Valid @RequestBody UpdateUserMetadataDTO updateUserMetadataDTO
    ) {
        var updatedUserDTO = this.userMetadataService.updateMetadataByUserUUID(id, updateUserMetadataDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @Operation(
            summary = "Update user preferences",
            description = "Use this endpoint to update the preferences of a user. Non-admin users can only update their own preferences."
    )
    @ErrorResponses(value = UserNotFoundException.class)
    @PutMapping("/{id}" + Endpoint.User.PREFERENCES)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<UserPreferencesDTO> updateUserPreferences(
            @Schema(description = "The user unique identifier") @PathVariable String id,
            @Valid @RequestBody UpdateUserPreferencesDTO updateUserPreferencesDTO
    ) {
        var updatedUserDTO = this.userPreferencesService.updateUserPreferencesByUserUUID(id, updateUserPreferencesDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }


    @Operation(
            summary = "Update user password",
            description = "Use this endpoint to update the password of a user. The user must provide the old password to update the password. Non-admin users can only update their own password.",
            responses = @ApiResponse(responseCode = "204")
    )
    @ErrorResponses(value = {
            UserNotFoundException.class,
            IncorrectOldPasswordException.class,
            PasswordMatchException.class,
            PasswordAlreadyUsedException.class
    })
    @PatchMapping("/{id}" + Endpoint.User.PASSWORD)
    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    public ResponseEntity<Void> updateUserPassword(
            @Schema(description = "The user unique identifier") @PathVariable String id,
            @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO
    ) {
        this.userService.updateUserPassword(id, updatePasswordDTO);

        return ResponseEntity.noContent().build();
    }

}
