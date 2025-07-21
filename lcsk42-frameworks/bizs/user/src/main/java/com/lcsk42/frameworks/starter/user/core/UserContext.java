package com.lcsk42.frameworks.starter.user.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.lcsk42.frameworks.starter.convention.dto.UserInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserContext {

    private static final ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void setUser(UserInfoDTO user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static UserInfoDTO getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static Long getUserId() {
        return Optional.ofNullable(USER_THREAD_LOCAL.get())
                .map(UserInfoDTO::getUserId)
                .orElse(null);
    }

    public static String getUsername() {
        return Optional.ofNullable(USER_THREAD_LOCAL.get())
                .map(UserInfoDTO::getUsername)
                .orElse(null);
    }

    public static String getToken() {
        return Optional.ofNullable(USER_THREAD_LOCAL.get())
                .map(UserInfoDTO::getToken)
                .orElse(null);
    }

    public static void removeUser() {
        USER_THREAD_LOCAL.remove();
    }
}
