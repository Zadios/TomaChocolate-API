package com.tomachocolate.api.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(15))))
                .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // Límite (cantidad de intentos por IP) estrictamente aplicado al endpoint creación de la juntada
        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && path.startsWith("/api/meetings")) {

            String ip = getClientIP(httpRequest);
            Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

            if (!bucket.tryConsume(1)) {
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpResponse.setContentType("application/json;charset=UTF-8");
                
                String envUrl = System.getenv("FRONTEND_URL");
                String frontendUrl = (envUrl != null) ? envUrl : "http://localhost:5173";

                httpResponse.setHeader("Access-Control-Allow-Origin", frontendUrl);
                httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                httpResponse.setHeader("Access-Control-Allow-Headers", "*");
                httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

                httpResponse.getWriter().write("{\"message\": \"Demasiadas peticiones. Intentá de nuevo en unos minutos.\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}