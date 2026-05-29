CREATE TABLE IF NOT EXISTS audit_logs (
id BIGSERIAL PRIMARY KEY,

product_id BIGINT,

user_ip VARCHAR(45) NOT NULL,
user_agent TEXT,

viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

session_id VARCHAR(100),
referrer_url VARCHAR(500)
);

CREATE INDEX idx_audit_ip_time ON audit_logs(user_ip,viewed_at);
CREATE INDEX idx_audit_viewed_at ON audit_logs(viewed_at);

COMMENT ON TABLE audit_logs IS  'High-speed audit log for product views (JDBC write,JPA read)';
COMMENT ON COLUMN audit_logs.user_ip IS 'Client IP address - used for rate limiting and fraud detection';
COMMENT ON COLUMN audit_logs.session_id IS 'Session identifier for tracking user journey across products';
COMMENT ON COLUMN audit_logs.product_id IS 'Historical reference identifier linking to the product catalog'

