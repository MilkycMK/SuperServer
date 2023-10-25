package net.mlk.adolfserver.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mlk.adolfserver.data.session.Session;
import net.mlk.adolfserver.data.session.SessionRepository;
import net.mlk.adolfserver.data.session.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SessionRepository sessionRepository = SessionService.getSessionRepository();
        String authorization = request.getHeader("Authorization");

        Session session;
        if (authorization == null || (session = sessionRepository.findByToken(authorization)) == null) {
            response.setStatus(401);
            return;
        } else if (session.getExpirationTime().isBefore(LocalDateTime.now())) {
            sessionRepository.delete(session);
            response.setStatus(401);
            return;
        }
        request.setAttribute("session", session);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI().toLowerCase();
        return path.startsWith("/signin") || path.startsWith("/signup");
    }


}
