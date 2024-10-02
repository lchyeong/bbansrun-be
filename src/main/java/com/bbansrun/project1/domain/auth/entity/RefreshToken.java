package com.bbansrun.project1.domain.auth.entity;

import com.bbansrun.project1.domain.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = true)
    private String deviceInfo;  // 기기 정보

    @Builder
    public RefreshToken(String token, Instant expiryDate, String deviceInfo, User user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.deviceInfo = deviceInfo;
        this.user = user;
    }

}
