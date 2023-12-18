package com.xlr8code.server.authentication.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xlr8code.server.authentication.exception.AuthenticationExceptionType;
import com.xlr8code.server.common.exception.ApplicationException;
import com.xlr8code.server.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AccessTokenService implements TokenService {

    @Value("${jwt.access-token.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration-time}")
    private long expirationTime;

    @Value("${jwt.access-token.unit}")
    private String unit;

    @Override
    public String generate(User user) {
        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("language", user.getMetadata().getLanguagePreference().getCode())
                    .withClaim("theme", user.getMetadata().getThemePreference().getCode())
                    .withClaim("profilePictureUrl", user.getMetadata().getProfilePictureUrl())
                    .withClaim("roles", user.getNamedRoles().stream().toList())
                    .withExpiresAt(this.getExpiresAt())
                    .withIssuedAt(this.getIssuedAt())
                    .sign(this.getAlgorithm());
        } catch (JWTCreationException e) {
            throw new ApplicationException(AuthenticationExceptionType.JWT_CREATION_ERROR);
        }
    }

    @Override
    @Nullable
    public DecodedJWT validate(String token) {
        try {
            return JWT.require(this.getAlgorithm()).build().verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

}
