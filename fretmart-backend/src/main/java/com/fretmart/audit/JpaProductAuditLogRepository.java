package com.fretmart.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductAuditLogRepository extends JpaRepository<ProductAuditLog, Long> {
}
