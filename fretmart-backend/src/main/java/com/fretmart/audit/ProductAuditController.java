package com.fretmart.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class ProductAuditController {
    private final ProductAuditService productAuditService;
    private final JdbcAuditLogRepository jdbcAuditLogRepository;

    @PostMapping("/products/{id}/view")
    public ResponseEntity<Void> recordProductView(@PathVariable("id") Long productId, HttpServletRequest request){
        if(productAuditService.isSuspiciousIp(request)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        productAuditService.logProductView(productId,request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/security/check")
    public ResponseEntity<Boolean> checkIpStatus(HttpServletRequest request){
        boolean isSuspicious = productAuditService.isSuspiciousIp(request);
        return ResponseEntity.ok(isSuspicious);
    }

    @GetMapping("/analytics/trending")
    public ResponseEntity<List<Map<String,Object>>> getTrendingProducts(@RequestParam(value = "limit",defaultValue = "5")int limit){
        List<Map<String,Object>> metrics = jdbcAuditLogRepository.getMostViewedProducts(limit);
        return ResponseEntity.ok(metrics);
    }
}
