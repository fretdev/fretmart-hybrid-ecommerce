package com.fretmart.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductAuditService {
    private final JdbcAuditLogRepository jdbcAuditLogRepository;

    public void logProductView(Long productId, HttpServletRequest request) {
        String userIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String sessionId = getOrCreateSessionId(request);
        String referrerUrl = request.getHeader("Referer");

        jdbcAuditLogRepository.saveAuditLog(
                productId, userIp, userAgent, sessionId, referrerUrl
        );
    }
    public boolean isSuspiciousIp(HttpServletRequest request){
        String userIp = getClientIp(request);
        return jdbcAuditLogRepository.isSuspiciousIp(userIp);
    }
    public String getClientIp(HttpServletRequest request){
        String xForwarderFor = request.getHeader("X-Forwarded-For");
        if (xForwarderFor != null && !xForwarderFor.isBlank()){
            return xForwarderFor.split(",")[0].trim();

        }
        return request.getRemoteAddr();
    }
    private String getOrCreateSessionId(HttpServletRequest request){
        String sessionId = request.getHeader("X-Session-Id");
        if(sessionId == null || sessionId.isBlank()){
            sessionId = UUID.randomUUID().toString();
        }
        return sessionId;
    }
}
