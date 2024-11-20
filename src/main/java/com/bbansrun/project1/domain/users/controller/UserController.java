package com.bbansrun.project1.domain.users.controller;

import com.bbansrun.project1.domain.users.dto.UserJoinDto;
import com.bbansrun.project1.domain.users.dto.UserUpdateDto;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserJoinDto UserJoinDto) {
        userService.createUser(UserJoinDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("사용자 등록이 완료되었습니다.");
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDto userUpdateDto, HttpServletRequest request) {
        User updateUser = userService.updateUser(request, userUpdateDto);
        if (updateUser == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        userService.deleteUser(request);
        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴가 완료되었습니다.");
    }
}
