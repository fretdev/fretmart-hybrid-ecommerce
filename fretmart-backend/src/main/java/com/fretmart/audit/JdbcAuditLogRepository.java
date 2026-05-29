package com.fretmart.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcAuditLogRepository{
    private final JdbcTemplate jdbcTemplate;

    public void saveAuditLog(Long productId, String userIp, String userAgent,String sessionId,String referrerUrl){
        String sql = """
                INSERT INTO audit_logs(product_id,user_ip,user_agent,session_id,referrer_url)
                VALUES(?,?,?,?,?)
                """;

        jdbcTemplate.update(sql,
                productId,
                userIp,
                userAgent,
                sessionId,
                referrerUrl
               );
    }
    public void saveAuditLogsBatch(List<Object[]> batchArgs){
        String sql = """
                INSERT INTO audit_logs (product_id,user_ip,user_agent,session_id,referrer_url)
                VALUES (?,?,?,?,?)
                """;
        jdbcTemplate.batchUpdate(sql,batchArgs);
    }
    public boolean isSuspiciousIp(String userIp){
        String sql = """
                SELECT COUNT(*) > 100 FROM audit_logs WHERE user_ip = ? AND viewed_at > NOW() - INTERVAL '1 minute'
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql,Boolean.class,userIp));
    }
    public List<Map<String,Object>> getMostViewedProducts(int limit){
        String sql = """
                SELECT p.id,p.name,COUNT(a.id) as view_count
                FROM audit_logs a
                JOIN products p ON a.product_id =p.id
                WHERE a.viewed_at > NOW() - INTERVAL '24 hours'
                GROUP BY p.id,p.name
                ORDER BY view_count DESC
                LIMIT ?
                """;
        return jdbcTemplate.queryForList(sql,limit);
    }
}
