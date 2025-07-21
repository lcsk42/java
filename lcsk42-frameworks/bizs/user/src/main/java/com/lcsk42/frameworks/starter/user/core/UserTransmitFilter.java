package com.lcsk42.frameworks.starter.user.core;

import com.lcsk42.frameworks.starter.convention.dto.UserInfoDTO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class UserTransmitFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        // todo: build user info and put in UserContext
        UserInfoDTO userInfoDTO = UserInfoDTO.builder().build();
        UserContext.setUser(userInfoDTO);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
