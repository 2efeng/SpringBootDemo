package com.hzf.auth.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.lang.String.format;

@Slf4j
@Component
public class RequestLoggingAspect implements HandlerInterceptor {

    private static final String START_TIME_ATTRIBUTE = "REQ::BEGIN";

    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @Nonnull Object handler, @Nullable Exception ex) {
        long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        long duration = System.currentTimeMillis() - startTime;
        logRequest(request, response.getStatus(), duration);
    }

    private static final String REST_CALL = "REQ::END "
            + "remote-addr: %s "
            + "forwarded-for: %s "
            + "forwarded-proto: %s "
            + "server-port: %s "
            + "isSecure: %s "
            + "scheme: %s "
            + "method: %s "
            + "url: %s "
            + "query-string: %s "
            + "status: %s "
            + "duration: %s ";

    private static final String FORWARDED_FOR = "X-Forwarded-For";
    private static final String FORWARDED_PROTO = "X-Forwarded-Proto";

    public void logRequest(HttpServletRequest request, int status, long duration) {
        String logMsg = format(REST_CALL,
                request.getRemoteAddr(),
                request.getHeader(FORWARDED_FOR),
                request.getHeader(FORWARDED_PROTO),
                request.getServerPort(),
                request.isSecure(),
                request.getScheme(),
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                status,
                duration);
        if (log.isDebugEnabled()) log.debug(logMsg);
    }
}
