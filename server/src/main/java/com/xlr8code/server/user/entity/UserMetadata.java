package com.xlr8code.server.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_metadata")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMetadata {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @OneToOne
    @PrimaryKeyJoinColumn(referencedColumnName = "id")
    private User user;

    @Column(name = "language_preference", nullable = false, length = 5)
    private String languagePreference;

    @Column(name = "theme_preference", nullable = false)
    private String themePreference;

}
