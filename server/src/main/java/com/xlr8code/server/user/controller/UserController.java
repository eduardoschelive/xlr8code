package com.xlr8code.server.user.controller;

import com.xlr8code.server.common.dto.ApplicationExceptionResponseDTO;
import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.user.dto.*;
import com.xlr8code.server.user.exception.UserNotFoundException;
import com.xlr8code.server.user.service.UserMetadataService;
import com.xlr8code.server.user.service.UserPreferencesService;
import com.xlr8code.server.user.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been found in the database"),
            @ApiResponse(responseCode = "404", description = "User has not been found in the database", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApplicationExceptionResponseDTO.class), examples = {
                    @ExampleObject(name = "User not found", value = "{\"status\": 404, \"error\": \"USER_NOT_FOUND\", \"message\": \"User not found\", \"timestamp\": \"2021-09-01T12:00:00Z\", \"path\": \"/api/v1/users/123\"}")
            }))
    })
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
