package com.bbansrun.project1.domain.users.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserJoinDto {

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
    private final String email;

    @NotBlank
    private final String nickName;

    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    private final String password;
}
