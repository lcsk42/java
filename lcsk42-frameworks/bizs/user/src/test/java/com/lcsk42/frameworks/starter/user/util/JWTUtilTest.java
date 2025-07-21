package com.lcsk42.frameworks.starter.user.util;

import com.lcsk42.frameworks.starter.common.util.JwtUtil;
import com.lcsk42.frameworks.starter.convention.dto.UserInfoDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class JWTUtilTest {

    private static final String SECRET = "SecretKey092781034567890123456789012345678901234567890123456789012345";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    @Test
    void testGenerateAndParseToken() {

        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setUserId(123L);
        userInfo.setUsername("test_user");

        String token = JwtUtil.generateAccessToken(userInfo);

        log.info(token);

        assertThat(token).isNotBlank();
        assertThat(token).startsWith(JwtUtil.TOKEN_PREFIX);

        UserInfoDTO parsedUser = JwtUtil.parseJwtToken(token);
        assertThat(parsedUser).isNotNull();
        assertThat(parsedUser.getUserId()).isEqualTo(userInfo.getUserId());
        assertThat(parsedUser.getUsername()).isEqualTo(userInfo.getUsername());
    }

    @Test
    void testParseExpiredToken() {
        String expiredToken = Jwts.builder()
                .subject("{\"userId\":1,\"username\":\"expired\"}")
                .signWith(SECRET_KEY)
                .issuer(JwtUtil.ISSUER)
                .issuedAt(new Date(System.currentTimeMillis() - 60 * 60 * 1000))
                .expiration(new Date(System.currentTimeMillis() - 30 * 60 * 1000))
                .compact();

        String tokenWithPrefix = JwtUtil.TOKEN_PREFIX + expiredToken;
        UserInfoDTO result = JwtUtil.parseJwtToken(tokenWithPrefix);

        assertThat(result).isNull();
    }

    @Test
    void testParseInvalidToken() {
        String invalidToken = JwtUtil.TOKEN_PREFIX + "invalid.jwt.token";
        UserInfoDTO result = JwtUtil.parseJwtToken(invalidToken);
        assertThat(result).isNull();
    }

    @Test
    void testParseEmptyToken() {
        UserInfoDTO result = JwtUtil.parseJwtToken("");
        assertThat(result).isNull();
    }
}