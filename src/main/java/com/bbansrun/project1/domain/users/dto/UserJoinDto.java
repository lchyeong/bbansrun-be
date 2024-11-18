package com.bbansrun.project1.domain.users.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true) //필요 없을까
public class UserJoinDto {

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    private final String password;
}
