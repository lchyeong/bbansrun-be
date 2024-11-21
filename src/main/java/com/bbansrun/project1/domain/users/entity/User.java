package com.bbansrun.project1.domain.users.entity;

import com.bbansrun.project1.domain.auth.entity.RefreshToken;
import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false, updatable = false, unique = true)
    private UUID userUuid;

    @Column(nullable = false)
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @Builder
    public User(UUID userUuid, String email, String password, List<Role> roles) {
        this.userUuid = userUuid;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void updateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new ApiException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }
        this.email = email;
    }

    public void updateNickname(String newNickname) {
        if (newNickname == null || newNickname.isEmpty()) {
            throw new ApiException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }
        this.nickName = newNickname;
    }

    public void updatePassword(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new ApiException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }
        this.password = newPassword;
    }

    public void updateRoles(List<Role> newRoles) {
        if (newRoles == null || newRoles.isEmpty()) {
            throw new ApiException(ErrorCode.MISSING_REQUIRED_FIELDS);
        }
        this.roles = newRoles;
    }
}
