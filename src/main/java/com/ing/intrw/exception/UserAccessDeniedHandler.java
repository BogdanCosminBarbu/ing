package com.ing.intrw.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        String requestPath = request.getRequestURI();
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.warn("User '{}' tried to access '{}' without permission at {}", username, requestPath, timestamp);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        PrintWriter writer = response.getWriter();
        writer.write("{\"message\": \"Access Denied: User '" + username + "' does not have permission to access this resource at '" + requestPath + "'\"}");
        writer.flush();
    }
}
