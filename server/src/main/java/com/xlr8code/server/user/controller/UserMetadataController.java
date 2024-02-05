package com.xlr8code.server.user.controller;

import com.xlr8code.server.common.utils.Endpoint;
import com.xlr8code.server.user.dto.UpdateUserMetadataDTO;
import com.xlr8code.server.user.dto.UserMetadataDTO;
import com.xlr8code.server.user.service.UserMetadataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.UserMetadata.BASE_PATH)
@RequiredArgsConstructor
public class UserMetadataController {

    private final UserMetadataService userMetadataService;

    @PreAuthorize("@userSecurityService.canModifyResource(principal, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<UserMetadataDTO> updateUserMetadata(@PathVariable String id, @Valid @RequestBody UpdateUserMetadataDTO  updateUserMetadataDTO ) {
       var updatedMetadata = this.userMetadataService.updateUserMetadata(id, updateUserMetadataDTO);
       return ResponseEntity.ok(updatedMetadata);
    }

}
