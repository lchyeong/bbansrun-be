package com.bbansrun.project1.domain.users.service;

import static com.bbansrun.project1.domain.users.entity.Role.ROLE_USER;

import com.bbansrun.project1.domain.users.dto.UserJoinDto;
import com.bbansrun.project1.domain.users.entity.User;
import com.bbansrun.project1.domain.users.repository.UserRepository;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.jwt.JwtTokenProvider;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public void createUser(UserJoinDto userJoinDto) {
        String email = userJoinDto.getEmail();
        String password = userJoinDto.getPassword();

        if (userRepository.existsByEmail(email)) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTED);
        }
        User user = new User(UUID.randomUUID(), email, password, List.of(ROLE_USER));
        userRepository.save(user);
    }
}
