CREATE TABLE contact_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    user_name VARCHAR(255),
    user_email VARCHAR(255),
    type VARCHAR(50),
    subject VARCHAR(255),
    message TEXT,
    sent_at DATETIME
);