package com.xlr8code.server.user.controller;

import com.xlr8code.server.authentication.utils.Endpoint;
import com.xlr8code.server.user.dto.UserDTO;
import com.xlr8code.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        this.userService.deleteByUUID(id);

        return ResponseEntity.noContent().build();
    }

}
