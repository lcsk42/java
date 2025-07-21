package com.lcsk42.frameworks.starter.common.util;

import com.lcsk42.frameworks.starter.convention.dto.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtil {

    private static final long EXPIRATION = 60 * 60 * 24L;

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String ISSUER = "lcsk42";

    private static final String SECRET = "SecretKey092781034567890123456789012345678901234567890123456789012345";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateAccessToken(UserInfoDTO userInfo) {
        String jwtToken = Jwts.builder()
                .subject(JacksonUtil.toJSON(userInfo))
                .signWith(SECRET_KEY)
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION * 1_000))
                .compact();
        return TOKEN_PREFIX + jwtToken;
    }

    public static UserInfoDTO parseJwtToken(String jwtToken) {

        if (StringUtils.hasText(jwtToken)) {
            String actualJwtToken = jwtToken.replace(TOKEN_PREFIX, "");
            try {

                Claims claims = Jwts.parser()
                        .verifyWith(SECRET_KEY)
                        .build()
                        .parseSignedClaims(actualJwtToken)
                        .getPayload();

                Date expiration = claims.getExpiration();
                if (expiration.after(new Date())) {
                    String subject = claims.getSubject();
                    return JacksonUtil.fromJson(subject, UserInfoDTO.class);
                }
            } catch (JwtException ex) {
                log.warn("JWT token is invalid: {}", ex.getMessage());
            } catch (Exception ex) {
                log.error("Unexpected error during JWT parsing", ex);
            }
        }
        return null;
    }
}
