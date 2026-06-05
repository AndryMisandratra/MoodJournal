-- Table des activités (catalogue partagé)
CREATE TABLE IF NOT EXISTS activities (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL,
                                          icon VARCHAR(100) NOT NULL,
                                          color VARCHAR(7) NOT NULL,
                                          user_id BIGINT NULL REFERENCES users(id) ON DELETE CASCADE,
                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table des entrées du journal
CREATE TABLE IF NOT EXISTS journal_entries (
                                               id BIGSERIAL PRIMARY KEY,
                                               user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                               mood VARCHAR(20) NOT NULL CHECK (mood IN ('VERY_GOOD','GOOD','NEUTRAL','BAD','VERY_BAD')),
                                               note TEXT,
                                               recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table de jointure (many-to-many entre entries et activities)
CREATE TABLE IF NOT EXISTS entry_activities (
                                                entry_id BIGINT NOT NULL REFERENCES journal_entries(id) ON DELETE CASCADE,
                                                activity_id BIGINT NOT NULL REFERENCES activities(id) ON DELETE CASCADE,
                                                PRIMARY KEY (entry_id, activity_id)
);

-- Index pour optimiser les requêtes
CREATE INDEX idx_journal_entries_user_id ON journal_entries(user_id);
CREATE INDEX idx_journal_entries_recorded_at ON journal_entries(recorded_at);
CREATE INDEX idx_entry_activities_entry_id ON entry_activities(entry_id);
CREATE INDEX idx_entry_activities_activity_id ON entry_activities(activity_id);
