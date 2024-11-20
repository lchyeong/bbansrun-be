package com.bbansrun.project1.domain.users.service;

import static com.bbansrun.project1.domain.users.entity.Role.ROLE_USER;

import com.bbansrun.project1.domain.users.dto.UserJoinDto;
import com.bbansrun.project1.domain.users.dto.UserUpdateDto;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.JwtTokenParser;
import com.bbansrun.project1.global.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenParser jwtTokenParser;


    public void createUser(UserJoinDto userJoinDto) {
        String email = userJoinDto.getEmail();
        String password = userJoinDto.getPassword();

        if (userRepository.existsByEmail(email)) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTED);
        }
        User user = new User(UUID.randomUUID(), email, password, List.of(ROLE_USER));
        userRepository.save(user);
    }

    public User updateUser(HttpServletRequest request, UserUpdateDto userUpdateDto) {
        User user = findUserByToken(request);

        if (userUpdateDto.getEmail() != null) {
            user.updateEmail(userUpdateDto.getEmail());
        }

        if (userUpdateDto.getPassword() != null) {
            user.updatePassword(userUpdateDto.getPassword());
        }

        if (userUpdateDto.getNickName() != null) {
            user.updateNickname(userUpdateDto.getNickName());
        }

        if (userUpdateDto.getRoles() != null && !userUpdateDto.getRoles().isEmpty()) {
            user.updateRoles(userUpdateDto.getRoles());
        }
        return user;
    }

    public void deleteUser(HttpServletRequest request) {
        User user = findUserByToken(request);
        userRepository.delete(user);
    }

    private User findUserByToken(HttpServletRequest request) {
        String token = HeaderUtil.extractTokenFromRequest(request);

        if (token.isBlank()) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        return userRepository.findByUserUuid(jwtTokenParser.getUserUuid(token))
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }
}
