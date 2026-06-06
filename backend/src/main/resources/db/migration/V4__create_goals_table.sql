CREATE TABLE IF NOT EXISTS goals (
                                     id BIGSERIAL PRIMARY KEY,
                                     user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                     title VARCHAR(255) NOT NULL,
                                     target_activity_id BIGINT NOT NULL REFERENCES activities(id) ON DELETE RESTRICT,
                                     current_streak INT NOT NULL DEFAULT 0,
                                     longest_streak INT NOT NULL DEFAULT 0,
                                     status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('INACTIVE', 'PENDING', 'COMPLETED', 'FAILED')),
                                     last_completed_at TIMESTAMP,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_goals_user_status ON goals(user_id, status);