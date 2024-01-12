package com.xlr8code.server.user.entity;

import com.xlr8code.server.common.utils.Language;
import com.xlr8code.server.common.utils.Theme;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_metadata")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
@Getter
public class UserMetadata {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    @MapsId
    private User user;

    @Column(name = "language_preference", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language languagePreference;

    @Column(name = "theme_preference", nullable = false)
    @Enumerated(EnumType.STRING)
    private Theme themePreference;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

}
