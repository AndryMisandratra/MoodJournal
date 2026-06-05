CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password_hash VARCHAR(255) NOT NULL,
                                     display_name VARCHAR(100) NOT NULL,
                                     biometric_enabled BOOLEAN NOT NULL DEFAULT false,
                                     notify_at TIME DEFAULT '21:00:00',
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);