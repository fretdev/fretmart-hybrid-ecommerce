package com.fretmart.audit;

import com.fretmart.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "user_ip",nullable = false,length = 45)
    private String userIp;

    @Column(name = "user_agent",columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    @Column(name = "session_id",length = 100)
    private String sessionId;

    @Column(name = "referrer_url",length = 500)
    private String referrerUrl;

}
