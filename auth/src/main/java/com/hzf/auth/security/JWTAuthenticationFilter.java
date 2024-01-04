package com.hzf.auth.security;

import com.hzf.auth.common.ResponseEntity;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final static String Authorization = "Authorization";
    private final static String Bearer = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader(Authorization);
        if (StringUtils.isBlank(authorization)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String realToken = authorization.replace(Bearer, "");
        ResponseEntity<Claims> responseE = JWTUtil.verifyToken(realToken);
        Claims data = responseE.getData();
        if (ObjectUtils.isEmpty(data)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (responseE.getCode() != 200) {
            response.setStatus(responseE.getCode());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
