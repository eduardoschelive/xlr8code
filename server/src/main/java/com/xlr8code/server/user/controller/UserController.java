package com.xlr8code.server.user.controller;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.user.dto.*;
import com.xlr8code.server.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.User.BASE_PATH)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        var updatedUserDTO = this.userService.updateMetadataByUUID(id, updateUserMetadataDTO);

        return ResponseEntity.ok(updatedUserDTO);
    }

    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    @PatchMapping("/{id}" + Endpoint.User.PASSWORD)
    public ResponseEntity<Void> updateUserPasswordById(@PathVariable String id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        this.userService.updateUserPassword(id, updatePasswordDTO);

        return ResponseEntity.noContent().build();
    }

}
